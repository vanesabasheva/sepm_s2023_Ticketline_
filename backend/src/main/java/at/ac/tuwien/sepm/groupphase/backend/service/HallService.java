package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.hall.HallDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface HallService {

    /**
     * get all halls from the database.
     *
     * @return a list of all halls
     */
    List<HallDto> getHalls();
}
