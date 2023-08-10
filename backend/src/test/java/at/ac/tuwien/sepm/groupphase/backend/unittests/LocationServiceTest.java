package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.search.LocationSearchDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Hall;
import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.HallRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.LocationRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.PerformanceRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.LocationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class LocationServiceTest {
    @Autowired
    LocationService locationService;
    @Autowired
    LocationRepository locationRepository;
    @Autowired
    private PerformanceRepository performanceRepository;
    @Autowired
    private HallRepository hallRepository;
    @Autowired
    private EventRepository eventRepository;
    private Location location;
    private Location location1;
    private Performance performance;
    private Hall hall;
    private Event event;
    private LocationSearchDto locationSearchDto;
    private LocationSearchDto locationSearchDto1;

    @BeforeEach
    public void beforeAll() {

        location = new Location();
        location.setPostalCode(1111);
        location.setCountry("Austria");
        location.setCity("Vienna");
        location.setStreet("Street 1");
        locationRepository.save(location);

        location1 = new Location();
        location1.setPostalCode(2222);
        location1.setCountry("Austria");
        location1.setCity("Vienna");
        location1.setStreet("Street 2");
        locationRepository.save(location1);

        locationSearchDto = new LocationSearchDto();
        locationSearchDto.setCountry("Aus");

        locationSearchDto1 = new LocationSearchDto();
        locationSearchDto1.setCity("Sofia");

        event = new Event();
        event.setName("The Eras Tour 2");
        event.setLength(Duration.ZERO);
        eventRepository.save(event);

        hall = new Hall();
        hall.setLocation(location);
        hall.setName("Halle 2");
        hallRepository.save(hall);

        performance = new Performance();
        performance.setDatetime(LocalDateTime.of(2023, 10, 10, 10, 10));
        performance.setHall(hall);
        performance.setEvent(event);
        performanceRepository.save(performance);
    }

    @Test
    void getAllLocationsWithCityNotMatchingTheParameterThrowsNotFound() {
        Assertions.assertThrows(NotFoundException.class, () -> locationService.getAllLocationsWithParameters(locationSearchDto1));
    }

    @Test
    void getAllLocationsWithCountryMatchingTheGivenParametersShouldReturnAllLocations() {
        Assertions.assertDoesNotThrow(() -> locationService.getAllLocationsWithParameters(locationSearchDto));

        List<Location> locations = locationService.getAllLocationsWithParameters(locationSearchDto);
        assertEquals(1, locations.size());
        assertEquals(location.getCountry(), locations.get(0).getCountry());
    }

}
