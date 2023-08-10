package at.ac.tuwien.sepm.groupphase.backend.datagenerator;


import at.ac.tuwien.sepm.groupphase.backend.entity.Hall;
import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;
import at.ac.tuwien.sepm.groupphase.backend.entity.PerformanceSector;
import at.ac.tuwien.sepm.groupphase.backend.entity.Sector;
import at.ac.tuwien.sepm.groupphase.backend.repository.PerformanceRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.PerformanceSectorRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SectorRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Profile("generateData")
@Component
@DependsOn({"performanceDataGenerator", "sectorDataGenerator"})
public class PerformanceSectorDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final SectorRepository sectorRepository;

    private final PerformanceRepository performanceRepository;
    private final PerformanceSectorRepository performanceSectorRepository;

    public PerformanceSectorDataGenerator(SectorRepository sectorRepository, PerformanceRepository performanceRepository,
                                          PerformanceSectorRepository performanceSectorRepository) {
        this.sectorRepository = sectorRepository;
        this.performanceRepository = performanceRepository;
        this.performanceSectorRepository = performanceSectorRepository;
    }

    @PostConstruct
    private void generatePerformanceSector() {
        if (performanceSectorRepository.findAll().size() > 0) {
            LOGGER.debug("performance sector already generated");
        } else {
            long performanceNumber = performanceRepository.count();
            LOGGER.debug("generating {} performance sector entries", performanceNumber);
            for (int i = 1; i <= performanceNumber; i++) {

                Optional<Performance> performance = performanceRepository.findById(i);
                if (performance.isEmpty()) {
                    LOGGER.debug("performance {} not found", i);
                    continue;
                }

                Hall hall = performance.get().getHall();

                List<Sector> sectors = sectorRepository.findAllByHallId(hall.getId());

                int offset = 0;
                for (Sector sector : sectors) {
                    PerformanceSector performanceSector = PerformanceSector.PerformanceSectorBuilder.aPerformanceSector()
                        .withPrice(BigDecimal.valueOf(10 + offset * 10L))
                        .withPointsReward(10 + offset * 10)
                        .withPerformance(performance.get())
                        .withSector(sector)
                        .build();
                    LOGGER.debug("saving performance sector {}", sector);
                    performanceSectorRepository.save(performanceSector);
                    offset = (int) (Math.random() * 10);
                }
            }
        }
    }
}

