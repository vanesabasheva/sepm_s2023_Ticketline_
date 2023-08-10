package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CartDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CartTicketDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.user.UserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Hall;
import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import at.ac.tuwien.sepm.groupphase.backend.entity.PaymentDetail;
import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;
import at.ac.tuwien.sepm.groupphase.backend.entity.PerformanceSector;
import at.ac.tuwien.sepm.groupphase.backend.entity.Reservation;
import at.ac.tuwien.sepm.groupphase.backend.entity.Seat;
import at.ac.tuwien.sepm.groupphase.backend.entity.Sector;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.HallRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.LocationRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.OrderRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.PaymentDetailRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.PerformanceRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.PerformanceSectorRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ReservationRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SeatRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SectorRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TicketRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.CartService;
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

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class CartServiceTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PerformanceRepository performanceRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private HallRepository hallRepository;

    @Autowired
    private at.ac.tuwien.sepm.groupphase.backend.repository.NotUserRepository NotUserRepository;
    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private UserMapper userMapper;

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
    private ReservationRepository reservationRepository;

    private Performance performance;
    private ApplicationUser user;
    private Location location;
    private Set<Location> locationSet;
    private PaymentDetail paymentDetail;
    private Set<PaymentDetail> paymentDetailSet;
    private UserDto userDto;
    private Event event;
    private Hall hall;
    private Sector standingSector;
    private Sector seatedSector;
    private PerformanceSector standingPerformanceSector;
    private PerformanceSector seatedPerformanceSector;
    private Seat standingSeat;
    private Seat seatedSeat;
    private CartTicketDto cartTicketDtoSeated;
    private CartTicketDto cartTicketDtoStanding;
    private CartDto cartDto;
    private Ticket standingTicket;
    private Ticket seatedTicket;
    private Reservation reservation1;
    private Reservation reservation2;

    @BeforeEach
    public void beforeAll() {
        event = new Event();
        event.setName("The Eras Tour 2");
        event.setLength(Duration.ZERO);
        eventRepository.save(event);

        location = new Location();
        location.setCity("Vienna");
        location.setCountry("Austria");
        location.setPostalCode(1130);
        location.setStreet("Straße 2");

        locationSet = new HashSet<>();
        locationSet.add(location);
        locationRepository.save(location);

        hall = new Hall();
        hall.setName("Halle 2");
        hall.setLocation(location);
        hallRepository.save(hall);

        this.performance = new Performance();
        this.performance.setDatetime(LocalDateTime.now());
        this.performance.setEvent(event);
        this.performance.setHall(hall);
        this.performanceRepository.save(performance);

        this.user = new ApplicationUser();
        this.user.setEmail("hallo@12334");
        this.user.setAdmin(false);
        this.user.setFirstName("Vanesa");
        this.user.setLastName("Besheva");
        this.user.setPassword("Password");
        this.user.setLocked(false);
        this.user.setPoints(10000);

        paymentDetail = new PaymentDetail();
        paymentDetail.setCvv(222);
        paymentDetail.setCardHolder("hallo2");
        paymentDetail.setCardNumber("23123131");
        paymentDetail.setExpirationDate(LocalDate.now());
        paymentDetail.setUser(user);

        paymentDetailSet = new HashSet<>();
        paymentDetailSet.add(paymentDetail);
        user.setPaymentDetails(paymentDetailSet);
        user.setLocations(locationSet);

        NotUserRepository.save(user);
        paymentDetailRepository.save(paymentDetail);

        userDto = new UserDto();
        userDto = userMapper.applicationUserToDto(user);

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
        seatedPerformanceSector.setPrice(BigDecimal.valueOf(50.0));
        seatedPerformanceSector.setPointsReward(50);
        performanceSectorRepository.save(seatedPerformanceSector);

        Set<PerformanceSector> performanceSectorSet = new HashSet<>();
        performanceSectorSet.add(standingPerformanceSector);
        performanceSectorSet.add(seatedPerformanceSector);

        seatedSector.setPerformanceSectors(performanceSectorSet);
        standingSector.setPerformanceSectors(performanceSectorSet);


        standingSeat = new Seat();
        standingSeat.setSector(standingSector);
        standingSeat.setRow(6);
        standingSeat.setNumber(6);
        seatRepository.save(standingSeat);

        seatedSeat = new Seat();
        seatedSeat.setNumber(2);
        seatedSeat.setRow(2);
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
        cartDto.setTickets(ticketList);

        reservation1 = new Reservation();
        reservation2 = new Reservation();
        reservation1.setUser(user);
        reservation1.setExpirationTs(LocalDateTime.now());
        reservation1.setCart(true);
        reservation1.setTicket(seatedTicket);
        reservationRepository.save(reservation1);

        reservation2.setUser(user);
        reservation2.setExpirationTs(LocalDateTime.now());
        reservation2.setCart(true);
        reservation2.setTicket(standingTicket);
        reservationRepository.save(reservation2);
    }

    @Test
    void getCartShouldReturnTickets() {

        CartDto cartDto = cartService.getCart(user.getId());
        List<CartTicketDto> list = cartDto.getTickets();
        assertThat(list)
            .isNotEmpty()
            .hasSize(2);
    }

    @Test
    void getCartShouldReturnRightAmountOfPoints() {
        CartDto cartDto = cartService.getCart(user.getId());
        List<CartTicketDto> list = cartDto.getTickets();
        assertThat(list).isNotEmpty();
        assertThat(cartDto.getUserPoints()).isEqualTo(user.getPoints());
    }
}
