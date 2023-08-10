package at.ac.tuwien.sepm.groupphase.backend.datagenerator;


import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import at.ac.tuwien.sepm.groupphase.backend.repository.LocationRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;

@Profile("generateData")
@Component
public class LocationDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final int NUMBER_OF_LOCATIONS_TO_GENERATE = 20;
    private static final String TEST_LOCATION_STREET = "STREET";
    private static final String TEST_LOCATION_CITY = "CITY";
    private static final int TEST_LOCATION_POSTAL_CODE = 1234;
    private static final String TEST_LOCATION_COUNTRY = "COUNTRY";
    private final LocationRepository locationRepository;

    public LocationDataGenerator(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @PostConstruct
    private void generateLocation() {
        if (locationRepository.findAll().size() > 0) {
            LOGGER.debug("location already generated");
        } else {
            LOGGER.debug("generating {} location entries", NUMBER_OF_LOCATIONS_TO_GENERATE);
            for (int i = 0; i < NUMBER_OF_LOCATIONS_TO_GENERATE; i++) {
                Location location = Location.LocationBuilder.aLocation()
                    .withStreet(TEST_LOCATION_STREET + " " + i)
                    .withCity(TEST_LOCATION_CITY + " " + i)
                    .withPostalCode(TEST_LOCATION_POSTAL_CODE + i)
                    .withCountry(TEST_LOCATION_COUNTRY + " " + i)
                    .build();
                LOGGER.debug("saving location {}", location);
                locationRepository.save(location);
            }
        }
    }
}
