package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.DetailedReservationDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ReservationMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Reservation;
import at.ac.tuwien.sepm.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.UnauthorizedException;
import at.ac.tuwien.sepm.groupphase.backend.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.lang.invoke.MethodHandles;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1")
public class ReservationEndpoint {


    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final ReservationService reservationService;
    private final ReservationMapper reservationMapper;

    public ReservationEndpoint(ReservationService reservationService, ReservationMapper reservationMapper) {
        this.reservationService = reservationService;

        this.reservationMapper = reservationMapper;
    }


    @Secured("ROLE_USER")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/users/{id}/reservations")
    @Operation(summary = "get user reservations data", security = @SecurityRequirement(name = "apiKey"))
    public List<DetailedReservationDto> findUserReservations(@Valid @PathVariable("id") Integer userId, Authentication auth) {
        Integer authentication = (Integer) auth.getPrincipal();
        if (!authentication.equals(userId)) {
            LOGGER.warn("Unauthorized reservation request");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized reservation request\"");
        }

        try {
            List<Reservation> reservations = reservationService.findReservationsByUserIdAndCart(userId, false);
            List<DetailedReservationDto> reservationDto = reservationMapper.reservationToDetailedReservationDto(reservations);
            return reservationDto;
        } catch (NotFoundException e) {
            LOGGER.warn("Unable to find reservations" + e.getMessage());
            HttpStatus status = HttpStatus.NOT_FOUND;
            throw new ResponseStatusException(status, e.getMessage(), e);
        }
    }

    @Secured("ROLE_USER")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/reservations/{id}")
    @Operation(summary = "delete user reservation", security = @SecurityRequirement(name = "apiKey"))
    public void deleteReservation(@Valid @PathVariable("id") Integer reservationId, Authentication auth) {
        try {
            reservationService.deleteReservation(reservationId, (Integer) auth.getPrincipal());
        } catch (NotFoundException e) {
            LOGGER.warn("Unable to find reservation" + e.getMessage());
            HttpStatus status = HttpStatus.NOT_FOUND;
            throw new ResponseStatusException(status, e.getMessage(), e);
        } catch (ConflictException e) {
            LOGGER.warn("Unable to delete reservation" + e.getMessage());
            HttpStatus status = HttpStatus.CONFLICT;
            throw new ResponseStatusException(status, e.getMessage(), e);
        } catch (UnauthorizedException e) {
            LOGGER.warn("Unauthorized to delete reservation" + e.getMessage());
            HttpStatus status = HttpStatus.UNAUTHORIZED;
            throw new ResponseStatusException(status, e.getMessage(), e);
        }
    }
}
