package at.ac.tuwien.sepm.groupphase.backend.endpoint;


import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CartDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleReservationDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleTicketDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.checkout.CheckoutDetailsDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.CheckoutMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ReservationMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import at.ac.tuwien.sepm.groupphase.backend.entity.PaymentDetail;
import at.ac.tuwien.sepm.groupphase.backend.entity.Reservation;
import at.ac.tuwien.sepm.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.UnauthorizedException;
import at.ac.tuwien.sepm.groupphase.backend.service.CartService;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.lang.invoke.MethodHandles;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/users")
public class CartEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final CartService cartService;
    private final UserService userService;
    private final ReservationMapper reservationMapper;
    private final CheckoutMapper checkoutMapper;

    @Autowired
    public CartEndpoint(CartService cartService, UserService userService, ReservationMapper reservationMapper, CheckoutMapper checkoutMapper) {
        this.cartService = cartService;
        this.userService = userService;
        this.reservationMapper = reservationMapper;
        this.checkoutMapper = checkoutMapper;
    }

    @Secured("ROLE_USER")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{id}/cart")
    @Operation(summary = "get cart of a user", security = @SecurityRequirement(name = "apiKey"))
    public CartDto getCart(@Valid @PathVariable("id") Integer userId, Authentication auth) {
        LOGGER.info("GET /api/v1/users/{}/cart", userId);
        Integer authentication = (Integer) auth.getPrincipal();
        if (!authentication.equals(userId)) {
            LOGGER.warn("Unauthorized cart get");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized checkout request\"");
        }
        try {
            return cartService.getCart(userId);
        } catch (NotFoundException e) {
            LOGGER.info("Unable to get cart: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }


    @Secured("ROLE_USER")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/{id}/cart/tickets")
    @Operation(summary = "put tickets in the cart", security = @SecurityRequirement(name = "apiKey"))
    public List<SimpleReservationDto> addTicketToCart(@Valid @PathVariable("id") Integer userId, @Valid @RequestBody List<SimpleTicketDto> tickets, Authentication auth) {
        LOGGER.info("GET /api/v1/users/{}/cart/tickets", tickets);
        Integer authentication = (Integer) auth.getPrincipal();
        if (!authentication.equals(userId)) {
            LOGGER.warn("Unauthorized add ticket to cart");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized checkout request\"");
        }
        try {
            List<Reservation> reservations = cartService.reserveTickets(userId, tickets);
            return reservationMapper.reservationToSimpleReservationDto(reservations);
        } catch (NotFoundException e) {
            LOGGER.info("Unable to buy tickets: " + e.getMessage());
            HttpStatus status = HttpStatus.NOT_FOUND;
            throw new ResponseStatusException(status, e.getMessage(), e);
        } catch (ConflictException e) {
            LOGGER.info("Unable to buy tickets: " + e.getMessage());
            HttpStatus status = HttpStatus.CONFLICT;
            throw new ResponseStatusException(status, e.getMessage(), e);

        }
    }

    @Secured("ROLE_USER")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{id}/cart/tickets/{ticketId}")
    @Operation(summary = "delete ticket from cart", security = @SecurityRequirement(name = "apiKey"))
    public void deleteTicketFromCart(@Valid @PathVariable("id") Integer userId, @Valid @PathVariable("ticketId") Integer ticketId, Authentication auth) {
        LOGGER.info("DELETE /api/v1/users/{}/cart/tickets/{}", userId, ticketId);
        Integer authentication = (Integer) auth.getPrincipal();
        if (!authentication.equals(userId)) {
            LOGGER.warn("Unauthorized delete ticket from cart");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized checkout request\"");
        }
        try {
            cartService.deleteTicketFromCart(userId, ticketId);
        } catch (NotFoundException e) {
            LOGGER.info("Unable to delete ticket from cart: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (ConflictException e) {
            LOGGER.info("Unable to delete ticket from cart: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
        } catch (UnauthorizedException e) {
            LOGGER.info("Unable to delete ticket from cart: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage(), e);
        }
    }

    @Secured("ROLE_USER")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{id}/checkout-details")
    @Operation(summary = "get checkout details (locations and payment details)", security = @SecurityRequirement(name = "apiKey"))
    public CheckoutDetailsDto getUserCheckoutDetails(@Valid @PathVariable("id") Integer userId, Authentication auth) {
        LOGGER.info("GET /api/v1/users/{}/checkout-details", userId);

        Integer authentication = (Integer) auth.getPrincipal();
        if (!authentication.equals(userId)) {
            LOGGER.warn("Unauthorized checkout request");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized checkout request\"");
        }

        try {
            List<PaymentDetail> paymentDetails = userService.getUserPaymentDetails(userId);
            List<Location> locations = userService.getUserLocations(userId);
            return checkoutMapper.generateCheckoutDetailsDto(paymentDetails, locations);
        } catch (NotFoundException e) {
            LOGGER.info("Unable to get checkout details: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }
}
