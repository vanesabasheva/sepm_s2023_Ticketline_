package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.topten.EventTicketCountDto;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.TopTenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;

@Service
public class CustomTopTenService implements TopTenService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final EventRepository eventRepository;

    public CustomTopTenService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public List<EventTicketCountDto> getTopTenEventsByTicketCount() {
        LOGGER.debug("Get top ten events by ticket count");
        return eventRepository.findTopTenEventsByTicketCount();
    }
}
