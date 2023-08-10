package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.MerchandiseDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.bookings.BookingMerchandiseDto;
import at.ac.tuwien.sepm.groupphase.backend.service.MerchandiseService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;
import java.util.List;

@RestController
public class MerchandiseEndpoint {


    static final String BASE_PATH = "/api/v1/merch";
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final MerchandiseService merchandiseService;

    @Autowired
    public MerchandiseEndpoint(MerchandiseService merchandiseService) {
        this.merchandiseService = merchandiseService;
    }

    @Secured("ROLE_USER")
    @GetMapping(BASE_PATH)
    @Operation(summary = "Return all merch items")
    public List<MerchandiseDto> getMerchandise(@RequestParam(required = false) boolean withPoints) {
        LOGGER.info("GET" + BASE_PATH);
        LOGGER.info("withPoints: {}", withPoints);
        return this.merchandiseService.getMerchandise(withPoints);
    }

    @Secured("ROLE_USER")
    @PostMapping(BASE_PATH) // /api/v1/merch/{id}
    @Operation(summary = "Return merch items by Id")
    public List<MerchandiseDto> getInfoFromBooking(@Valid @RequestBody List<BookingMerchandiseDto> bookingMerchandises) {
        LOGGER.info("POST" + BASE_PATH + "/{}", bookingMerchandises);
        return this.merchandiseService.getInfoFromBooking(bookingMerchandises);
    }

}
