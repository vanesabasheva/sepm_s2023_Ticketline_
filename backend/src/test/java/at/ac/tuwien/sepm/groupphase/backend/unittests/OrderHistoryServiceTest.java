package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.OrderHistoryDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.OrderMerchandiseDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.OrderTicketDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.OrderMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.*;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.*;
import at.ac.tuwien.sepm.groupphase.backend.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class OrderHistoryServiceTest {
    @MockBean
    private NotUserRepository notUserRepository;
    @MockBean
    private OrderRepository orderRepository;
    @MockBean
    private TicketRepository ticketRepository;
    @MockBean
    private MerchandiseOrderedRepository merchandiseOrderedRepository;
    @MockBean
    @Qualifier("MerchandiseRepositoryImpl")
    private MerchandiseRepository merchandiseRepository;
    @MockBean
    private SeatRepository seatRepository;
    @MockBean
    private SectorRepository sectorRepository;
    @MockBean
    private PerformanceRepository performanceRepository;
    @MockBean
    private PerformanceSectorRepository performanceSectorRepository;
    @MockBean
    private EventRepository eventRepository;
    @MockBean
    private ArtistRepository artistRepository;
    @MockBean
    private LocationRepository locationRepository;
    @MockBean
    private HallRepository hallRepository;
    @Autowired
    private OrderService orderService;
    @MockBean
    private OrderMapper orderMapper;

    private Order order;
    private MerchandiseOrdered merchandiseOrdered;
    private Seat seat;
    private Sector sector;
    private Performance performance1;
    private PerformanceSector performanceSector;
    private Merchandise merchandise1;
    private Event event1;
    private Artist artist1;
    private Ticket ticket1;
    private ApplicationUser user;
    private OrderHistoryDto orderHistoryDto;

    @BeforeEach
    public void beforeEach() {
        artist1 = new Artist();
        artist1.setId(1);
        artist1.setName("Nothing but Thieves");
        // artistRepository.save(artist1);

        event1 = new Event();
        event1.setId(1);
        event1.setName("Nothing but Thieves Tour");
        event1.setLength(Duration.ofHours(2));
        event1.setArtists(Set.of(artist1));
        event1.setDescription("Nothing but Thieves are an English rock band, formed in 2012 in Southend-on-Sea, Essex.");
        // eventRepository.save(event1);

        Location location = new Location();
        location.setId(1);
        location.setPostalCode(1010);
        location.setStreet("Stephansplatz 1");
        location.setCity("Vienna");
        location.setCountry("Austria");
        // locationRepository.save(location);

        Hall hall = new Hall();
        hall.setId(1);
        hall.setName("Hall 1");
        hall.setLocation(location);
        // hallRepository.save(hall);

        sector = new Sector();
        sector.setId(1);
        sector.setName("Sector 1");
        sector.setStanding(false);
        sector.setHall(hall);
        // sectorRepository.save(sector);

        performance1 = new Performance();
        performance1.setId(1);
        performance1.setDatetime(LocalDateTime.of(2022, 1, 1, 20, 0));
        performance1.setEvent(event1);
        performance1.setHall(hall);
        // performanceRepository.save(performance1);

        performanceSector = new PerformanceSector();
        performanceSector.setId(1);
        performanceSector.setPrice(new BigDecimal(10.99));
        performanceSector.setPointsReward(10);
        performanceSector.setSector(sector);
        performanceSector.setPerformance(performance1);
        // performanceSectorRepository.save(performanceSector);

        sector.setPerformanceSectors(Set.of(performanceSector));
        performance1.setPerformanceSectors(Set.of(performanceSector));
        // performanceRepository.save(performance1);

        seat = new Seat();
        seat.setId(1);
        seat.setRow(1);
        seat.setNumber(1);
        seat.setSector(sector);
        // seatRepository.save(seat);

        sector.setSeats(Set.of(seat));
        // sectorRepository.save(sector);

        merchandise1 = new Merchandise();
        merchandise1.setId(1);
        merchandise1.setPrice(new BigDecimal(10.99));
        merchandise1.setPointsReward(10);
        merchandise1.setTitle("Falco T-Shirt");
        merchandise1.setDescription("Amadeus Amadeus A-A-A-A-Amadeus");
        // merchandiseRepository.save(merchandise1);

        merchandiseOrdered = new MerchandiseOrdered();
        merchandiseOrdered.setId(1);
        merchandiseOrdered.setQuantity(19);
        merchandiseOrdered.setPoints(false);
        merchandiseOrdered.setMerchandise(merchandise1);

        ticket1 = new Ticket();
        ticket1.setId(1);
        ticket1.setOrder(order);
        ticket1.setPerformance(performance1);
        ticket1.setSeat(seat);
        // ticketRepository.save(ticket1);

        user = new ApplicationUser();
        user.setId(1);
        user.setAdmin(false);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("johndoe@genericemail.com");
        user.setPassword("password");
        user.setPoints(0);
        user.setLocked(false);

        order = new Order();
        order.setId(1);
        order.setOrderTs(LocalDateTime.of(2020, 1, 1, 0, 0));
        order.setCancelled(false);
        order.setUser(user);

        // notUserRepository.save(user);
        // orderRepository.save(order);
        merchandiseOrdered.setOrder(order);
        // merchandiseOrderedRepository.save(merchandiseOrdered);

        order.setTickets(Set.of(ticket1));
        order.setMerchandiseOrdered(Set.of(merchandiseOrdered));
        user.setOrders(Set.of(order));

        // notUserRepository.save(user);
        // orderRepository.save(order);
        // merchandiseOrderedRepository.save(merchandiseOrdered);

        OrderTicketDto orderTicketDto = OrderTicketDto.OrderTicketDtoBuilder.aOrderTicketDto()
                .withId(1)
                .withPrice(BigDecimal.valueOf(10.99))
                .withArtists(artist1.getName())
                .withEventName(event1.getName())
                .withDatetime(performance1.getDatetime())
                .build();

        OrderMerchandiseDto orderMerchandiseDto = OrderMerchandiseDto.OrderMerchandiseDtoBuilder.aOrderMerchandiseDto()
                .withId(1)
                .withItemName(merchandise1.getTitle())
                .withQuantity(merchandiseOrdered.getQuantity())
                .withPrice(merchandise1.getPrice())
                .build();

        orderHistoryDto = OrderHistoryDto.OrderHistoryDtoBuilder.aOrderHistoryDto()
                .withId(1)
                .withOrderTs(order.getOrderTs())
                .withCancelled(order.getCancelled())
                .withTickets(List.of(orderTicketDto))
                .withMerchandises(List.of(orderMerchandiseDto))
                // CAREFUL with nullable!
                .withTotalPrice(new BigDecimal(0))
                .withTotalPoints(0)
                .build();
    }

    @Test
    public void getOrderHistoryThrowsValidationExceptionWithWithNegativeId() {
        when(notUserRepository.getApplicationUserById(1)).thenReturn(user);
        when(orderRepository.getAllOrdersByUserId(1)).thenReturn(List.of(order));
        when(orderMapper.orderToOrderHistoryDto(order)).thenReturn(orderHistoryDto);
        ValidationException result = assertThrows(ValidationException.class,() -> orderService.getOrderHistory(-100));
        assertTrue(result.getMessage().contains("Id can't be a negative number"));
    }

    @Test
    public void getOrderHistoryThrowsNotFoundExceptionWithNonexistentId() {
        when(notUserRepository.getApplicationUserById(1)).thenReturn(user);
        when(orderRepository.getAllOrdersByUserId(1)).thenReturn(List.of(order));
        when(orderMapper.orderToOrderHistoryDto(order)).thenReturn(orderHistoryDto);
        NotFoundException result = assertThrows(NotFoundException.class,() -> orderService.getOrderHistory(1234567890));
        assertTrue(result.getMessage().contains("Could not find user with id 1234567890"));
    }

    @Test
    public void getOrderHistorySetCorrectValuesCheckIfResultsAreCorrect() {
        when(notUserRepository.getApplicationUserById(1)).thenReturn(user);
        when(orderRepository.getAllOrdersByUserId(1)).thenReturn(List.of(order));
        when(orderMapper.orderToOrderHistoryDto(order)).thenReturn(orderHistoryDto);
        List<OrderHistoryDto> result = assertDoesNotThrow(() -> orderService.getOrderHistory(1));
        // test correctness of order
        assertAll(
            () -> assertEquals(result.size(), 1),
            () -> assertEquals(result.get(0).getId(), 1),
            () -> assertEquals(result.get(0).getOrderTs(), order.getOrderTs()),
            () -> assertEquals(result.get(0).getCancelled(), order.getCancelled()),
            () -> assertEquals(result.get(0).getTickets().size(), 1),
            () -> assertEquals(result.get(0).getMerchandises().size(), 1),
            () -> assertEquals(result.get(0).getTotalPrice(), new BigDecimal(0)),
            () -> assertEquals(result.get(0).getTotalPoints(), 0)
        );
        // test correctness of ticket in order
        assertAll(
            () -> assertEquals(result.get(0).getTickets().get(0).getId(), 1),
            () -> assertEquals(result.get(0).getTickets().get(0).getPrice(), BigDecimal.valueOf(10.99)),
            () -> assertEquals(result.get(0).getTickets().get(0).getArtists(), artist1.getName()),
            () -> assertEquals(result.get(0).getTickets().get(0).getEventName(), event1.getName()),
            () -> assertEquals(result.get(0).getTickets().get(0).getDatetime(), performance1.getDatetime())
        );
        // test correctness of merchandise in order
        assertAll(
            () -> assertEquals(result.get(0).getMerchandises().get(0).getId(), 1),
            () -> assertEquals(result.get(0).getMerchandises().get(0).getItemName(), merchandise1.getTitle()),
            () -> assertEquals(result.get(0).getMerchandises().get(0).getQuantity(), merchandiseOrdered.getQuantity()),
            () -> assertEquals(result.get(0).getMerchandises().get(0).getPrice(), merchandise1.getPrice())
        );
    }
}
