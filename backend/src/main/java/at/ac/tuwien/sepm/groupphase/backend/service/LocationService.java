package at.ac.tuwien.sepm.groupphase.backend.service;

/*
 Describes a service for managing locations
 */

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.search.LocationSearchDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LocationService {

    /**

     * Get the locations filtered by the given parameters.
     *
     * @param location the artist whose events should be listed
     * @return list of the found locations
     */
    List<Location> getAllLocationsWithParameters(LocationSearchDto location);
}
