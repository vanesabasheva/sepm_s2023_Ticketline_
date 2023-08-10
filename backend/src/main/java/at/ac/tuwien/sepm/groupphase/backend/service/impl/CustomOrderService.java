package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.OrderDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.OrderHistoryDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.bookings.BookingDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.bookings.BookingMerchandiseDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.bookings.BookingTicketDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.OrderMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import at.ac.tuwien.sepm.groupphase.backend.entity.Merchandise;
import at.ac.tuwien.sepm.groupphase.backend.entity.MerchandiseOrdered;
import at.ac.tuwien.sepm.groupphase.backend.entity.Order;
import at.ac.tuwien.sepm.groupphase.backend.entity.PaymentDetail;
import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;
import at.ac.tuwien.sepm.groupphase.backend.entity.PerformanceSector;
import at.ac.tuwien.sepm.groupphase.backend.entity.Reservation;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.entity.Transaction;
import at.ac.tuwien.sepm.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepm.groupphase.backend.exception.FatalException;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.UnauthorizedException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.MerchandiseOrderedRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.MerchandiseRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.NotUserRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.OrderRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ReservationRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TicketRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TransactionRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.OrderService;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.StrictMath.floor;

@Service
public class CustomOrderService implements OrderService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final OrderRepository orderRepository;
    private final NotUserRepository notUserRepository;
    private final OrderMapper orderMapper;
    private final TransactionRepository transactionRepository;
    private final TicketRepository ticketRepository;
    private final ReservationRepository reservationRepository;
    private final MerchandiseRepository merchandiseRepository;
    private final MerchandiseOrderedRepository merchandiseOrderedRepository;
    private final CustomOrderValidator validator;

    @Autowired
    public CustomOrderService(OrderRepository orderRepository, NotUserRepository notUserRepository,
                              OrderMapper orderMapper, TransactionRepository transactionRepository,
                              TicketRepository ticketRepository, ReservationRepository reservationRepository,
                              MerchandiseRepository merchandiseRepository,
                              MerchandiseOrderedRepository merchandiseOrderedRepository,
                              CustomOrderValidator validator) {
        this.orderRepository = orderRepository;
        this.notUserRepository = notUserRepository;
        this.orderMapper = orderMapper;
        this.transactionRepository = transactionRepository;
        this.ticketRepository = ticketRepository;
        this.reservationRepository = reservationRepository;
        this.merchandiseRepository = merchandiseRepository;
        this.merchandiseOrderedRepository = merchandiseOrderedRepository;
        this.validator = validator;
    }


    @Override
    public List<OrderHistoryDto> getOrderHistory(Integer id) throws NotFoundException, ValidationException {
        LOGGER.info("Find all orders for user with id {}", id);
        List<String> validationErrors = new ArrayList<>();
        if (id == null) {
            throw new NotFoundException("No id has been provided");
        }
        if (id <= 0) {
            validationErrors.add("Id can't be a negative number");
        }
        if (!validationErrors.isEmpty()) {
            throw new ValidationException("Validation failed:", validationErrors);
        }
        ApplicationUser user = notUserRepository.getApplicationUserById(id);
        if (user == null) {
            throw new NotFoundException(String.format("Could not find user with id %s", id));
        }
        List<Order> allOrders = orderRepository.getAllOrdersByUserId(id);
        List<OrderHistoryDto> allOrdersDto = new ArrayList<>();
        for (Order order : allOrders) {
            allOrdersDto.add(orderMapper.orderToOrderHistoryDto(order));
        }
        return allOrdersDto;
    }

    @Transactional(rollbackFor = {ConflictException.class, ValidationException.class})
    @Override
    public OrderDto buyTickets(Integer userId, BookingDto bookingDto) throws ConflictException, ValidationException {
        LOGGER.debug("Buy Tickets from Cart, userId: {}", userId);
        List<String> conflictMsg = new ArrayList<>();
        List<String> validationMsg = new ArrayList<>();

        // input validation
        boolean hasTickets = bookingDto.getTickets() != null && !bookingDto.getTickets().isEmpty();
        boolean hasMerchandise = bookingDto.getMerchandise() != null && !bookingDto.getMerchandise().isEmpty();
        boolean buysTickets = false;
        if (hasTickets) {
            buysTickets = bookingDto.getTickets().stream().anyMatch(ticketDto -> !ticketDto.getReservation());
        }
        boolean orderIsCreated = buysTickets || hasMerchandise;
        // throw exception if bookingDto both tickets and merchandise are null
        if (!hasTickets && !hasMerchandise) {
            validationMsg.add("No Tickets or Merchandise in Booking");
        }
        // throw exception if tickets or merchandise are bought and no payment id given
        if (orderIsCreated && bookingDto.getPaymentDetailId() == null) {
            validationMsg.add("No payment id for buying tickets");
        }
        // throw exception if bookingDto has merchandise and no location id
        if (hasMerchandise && bookingDto.getLocationId() == null) {
            validationMsg.add("No delivery address for merchandise");
        }
        if (!validationMsg.isEmpty()) {
            throw new ValidationException("Error processing order", validationMsg);
        }

        ApplicationUser user = notUserRepository.findApplicationUserById(userId);
        if (user == null) {
            throw new NotFoundException("Could not find User");
        }
        validator.validPoints(user.getPoints(), bookingDto);

        //create order
        Order order = null;
        if (orderIsCreated) {
            order = new Order();
            order.setUser(user);
            order.setCancelled(false);
            order.setOrderTs(LocalDateTime.now());

            // check if bookingDto.getPaymentDetailId() is contained in user.getPaymentDetails()
            if (bookingDto.getPaymentDetailId() != null) {
                PaymentDetail paymentDetail =
                    user.getPaymentDetails().stream().filter(paymentDetail1 -> paymentDetail1.getId().equals(bookingDto.getPaymentDetailId())).findFirst()
                        .orElse(null);
                if (paymentDetail == null) {
                    conflictMsg.add("Payment Detail not found for User");
                    throw new ConflictException("Error creating order", conflictMsg);
                }

                Set<Order> orders = new HashSet<>();
                if (paymentDetail.getOrders() != null) {
                    orders = paymentDetail.getOrders();
                }
                orders.add(order);
                paymentDetail.setOrders(orders);
                order.setPaymentDetail(paymentDetail);
            }
            // check if bookingDto.getLocationId() is contained in user.getLocations()
            if (bookingDto.getLocationId() != null) {
                Location location =
                    user.getLocations().stream().filter(location1 -> location1.getId().equals(bookingDto.getLocationId())).findFirst().orElse(null);
                if (location == null) {
                    conflictMsg.add("Location not found for User");
                    throw new ConflictException("Error creating order", conflictMsg);
                }
                order.setDeliveryAddress(location);
            }
            orderRepository.save(order);
        }

        BigDecimal price = new BigDecimal(0);
        int totalPoints = 0;
        StringBuilder receipt = new StringBuilder();
        // Tickets
        if (hasTickets) {
            // fetch tickets from db
            List<Ticket> fetchedTickets = ticketRepository.findAllByIdIn(
                bookingDto.getTickets().stream().map(BookingTicketDto::getTicketId).collect(Collectors.toList()));

            // check if all tickets are found
            if (fetchedTickets.size() != bookingDto.getTickets().size()) {
                conflictMsg.add("Not all tickets found");
                throw new ConflictException("Error creating order", conflictMsg);
            }

            Set<Ticket> ticketsToSave = new HashSet<>();
            Set<Reservation> reservationsToSave = new HashSet<>();
            Set<Reservation> reservationsToDelete = new HashSet<>();

            for (Ticket ticket : fetchedTickets) {

                // find ticket in bookingDto by id
                BookingTicketDto bookingTicketDto =
                    bookingDto.getTickets().stream().filter(ticketDto -> ticketDto.getTicketId().equals(ticket.getId())).findFirst().get();

                // check if ticket is already bought or reserved
                if (ticket.getOrder() != null
                    || (ticket.getReservation() != null
                    && (!ticket.getReservation().getUser().getId().equals(user.getId())
                    || (bookingTicketDto.getReservation() && !ticket.getReservation().getCart())))) {
                    conflictMsg.add("Ticket " + ticket.getId() + " is already bought or reserved");
                    continue;
                }
                LocalDateTime expirationTs = ticket.getPerformance().getDatetime().minusMinutes(30);
                if (expirationTs.isBefore(LocalDateTime.now())) {
                    conflictMsg.add("Ticket " + ticket.getId() + " is for a performance starting too soon or already in the past");
                    continue;
                }

                if (bookingTicketDto.getReservation()) {
                    // reserve ticket
                    Integer id = ticket.getReservation() == null ? null : ticket.getReservation().getId();
                    Reservation reservation = Reservation.ReservationBuilder.aReservation()
                        .withId(id)
                        .withTicket(ticket)
                        .withCart(false)
                        .withExpirationTs(expirationTs)
                        .withUser(user)
                        .build();
                    reservationsToSave.add(reservation);
                } else {
                    // buy ticket
                    BigDecimal ticketPrice = getTicketPrice(ticket);
                    price = price.add(ticketPrice);
                    totalPoints += (int) floor(ticketPrice.doubleValue());
                    ticket.setOrder(order);
                    ticketsToSave.add(ticket);
                    if (ticket.getReservation() != null) {
                        reservationsToDelete.add(ticket.getReservation());
                    }
                    addTicketToReceipt(receipt, ticket);
                }
            }
            if (!conflictMsg.isEmpty()) {
                throw new ConflictException("Error creating order", conflictMsg);
            }

            reservationRepository.saveAll(reservationsToSave);
            reservationRepository.deleteAll(reservationsToDelete);
            if (orderIsCreated) {
                order.setTickets(ticketsToSave);
            }
        }

        //Merchandise
        if (hasMerchandise) {
            List<Merchandise> fetchedMerchandise = merchandiseRepository.findAllById(
                bookingDto.getMerchandise().stream().map(BookingMerchandiseDto::getId).collect(Collectors.toList()));

            if (fetchedMerchandise.size() != bookingDto.getMerchandise().size()) {
                conflictMsg.add("Not all merchandise found");
                throw new ConflictException("Error creating order", conflictMsg);
            }

            Set<MerchandiseOrdered> merchandiseOrderedToSave = new HashSet<>();

            for (BookingMerchandiseDto merchandiseDto : bookingDto.getMerchandise()) {
                Merchandise merchandise =
                    fetchedMerchandise.stream().filter(merchandise1 -> merchandise1.getId().equals(merchandiseDto.getId())).findFirst().get();
                MerchandiseOrdered merchandiseOrdered = MerchandiseOrdered.MerchandiseOrderedBuilder.aMerchandiseOrdered()
                    .withMerchandise(merchandise)
                    .withOrder(order)
                    .withQuantity(merchandiseDto.getQuantity())
                    .withPoints(merchandiseDto.getBuyWithPoints())
                    .build();
                merchandiseOrderedToSave.add(merchandiseOrdered);
                if (!merchandiseDto.getBuyWithPoints()) {
                    BigDecimal merchPrice = merchandise.getPrice().multiply(new BigDecimal(merchandiseDto.getQuantity()));
                    price = price.add(merchPrice);
                    totalPoints += (int) floor(merchPrice.doubleValue());
                } else {
                    totalPoints -= merchandise.getPointsPrice() * merchandiseDto.getQuantity();
                }
                addMerchandiseToReceipt(receipt, merchandise, merchandiseDto.getQuantity(), merchandiseDto.getBuyWithPoints());
            }

            merchandiseOrderedRepository.saveAll(merchandiseOrderedToSave);
            order.setMerchandiseOrdered(merchandiseOrderedToSave);
        }

        //transaction
        if (orderIsCreated) {
            Transaction transaction = new Transaction();
            transaction.setOrder(order);
            transaction.setDeductedAmount(price);
            transaction.setDeductedPoints(totalPoints);
            transaction.setTransactionTs(LocalDateTime.now());
            transaction.setReceipt(receipt.toString());
            order.setTransactions(Collections.singleton(transaction));
            user.setPoints(user.getPoints() + totalPoints);
            transactionRepository.save(transaction);
        }

        return orderMapper.orderToOrderDto(order);
    }

    private void addTicketToReceipt(StringBuilder receipt, Ticket ticket) {
        Performance performance = ticket.getPerformance();
        receipt.append("Ticket ").append(performance.getEvent().getName()).append(" - ")
            .append(performance.getHall().getName()).append(" - ")
            .append(performance.getDatetime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))).append(" - ")
            .append(ticket.getSeat().getSector().getStanding()
                ? "Sector " + ticket.getSeat().getSector().getName() : "Row " + ticket.getSeat().getRow() + " Seat " + ticket.getSeat().getNumber())
            .append("\n")
            .append("1").append("\n")
            .append(getTicketPrice(ticket).divide(BigDecimal.valueOf(1.2), 2, RoundingMode.HALF_UP)).append("\n")
            .append("20%").append("\n")
            .append("\n");
    }

    private void addMerchandiseToReceipt(StringBuilder receipt, Merchandise merchandise, Integer quantity, boolean points) {
        receipt.append(merchandise.getTitle()).append("\n")
            .append(quantity).append("\n")
            .append(merchandise.getPrice().multiply(new BigDecimal(quantity))
                .divide(BigDecimal.valueOf(1.2), 2, RoundingMode.HALF_UP)).append("\n")
            .append("20%").append("\n")
            .append(points ? "100%" : "").append("\n");
    }


    @Transactional(rollbackFor = {UnauthorizedException.class, ConflictException.class, ValidationException.class})
    @Override
    public void cancelItems(Integer userId, Integer orderId, Integer[] tickets, Integer[] merchandise) throws UnauthorizedException, ConflictException, ValidationException {
        LOGGER.debug("cancelItems({}, {}, {}, {})", userId, orderId, tickets, merchandise);

        if (tickets.length == 0 && merchandise.length == 0) {
            throw new ValidationException("Error cancelling order", List.of("No items to cancel"));
        }

        ApplicationUser user = notUserRepository.findApplicationUserById(userId);
        if (user == null) {
            throw new NotFoundException("Could not find User");
        }

        Order order = getAndCheckOrder(userId, orderId);

        List<String> conflictMsg = new ArrayList<>();
        for (Integer ticket : tickets) {
            // check if ticket.getId() is in order.getTickets()
            Optional<Ticket> ticketToCancel = order.getTickets().stream().filter(t -> t.getId().equals(ticket)).findFirst();
            if (ticketToCancel.isEmpty()) {
                conflictMsg.add("Ticket " + ticket + " is not in order");
                continue;
            }
            // check if ticket performance is in the past
            if (ticketToCancel.get().getPerformance().getDatetime().isBefore(LocalDateTime.now())) {
                conflictMsg.add("Ticket " + ticket + " is in the past");
            }
        }
        for (Integer merch : merchandise) {
            // check if merchandise.getId() is in order.getMerchandiseOrdered()
            Optional<MerchandiseOrdered> merchandiseToCancel = order.getMerchandiseOrdered().stream().filter(m -> m.getId().equals(merch)).findFirst();
            if (merchandiseToCancel.isEmpty()) {
                conflictMsg.add("Merchandise " + merch + " is not in order");
                continue;
            }
            // check if order ts is more than 3 days ago
            if (order.getOrderTs().isBefore(LocalDateTime.now().minusDays(3))) {
                conflictMsg.add("Merchandise " + merch + " has already been shipped");
            }
        }
        if (!conflictMsg.isEmpty()) {
            throw new ConflictException("Error cancelling order", conflictMsg);
        }

        BigDecimal price = new BigDecimal(0);
        Integer totalPoints = 0;
        StringBuilder receipt = new StringBuilder();
        Transaction transaction = new Transaction();
        transaction.setOrder(order);

        for (Integer ticket : tickets) {
            // find ticket in order.getTickets()
            Ticket ticketToCancel = order.getTickets().stream().filter(t -> t.getId().equals(ticket)).findFirst().get();
            BigDecimal ticketPrice = getTicketPrice(ticketToCancel);
            price = price.subtract(ticketPrice);
            totalPoints -= (int) floor(ticketPrice.doubleValue());
            ticketToCancel.setOrder(null);
            addTicketToReceipt(receipt, ticketToCancel);
        }

        for (Integer merch : merchandise) {
            // find merchandise in order.getMerchandiseOrdered()
            MerchandiseOrdered merchandiseOrderedToCancel = order.getMerchandiseOrdered().stream().filter(m -> m.getId().equals(merch)).findFirst().get();
            if (!merchandiseOrderedToCancel.getPoints()) {
                BigDecimal merchPrice = merchandiseOrderedToCancel.getMerchandise().getPrice().multiply(new BigDecimal(merchandiseOrderedToCancel.getQuantity()));
                price = price.subtract(merchPrice);
                int points = (int) floor(merchPrice.doubleValue());
                totalPoints -= points;
            } else {
                totalPoints += merchandiseOrderedToCancel.getMerchandise().getPointsPrice() * merchandiseOrderedToCancel.getQuantity();
            }
            addMerchandiseToReceipt(receipt, merchandiseOrderedToCancel.getMerchandise(), merchandiseOrderedToCancel.getQuantity(), merchandiseOrderedToCancel.getPoints());
            merchandiseOrderedRepository.delete(merchandiseOrderedToCancel);
        }

        transaction.setDeductedAmount(price);
        transaction.setDeductedPoints(totalPoints);
        transaction.setTransactionTs(LocalDateTime.now());
        transaction.setReceipt(receipt.toString());
        transactionRepository.save(transaction);
        Integer newPoints = user.getPoints() + totalPoints;
        if (newPoints < 0) {
            throw new ConflictException("Error cancelling order", List.of("User has already spent points"));
        }
        user.setPoints(newPoints);
        if (tickets.length == order.getTickets().size() && merchandise.length == order.getMerchandiseOrdered().size()) {
            order.setCancelled(true);
        }
    }

    @Transactional(rollbackFor = {UnauthorizedException.class, ConflictException.class, ValidationException.class})
    @Override
    public void cancelOrder(Integer userId, Integer orderId) throws UnauthorizedException, ConflictException {
        LOGGER.debug("cancelOrder({}, {})", userId, orderId);
        ApplicationUser user = notUserRepository.findApplicationUserById(userId);
        if (user == null) {
            throw new NotFoundException("Could not find User");
        }

        Order order = getAndCheckOrder(userId, orderId);

        if (!order.getTickets().isEmpty() && order.getTickets().stream().anyMatch(t -> t.getPerformance().getDatetime().isBefore(LocalDateTime.now()))) {
            throw new ConflictException("Error cancelling order", List.of("Ticket has already been used"));
        }

        if (!order.getMerchandiseOrdered().isEmpty() && order.getOrderTs().isBefore(LocalDateTime.now().minusDays(3))) {
            throw new ConflictException("Error cancelling order", List.of("Order has already been shipped"));
        }

        BigDecimal price = new BigDecimal(0);
        Integer totalPoints = 0;
        StringBuilder receipt = new StringBuilder();
        Transaction transaction = new Transaction();
        transaction.setOrder(order);

        for (Ticket ticket : order.getTickets()) {
            BigDecimal ticketPrice = getTicketPrice(ticket);
            price = price.subtract(ticketPrice);
            totalPoints -= (int) floor(ticketPrice.doubleValue());
            ticket.setOrder(null);
            addTicketToReceipt(receipt, ticket);
        }

        for (MerchandiseOrdered merchandiseOrdered : order.getMerchandiseOrdered()) {
            if (!merchandiseOrdered.getPoints()) {
                BigDecimal merchPrice = merchandiseOrdered.getMerchandise().getPrice().multiply(new BigDecimal(merchandiseOrdered.getQuantity()));
                price = price.subtract(merchPrice);
                totalPoints -= (int) floor(merchPrice.doubleValue());
            } else {
                totalPoints += merchandiseOrdered.getMerchandise().getPointsPrice() * merchandiseOrdered.getQuantity();
            }
            addMerchandiseToReceipt(receipt, merchandiseOrdered.getMerchandise(), merchandiseOrdered.getQuantity(), merchandiseOrdered.getPoints());
            merchandiseOrderedRepository.delete(merchandiseOrdered);
        }

        transaction.setDeductedAmount(price);
        transaction.setDeductedPoints(totalPoints);
        transaction.setTransactionTs(LocalDateTime.now());
        transaction.setReceipt(receipt.toString());
        Integer newPoints = user.getPoints() + totalPoints;
        if (newPoints < 0) {
            throw new ConflictException("Error cancelling order", List.of("User has already spent points"));
        }
        transactionRepository.save(transaction);
        user.setPoints(newPoints);
        order.setCancelled(true);
    }


    private Order getAndCheckOrder(Integer userId, Integer orderId) throws UnauthorizedException, ConflictException {
        Order order = orderRepository.getOrderHereById(orderId);
        if (order == null) {
            throw new NotFoundException("Could not find Order");
        }

        if (!order.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("Error cancelling order", List.of("User is not authorized to cancel this order"));
        }

        if (order.getCancelled()) {
            throw new ConflictException("Error cancelling order", List.of("Order is already cancelled"));
        }
        return order;
    }

    private BigDecimal getTicketPrice(Ticket ticket) {
        Optional<PerformanceSector> matchingSector = ticket.getPerformance().getPerformanceSectors()
            .stream()
            .filter(perfSector -> perfSector.getSector() == ticket.getSeat().getSector())
            .findFirst();

        if (matchingSector.isPresent()) {
            return matchingSector.get().getPrice();
        } else {
            throw new FatalException("No Performance Sector assigned");
        }
    }


    @Override
    public Order getOrder(Integer userId, Integer orderId) throws UnauthorizedException {
        LOGGER.debug("cancelOrder({}, {})", userId, orderId);
        ApplicationUser user = notUserRepository.findApplicationUserById(userId);
        if (user == null) {
            throw new NotFoundException("Could not find User");
        }

        Order order = orderRepository.getOrderNowById(orderId);
        if (order == null) {
            throw new NotFoundException("Could not find Order");
        }

        if (!order.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("Error cancelling order", List.of("User is not authorized to cancel this order"));
        }

        return order;
    }

    @Override
    public void getTransaction(Integer userId, Integer transactionId, HttpServletResponse response) throws UnauthorizedException {
        LOGGER.debug("getTransaction({}, {})", userId, transactionId);
        ApplicationUser user = notUserRepository.findApplicationUserById(userId);
        if (user == null) {
            throw new NotFoundException("Could not find User");
        }
        Transaction transaction = transactionRepository.getTransactionById(transactionId);
        if (transaction == null) {
            throw new NotFoundException("Could not find Transaction");
        }
        if (!transaction.getOrder().getUser().getId().equals(userId)) {
            throw new UnauthorizedException("Error getting transaction", List.of("User is not authorized to get this transaction"));
        }
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            PdfPTable company = new PdfPTable(1);
            company.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
            company.getDefaultCell().setPaddingBottom(5);
            company.getDefaultCell().setBorder(Rectangle.NO_BORDER);

            company.addCell(new Paragraph("Ticketline"));
            company.addCell(new Paragraph("Karlsplatz 13, 1040 Wien, AT"));
            company.addCell(new Paragraph("ATU12345678"));

            document.add(company);

            PdfPTable receiver = new PdfPTable(1);
            receiver.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            receiver.getDefaultCell().setPaddingBottom(5);
            receiver.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            receiver.setSpacingAfter(20);

            receiver.addCell(new Paragraph(user.getFirstName() + " " + user.getLastName()));
            Location deliveryAddress = transaction.getOrder().getDeliveryAddress();
            receiver.addCell(new Paragraph(deliveryAddress.getStreet()
                + ", " + deliveryAddress.getPostalCode()
                + " " + deliveryAddress.getCity()
                + ", " + deliveryAddress.getCountry()));
            document.add(receiver);


            Font header = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
            header.setSize(16);
            boolean reversal = transaction.getDeductedAmount().compareTo(BigDecimal.ZERO) < 0 || (transaction.getDeductedAmount().compareTo(BigDecimal.ZERO) == 0 && transaction.getDeductedPoints() > 0);
            Paragraph title = new Paragraph(reversal ? "Stornorechnung" : "Rechnung", header);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(40);
            document.add(title);

            PdfPTable meta = new PdfPTable(2);
            meta.setSpacingAfter(20);

            Paragraph transactionNumber = new Paragraph("Rechnung Nr: " + transaction.getId());
            PdfPCell numberCell = new PdfPCell(transactionNumber);
            numberCell.setBorder(Rectangle.NO_BORDER);
            numberCell.setHorizontalAlignment(Element.ALIGN_LEFT);

            Paragraph transactionDate = new Paragraph(transaction.getTransactionTs().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
            PdfPCell dateCell = new PdfPCell(transactionDate);
            dateCell.setBorder(Rectangle.NO_BORDER);
            dateCell.setHorizontalAlignment(Element.ALIGN_RIGHT);

            meta.addCell(numberCell);
            meta.addCell(dateCell);

            document.add(meta);

            PdfPTable articles = new PdfPTable(5);
            articles.getDefaultCell().setBorder(Rectangle.BOTTOM);
            articles.getDefaultCell().setPaddingBottom(5);
            articles.setSpacingAfter(20);
            articles.setWidths(new float[] {5f, 1.5f, 1f, 1.5f, 1f});

            Font tableHeader = FontFactory.getFont(FontFactory.HELVETICA_BOLD);

            articles.addCell(new Paragraph("Artikel", tableHeader));
            articles.addCell(new Paragraph("Menge", tableHeader));
            articles.addCell(new Paragraph("Preis", tableHeader));
            articles.addCell(new Paragraph("USt-Satz", tableHeader));
            articles.addCell(new Paragraph("Rabatt", tableHeader));

            String[] cells = transaction.getReceipt().split("\n");
            for (String cell : cells) {
                articles.addCell(new Paragraph(cell));
            }
            articles.addCell(new Paragraph(""));

            document.add(articles);

            PdfPTable sum = new PdfPTable(2);
            sum.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            sum.getDefaultCell().setPaddingBottom(5);
            sum.setWidthPercentage(75);
            sum.setHorizontalAlignment(PdfPTable.ALIGN_RIGHT);


            BigDecimal preTax = transaction.getDeductedAmount().divide(BigDecimal.valueOf(1.2), 2, RoundingMode.HALF_UP);
            sum.addCell(new Paragraph("Zwischensumme", tableHeader));
            sum.addCell(preTax.toString());
            sum.addCell(new Paragraph("+20% USt", tableHeader));
            sum.addCell(transaction.getDeductedAmount().subtract(preTax).toString());
            sum.addCell(new Paragraph("Gesamtbetrag", tableHeader));
            sum.addCell(transaction.getDeductedAmount().toString());
            sum.addCell(new Paragraph("Prämienpunkte", tableHeader));
            sum.addCell(String.valueOf(transaction.getDeductedPoints()));

            document.add(sum);
        } catch (DocumentException | IOException e) {
            throw new FatalException("Error creating PDF", e);
        }
        document.close();
    }
}
