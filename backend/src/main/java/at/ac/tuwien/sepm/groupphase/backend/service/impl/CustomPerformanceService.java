package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.DetailedPerformanceDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.search.PerformanceSearchDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.PerformanceMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;
import at.ac.tuwien.sepm.groupphase.backend.entity.PerformanceSector;
import at.ac.tuwien.sepm.groupphase.backend.entity.Seat;
import at.ac.tuwien.sepm.groupphase.backend.entity.Sector;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.exception.FatalException;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.PerformanceRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.PerformanceSectorRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SeatRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TicketRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.PerformanceService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomPerformanceService implements PerformanceService {


    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final PerformanceRepository performanceRepository;
    private final PerformanceSectorRepository performanceSectorRepository;
    private final SeatRepository seatRepository;
    private final TicketRepository ticketRepository;
    private final PerformanceMapper performanceMapper;


    public CustomPerformanceService(PerformanceRepository performanceRepository, PerformanceMapper performanceMapper,
                                    PerformanceSectorRepository performanceSectorRepository, SeatRepository seatRepository,
                                    TicketRepository ticketRepository) {
        this.performanceRepository = performanceRepository;
        this.performanceMapper = performanceMapper;
        this.performanceSectorRepository = performanceSectorRepository;
        this.seatRepository = seatRepository;
        this.ticketRepository = ticketRepository;
    }

    @Override
    @Transactional
    public DetailedPerformanceDto getPerformancePlanById(Integer id) {
        LOGGER.debug("Find performance with id {}", id);
        Optional<Performance> p = performanceRepository.findAllPerformancesById(id);
        if (p.isEmpty()) {
            throw new NotFoundException(String.format("Could not find performance with id %s", id));
        }

        // preload
        Performance performance = p.get();
        if (performance.getHall() == null) {
            throw new NotFoundException(String.format("Could not find the hall associated with the performance with id %s", id));
        }
        performance.setHall(performance.getHall());

        if (performance.getEvent() == null) {
            throw new NotFoundException(String.format("Could not find the event associated with the performance with id %s", id));
        }
        performance.setEvent(performance.getEvent());

        // get performanceSector and load all sectors
        Set<PerformanceSector> performanceSector = performanceSectorRepository.findAllSetByPerformanceId(id);
        if (performanceSector.isEmpty()) {
            throw new NotFoundException(String.format("Could not find the performanceSectors associated with the performance with id %s", id));

        }
        performance.setPerformanceSectors(performanceSector);
        for (PerformanceSector ps : performanceSector) {
            Sector sector = ps.getSector();
            if (sector == null) {
                throw new NotFoundException(
                    String.format("Could not find the sector associated with the performanceSector of performance with id %s", ps.getId()));
            }
            ps.setSector(sector);
        }

        // find all seats by sectorId
        List<Seat> seats = seatRepository.findAllBySectorIdIn(performanceSector.stream().map(ps -> ps.getSector().getId()).toList());
        if (seats.isEmpty()) {
            throw new NotFoundException(String.format("Could not find the seats associated with the performance with id %s", id));
        }
        // match sectors with seats
        for (PerformanceSector ps : performanceSector) {
            Sector sector = ps.getSector();
            Set<Seat> sectorSeats = seats.stream()
                .filter(seat -> Objects.equals(seat.getSector().getId(), sector.getId()))
                .collect(Collectors.toSet());
            sector.setSeats(sectorSeats);
        }

        Set<Ticket> tickets = ticketRepository.findReservationsAndTicketsByPerformanceId(id);
        if (tickets.isEmpty() || tickets.size() != seats.size()) {
            throw new NotFoundException(String.format("Could not find the tickets associated with the performance with id %s", id));
        }
        performance.setTickets(tickets);

        DetailedPerformanceDto perf = performanceMapper.performanceToDetailedPerformanceDto(performance);
        if (perf == null) {
            throw new FatalException(String.format("Could not map performance with id %s", id));
        }
        return perf;
    }

    @Override
    public List<Performance> getPerformancesOfEventById(Integer id) {
        LOGGER.debug("Find performances of the event with id{}", id);
        List<Performance> performances = performanceRepository.findAllByEvent_Id(id);
        if (performances.isEmpty()) {
            throw new NotFoundException(String.format("Could not find performances of event with the given id{}", id));
        }
        return performances;
    }

    @Override
    public List<Performance> getPerformancesOfLocationById(Integer id) {
        LOGGER.debug("Find performances of location with id{}", id);
        List<Performance> performances = performanceRepository.findAllByHallLocation_Id(id);
        if (performances.isEmpty()) {
            throw new NotFoundException(String.format("Could not find performances of event with the given id{}", id));
        }
        return performances;
    }

    @Override
    public List<Performance> getAllPerformancesWithParameters(PerformanceSearchDto parameters) {
        LOGGER.debug("Find performances of location with parameters{}", parameters);
        BigDecimal lowerPrice;
        BigDecimal upperPrice;
        LocalDateTime dateTimeFrom;
        LocalDateTime dateTimeTill;

        if (parameters.getPrice() == null || Objects.equals(parameters.getPrice(), "")) {
            lowerPrice = null;
            upperPrice = null;
        } else {
            // subtract 10 euro from the searched price
            lowerPrice = BigDecimal.valueOf(Double.parseDouble(parameters.getPrice()) - 10);
            // add 10 euro to the searched price
            upperPrice = BigDecimal.valueOf(Double.parseDouble(parameters.getPrice()) + 10);
        }

        if (parameters.getDateTime() == null) {
            dateTimeFrom = null;
            dateTimeTill = null;
        } else {
            dateTimeFrom = parameters.getDateTime().minusHours(10);
            dateTimeTill = parameters.getDateTime().plusHours(10);
        }
        List<Performance> performances = performanceRepository.findAllPerformancesWithParams(
            dateTimeFrom,
            dateTimeTill,
            parameters.getEventName(),
            parameters.getHallName(),
            lowerPrice,
            upperPrice);
        if (performances.isEmpty()) {
            throw new NotFoundException(String.format("Could not find performances with the given parameters{}", parameters));
        }
        return performances;
    }
}