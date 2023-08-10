package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.search.LocationSearchDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.LocationRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.LocationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;

@Service
public class CustomLocationService implements LocationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final LocationRepository locationRepository;

    public CustomLocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Override
    public List<Location> getAllLocationsWithParameters(LocationSearchDto location) {
        LOGGER.debug("Find all locations with parameters{}", location);
        List<Location> locations = this.locationRepository.findAllContaining(location.getCity(), location.getCountry(), location.getStreet(), location.getPostalCode());
        if (locations.isEmpty()) {
            throw new NotFoundException(String.format("Could not find locations associated with the given parameters", location));
        }
        return locations;
    }
}
