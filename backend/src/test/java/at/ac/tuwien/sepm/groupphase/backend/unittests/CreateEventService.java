package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.createevent.CreateDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.createevent.CreateEventDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.createevent.CreatePerformanceDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.createevent.CreatePerformanceSectorDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;
import at.ac.tuwien.sepm.groupphase.backend.entity.PerformanceSector;
import at.ac.tuwien.sepm.groupphase.backend.entity.Sector;
import at.ac.tuwien.sepm.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@SpringBootTest
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles(profiles = {"test", "generateData"})
public class CreateEventService {


    private final String[] eventName = {"Nova Rock", "Rock amm Ring", "Rock im Park", "Frequency", "Knotfest"};

    private final String[] eventTypes = {"Concert", "Festival", "Theater", "Opera", "Musical", "Circus", "Show", "Other"};
    private final int[] eventLength = new int[] {60, 120, 180, 240};
    private final String[] eventDescription = {
        "Description 1", "Description 2", "Description 3", "Description 4",
        "Description 5", "Description 6", "Description 7", "Description 8",
        "Description 9", "Description 10", "Description 11", "Description 12"
    };
    private final String[] eventArtists = {
        "Artist 1", "Artist 2", "Artist 3", "Artist 4",
        "Artist 5", "Artist 6", "Artist 7", "Artist 8",
        "Artist 9", "Artist 10", "Artist 11", "Artist 12"
    };
    private final String[] eventImagePaths = {"cat.png", "cat_black.png", "duck.png", "turtle.png"};

    private final int[] hallIds = {
        1, 2, 3, 4, 5, 6, 7, 8, 9, 10,
        11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
    };
    private final Random random = new Random();
    private final int eventArtistsCounter = 0;
    private int eventNameCounter = 0;
    private int eventTypesCounter = 0;
    private int eventLengthCounter = 0;
    private int eventDescriptionCounter = 0;
    private int eventImagePathsCounter = 0;
    private LocalDateTime localDateTime = LocalDateTime.of(2023, 10, 10, 10, 10);
    @Autowired
    private PerformanceService performanceService;
    @Autowired
    private EventService eventService;
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


    //======================================TESTS======================================

    @Test
    void createEvent() {
        CreateDto createDto = generateCreateDto(1, 1);
        Assertions.assertDoesNotThrow(() -> eventService.createEvent(createDto));
        validate(createDto, 1);
    }


    @Test
    void createEvent_withMultiplePerformances() {
        CreateDto createDto = generateCreateDto(3, 3);
        Assertions.assertDoesNotThrow(() -> eventService.createEvent(createDto));
        validate(createDto, 3);
    }

    @Test
    void createEvent_withNoPerformances() {
        CreateDto createDto = generateCreateDto(0, 3);
        Assertions.assertThrows(ValidationException.class, () -> eventService.createEvent(createDto));
    }

    @Test
    void createEvent_withNoPerformances1() {
        CreateDto createDto = generateCreateDto(0, 3);
        createDto.setPerformances(null);
        Assertions.assertThrows(ValidationException.class, () -> eventService.createEvent(createDto));
    }

    @Test
    void createEvent_withNoEvent() {
        CreateDto createDto = generateCreateDto(2, 3);
        createDto.setEvent(null);
        Assertions.assertThrows(ValidationException.class, () -> eventService.createEvent(createDto));
    }

    @Test
    void createEvent_withNoName() {
        CreateDto createDto = generateCreateDto(2, 3);
        CreateEventDto eventDto = createDto.getEvent();
        eventDto.setName(null);
        createDto.setEvent(eventDto);
        Assertions.assertThrows(ValidationException.class, () -> eventService.createEvent(createDto));
    }

    @Test
    void createEvent_withNoType() {
        CreateDto createDto = generateCreateDto(2, 3);
        CreateEventDto eventDto = createDto.getEvent();
        eventDto.setType(null);
        createDto.setEvent(eventDto);
        Assertions.assertThrows(ValidationException.class, () -> eventService.createEvent(createDto));
    }

    @Test
    void createEvent_withNoLength() {
        CreateDto createDto = generateCreateDto(2, 3);
        CreateEventDto eventDto = createDto.getEvent();
        eventDto.setLength(null);
        createDto.setEvent(eventDto);
        Assertions.assertThrows(ValidationException.class, () -> eventService.createEvent(createDto));
    }

    @Test
    void createEvent_withNoDescription() {
        CreateDto createDto = generateCreateDto(2, 3);
        CreateEventDto eventDto = createDto.getEvent();
        eventDto.setDescription(null);
        createDto.setEvent(eventDto);
        Assertions.assertThrows(ValidationException.class, () -> eventService.createEvent(createDto));
    }

    @Test
    void createEvent_withNoArtists() {
        CreateDto createDto = generateCreateDto(2, 0);
        Assertions.assertThrows(ValidationException.class, () -> eventService.createEvent(createDto));
    }

    @Test
    void createEvent_withNoArtists1() {
        CreateDto createDto = generateCreateDto(2, 0);
        CreateEventDto eventDto = createDto.getEvent();
        eventDto.setArtists(null);
        createDto.setEvent(eventDto);
        Assertions.assertThrows(ValidationException.class, () -> eventService.createEvent(createDto));
    }


    @Test
    void createEvent_withExistingName() {
        CreateDto createDto = generateCreateDto(2, 2);
        CreateEventDto eventDto = createDto.getEvent();
        eventDto.setName("Vorhanden");
        createDto.setEvent(eventDto);

        CreateDto createDto1 = generateCreateDto(4, 4);
        CreateEventDto eventDto1 = createDto1.getEvent();
        eventDto1.setName("Vorhanden");
        createDto1.setEvent(eventDto);

        Assertions.assertDoesNotThrow(() -> eventService.createEvent(createDto));
        Assertions.assertThrows(ConflictException.class, () -> eventService.createEvent(createDto1));
    }

    @Test
    void createEvent_withInvalidArtistsNames() {
        CreateDto createDto = generateCreateDto(2, 0);
        CreateEventDto eventDto = createDto.getEvent();
        eventDto.setArtists("NICHT VORHANDEN;AUCH NICHT");
        createDto.setEvent(eventDto);
        Assertions.assertThrows(NotFoundException.class, () -> eventService.createEvent(createDto));
    }

    @Test
    void createEvent_sameDayAsOther() {
        int numberOfPerformances = 2;
        int numberOfArtists = 2;
        CreateDto createDto = generateCreateDto(numberOfPerformances, numberOfArtists);
        CreatePerformanceDto performanceDto = createDto.getPerformances()[0];
        Assertions.assertDoesNotThrow(() -> eventService.createEvent(createDto));

        CreateDto createDto1 = generateCreateDto(numberOfPerformances, numberOfArtists);
        CreatePerformanceDto[] performanceDto1 = createDto1.getPerformances();
        performanceDto1[0].setHallId(performanceDto.getHallId());
        performanceDto1[0].setDateTime(performanceDto.getDateTime());
        createDto1.setPerformances(performanceDto1);


        ConflictException exception = Assertions.assertThrows(ConflictException.class, () -> eventService.createEvent(createDto1));
        System.out.println(exception.getMessage());
        String expectedText = "on the same Day";
        assertTrue(exception.getMessage().contains(expectedText));
    }


    //======================================HELPER METHODS======================================


    void cleanup(Event event) {
        Set<Performance> performances = event.getPerformances();

        //cleanup
        performances.stream().map(Performance::getPerformanceSectors).forEach(set -> {
            set.forEach(performanceSector ->
                {
                    this.performanceSectorRepository.deleteById(Long.valueOf(performanceSector.getId()));
                    this.ticketRepository.deleteAllById(this.ticketRepository.findAllByPerformance(
                            performanceSector.getPerformance()).stream().map(
                            ticket -> ticket.getId()
                        ).collect(Collectors.toList())
                    );
                }
            );
        });

        performanceRepository.deleteAllById(performances.stream().map(Performance::getId).collect(Collectors.toList()));


        List<Artist> artists = this.artistRepository.findAllByEventsId(event.getId());
        artists.forEach(artist -> {
            artist.getEvents().removeIf(event1 -> event1.getId() == event.getId());
            this.artistRepository.save(artist);
        });
        //cleanup
        eventRepository.deleteById(event.getId());
    }

    void validate(CreateDto create, int numOfPerformances) {
        List<Optional<Event>> tmp = this.eventRepository.findByName(create.getEvent().getName());
        if (tmp.size() != 1) {
            fail();
        }
        Event ev = tmp.get(0).get();
        Set<Artist> dtoArtists = new HashSet<>();
        Arrays.stream(create.getEvent().getArtists().split(";")).map(s -> s.trim()).forEach(s -> {
            Optional<Artist> tmp2 = this.artistRepository.findByName(s);
            if (tmp2.isEmpty()) {
                fail();
            }
            dtoArtists.add(tmp2.get());
        });
        Set<Artist> savedArtist = ev.getArtists();
        savedArtist.size();
        assertAll(() -> {
            assertEquals(create.getEvent().getName(), ev.getName());
            assertEquals(create.getEvent().getType(), ev.getType());
            assertEquals(Duration.ofMinutes(create.getEvent().getLength()), ev.getLength());
            assertEquals(create.getEvent().getDescription(), ev.getDescription());
            //check if the dtoArtists are the same as savedArtist
            dtoArtists.forEach(artist1 -> {
                if (!savedArtist.stream().map(artist3 -> artist3.getId()).collect(Collectors.toList()).contains(artist1.getId())) {
                    fail();
                }
            });
            assertEquals(create.getEvent().getImagePath(), ev.getImagePath());
        });

        List<Performance> performances = this.performanceRepository.findAllByEvent_Id(ev.getId());
        for (int i = 0; i < performances.size(); i++) {
            assertEquals(create.getPerformances()[i].getHallId(), performances.get(i).getHall().getId());
            assertEquals(create.getPerformances()[i].getDateTime(), performances.get(i).getDatetime().toString());
            Set<PerformanceSector> performanceSectors = performances.get(i).getPerformanceSectors();
            for (PerformanceSector performanceSector : performanceSectors) {
                assertEquals(performanceSector.getPerformance(), performances.get(i));
                assertEquals(performanceSector.getPrice().doubleValue(), Double.valueOf(10));
            }
        }
        //cleanup
        cleanup(ev);
    }


    //======================================GENERATE TEST DATA======================================


    private String generateEventName() {
        return eventName[eventNameCounter++ % eventName.length] + eventNameCounter;
    }

    private String generateEventTypes() {
        return eventTypes[eventTypesCounter++ % eventTypes.length];
    }

    private int generateEventLength() {
        return eventLength[eventLengthCounter++ % eventLength.length];
    }

    private String generateEventDescription() {
        return eventDescription[eventDescriptionCounter++ % eventDescription.length] + eventDescriptionCounter;
    }

    /*
    private String generateEventArtists() {
        return eventArtists[eventArtistsCounter % eventArtists.length];
    }
     */

    private String generateEventImagePath() {
        return eventImagePaths[eventImagePathsCounter++ % eventImagePaths.length];
    }


    private String generateMultipleDifferentArtists(int numOfArtists) {
        String output = "";
        List<Integer> used = new ArrayList<>();
        while (used.size() < numOfArtists) {
            int index = random.nextInt(eventArtists.length);
            if (!used.contains(eventArtists[index])) {
                output += eventArtists[index] + ";";
                used.add(index);
            }
        }
        if (numOfArtists == 0) {
            return null;
        }
        return output.substring(0, output.length() - 1);
    }

    private List<Integer> generateMultipleDifferentHallIDs(int numOfPerformances) {
        List<Integer> used = new ArrayList<>();
        while (used.size() < numOfPerformances) {
            int index = random.nextInt(hallIds.length);
            if (!used.contains(hallIds[index])) {
                used.add(hallIds[index]);
            }
        }
        if (numOfPerformances == 0) {
            return null;
        }
        return used;
    }

    private List<LocalDateTime> generateMultipleEventDateTimes(int numOfPerformances) {
        List<LocalDateTime> used = new ArrayList<>();
        while (used.size() < numOfPerformances) {
            LocalDateTime dateTime = this.localDateTime;
            used.add(dateTime);
            this.localDateTime = this.localDateTime.plusDays(1);
        }
        return used;
    }


    public CreateDto generateCreateDto(int numOfPerformances, int numOfArtists) {
        //Event:
        String eventName = generateEventName();
        String eventType = generateEventTypes();
        Integer eventLength = generateEventLength();
        String eventDescription = generateEventDescription();
        String eventArtists = generateMultipleDifferentArtists(numOfArtists);
        String imagePath = generateEventImagePath();

        //Performances:
        List<Integer> hallIds = generateMultipleDifferentHallIDs(numOfPerformances);
        List<LocalDateTime> dateTimes = generateMultipleEventDateTimes(numOfPerformances);

        //Create DTOs
        CreateEventDto createEventDto;
        CreatePerformanceDto[] createPerformanceDtos = new CreatePerformanceDto[numOfPerformances];

        //create event
        createEventDto = CreateEventDto.CreateEventDtoBuilder.aCreateEventDto()
            .withName(eventName)
            .withType(eventType)
            .withLength(eventLength)
            .withDescription(eventDescription)
            .withArtists(eventArtists)
            .withImagePath(imagePath)
            .build();

        //create Performances:
        for (int i = 0; i < numOfPerformances; i++) {
            Integer hallId = hallIds.get(i);
            List<Sector> sectors = this.sectorRepository.findAllByHallId(hallId);

            CreatePerformanceSectorDto[] perf = sectors.stream().map(sector -> {
                return CreatePerformanceSectorDto.CreatePerformanceSectorDtoBuilder.aCreatePerformanceSectorDto()
                    .withHallId(hallId)
                    .withSectorId(sector.getId())
                    .withPrice(Double.valueOf(10))
                    .withName(sector.getName())
                    .withStanding(sector.getStanding())
                    .build();
            }).toArray(CreatePerformanceSectorDto[]::new);

            createPerformanceDtos[i] = CreatePerformanceDto.CreatePerformanceDtoBuilder.aCreatePerformanceDto().withHallId(hallId)
                .withHallId(hallId)
                .withDateTime(dateTimes.get(i).toString())
                .withSectorPrices(perf)
                .build();
        }

        return CreateDto.CreateDtoBuilder.aCreateDto()
            .withEvent(createEventDto)
            .withPerformance(createPerformanceDtos)
            .build();
    }


}
