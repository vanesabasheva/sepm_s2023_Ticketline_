package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.topten.EventTicketCountDto;
import at.ac.tuwien.sepm.groupphase.backend.service.TopTenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/top-ten")
public class TopTenEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final TopTenService topTenService;

    public TopTenEndpoint(TopTenService topTenService) {
        this.topTenService = topTenService;
    }

    @Secured("ROLE_USER")
    @GetMapping()
    @Operation(summary = "Get information about the top ten events", security = @SecurityRequirement(name = "apiKey"))
    public List<EventTicketCountDto> getTopTenEventsByTicketCount() {
        LOGGER.debug("Get top ten events by ticket count");
        return topTenService.getTopTenEventsByTicketCount();
    }

}
