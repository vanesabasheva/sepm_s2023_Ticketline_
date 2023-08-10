package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.topten.EventTicketCountDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Hall;
import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import at.ac.tuwien.sepm.groupphase.backend.entity.Order;
import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;
import at.ac.tuwien.sepm.groupphase.backend.entity.PerformanceSector;
import at.ac.tuwien.sepm.groupphase.backend.entity.Seat;
import at.ac.tuwien.sepm.groupphase.backend.entity.Sector;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.HallRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.LocationRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.NotUserRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.OrderRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.PerformanceRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.PerformanceSectorRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SeatRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SectorRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TicketRepository;
import jakarta.transaction.Transactional;
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
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class TopTenTest {

    @Autowired
    private NotUserRepository notUserRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private SeatRepository seatRepository;
    @Autowired
    private SectorRepository sectorRepository;
    @Autowired
    private PerformanceRepository performanceRepository;
    @Autowired
    private PerformanceSectorRepository performanceSectorRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private HallRepository hallRepository;

    private Order order;
    private Order order2;
    private Order order3;
    private Seat seat;
    private Seat seat2;
    private Seat seat3;
    private Sector sector;
    private Performance performance1;
    private Performance performance2;
    private PerformanceSector performanceSector;
    private PerformanceSector performanceSector2;
    private Event event1;
    private Event event2;
    private Ticket ticket1;
    private Ticket ticket2;
    private Ticket ticket3;
    private ApplicationUser user;

    @BeforeEach
    public void beforeEach() {

        event1 = new Event();
        event1.setId(1);
        event1.setName("Nothing but Thieves Tour");
        event1.setLength(Duration.ofHours(2));
        event1.setType("Concert");
        event1.setDescription("Nothing but Thieves are an English rock band, formed in 2012 in Southend-on-Sea, Essex.");
        eventRepository.save(event1);

        event2 = new Event();
        event2.setId(2);
        event2.setName("Ballet");
        event2.setLength(Duration.ofHours(2));
        event2.setType("Ballet");
        event2.setDescription("Ballet");
        eventRepository.save(event2);

        Location location = new Location();
        location.setId(1);
        location.setPostalCode(1010);
        location.setStreet("Stephansplatz 1");
        location.setCity("Vienna");
        location.setCountry("Austria");
        locationRepository.save(location);

        Hall hall = new Hall();
        hall.setId(1);
        hall.setName("Hall 1");
        hall.setLocation(location);
        hallRepository.save(hall);

        sector = new Sector();
        sector.setId(1);
        sector.setName("Sector 1");
        sector.setStanding(false);
        sector.setHall(hall);
        sectorRepository.save(sector);

        performance1 = new Performance();
        performance1.setId(1);
        performance1.setDatetime(LocalDateTime.of(2024, 1, 1, 20, 0));
        performance1.setEvent(event1);
        performance1.setHall(hall);
        performanceRepository.save(performance1);

        performance2 = new Performance();
        performance2.setId(2);
        performance2.setDatetime(LocalDateTime.of(2024, 1, 1, 20, 0));
        performance2.setEvent(event2);
        performance2.setHall(hall);
        performanceRepository.save(performance2);

        performanceSector = new PerformanceSector();
        performanceSector.setId(1);
        performanceSector.setPrice(new BigDecimal(10.99));
        performanceSector.setPointsReward(10);
        performanceSector.setSector(sector);
        performanceSector.setPerformance(performance1);
        performanceSectorRepository.save(performanceSector);

        performanceSector2 = new PerformanceSector();
        performanceSector2.setId(1);
        performanceSector2.setPrice(new BigDecimal(10.99));
        performanceSector2.setPointsReward(10);
        performanceSector2.setSector(sector);
        performanceSector2.setPerformance(performance2);
        performanceSectorRepository.save(performanceSector2);

        sector.setPerformanceSectors(Set.of(performanceSector));
        performance1.setPerformanceSectors(Set.of(performanceSector));
        performanceRepository.save(performance1);

        sector.setPerformanceSectors(Set.of(performanceSector, performanceSector2));
        performance2.setPerformanceSectors(Set.of(performanceSector2));
        performanceRepository.save(performance2);

        seat = new Seat();
        seat.setId(1);
        seat.setRow(1);
        seat.setNumber(1);
        seat.setSector(sector);
        seatRepository.save(seat);

        seat2 = new Seat();
        seat2.setId(2);
        seat2.setRow(2);
        seat2.setNumber(2);
        seat2.setSector(sector);
        seatRepository.save(seat2);

        seat3 = new Seat();
        seat3.setId(3);
        seat3.setRow(3);
        seat3.setNumber(3);
        seat3.setSector(sector);
        seatRepository.save(seat3);

        sector.setSeats(Set.of(seat, seat2, seat3));
        sectorRepository.save(sector);


        ticket1 = new Ticket();
        ticket1.setId(1);
        ticket1.setPerformance(performance1);
        ticket1.setSeat(seat);
        ticketRepository.save(ticket1);


        ticket2 = new Ticket();
        ticket2.setId(2);
        ticket2.setPerformance(performance2);
        ticket2.setSeat(seat2);
        ticketRepository.save(ticket2);

        ticket3 = new Ticket();
        ticket3.setId(3);
        ticket3.setPerformance(performance1);
        ticket3.setSeat(seat3);
        ticketRepository.save(ticket3);


        user = new ApplicationUser();
        user.setId(1);
        user.setAdmin(false);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("johndoe@genericemail.com");
        user.setPassword("password");
        user.setPoints(0);
        user.setLocked(false);
        notUserRepository.save(user);

        order = new Order();
        order.setId(1);
        order.setOrderTs(LocalDateTime.of(2023, 6, 15, 1, 1));
        order.setCancelled(false);
        order.setUser(user);
        order.setTickets(Set.of(ticket1));

        order2 = new Order();
        order2.setId(2);
        order2.setOrderTs(LocalDateTime.of(2023, 6, 15, 2, 2));
        order2.setCancelled(false);
        order2.setUser(user);
        order2.setTickets(Set.of(ticket2));

        order3 = new Order();
        order3.setId(3);
        order3.setOrderTs(LocalDateTime.of(2023, 6, 15, 2, 2));
        order3.setCancelled(false);
        order3.setUser(user);
        order3.setTickets(Set.of(ticket3));

        orderRepository.save(order);

        ticket1.setOrder(order);
        ticketRepository.save(ticket1);

        orderRepository.save(order2);

        ticket2.setOrder(order2);
        ticketRepository.save(ticket2);

        orderRepository.save(order3);

        ticket3.setOrder(order3);
        ticketRepository.save(ticket3);
    }

    @Test
    public void getTopTenEventsOfLastMonthFindTwoWithTicketCountOneAndTwo() {
        List<EventTicketCountDto> eventTicketCountDto = eventRepository.findTopTenEventsByTicketCount();
        assertEquals(2, eventTicketCountDto.size());
        assertEquals(event2.getType(), eventTicketCountDto.get(0).getType());
        assertEquals(1, eventTicketCountDto.get(0).getTicketCount());
        assertEquals(event1.getType(), eventTicketCountDto.get(1).getType());
        assertEquals(2, eventTicketCountDto.get(1).getTicketCount());
    }

    @Test
    public void getTopTenEventsOfLastMonthFindZero() {
        order.setOrderTs(LocalDateTime.of(2020, 5, 15, 1, 1));
        orderRepository.save(order);
        order2.setOrderTs(LocalDateTime.of(2020, 5, 15, 1, 1));
        orderRepository.save(order2);
        order3.setOrderTs(LocalDateTime.of(2020, 5, 15, 1, 1));
        orderRepository.save(order3);
        List<EventTicketCountDto> eventTicketCountDto = eventRepository.findTopTenEventsByTicketCount();
        assertEquals(0, eventTicketCountDto.size());
    }


}
