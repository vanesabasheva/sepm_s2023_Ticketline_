package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.DetailedPerformanceDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.PerformanceMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.service.EventService;
import at.ac.tuwien.sepm.groupphase.backend.service.PerformanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/performances")
public class PerformanceEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final PerformanceService performanceService;
    private final EventService eventService;
    private final PerformanceMapper performanceMapper;

    @Autowired
    public PerformanceEndpoint(PerformanceService performanceService, EventService eventService, PerformanceMapper performanceMapper) {
        this.performanceService = performanceService;
        this.eventService = eventService;
        this.performanceMapper = performanceMapper;
    }


    @Secured("ROLE_USER")
    @GetMapping(value = "/{id}")
    @Operation(summary = "Get detailed information about a specific performance", security = @SecurityRequirement(name = "apiKey"))
    public DetailedPerformanceDto getPerformancePlan(@Valid @PathVariable Integer id) {
        LOGGER.info("GET /api/v1/performance/{}", id);

        try {
            return performanceService.getPerformancePlanById(id);
        } catch (NotFoundException e) {
            LOGGER.warn("Unable to find performance" + e.getMessage());
            HttpStatus status = HttpStatus.NOT_FOUND;
            throw new ResponseStatusException(status, e.getMessage(), e);
        }
    }

    @Secured("ROLE_USER")
    @GetMapping("/events/{id}")
    @Operation(summary = "Get all performances of the event with the given id", security = @SecurityRequirement(name = "apiKey"))
    public List<DetailedPerformanceDto> getPerformancesOfEvent(@Valid @PathVariable("id") Integer id) {
        LOGGER.info("GET api/v1/performances/event/{}", id);
        try {
            List<Performance> performances = this.performanceService.getPerformancesOfEventById(id);
            List<DetailedPerformanceDto> performanceDtos = new ArrayList<>();
            for (Performance performance : performances) {
                performanceDtos.add(this.performanceMapper.performanceDtotoDetailedPerformanceDtoForSearch(performance));
            }
            return performanceDtos;
        } catch (NotFoundException e) {
            LOGGER.warn("Unable to find performances of event" + e.getMessage());
            HttpStatus status = HttpStatus.OK;
            throw new ResponseStatusException(status, e.getMessage(), e);
        }
    }

    @Secured("ROLE_USER")
    @GetMapping("/locations/{id}")
    @Operation(summary = "Get all performances whose location is with the given id", security = @SecurityRequirement(name = "apiKey"))
    public List<DetailedPerformanceDto> getPerformancesOfLocation(@Valid @PathVariable("id") Integer id) {
        LOGGER.info("GET api/v1/performances/location/{}", id);
        try {
            List<Performance> performances = this.performanceService.getPerformancesOfLocationById(id);
            List<DetailedPerformanceDto> performanceDtos = new ArrayList<>();
            for (Performance performance : performances) {
                performanceDtos.add(this.performanceMapper.performanceDtotoDetailedPerformanceDtoForSearch(performance));
            }
            return performanceDtos;

        } catch (NotFoundException e) {
            LOGGER.warn("Unable to find performances of location" + e.getMessage());
            HttpStatus status = HttpStatus.OK;
            throw new ResponseStatusException(status, e.getMessage(), e);
        }
    }
}
