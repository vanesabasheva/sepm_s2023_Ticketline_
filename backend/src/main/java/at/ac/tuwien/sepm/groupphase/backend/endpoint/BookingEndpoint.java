package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.OrderDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.bookings.BookingDto;
import at.ac.tuwien.sepm.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.lang.invoke.MethodHandles;


@RestController
@RequestMapping(value = "/api/v1/bookings")
public class BookingEndpoint {
    private final OrderService orderService;

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());


    @Autowired
    public BookingEndpoint(OrderService orderService) {
        this.orderService = orderService;
    }

    @Secured("ROLE_USER")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Buy Tickets from Cart", security = @SecurityRequirement(name = "apiKey"))
    public OrderDto buyTickets(@Valid @RequestBody BookingDto bookingDto, Authentication auth) {
        LOGGER.info("POST /api/v1/bookings  cart: {}", 1);
        Integer userId = (Integer) auth.getPrincipal();
        try {
            return this.orderService.buyTickets(userId, bookingDto);
        } catch (NotFoundException e) {
            LOGGER.info("Unable to buy Tickets: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (ConflictException e) {
            LOGGER.info("Unable to buy tickets: " + e.getMessage());
            HttpStatus status = HttpStatus.CONFLICT;
            throw new ResponseStatusException(status, e.getMessage(), e);
        } catch (ValidationException e) {
            LOGGER.info("Unable to buy tickets: " + e.getMessage());
            HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
            throw new ResponseStatusException(status, e.getMessage(), e);
        }
    }
}
