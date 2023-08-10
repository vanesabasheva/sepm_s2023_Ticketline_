package at.ac.tuwien.sepm.groupphase.backend.service.impl;


import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CartDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CartTicketDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleTicketDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.TicketMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import at.ac.tuwien.sepm.groupphase.backend.entity.PaymentDetail;
import at.ac.tuwien.sepm.groupphase.backend.entity.PerformanceSector;
import at.ac.tuwien.sepm.groupphase.backend.entity.Reservation;
import at.ac.tuwien.sepm.groupphase.backend.entity.Sector;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepm.groupphase.backend.exception.FatalException;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.UnauthorizedException;
import at.ac.tuwien.sepm.groupphase.backend.repository.LocationRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.NotUserRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.PaymentDetailRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ReservationRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TicketRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.CartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
public class CustomCartService implements CartService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final TicketRepository ticketRepository;
    private final NotUserRepository notUserRepository;
    private final ReservationRepository reservationRepository;
    private final PaymentDetailRepository paymentDetailRepository;
    private final LocationRepository locationRepository;

    private final TicketMapper ticketMapper;

    public CustomCartService(TicketRepository ticketRepository, NotUserRepository notUserRepository, ReservationRepository reservationRepository,
                             TicketMapper ticketMapper, PaymentDetailRepository paymentDetailRepository, LocationRepository locationRepository) {
        this.ticketRepository = ticketRepository;
        this.notUserRepository = notUserRepository;
        this.reservationRepository = reservationRepository;
        this.ticketMapper = ticketMapper;
        this.paymentDetailRepository = paymentDetailRepository;
        this.locationRepository = locationRepository;
    }

    @Override
    public CartDto getCart(Integer userId) {
        LOGGER.debug("Get Cart of User {}", userId);
        ApplicationUser user = notUserRepository.findApplicationUserById(userId);
        if (user == null) {
            throw new NotFoundException("Could not find User");
        }
        List<CartTicketDto> tickets = new ArrayList<>();
        Set<Reservation> reservationSet = user.getReservations();

        for (Reservation r : reservationSet) {
            if (!r.getCart()) {
                continue;
            }
            Ticket ticket = ticketRepository.findTicketById(r.getTicket().getId());

            //price
            BigDecimal price;
            Optional<PerformanceSector> matchingSector = ticket.getPerformance().getPerformanceSectors()
                .stream()
                .filter(perfSector -> perfSector.getSector().getId().equals(ticket.getSeat().getSector().getId()))
                .findFirst();

            if (matchingSector.isPresent()) {
                price = matchingSector.get().getPrice();
            } else {
                throw new FatalException("No Performance Sector assigned");
            }

            CartTicketDto cartTicketDto = CartTicketDto.CartTicketDtoBuilder.aCartTicketDto()
                .withId(ticket.getId())
                .withSeatRow(ticket.getSeat().getRow())
                .withSeatNumber(ticket.getSeat().getNumber())
                .withSectorName(ticket.getSeat().getSector().getName())
                .withStanding(ticket.getSeat().getSector().getStanding())
                .withDate(ticket.getPerformance().getDatetime())
                .withEventName(ticket.getPerformance().getEvent().getName())
                .withHallName(ticket.getPerformance().getHall().getName())
                .withLocationCity(ticket.getPerformance().getHall().getLocation().getCity())
                .withLocationStreet(ticket.getPerformance().getHall().getLocation().getStreet())
                .withPrice(price)
                .build();
            tickets.add(cartTicketDto);

        }
        // to keep the cart consistent between reloads
        tickets.sort(Comparator.comparing(CartTicketDto::getId));

        CartDto cartDto = CartDto.CartDtoBuilder.aCartDto()
            .withUserId(userId)
            .withUserPoints(user.getPoints())
            .withTickets(tickets)
            .build();

        return cartDto;
    }


    @Override
    public List<Reservation> reserveTickets(Integer userId, List<SimpleTicketDto> tickets) throws ConflictException {

        Optional<ApplicationUser> user = notUserRepository.findById(userId);


        if (user.isEmpty()) {
            throw new NotFoundException("User not found");
        }

        List<Ticket> foundTickets = ticketRepository.findTicketsBySimpleTicketDtoList(tickets);
        if (foundTickets.isEmpty()) {
            throw new NotFoundException("No tickets found");
        }
        List<String> conflictMsg = new ArrayList<>();
        if (foundTickets.size() != tickets.size()) {
            List<SimpleTicketDto> foundTicketsDto = ticketMapper.ticketToSimpleTicket(foundTickets);
            tickets.removeAll(foundTicketsDto);
            for (SimpleTicketDto ticket : tickets) {
                conflictMsg.add("Ticket " + ticket.getId() + " not found");
            }
            throw new ConflictException("At least one ticket does not exist", conflictMsg);
        }

        List<Reservation> reserved = new ArrayList<>();
        Map<Integer, List<Ticket>> ticketsBySector = new HashMap<>();
        for (Ticket ticket : foundTickets) {
            // check if ticket performance is in the past
            if (ticket.getPerformance().getDatetime().minusMinutes(30).isBefore(LocalDateTime.now())) {
                conflictMsg.add("Ticket " + ticket.getId() + " is in the past or starting too soon");
                continue;
            }
            // check if ticket is already bought
            if (ticket.getOrder() != null || (ticket.getReservation() != null
                && ticket.getReservation().getExpirationTs().isAfter(LocalDateTime.now())
                && !ticket.getReservation().getUser().getId().equals(userId))) {
                // if ticket cannot be reserved, check if it is standing
                Sector sector = ticket.getSeat().getSector();
                if (sector.getStanding()) {
                    // if ticket is standing, find any ticket in the same sector that is not reserved or bought and reserve it
                    if (!ticketsBySector.containsKey(sector.getId())) {
                        // if no ticket in this sector has been fetched, find all tickets in this sector
                        List<Ticket> sectorTickets = ticketRepository.findBySeatSectorId(sector.getId());
                        ticketsBySector.put(sector.getId(), sectorTickets);
                    }
                    // find any ticket in this sector that is not reserved or bought
                    Optional<Ticket> availableTicket = ticketsBySector.get(sector.getId()).stream().filter(t ->
                        t.getOrder() == null
                            && (t.getReservation() == null || t.getReservation().getExpirationTs().isBefore(LocalDateTime.now()))
                    ).findFirst();
                    if (availableTicket.isPresent()) {
                        // if such a ticket exists, reserve it
                        addTicketToReserved(user.get(), reserved, availableTicket.get());
                        ticketsBySector.get(sector.getId()).remove(availableTicket.get());
                        continue;
                    }
                }
                conflictMsg.add("Ticket: " + ticket.getId() + " cannot be added to cart, because it is already reserved or bought");
                continue;
            }
            addTicketToReserved(user.get(), reserved, ticket);
        }
        if (conflictMsg.size() > 0) {
            throw new ConflictException("Error adding tickets to cart", conflictMsg);
        }
        LOGGER.info("reserving tickets {}", tickets);
        reservationRepository.saveAll(reserved);
        return reserved;
    }

    /**
     * Delete ticket from cart.
     *
     * @param userId   the user id
     * @param ticketId the ticket id
     */
    @Override
    public void deleteTicketFromCart(Integer userId, Integer ticketId) throws ConflictException, UnauthorizedException {
        LOGGER.debug("Delete Ticket {} from Cart of User {}", ticketId, userId);
        ApplicationUser user = notUserRepository.findApplicationUserById(userId);
        if (user == null) {
            throw new NotFoundException("Could not find User");
        }
        Reservation reservation = reservationRepository.findReservationByTicketId(ticketId);
        if (reservation == null) {
            throw new NotFoundException("Could not find Reservation");
        }
        List<String> conflictMsg = new ArrayList<>();
        if (!reservation.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("Could not delete ticekt from cart", List.of("User is not authorized"));
        }
        if (!reservation.getCart()) {
            conflictMsg.add("Ticket is not in cart");
        }
        if (!conflictMsg.isEmpty()) {
            throw new ConflictException("Could not delete ticket from cart", conflictMsg);
        }
        if (reservation.getExpirationTs().equals(reservation.getTicket().getPerformance().getDatetime().minusMinutes(30))) {
            // reservation is full reservation added to cart
            reservation.setCart(false);
            reservationRepository.save(reservation);
        } else {
            reservationRepository.delete(reservation);
        }
    }

    private void addTicketToReserved(ApplicationUser user, List<Reservation> reserved, Ticket ticket) {
        LOGGER.debug("Adding Ticket {} to reserved", ticket.getId());
        Integer id = ticket.getReservation() == null ? null : ticket.getReservation().getId();
        LocalDateTime expiration = ticket.getReservation() == null ? LocalDateTime.now().plusMinutes(15) : ticket.getReservation().getExpirationTs();
        Reservation reservation = Reservation.ReservationBuilder.aReservation()
            .withId(id)
            .withTicket(ticket)
            .withCart(true)
            .withExpirationTs(expiration)
            .withUser(user)
            .build();
        reserved.add(reservation);
    }
}
