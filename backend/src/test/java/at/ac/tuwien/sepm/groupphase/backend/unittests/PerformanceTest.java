package at.ac.tuwien.sepm.groupphase.backend.unittests;


import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CartDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CartTicketDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.DetailedPerformanceDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PerformanceTicketDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.user.UserDto;
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
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.HallRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.LocationRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.PaymentDetailRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.PerformanceRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.PerformanceSectorRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ReservationRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SeatRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SectorRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TicketRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.PerformanceService;
import org.junit.jupiter.api.Assertions;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class PerformanceTest {


    @Autowired
    PerformanceService performanceService;
    @Autowired
    private PerformanceRepository performanceRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private HallRepository hallRepository;
    @Autowired
    private at.ac.tuwien.sepm.groupphase.backend.repository.NotUserRepository NotUserRepository;
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
    private ReservationRepository reservationRepository;
    private Performance performance;
    private ApplicationUser user;
    private ApplicationUser user2;
    private Location location;
    private Location location1;
    private Set<Location> locationSet;
    private PaymentDetail paymentDetail;
    private Set<PaymentDetail> paymentDetailSet;
    private UserDto userDto;
    private Event event;
    private Event event2;
    private Hall hall;
    private Sector standingSector;
    private Sector seatedSector;
    private PerformanceSector standingPerformanceSector;
    private PerformanceSector seatedPerformanceSector;
    private Seat standingSeat;
    private Seat seatedSeat;
    private Seat seatedSeat2;
    private CartTicketDto cartTicketDtoSeated;
    private CartTicketDto cartTicketDtoStanding;
    private CartDto cartDto;
    private Ticket standingTicket;
    private Ticket seatedTicket;
    private Ticket seatedTicket2;
    private Reservation reservation1;
    private Reservation reservation2;
    private Reservation reservation3;

    @BeforeEach
    public void beforeAll() {
        event = new Event();
        event.setName("The Eras Tour 2");
        event.setLength(Duration.ZERO);
        eventRepository.save(event);

        event2 = new Event();
        event2.setName("The Eras Tour 3");
        event2.setLength(Duration.ZERO);
        eventRepository.save(event2);

        location = new Location();
        location.setCity("Vienna");
        location.setCountry("Austria");
        location.setPostalCode(1130);
        location.setStreet("Straße 2");

        locationSet = new HashSet<>();
        locationSet.add(location);
        locationRepository.save(location);

        location1 = new Location();
        location1.setCity("Vienna");
        location1.setCountry("Austria");
        location1.setPostalCode(1030);
        location1.setStreet("Straße 3");

        locationRepository.save(location1);

        hall = new Hall();
        hall.setName("Halle 2");
        hall.setLocation(location);
        hallRepository.save(hall);

        this.performance = new Performance();
        this.performance.setDatetime(LocalDateTime.of(2023, 10, 10, 10, 10));
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

        this.user2 = new ApplicationUser();
        this.user2.setEmail("tschau@12334");
        this.user2.setAdmin(false);
        this.user2.setFirstName("Vanesa");
        this.user2.setLastName("Besheva");
        this.user2.setPassword("Password");
        this.user2.setLocked(false);
        this.user2.setPoints(10000);

        paymentDetail = new PaymentDetail();
        paymentDetail.setCvv(222);
        paymentDetail.setCardHolder("hallo2");
        paymentDetail.setCardNumber("23123131");
        paymentDetail.setExpirationDate(LocalDate.of(2024, 10, 10));
        paymentDetail.setUser(user);

        paymentDetailSet = new HashSet<>();
        paymentDetailSet.add(paymentDetail);
        user.setPaymentDetails(paymentDetailSet);
        user.setLocations(locationSet);
        user2.setPaymentDetails(paymentDetailSet);
        user2.setLocations(locationSet);

        NotUserRepository.save(user);
        NotUserRepository.save(user2);
        paymentDetailRepository.save(paymentDetail);


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

        seatedSeat2 = new Seat();
        seatedSeat2.setNumber(3);
        seatedSeat2.setRow(2);
        seatedSeat2.setSector(seatedSector);
        seatRepository.save(seatedSeat2);


        seatedTicket = new Ticket();
        seatedTicket.setSeat(seatedSeat);
        seatedTicket.setPerformance(performance);
        ticketRepository.save(seatedTicket);

        standingTicket = new Ticket();
        standingTicket.setSeat(standingSeat);
        standingTicket.setPerformance(performance);
        ticketRepository.save(standingTicket);

        seatedTicket2 = new Ticket();
        seatedTicket2.setSeat(seatedSeat2);
        seatedTicket2.setPerformance(performance);
        ticketRepository.save(seatedTicket2);

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
        reservation1.setExpirationTs(LocalDateTime.of(2023, 9, 9, 10, 10));
        reservation1.setCart(false);
        reservation1.setTicket(seatedTicket);
        reservationRepository.save(reservation1);

        reservation2.setUser(user);
        reservation2.setExpirationTs(LocalDateTime.of(2023, 9, 9, 10, 10));
        reservation2.setCart(false);
        reservation2.setTicket(standingTicket);
        reservationRepository.save(reservation2);

        reservation3 = new Reservation();
        reservation3.setUser(user2);
        reservation3.setExpirationTs(LocalDateTime.of(2023, 9, 9, 10, 10));
        reservation3.setCart(true);
        reservation3.setTicket(seatedTicket2);
        reservationRepository.save(reservation3);

    }


    @Test
    void getPerformanceShouldReturnNotFound() {
        Assertions.assertThrows(NotFoundException.class, () -> performanceService.getPerformancePlanById(-1));
    }


    @Test
    void getPerformanceShouldMatchNamesAndLocations() {
        Assertions.assertDoesNotThrow(() -> performanceService.getPerformancePlanById(1));
        DetailedPerformanceDto detailedPerformanceDto = performanceService.getPerformancePlanById(performance.getId());

        assertEquals(detailedPerformanceDto.getDateTime(), performance.getDatetime());
        assertEquals(detailedPerformanceDto.getEventName(), performance.getEvent().getName());
        assertEquals(detailedPerformanceDto.getHallName(), performance.getHall().getName());
        assertEquals(detailedPerformanceDto.getLocation().getCountry(), location.getCountry());
        assertEquals(detailedPerformanceDto.getLocation().getCity(), location.getCity());
        assertEquals(detailedPerformanceDto.getLocation().getPostalCode(), location.getPostalCode());
        assertEquals(detailedPerformanceDto.getLocation().getStreet(), location.getStreet());

    }

    @Test
    void getPerformanceSeatsAndSectorsShouldMatch() {
        Assertions.assertDoesNotThrow(() -> performanceService.getPerformancePlanById(1));
        DetailedPerformanceDto detailedPerformanceDto = performanceService.getPerformancePlanById(performance.getId());


        PerformanceTicketDto[][] performanceTicketDto = detailedPerformanceDto.getTickets();

        assertEquals(performanceTicketDto[6][6].getSectorId(), standingSeat.getSector().getId());
        assertEquals(performanceTicketDto[6][6].getSectorId(), standingSeat.getSector().getId());
        assertEquals(detailedPerformanceDto.getPerformanceSector().get(performanceTicketDto[6][6].getSectorId()).getName(),
            standingPerformanceSector.getSector().getName());

        assertEquals(performanceTicketDto[2][2].getSectorId(), seatedSeat.getSector().getId());
        assertEquals(performanceTicketDto[2][2].getSectorId(), seatedSeat.getSector().getId());
        assertEquals(detailedPerformanceDto.getPerformanceSector().get(performanceTicketDto[2][2].getSectorId()).getName(),
            seatedPerformanceSector.getSector().getName());
    }

    @Test
    void getPerformancesByEventIdOfNonExistingEventShouldThrowNotFoundException() {
        Assertions.assertThrows(NotFoundException.class, () -> performanceService.getPerformancesOfEventById(-1));
    }

    @Test
    void getPerformancesByEventIdShouldReturnAllPerformancesOfEvent() {
        Assertions.assertDoesNotThrow(() -> performanceService.getPerformancesOfEventById(1));
        List<Performance> performances = performanceService.getPerformancesOfEventById(performance.getEvent().getId());

        assertEquals(1, performances.size());

    }

    @Test
    void getPerformancesOfEventWithNoPerformancesShouldThrowNotFoundException() {
        Assertions.assertThrows(NotFoundException.class, () -> performanceService.getPerformancesOfEventById(2));
    }

    @Test
    void getPerformancesOnLocationShouldReturnAllPerformances() {
        Assertions.assertDoesNotThrow(() -> performanceService.getPerformancesOfLocationById(1));
        List<Performance> performances = performanceService.getPerformancesOfLocationById(performance.getHall().getLocation().getId());

        assertEquals(1, performances.size());
    }

    @Test
    void getPerformancesOnNonExistingLocationShouldThrowNotFoundException() {
        Assertions.assertThrows(NotFoundException.class, () -> performanceService.getPerformancesOfLocationById(-1));
    }

    @Test
    void getPerformancesOnLocationWithNoPerformancesShouldThrowNotFoundException() {
        Assertions.assertThrows(NotFoundException.class, () -> performanceService.getPerformancesOfLocationById(2));
    }

}
