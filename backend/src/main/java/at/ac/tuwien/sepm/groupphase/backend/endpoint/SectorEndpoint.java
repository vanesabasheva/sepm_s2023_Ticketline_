package at.ac.tuwien.sepm.groupphase.backend.endpoint;


import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.createevent.SectorDto;
import at.ac.tuwien.sepm.groupphase.backend.service.SectorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1")
public class SectorEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final SectorService sectorService;


    @Autowired
    public SectorEndpoint(SectorService sectorService) {
        this.sectorService = sectorService;
    }


    @PermitAll
    @GetMapping(value = "/sectors/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Buy Tickets from Cart", security = @SecurityRequirement(name = "apiKey"))
    public List<SectorDto> getSectorsFromHall(@Valid @PathVariable("id") Integer id) {
        LOGGER.info("Get /api/v1/sectors {}", id);
        return sectorService.getSectorsFromHall(id);
    }

}
