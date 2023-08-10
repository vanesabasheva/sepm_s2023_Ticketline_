package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.hall.HallDto;
import at.ac.tuwien.sepm.groupphase.backend.service.HallService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1")
public class HallEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final HallService hallService;

    @Autowired
    public HallEndpoint(HallService hallService) {
        this.hallService = hallService;
    }

    //getHalls
    @Secured("ROLE_USER")
    @GetMapping(value = "/halls")
    @Operation(summary = "get all Halls", security = @SecurityRequirement(name = "apiKey"))
    public List<HallDto> getHalls() {
        LOGGER.info("Get /api/v1/halls");
        return hallService.getHalls();
    }
}
