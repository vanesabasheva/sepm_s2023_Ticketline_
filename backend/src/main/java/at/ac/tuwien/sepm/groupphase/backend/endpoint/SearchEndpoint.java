package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.search.ArtistSearchDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.search.EventSearchDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.search.LocationSearchDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.search.PerformanceSearchDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ArtistMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.EventMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.LocationMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.PerformanceMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.service.EventService;
import at.ac.tuwien.sepm.groupphase.backend.service.LocationService;
import at.ac.tuwien.sepm.groupphase.backend.service.PerformanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/search")
public class SearchEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final EventService eventService;
    private final EventMapper eventMapper;
    private final ArtistMapper artistMapper;
    private final LocationService locationService;
    private final LocationMapper locationMapper;
    private final PerformanceService performanceService;
    private final PerformanceMapper performanceMapper;

    @Autowired
    public SearchEndpoint(EventService eventService, EventMapper eventMapper, ArtistMapper artistMapper, LocationService locationService,
                          LocationMapper locationMapper, PerformanceService performanceService, PerformanceMapper performanceMapper) {
        this.eventService = eventService;
        this.eventMapper = eventMapper;
        this.artistMapper = artistMapper;
        this.locationService = locationService;
        this.locationMapper = locationMapper;
        this.performanceService = performanceService;
        this.performanceMapper = performanceMapper;
    }

    @Secured("ROLE_USER")
    @GetMapping(value = "/events-by-artist")
    @Operation(summary = "Get all events of the given artist", security = @SecurityRequirement(name = "apiKey"))
    public List<EventSearchDto> getAllEventsOfArtist(@Valid ArtistSearchDto artist) {
        LOGGER.info("GET /api/v1/search/events-by-artist {}", artist);

        try {
            List<Event> events = this.eventService.getAllEventsOfArtist(artist);
            return this.eventMapper.eventToEventSearchDto(events);
        } catch (NotFoundException e) {
            LOGGER.warn("Unable to find events of artist" + e.getMessage());
            HttpStatus status = HttpStatus.OK;
            return new ArrayList<>();
        }
    }

    @Secured("ROLE_USER")
    @GetMapping(value = "/artists")
    @Operation(summary = "Get all artist containing the given parameters", security = @SecurityRequirement(name = "apiKey"))
    public List<ArtistSearchDto> getAllArtistWithParams(ArtistSearchDto parameters) {
        LOGGER.info("GET /api/v1/search/artists {}", parameters);
        try {
            List<Artist> artists = this.eventService.getAllArtists(parameters);
            return artistMapper.artistToEventArtistSearchDto(artists);
        } catch (NotFoundException e) {
            LOGGER.warn("Unable to find artists" + e.getMessage());
            HttpStatus status = HttpStatus.OK;
            return new ArrayList<>();
        }
    }

    @Secured("ROLE_USER")
    @GetMapping(value = "/locations")
    @Operation(summary = "Get all locations with the given parameters", security = @SecurityRequirement(name = "apiKey"))
    public List<LocationSearchDto> getAllLocationsWithParameters(LocationSearchDto parameters) {
        LOGGER.info("GET /api/v1/search/locations {}", parameters);

        try {
            List<Location> locations = this.locationService.getAllLocationsWithParameters(parameters);
            List<LocationSearchDto> locationSearchDtos = new ArrayList<>();
            for (Location location : locations) {
                locationSearchDtos.add(this.locationMapper.locationToLocationSearchDto(location));
            }
            return locationSearchDtos;
        } catch (NotFoundException e) {
            LOGGER.warn("Unable to find locations with the given parameters" + e.getMessage());
            HttpStatus status = HttpStatus.OK;
            return new ArrayList<>();
        }
    }

    @Secured("ROLE_USER")
    @GetMapping(value = "/events")
    @Operation(summary = "Get all events with the given parameters", security = @SecurityRequirement(name = "apiKey"))
    public List<EventSearchDto> getAllEventsWithParameters(EventSearchDto parameters) {
        LOGGER.info("GET /api/v1/search/events {}", parameters);
        try {
            List<Event> events = this.eventService.getAllEventsWithParameters(parameters);
            return this.eventMapper.eventToEventSearchDto(events);
        } catch (NotFoundException e) {
            LOGGER.warn("Unable to find locations with the given parameters" + e.getMessage());
            HttpStatus status = HttpStatus.OK;
            return new ArrayList<>();
        }
    }

    @Secured("ROLE_USER")
    @GetMapping(value = "/performances")
    @Operation(summary = "Get all performances with the given parameters", security = @SecurityRequirement(name = "apiKey"))
    public List<PerformanceSearchDto> getAllPerformancesWithParameters(PerformanceSearchDto parameters) {
        LOGGER.info("GET /api/v1/search/performances {}", parameters);
        try {
            List<Performance> performances = this.performanceService.getAllPerformancesWithParameters(parameters);
            return this.performanceMapper.performanceToPerformanceSearchDto(performances);
        } catch (NotFoundException e) {
            LOGGER.warn("Unable to find performances with the given parameters" + e.getMessage());
            HttpStatus status = HttpStatus.OK;
            return new ArrayList<>();
        }
    }
}
