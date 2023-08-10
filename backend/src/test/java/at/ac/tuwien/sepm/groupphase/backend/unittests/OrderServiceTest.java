package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CartDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CartTicketDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.OrderDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.bookings.BookingDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.bookings.BookingMerchandiseDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.bookings.BookingTicketDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Hall;
import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import at.ac.tuwien.sepm.groupphase.backend.entity.Merchandise;
import at.ac.tuwien.sepm.groupphase.backend.entity.MerchandiseOrdered;
import at.ac.tuwien.sepm.groupphase.backend.entity.Order;
import at.ac.tuwien.sepm.groupphase.backend.entity.PaymentDetail;
import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;
import at.ac.tuwien.sepm.groupphase.backend.entity.PerformanceSector;
import at.ac.tuwien.sepm.groupphase.backend.entity.Reservation;
import at.ac.tuwien.sepm.groupphase.backend.entity.Seat;
import at.ac.tuwien.sepm.groupphase.backend.entity.Sector;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.HallRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.LocationRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.MerchandiseOrderedRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.MerchandiseRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.NotUserRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.OrderRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.PaymentDetailRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.PerformanceRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.PerformanceSectorRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ReservationRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SeatRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SectorRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TicketRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.OrderService;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class OrderServiceTest {
    @Autowired
    private UserService userService;
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PerformanceRepository performanceRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private HallRepository hallRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private PaymentDetailRepository paymentDetailRepository;

    @Autowired
    private SectorRepository sectorRepository;

    @Autowired
    private PerformanceSectorRepository performanceSectorRepository;

    @Autowired
    private SeatRepository seatRepository;
    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private MerchandiseRepository merchandiseRepository;
    @Autowired
    private MerchandiseOrderedRepository merchandiseOrderedRepository;
    @Autowired
    private NotUserRepository notUserRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    private Performance performance;
    private ApplicationUser user;
    private Location location;
    private Set<Location> locationSet;
    private PaymentDetail paymentDetail;
    private Set<PaymentDetail> paymentDetailSet;
    private Event event;
    private Hall hall;
    private Sector standingSector;
    private Sector seatedSector;
    private PerformanceSector standingPerformanceSector;
    private PerformanceSector seatedPerformanceSector;
    private Merchandise merchandise;
    private Merchandise merchandise2;
    private Seat standingSeat;
    private Seat seatedSeat;
    private CartTicketDto cartTicketDtoSeated;
    private CartTicketDto cartTicketDtoStanding;
    private CartDto cartDto;
    private Ticket standingTicket;
    private Ticket seatedTicket;
    private CartDto cartDto2;
    private CartDto cartDto3;
    private ApplicationUser user2;

    private BookingDto bookingDto;
    private BookingDto bookingDto2;
    private BookingDto bookingDto3;
    private BookingDto bookingDtoOnlyMerch;
    private BookingDto bookingDtoOnlyMerch2;
    private BookingDto bookingDtoOnlyMerch3;
    private BookingDto bookingDtoInvalid;
    private BookingDto bookingDtoOnlyMerch4;
    private BookingDto bookingDtoMerchAndTickets;
    private BookingTicketDto bookingTicketDto1;
    private BookingTicketDto bookingTicketDto2;
    private BookingMerchandiseDto bookingMerchandiseDto1;
    private BookingMerchandiseDto bookingMerchandiseDto2;
    private BookingMerchandiseDto bookingMerchandiseDto3;
    private BookingMerchandiseDto bookingMerchandiseDtoInvalid;


    @BeforeEach
    public void beforeAll() {
        event = new Event();
        event.setName("The Eras Tour");
        event.setLength(Duration.ZERO);
        eventRepository.save(event);

        location = new Location();
        location.setCity("Vienna");
        location.setCountry("Austria");
        location.setPostalCode(1120);
        location.setStreet("Straße 1");

        locationSet = new HashSet<>();
        locationSet.add(location);


        this.user = new ApplicationUser();
        this.user.setId(1);
        this.user.setEmail("hallo@123");
        this.user.setAdmin(false);
        this.user.setFirstName("Theo");
        this.user.setLastName("Kretz");
        this.user.setPassword("Password");
        this.user.setLocked(false);
        this.user.setFailedLogin(0);
        this.user.setPoints(100);

        this.user2 = new ApplicationUser();
        this.user2.setId(9);
        this.user2.setEmail("hallo@12332323");
        this.user2.setAdmin(false);
        this.user2.setFirstName("Hallo");
        this.user2.setLastName("Kretz");
        this.user2.setPassword("Password2");
        this.user2.setLocked(false);
        this.user2.setFailedLogin(0);
        this.user2.setPoints(10000);
        notUserRepository.save(user2);

        paymentDetail = new PaymentDetail();
        paymentDetail.setCvv(222);
        paymentDetail.setCardHolder("hallo");
        paymentDetail.setCardNumber("23123131");
        paymentDetail.setExpirationDate(LocalDate.now().plusDays(10));
        paymentDetail.setUser(user);

        paymentDetailSet = new HashSet<>();
        paymentDetailSet.add(paymentDetail);
        user.setPaymentDetails(paymentDetailSet);
        user.setLocations(locationSet);
        notUserRepository.save(user);

        location.setUser(user);
        locationRepository.save(location);
        paymentDetailRepository.save(paymentDetail);

        hall = new Hall();
        hall.setName("Halle 1");
        hall.setLocation(location);
        hallRepository.save(hall);

        this.performance = new Performance();
        this.performance.setDatetime(LocalDateTime.now().plusDays(10));
        this.performance.setEvent(event);
        this.performance.setHall(hall);
        this.performanceRepository.save(performance);


        standingSector = new Sector();
        standingSector.setHall(hall);
        standingSector.setName("Standing Sector 1");
        standingSector.setStanding(true);
        sectorRepository.save(standingSector);

        seatedSector = new Sector();
        seatedSector.setStanding(false);
        seatedSector.setName("Seated Sector 1");
        seatedSector.setHall(hall);
        sectorRepository.save(seatedSector);

        standingPerformanceSector = new PerformanceSector();
        standingPerformanceSector.setPerformance(performance);
        standingPerformanceSector.setSector(standingSector);
        standingPerformanceSector.setPrice(BigDecimal.valueOf(100.0));
        standingPerformanceSector.setPointsReward(100);
        performanceSectorRepository.save(standingPerformanceSector);

        seatedPerformanceSector = new PerformanceSector();
        seatedPerformanceSector.setPerformance(performance);
        seatedPerformanceSector.setSector(seatedSector);
        seatedPerformanceSector.setPrice(BigDecimal.valueOf(50.99));
        seatedPerformanceSector.setPointsReward(50);
        performanceSectorRepository.save(seatedPerformanceSector);

        Set<PerformanceSector> performanceSectorSet = new HashSet<>();
        performanceSectorSet.add(standingPerformanceSector);
        performanceSectorSet.add(seatedPerformanceSector);

        seatedSector.setPerformanceSectors(performanceSectorSet);
        standingSector.setPerformanceSectors(performanceSectorSet);


        standingSeat = new Seat();
        standingSeat.setSector(standingSector);
        standingSeat.setRow(5);
        standingSeat.setNumber(5);
        seatRepository.save(standingSeat);

        seatedSeat = new Seat();
        seatedSeat.setNumber(1);
        seatedSeat.setRow(1);
        seatedSeat.setSector(seatedSector);
        seatRepository.save(seatedSeat);


        seatedTicket = new Ticket();
        seatedTicket.setSeat(seatedSeat);
        seatedTicket.setPerformance(performance);
        ticketRepository.save(seatedTicket);

        standingTicket = new Ticket();
        standingTicket.setSeat(standingSeat);
        standingTicket.setPerformance(performance);
        ticketRepository.save(standingTicket);

        cartTicketDtoSeated = new CartTicketDto();
        cartTicketDtoStanding = new CartTicketDto();
        cartTicketDtoSeated.setId(seatedSeat.getId());
        cartTicketDtoSeated.setSeatNumber(seatedSeat.getNumber());
        cartTicketDtoSeated.setSeatRow(seatedSeat.getRow());
        cartTicketDtoStanding.setId(standingSeat.getId());
        cartTicketDtoStanding.setSeatNumber(standingSeat.getNumber());
        cartTicketDtoStanding.setSeatRow(standingSeat.getRow());
        cartDto = new CartDto();
        List<CartTicketDto> ticketList = new ArrayList<>();
        ticketList.add(cartTicketDtoSeated);
        ticketList.add(cartTicketDtoStanding);
        cartDto.setUserId(1);
        cartDto.setTickets(ticketList);

        cartDto2 = new CartDto();
        cartDto2.setUserId(999);

        cartDto3 = new CartDto();
        cartDto3.setUserId(9);

        bookingTicketDto1 = new BookingTicketDto();
        bookingTicketDto1.setTicketId(standingTicket.getId());
        bookingTicketDto1.setReservation(false);
        List<BookingTicketDto> bookingTicketDtoList = new ArrayList<>();
        bookingTicketDtoList.add(bookingTicketDto1);

        bookingTicketDto2 = new BookingTicketDto();
        bookingTicketDto2.setTicketId(seatedTicket.getId());
        bookingTicketDto2.setReservation(false);
        bookingTicketDtoList.add(bookingTicketDto2);

        bookingDto = new BookingDto();
        bookingDto.setTickets(bookingTicketDtoList);
        bookingDto.setLocationId(location.getId());
        bookingDto.setPaymentDetailId(paymentDetail.getId());

        bookingDto2 = new BookingDto();


        //merch
        merchandise = new Merchandise();
        merchandise.setDescription("Test");
        merchandise.setTitle("Test");
        merchandise.setPrice(BigDecimal.valueOf(20.0));
        merchandise.setPointsPrice(80);
        merchandise.setPointsReward(20);
        merchandiseRepository.save(merchandise);

        merchandise2 = new Merchandise();
        merchandise2.setDescription("Test2");
        merchandise2.setTitle("Test2");
        merchandise2.setPrice(BigDecimal.valueOf(20.0));
        merchandise2.setPointsPrice(80);
        merchandise2.setPointsReward(20);
        merchandiseRepository.save(merchandise2);

        bookingMerchandiseDto1 = new BookingMerchandiseDto();
        bookingMerchandiseDto1.setId(merchandise.getId());
        bookingMerchandiseDto1.setQuantity(5);
        bookingMerchandiseDto1.setBuyWithPoints(false);

        bookingMerchandiseDto2 = new BookingMerchandiseDto();
        bookingMerchandiseDto2.setId(merchandise2.getId());
        bookingMerchandiseDto2.setQuantity(1);
        bookingMerchandiseDto2.setBuyWithPoints(true);

        bookingMerchandiseDto3 = new BookingMerchandiseDto();
        bookingMerchandiseDto3.setId(merchandise2.getId());
        bookingMerchandiseDto3.setQuantity(3);
        bookingMerchandiseDto3.setBuyWithPoints(true);

        bookingMerchandiseDtoInvalid = new BookingMerchandiseDto();
        bookingMerchandiseDtoInvalid.setId(999);
        bookingMerchandiseDtoInvalid.setQuantity(3);
        bookingMerchandiseDtoInvalid.setBuyWithPoints(true);

        bookingDtoOnlyMerch = new BookingDto();
        bookingDtoOnlyMerch.setLocationId(location.getId());
        bookingDtoOnlyMerch.setPaymentDetailId(paymentDetail.getId());

        bookingDtoOnlyMerch2 = new BookingDto();
        bookingDtoOnlyMerch2.setLocationId(location.getId());
        bookingDtoOnlyMerch2.setPaymentDetailId(paymentDetail.getId());

        bookingDtoOnlyMerch3 = new BookingDto();
        bookingDtoOnlyMerch3.setLocationId(location.getId());
        bookingDtoOnlyMerch3.setPaymentDetailId(paymentDetail.getId());

        bookingDtoOnlyMerch4 = new BookingDto();
        bookingDtoOnlyMerch4.setLocationId(location.getId());
        bookingDtoOnlyMerch4.setPaymentDetailId(paymentDetail.getId());

        bookingDtoMerchAndTickets = new BookingDto();
        bookingDtoMerchAndTickets.setLocationId(location.getId());
        bookingDtoMerchAndTickets.setPaymentDetailId(paymentDetail.getId());

        bookingDtoInvalid = new BookingDto();
        bookingDtoInvalid.setLocationId(location.getId());
        bookingDtoInvalid.setPaymentDetailId(paymentDetail.getId());

        List<BookingMerchandiseDto> merchandiseDtoList = new ArrayList<>();
        merchandiseDtoList.add(bookingMerchandiseDto1);
        bookingDtoOnlyMerch.setMerchandise(merchandiseDtoList);

        List<BookingMerchandiseDto> merchandiseDtoList2 = new ArrayList<>();
        merchandiseDtoList2.add(bookingMerchandiseDto2);
        bookingDtoOnlyMerch2.setMerchandise(merchandiseDtoList2);

        List<BookingMerchandiseDto> merchandiseDtoList3 = new ArrayList<>();
        merchandiseDtoList3.add(bookingMerchandiseDto1);
        merchandiseDtoList3.add(bookingMerchandiseDto2);
        bookingDtoOnlyMerch3.setMerchandise(merchandiseDtoList3);


        bookingDtoMerchAndTickets.setMerchandise(merchandiseDtoList2);
        bookingDtoMerchAndTickets.setTickets(bookingTicketDtoList);

        List<BookingMerchandiseDto> merchandiseDtoList4 = new ArrayList<>();
        merchandiseDtoList4.add(bookingMerchandiseDto3);
        bookingDtoOnlyMerch4.setMerchandise(merchandiseDtoList4);

        List<BookingMerchandiseDto> merchandiseDtoListInvalid = new ArrayList<>();
        merchandiseDtoListInvalid.add(bookingMerchandiseDtoInvalid);
        bookingDtoInvalid.setMerchandise(merchandiseDtoListInvalid);

    }


    //Tickets
    @Test
    void buyValidTicketsReturnCorrectOrder() throws ConflictException, ValidationException {

        OrderDto order = orderService.buyTickets(1, bookingDto);
        Order orderEntity = orderRepository.getOrderById(order.getId());
        assertThat(orderEntity)
            .isNotNull()
            .extracting("id", "cancelled", "tickets")
            .contains(order.getId(), false, orderEntity.getTickets());
    }

    @Test
    void buyTicketsWithInvalidUserShouldThrow() {
        assertThrows(NotFoundException.class, () -> orderService.buyTickets(99999, bookingDto));
    }

    @Test
    void buyTicketsEmptyBookingDtoShouldThrow() {
        assertThrows(ValidationException.class, () -> orderService.buyTickets(1, bookingDto2));
    }

    @Test
    void buyTicketsReturnCorrectPrice() throws ConflictException, ValidationException {
        OrderDto order = orderService.buyTickets(1, bookingDto);
        Order orderEntity = orderRepository.getOrderById(order.getId());
        BigDecimal price = orderEntity.getTransactions().iterator().next().getDeductedAmount();

        assertThat(price)
            .isEqualTo(BigDecimal.valueOf(150.99));

    }

    @Test
    void buyTicketsReturnCorrectPoints() throws ConflictException, ValidationException {
        user.setPoints(100);
        OrderDto order = orderService.buyTickets(1, bookingDto);
        Order orderEntity = orderRepository.getOrderById(order.getId());
        int points = orderEntity.getTransactions().iterator().next().getDeductedPoints();

        assertThat(points)
            .isEqualTo(150);
    }

    //Merchandise
    @Test
    void buyMerchReturnCorrectOrder() throws ConflictException, ValidationException {
        user.setPoints(100);
        OrderDto order = orderService.buyTickets(1, bookingDtoOnlyMerch);
        MerchandiseOrdered merchandiseOrdered = merchandiseOrderedRepository.findByOrderId(order.getId()).get(0);
        assertThat(merchandiseOrdered)
            .isNotNull()
            .extracting("points", "quantity", "order.id")
            .contains(false, 5, order.getId());
    }

    @Test
    void buyMerchWithPoints() throws ConflictException, ValidationException {
        user.setPoints(100);
        OrderDto order = orderService.buyTickets(1, bookingDtoOnlyMerch2);
        Order orderEntity = orderRepository.getOrderById(order.getId());
        MerchandiseOrdered merchandiseOrdered = merchandiseOrderedRepository.findByOrderId(order.getId()).get(0);
        assertThat(merchandiseOrdered)
            .isNotNull()
            .extracting("points", "quantity", "order.id")
            .contains(true, 1, order.getId());

        int points = orderEntity.getTransactions().iterator().next().getDeductedPoints();
        assertThat(points)
            .isEqualTo(-80);

        BigDecimal price = orderEntity.getTransactions().iterator().next().getDeductedAmount();
        assertThat(price)
            .isZero();

        ApplicationUser user = notUserRepository.getApplicationUserById(1);
        int userPoints = user.getPoints();
        assertThat(userPoints)
            .isEqualTo(20);
    }

    @Test
    void buyMerchWithPointsAndWithMoney() throws ConflictException, ValidationException {
        user.setPoints(100);
        OrderDto order = orderService.buyTickets(1, bookingDtoOnlyMerch3);
        Order orderEntity = orderRepository.getOrderById(order.getId());

        int points = orderEntity.getTransactions().iterator().next().getDeductedPoints();
        assertThat(points)
            .isEqualTo(20);

        BigDecimal expectedPrice = new BigDecimal("100.00");
        BigDecimal price = orderEntity.getTransactions().iterator().next().getDeductedAmount();
        assertThat(price)
            .isEqualTo(expectedPrice);

        ApplicationUser user = notUserRepository.getApplicationUserById(1);
        int userPoints = user.getPoints();
        assertThat(userPoints)
            .isEqualTo(120);
    }

    @Test
    void buyMerchWithPointsAndTicketsWithMoney() throws ConflictException, ValidationException {
        user.setPoints(100);
        OrderDto order = orderService.buyTickets(1, bookingDtoMerchAndTickets);
        Order orderEntity = orderRepository.getOrderById(order.getId());

        int points = orderEntity.getTransactions().iterator().next().getDeductedPoints();
        assertThat(points)
            .isEqualTo(70);

        BigDecimal expectedprice = new BigDecimal("150.99");
        BigDecimal price = orderEntity.getTransactions().iterator().next().getDeductedAmount();
        assertThat(price)
            .isEqualTo(expectedprice);

        ApplicationUser user = notUserRepository.getApplicationUserById(1);
        int userPoints = user.getPoints();
        assertThat(userPoints)
            .isEqualTo(170);
    }

    @Test
    void buyMerchWithNotEnoughPointsShouldThrow() {
        user.setPoints(100);
        assertThrows(ValidationException.class, () -> orderService.buyTickets(1, bookingDtoOnlyMerch4));
    }

    @Test
    void buyMerchWithInvalidMerchIdShouldThrow() {
        user.setPoints(100);
        assertThrows(NotFoundException.class, () -> orderService.buyTickets(1, bookingDtoInvalid));
    }

}
