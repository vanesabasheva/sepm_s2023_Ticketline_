package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.DetailedEventDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.createevent.CreateDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.createevent.CreatePerformanceDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.search.ArtistSearchDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.search.EventSearchDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.EventMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Hall;
import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;
import at.ac.tuwien.sepm.groupphase.backend.entity.PerformanceSector;
import at.ac.tuwien.sepm.groupphase.backend.entity.Seat;
import at.ac.tuwien.sepm.groupphase.backend.entity.Sector;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtistRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.HallRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.PerformanceRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.PerformanceSectorRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SeatRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SectorRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TicketRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomEventService implements EventService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final EventRepository eventRepository;
    private final ArtistRepository artistRepository;
    private final HallRepository hallRepository;
    private final PerformanceRepository performanceRepository;
    private final PerformanceSectorRepository performanceSectorRepository;
    private final SectorRepository sectorRepository;
    private final SeatRepository seatRepository;

    private final TicketRepository ticketRepository;

    private final EventMapper eventMapper;


    public CustomEventService(EventRepository eventRepository, ArtistRepository artistRepository, PerformanceRepository performanceRepository, HallRepository hallRepository, EventMapper eventMapper,
                              PerformanceRepository performanceRepository1, PerformanceSectorRepository performanceSectorRepository, SectorRepository sectorRepository, SeatRepository seatRepository, TicketRepository ticketRepository) {
        this.eventRepository = eventRepository;
        this.artistRepository = artistRepository;
        this.hallRepository = hallRepository;
        this.eventMapper = eventMapper;
        this.performanceRepository = performanceRepository1;
        this.performanceSectorRepository = performanceSectorRepository;
        this.sectorRepository = sectorRepository;
        this.seatRepository = seatRepository;
        this.ticketRepository = ticketRepository;
    }

    @Override
    public List<Event> getAllEventsOfArtist(ArtistSearchDto artist) {
        LOGGER.debug("Find all events of artist{}", artist);
        List<Event> events = eventRepository.findEventsByArtistsId(artist.getId());
        if (events.isEmpty()) {
            throw new NotFoundException(String.format("Could not find events associated with the given artist", artist));
        }
        return events;
    }

    @Override
    public List<Artist> getAllArtists(ArtistSearchDto artistName) {
        LOGGER.debug("Find artist with name like{}", artistName);
        List<Artist> artists = artistRepository.findByNameContainingIgnoreCase(artistName.getName());
        if (artists.isEmpty()) {
            throw new NotFoundException(String.format("Could not find artists associated with the given name", artistName));
        }
        return artists;
    }

    @Override
    public List<Event> getAllEventsWithParameters(EventSearchDto parameters) {
        LOGGER.debug("Find all events with parameters{}", parameters);
        List<Event> events;
        Duration length1;
        Duration length2;

        // handle case where length of event is null
        if (parameters.getLength() == null) {
            length1 = null;
            length2 = null;
        } else {
            length1 = Objects.equals(parameters.getLength(), "") ? null : Duration.parse(parameters.getLength()).minusMinutes(30L);
            length2 = Objects.equals(parameters.getLength(), "") ? null : Duration.parse(parameters.getLength()).plusMinutes(30L);
        }
        events = eventRepository.findAllEventsContaining(
            parameters.getName(),
            parameters.getDescription(),
            parameters.getType(),
            length1,
            length2
        );
        if (events.isEmpty()) {
            throw new NotFoundException(String.format("Could not find events with the given parameters", parameters));
        }
        return events;
    }


    @Override
    @Transactional(rollbackFor = {ConflictException.class, ValidationException.class, NotFoundException.class})
    public DetailedEventDto createEvent(CreateDto createDto) throws ConflictException, ValidationException {
        LOGGER.debug("create new Event {}", createDto);
        List<String> errors = new ArrayList<>();
        if (createDto == null || (errors = createDto.validate()).size() > 0) {
            throw new ValidationException("UserPasswordResetRequestDto is not valid", errors);
        }
        //check if Event with this name already exists
        List<Optional<Event>> savedEvents = eventRepository.findByName(createDto.getEvent().getName());
        if (savedEvents.size() > 0) {
            throw new ConflictException("Event with this name already exists", List.of());
        }

        //create Event
        Event event = new Event();
        event.setName(createDto.getEvent().getName());
        event.setType(createDto.getEvent().getType());
        event.setLength(Duration.ofMinutes(createDto.getEvent().getLength()));
        event.setDescription(createDto.getEvent().getDescription());
        Set<Artist> artistSet = new HashSet<>();

        //create Artists from DTO-String
        String[] artists = createDto.getEvent().getArtists().split(";");
        for (String artist : artists) {
            Optional<Artist> tmp = this.artistRepository.findByName(artist.trim());
            if (tmp.isPresent()) {
                artistSet.add(tmp.get());
            } else {
                throw new NotFoundException(String.format("Artist %s not found", artist.trim()));
            }
        }
        Arrays.stream(artists)
            .forEach(artist -> {
                this.artistRepository.findByName(artist.trim()).ifPresent(artistSet::add);
            });
        event.setArtists(artistSet);
        event.setImagePath(createDto.getEvent().getImagePath());
        //save event
        Event savedEvent = this.eventRepository.save(event);

        //assign event to artists
        Arrays.stream(artists)
            .forEach(artist -> {
                this.artistRepository.findByName(artist.trim()).ifPresent(artist1 -> {
                    Set<Event> artistEvents = artist1.getEvents();
                    artistEvents.add(savedEvent);
                    artist1.setEvents(artistEvents);
                    this.artistRepository.save(artist1);
                });
            });

        //create Performances
        for (CreatePerformanceDto performance : createDto.getPerformances()) {
            Optional<Hall> hall = this.hallRepository.findById(performance.getHallId());
            Performance savedPerformance;
            if (hall.isPresent()) {
                //check if an Event exists on the selected Hall and on the same day
                LocalDateTime date = LocalDateTime.parse(performance.getDateTime(), DateTimeFormatter.ISO_DATE_TIME);
                List<Performance> duplicate = this.performanceRepository.findAllByHallLocation_Id(hall.get().getLocation().getId());
                for (Performance performance1 : duplicate) {
                    if (performance1.getDatetime().toLocalDate().equals(date.toLocalDate())) {
                        throw new ConflictException(String.format("Event %s is already in hall %s on the same Day", performance1.getEvent().getName(), hall.get().getName()), List.of());
                    }
                }

                //create Performance
                savedPerformance = this.performanceRepository.save(
                    Performance.PerformanceBuilder.aPerformance()
                        .withDatetime(LocalDateTime.parse(performance.getDateTime(), DateTimeFormatter.ISO_DATE_TIME))
                        .withHall(hall.get())
                        .withEvent(savedEvent)
                        .build()
                );

                Set<PerformanceSector> savedPerformanceSectors = new HashSet<>(); //keeps track of saved PerformanceSectors in the event of an error
                //create PerformanceSectors
                Arrays.stream(performance.getSectorPrices()).forEach(
                    performanceSector -> {
                        Optional<Sector> dbSector = this.sectorRepository.findById(performanceSector.getSectorId());
                        if (!dbSector.isPresent()) {
                            throw new NotFoundException(String.format("Could not find sector with id %s", performanceSector.getSectorId()));
                        }
                        Sector sector = dbSector.get();
                        List<Seat> seats = this.seatRepository.findAllBySectorId(sector.getId());
                        for (Seat seat : seats) {
                            Ticket ticket = Ticket.TicketBuilder.aTicket()
                                .withSeat(seat)
                                .withPerformance(savedPerformance)
                                .build();
                            LOGGER.debug("saving ticket {}", ticket);
                            this.ticketRepository.save(ticket);
                        }
                        //add created PerformanceSector to savedPerformanceSectors in case of error
                        savedPerformanceSectors.add(
                            this.performanceSectorRepository.save(
                                PerformanceSector.PerformanceSectorBuilder.aPerformanceSector()
                                    .withPerformance(savedPerformance)
                                    .withSector(sector)
                                    .withPrice(BigDecimal.valueOf(performanceSector.getPrice()))
                                    .withPointsReward(0) //not used -> 0
                                    .build()
                            )
                        );
                    }
                );
                //add created PerformanceSectors to Performance
                this.performanceRepository.findById(savedPerformance.getId()).ifPresent(performance1 -> {
                    savedPerformance.setPerformanceSectors(savedPerformanceSectors);
                    this.performanceRepository.save(savedPerformance);
                });

                savedEvent.setPerformances(
                    this.performanceRepository.findAllByEvent_Id(savedEvent.getId())
                        .stream().collect(Collectors.toSet())
                );
                this.eventRepository.save(savedEvent);
            } else {
                throw new NotFoundException(String.format("Could not find hall with name %s", performance.getHallId()));
            }
        }
        return eventMapper.eventToDetailedEventDto(savedEvent);
    }
}
