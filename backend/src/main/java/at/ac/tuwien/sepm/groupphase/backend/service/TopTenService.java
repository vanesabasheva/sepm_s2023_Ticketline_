package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.topten.EventTicketCountDto;

import java.util.List;

public interface TopTenService {


    /**
     * Get the top ten events by ticket count and category.
     *
     * @return a list of the top ten events by ticket count and category
     */
    List<EventTicketCountDto> getTopTenEventsByTicketCount();

}
