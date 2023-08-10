package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;
import at.ac.tuwien.sepm.groupphase.backend.entity.PerformanceSector;
import at.ac.tuwien.sepm.groupphase.backend.entity.Seat;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.repository.PerformanceRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.PerformanceSectorRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SeatRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TicketRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Optional;

@Profile("generateData")
@Component
@DependsOn({"performanceDataGenerator", "seatDataGenerator", "performanceSectorDataGenerator"})
public class TicketDataGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final PerformanceRepository performanceRepository;
    private final PerformanceSectorRepository performanceSectorRepository;
    private final TicketRepository ticketRepository;
    private final SeatRepository seatRepository;


    public TicketDataGenerator(PerformanceRepository performanceRepository,
                               PerformanceSectorRepository performanceSectorRepository, TicketRepository ticketRepository, SeatRepository seatRepository) {
        this.performanceRepository = performanceRepository;
        this.performanceSectorRepository = performanceSectorRepository;
        this.ticketRepository = ticketRepository;
        this.seatRepository = seatRepository;
    }

    @PostConstruct
    private void generateTickets() {
        if (ticketRepository.findAll().size() > 0) {
            LOGGER.debug("tickets already generated");
        } else {
            long performanceNumber = performanceRepository.count();

            LOGGER.debug("generating ticket entries");
            for (int i = 1; i <= performanceNumber; i++) {

                Optional<Performance> performance = performanceRepository.findById(i);
                if (performance.isEmpty()) {
                    LOGGER.debug("performance {} not found", i);
                    continue;
                }

                List<PerformanceSector> performanceSectors = performanceSectorRepository.findAllByPerformanceId(i);

                for (PerformanceSector sector : performanceSectors) {
                    List<Seat> seats = seatRepository.findAllBySectorId(sector.getSector().getId());

                    for (Seat seat : seats) {
                        Ticket ticket = Ticket.TicketBuilder.aTicket()
                            .withSeat(seat)
                            .withPerformance(performance.get())
                            .build();
                        LOGGER.debug("saving ticket {}", ticket);
                        ticketRepository.save(ticket);
                    }
                }
            }
        }
    }

}
