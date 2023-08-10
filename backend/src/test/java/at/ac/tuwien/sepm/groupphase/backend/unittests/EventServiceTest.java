package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.createevent.CreateDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.search.ArtistSearchDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.search.EventSearchDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Hall;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtistRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.PerformanceRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.PerformanceSectorRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SeatRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SectorRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TicketRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.EventService;
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

import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles(profiles = {"test"})
public class EventServiceTest {
    @Autowired
    PerformanceService performanceService;
    @Autowired
    EventService eventService;

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private ArtistRepository artistRepository;
    @Autowired
    private SectorRepository sectorRepository;
    @Autowired
    private PerformanceRepository performanceRepository;
    @Autowired
    private PerformanceSectorRepository performanceSectorRepository;
    @Autowired
    private SeatRepository seatRepository;
    @Autowired
    private TicketRepository ticketRepository;

    private Event event;
    private Set<Event> eventSet;
    private Event event2;
    private Set<Event> eventSet2;
    private Hall hall;
    private Artist artist;
    private Artist artist2;
    private Set<Artist> artistSet;
    private Set<Artist> artistSet2;
    private ArtistSearchDto artistSearchDto;
    private ArtistSearchDto artistSearchDto2;
    private ArtistSearchDto artistSearchDto3;
    private ArtistSearchDto SelenaGomez;
    private ArtistSearchDto Rihanna;
    private EventSearchDto eventSearchDtoWithEmptyValues;
    private EventSearchDto eventSearchDtoWithLength;
    private EventSearchDto eventSearchDtoWithNameAndLength;

    private CreateDto createDto;


    @BeforeEach
    public void beforeAll() {
        event = new Event();
        event.setName("The Eras Tour 2");
        event.setLength(Duration.ofHours(1L));
        event.setType("Movie");
        eventRepository.save(event);

        event2 = new Event();
        event2.setName("The Eras Tour 3");
        event2.setLength(Duration.ofHours(2L));
        event2.setType("Ballet");
        eventRepository.save(event2);

        eventSet = new HashSet<>();
        eventSet.add(event);

        artist = new Artist();
        artist.setName("Selena Gomez");
        artist.setEvents(eventSet);
        artistRepository.save(artist);

        // "Rihanna" participates in both events
        eventSet2 = new HashSet<>();
        eventSet2.add(event);
        eventSet2.add(event2);

        artist2 = new Artist();
        artist2.setName("Rihanna");
        artist2.setEvents(eventSet2);
        artistRepository.save(artist2);

        // save "Rihanna" as an artist in both events, "Selena Gomez only in event
        artistSet = new HashSet<>();
        artistSet.add(artist);
        artistSet.add(artist2);
        event.setArtists(artistSet);


        artistSet2 = new HashSet<>();
        artistSet2.add(artist2);
        event2.setArtists(artistSet2);

        artistSearchDto = new ArtistSearchDto();
        artistSearchDto.setName("sel");

        artistSearchDto2 = new ArtistSearchDto();
        artistSearchDto2.setName("rih");

        artistSearchDto3 = new ArtistSearchDto();
        artistSearchDto3.setName("just");

        SelenaGomez = new ArtistSearchDto();
        SelenaGomez.setName("Selena Gomez");
        SelenaGomez.setId(artist.getId());

        Rihanna = new ArtistSearchDto();
        Rihanna.setName("Rihanna");
        Rihanna.setId(artist2.getId());

        eventSearchDtoWithEmptyValues = new EventSearchDto();
        eventSearchDtoWithEmptyValues.setName(" ");
        eventSearchDtoWithEmptyValues.setDescription("");
        eventSearchDtoWithEmptyValues.setType("");
        eventSearchDtoWithEmptyValues.setLength("");

        eventSearchDtoWithLength = new EventSearchDto();
        eventSearchDtoWithLength.setName("");
        eventSearchDtoWithLength.setDescription("");
        eventSearchDtoWithLength.setType("");
        eventSearchDtoWithLength.setLength("PT1H30M");

        eventSearchDtoWithNameAndLength = new EventSearchDto();
        eventSearchDtoWithNameAndLength.setName(" 3");
        eventSearchDtoWithNameAndLength.setType("ba");
        eventSearchDtoWithNameAndLength.setLength("PT1H30M");


    }

    @Test
    void getAllArtistsWithNameNotMatchingTheParametersThrowsNotFound() {
        Assertions.assertThrows(NotFoundException.class, () -> eventService.getAllArtists(artistSearchDto3));
    }

    @Test
    void getAllArtistsWithNameMatchingTheParametersShouldReturnAllArtists() {
        Assertions.assertDoesNotThrow(() -> eventService.getAllArtists(artistSearchDto));
        Assertions.assertDoesNotThrow(() -> eventService.getAllArtists(artistSearchDto2));

        List<Artist> artistList = eventService.getAllArtists(artistSearchDto);
        List<Artist> artistList2 = eventService.getAllArtists(artistSearchDto2);

        assertEquals(artist.getName(), artistList.get(0).getName());
        assertEquals(artist2.getName(), artistList2.get(0).getName());
    }

    @Test
    void getAllEventsOfCorrectArtistShouldReturnAllEvents() {
        Assertions.assertDoesNotThrow(() -> eventService.getAllEventsOfArtist(Rihanna));
        Assertions.assertDoesNotThrow(() -> eventService.getAllEventsOfArtist(SelenaGomez));

        List<Event> eventList2 = eventService.getAllEventsOfArtist(Rihanna);
        List<Event> eventList = eventService.getAllEventsOfArtist(SelenaGomez);


        assertEquals(2, eventList2.size());
        assertEquals(1, eventList.size());
    }

    @Test
    void getAllEventsOfArtistWithNonExistentArtistShouldThrowNotFound() {
        Assertions.assertThrows(NotFoundException.class, () -> eventService.getAllEventsOfArtist(new ArtistSearchDto()));
    }

    @Test
    void getAllEventsWithEmptyParamsReturnsAllEvents() {
        Assertions.assertDoesNotThrow(() -> eventService.getAllEventsWithParameters(eventSearchDtoWithEmptyValues));


        Assertions.assertEquals(2, eventService.getAllEventsWithParameters(eventSearchDtoWithEmptyValues).size());
    }

    @Test
    void getAllEventsWithParamsWithLengthInRangeReturnsAllEvents() {
        Assertions.assertDoesNotThrow(() -> eventService.getAllEventsWithParameters(eventSearchDtoWithLength));

        Assertions.assertEquals(2, eventService.getAllEventsWithParameters(eventSearchDtoWithLength).size());
    }

    @Test
    void getALlEventsWithParamsWithNameAndLengthInRangeReturnsOneEvent() {
        Assertions.assertDoesNotThrow(() -> eventService.getAllEventsWithParameters(eventSearchDtoWithNameAndLength));

        List<Event> events = eventService.getAllEventsWithParameters(eventSearchDtoWithNameAndLength);

        Assertions.assertEquals(1, events.size());
        Assertions.assertEquals("The Eras Tour 3", events.get(0).getName());
    }

    @Test
    void getAllEventsWithLengthNotInRangeThrowsNotFound() {
        eventSearchDtoWithNameAndLength.setLength("PT4H");
        Assertions.assertThrows(NotFoundException.class, () -> eventService.getAllEventsWithParameters(eventSearchDtoWithNameAndLength));

    }

    @Test
    void getAllEventsWithParamsWithTypeLikeReturnsOneEvent() {
        Assertions.assertDoesNotThrow(() -> eventService.getAllEventsWithParameters(eventSearchDtoWithNameAndLength));
        List<Event> events = eventService.getAllEventsWithParameters(eventSearchDtoWithNameAndLength);

        Assertions.assertEquals(1, events.size());
        Assertions.assertEquals("Ballet", events.get(0).getType());
    }


}
