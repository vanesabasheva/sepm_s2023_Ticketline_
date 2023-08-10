package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.createevent.SectorDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SectorService {


    /**
     * get all sectors from a hall.
     *
     * @param hallId the id of the hall
     * @return list of sectors
     */
    List<SectorDto> getSectorsFromHall(Integer hallId);
}
