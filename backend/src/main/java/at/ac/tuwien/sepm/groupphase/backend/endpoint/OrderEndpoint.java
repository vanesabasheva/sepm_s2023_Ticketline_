package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.orderpage.OrderPageDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.OrderMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Order;
import at.ac.tuwien.sepm.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.UnauthorizedException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletResponse;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/v1")
public class OrderEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final OrderService orderService;
    private final OrderMapper orderMapper;

    public OrderEndpoint(OrderService orderService, OrderMapper orderMapper) {
        this.orderService = orderService;
        this.orderMapper = orderMapper;
    }

    @Secured("ROLE_USER")
    @DeleteMapping("/orders/{id}/items")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Cancel items from order", security = @SecurityRequirement(name = "apiKey"))
    public void cancelItemsFromOrder(@Valid @PathVariable("id") Integer orderId, Authentication auth,
                                     @RequestParam Optional<String> tickets, @RequestParam Optional<String> merchandise) {
        LOGGER.info("Cancel items from order {}", orderId);

        Integer[] ticketsArr = new Integer[0];
        Integer[] merchandiseArr = new Integer[0];
        if (tickets.isPresent()) {
            ticketsArr = Arrays.stream(tickets.get().split(",")).map(Integer::parseInt).toArray(Integer[]::new);
        }
        if (merchandise.isPresent()) {
            merchandiseArr = Arrays.stream(merchandise.get().split(",")).map(Integer::parseInt).toArray(Integer[]::new);
        }
        try {
            orderService.cancelItems((Integer) auth.getPrincipal(), orderId, ticketsArr, merchandiseArr);
        } catch (ValidationException e) {
            LOGGER.info("Error cancelling order: " + e.getMessage());
            HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
            throw new ResponseStatusException(status, e.getMessage(), e);
        } catch (ConflictException e) {
            LOGGER.info("Unable to cancel items: " + e.getMessage());
            HttpStatus status = HttpStatus.CONFLICT;
            throw new ResponseStatusException(status, e.getMessage(), e);
        } catch (UnauthorizedException e) {
            LOGGER.info("Unable to cancel items: " + e.getMessage());
            HttpStatus status = HttpStatus.UNAUTHORIZED;
            throw new ResponseStatusException(status, e.getMessage(), e);
        } catch (NotFoundException e) {
            LOGGER.info("Unable to cancel items: " + e.getMessage());
            HttpStatus status = HttpStatus.NOT_FOUND;
            throw new ResponseStatusException(status, e.getMessage(), e);
        }
    }

    @Secured("ROLE_USER")
    @DeleteMapping("/orders/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Cancel order", security = @SecurityRequirement(name = "apiKey"))
    public void cancelOrder(@Valid @PathVariable("id") Integer orderId, Authentication auth) {
        LOGGER.info("Cancel order {}", orderId);
        try {
            orderService.cancelOrder((Integer) auth.getPrincipal(), orderId);
        } catch (ConflictException e) {
            LOGGER.info("Error cancelling order: " + e.getMessage());
            HttpStatus status = HttpStatus.CONFLICT;
            throw new ResponseStatusException(status, e.getMessage(), e);
        } catch (UnauthorizedException e) {
            LOGGER.info("Error cancelling order: " + e.getMessage());
            HttpStatus status = HttpStatus.UNAUTHORIZED;
            throw new ResponseStatusException(status, e.getMessage(), e);
        } catch (NotFoundException e) {
            LOGGER.info("Error cancelling order: " + e.getMessage());
            HttpStatus status = HttpStatus.NOT_FOUND;
            throw new ResponseStatusException(status, e.getMessage(), e);
        }
    }

    @Secured("ROLE_USER")
    @GetMapping("/orders/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get order", security = @SecurityRequirement(name = "apiKey"))
    public OrderPageDto getOrder(@Valid @PathVariable("id") Integer orderId, Authentication auth) {
        LOGGER.info("Get order {}", orderId);
        try {
            Order order = orderService.getOrder((Integer) auth.getPrincipal(), orderId);
            return orderMapper.orderToOrderPageDto(order);
        } catch (UnauthorizedException e) {
            LOGGER.info("Error getting order: " + e.getMessage());
            HttpStatus status = HttpStatus.UNAUTHORIZED;
            throw new ResponseStatusException(status, e.getMessage(), e);
        } catch (NotFoundException e) {
            LOGGER.info("Error getting order: " + e.getMessage());
            HttpStatus status = HttpStatus.NOT_FOUND;
            throw new ResponseStatusException(status, e.getMessage(), e);
        }
    }

    @Secured("ROLE_USER")
    @GetMapping("/transactions/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get transaction", security = @SecurityRequirement(name = "apiKey"))
    public void getTransaction(@Valid @PathVariable("id") Integer transactionId, Authentication auth, HttpServletResponse response) {
        LOGGER.info("Get transaction {}", transactionId);
        try {
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=\"example.pdf\"");
            orderService.getTransaction((Integer) auth.getPrincipal(), transactionId, response);
        } catch (UnauthorizedException e) {
            LOGGER.info("Error getting transaction: " + e.getMessage());
            HttpStatus status = HttpStatus.UNAUTHORIZED;
            throw new ResponseStatusException(status, e.getMessage(), e);
        } catch (NotFoundException e) {
            LOGGER.info("Error getting transaction: " + e.getMessage());
            HttpStatus status = HttpStatus.NOT_FOUND;
            throw new ResponseStatusException(status, e.getMessage(), e);
        }
    }
}
