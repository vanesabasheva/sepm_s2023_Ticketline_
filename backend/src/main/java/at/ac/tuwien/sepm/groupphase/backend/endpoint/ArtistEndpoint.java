package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArtistDto;
import at.ac.tuwien.sepm.groupphase.backend.service.ArtistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.security.PermitAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1")
public class ArtistEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final ArtistService artistService;

    @Autowired
    public ArtistEndpoint(ArtistService artistService) {
        this.artistService = artistService;
    }


    //getHalls
    @PermitAll
    @GetMapping(value = "/artists")
    @Operation(summary = "get All Artists", security = @SecurityRequirement(name = "apiKey"))
    public List<ArtistDto> getArtists() {
        LOGGER.info("Get /api/v1/artists");
        return artistService.getAllArtists();
    }

}
