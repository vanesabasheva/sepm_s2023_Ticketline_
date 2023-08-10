package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Hall;
import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.HallRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.PerformanceRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;
import java.util.List;

@Profile("generateData")
@Component
@DependsOn({"eventDataGenerator", "hallDataGenerator"})
public class PerformanceDataGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final LocalDateTime TEST_PERFORMANCE_START = LocalDateTime.of(2024, 1, 1, 10, 0);

    private final PerformanceRepository performanceRepository;
    private final HallRepository hallRepository;
    private final EventRepository eventRepository;

    public PerformanceDataGenerator(PerformanceRepository performanceRepository, HallRepository hallRepository, EventRepository eventRepository) {
        this.performanceRepository = performanceRepository;
        this.hallRepository = hallRepository;
        this.eventRepository = eventRepository;
    }

    @PostConstruct
    private void generatePerformance() {
        if (performanceRepository.findAll().size() > 0) {
            LOGGER.debug("performance already generated");
        } else {
            LOGGER.debug("generating performance entries");
            List<Hall> halls = hallRepository.findAll();
            List<Event> events = eventRepository.findAll();
            int performancesGenerated = 0;

            for (Event event : events) {
                for (int i = 0; i < 2; i++) {
                    Hall hall = halls.get(performancesGenerated % halls.size());

                    Performance performance = Performance.PerformanceBuilder.aPerformance()
                        .withDatetime(TEST_PERFORMANCE_START.plusDays(performancesGenerated).plusHours(performancesGenerated))
                        .withHall(hall)
                        .withEvent(event)
                        .build();

                    LOGGER.debug("saving performance {}", performance);
                    performanceRepository.save(performance);
                    performancesGenerated++;
                }
            }
        }
    }
}
