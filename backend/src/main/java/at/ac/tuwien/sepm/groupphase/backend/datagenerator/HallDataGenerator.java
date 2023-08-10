package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.Hall;
import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import at.ac.tuwien.sepm.groupphase.backend.repository.HallRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.LocationRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.Optional;

@Profile("generateData")
@Component
@DependsOn({"locationDataGenerator"})
public class HallDataGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final int NUMBER_OF_HALLS_TO_GENERATE = 20;
    private static final String TEST_HALL_NAME = "HALL";
    private static final int TEST_LOCATION_ID = 1;
    private final HallRepository hallRepository;
    private final LocationRepository locationRepository;

    public HallDataGenerator(HallRepository hallRepository, LocationRepository locationRepository) {
        this.hallRepository = hallRepository;
        this.locationRepository = locationRepository;
    }

    @PostConstruct
    private void generateHall() {
        if (hallRepository.findAll().size() > 0) {
            LOGGER.debug("hall already generated");
        } else {
            LOGGER.debug("generating " + NUMBER_OF_HALLS_TO_GENERATE + " hall entries");
            for (int i = 0; i < NUMBER_OF_HALLS_TO_GENERATE; i++) {
                Optional<Location> location = locationRepository.findById(TEST_LOCATION_ID + i);
                if (location.isEmpty()) {
                    LOGGER.debug("location {} not found", TEST_LOCATION_ID + i);
                } else {
                    Hall hall = Hall.HallBuilder.aHall()
                        .withName(TEST_HALL_NAME + " " + i)
                        .withLocation(location.get())
                        .build();
                    LOGGER.debug("saving hall {}", hall);
                    hallRepository.save(hall);
                }
            }
        }
    }
}
