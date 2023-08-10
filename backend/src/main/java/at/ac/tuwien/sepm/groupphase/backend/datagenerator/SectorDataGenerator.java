package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.Hall;
import at.ac.tuwien.sepm.groupphase.backend.entity.Sector;
import at.ac.tuwien.sepm.groupphase.backend.repository.HallRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SectorRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.List;

@Profile("generateData")
@Component
@DependsOn({"hallDataGenerator"})
public class SectorDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final String TEST_SECTOR_NAME = "S";

    private final SectorRepository sectorRepository;
    private final HallRepository hallRepository;

    public SectorDataGenerator(SectorRepository sectorRepository, HallRepository hallRepository) {
        this.sectorRepository = sectorRepository;
        this.hallRepository = hallRepository;
    }

    @PostConstruct
    private void generateSector() {
        if (sectorRepository.findAll().size() > 0) {
            LOGGER.debug("sector already generated");
        } else {
            List<Hall> halls = hallRepository.findAll();
            int offset = 1;
            for (Hall hall : halls) {
                int sectorNumber = (int) (3 + Math.random() * 3);
                for (int i = 0; i < sectorNumber; i++) {
                    LOGGER.debug("generating sector");
                    Sector sector = Sector.SectorBuilder.aSector()
                        .withName(TEST_SECTOR_NAME + offset)
                        .withStanding(offset % 2 == 0)
                        .withHall(hall)
                        .build();
                    LOGGER.debug("saving sector {}", sector);
                    offset++;
                    sectorRepository.save(sector);
                }
            }
        }
    }
}

