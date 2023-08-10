package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.LocationDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimplePaymentDetailDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.checkout.CheckoutLocation;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.checkout.CheckoutPaymentDetail;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.user.UserPasswordChangeDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.user.UserProfileDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.LocationMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.PaymentDetailMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import at.ac.tuwien.sepm.groupphase.backend.entity.PaymentDetail;
import at.ac.tuwien.sepm.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.lang.invoke.MethodHandles;

@RestController
@RequestMapping(value = "/api/v1/users")
public class UserEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final UserService userService;
    private final LocationMapper locationMapper;
    private final PaymentDetailMapper paymentDetailMapper;
    private final UserMapper userMapper;

    @Autowired
    public UserEndpoint(UserService userService, LocationMapper locationMapper, PaymentDetailMapper paymentDetailMapper, UserMapper userMapper) {
        this.userService = userService;
        this.locationMapper = locationMapper;
        this.paymentDetailMapper = paymentDetailMapper;
        this.userMapper = userMapper;
    }


    @Secured("ROLE_USER")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/{id}/locations")
    @Operation(summary = "update Location from user", security = @SecurityRequirement(name = "apiKey"))
    public CheckoutLocation updateUserLocation(@Valid @PathVariable Integer id, @RequestBody LocationDto locationDto, Authentication auth) {
        if (!userService.isUserAuthenticated(id, auth)) {
            LOGGER.warn("Unauthorized update");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized update");
        }
        try {
            Location saved = this.userService.addUserLocation(id, locationDto);
            return this.locationMapper.locationToCheckoutLocation(saved);
        } catch (ValidationException e) {
            LOGGER.warn("Request was formatted incorrectly:" + e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
        } catch (ConflictException e) {
            LOGGER.warn("Cannot add Location:" + e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
        }
    }

    @Secured("ROLE_USER")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}/locations")
    @Operation(summary = "edit existing Location from user", security = @SecurityRequirement(name = "apiKey"))
    public CheckoutLocation editUserLocation(@Valid @PathVariable Integer id, @RequestBody CheckoutLocation locationDto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!userService.isUserAuthenticated(id, auth)) {
            LOGGER.warn("Unauthorized update");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized update");
        }
        try {
            Location saved = this.userService.editUserLocation(id, locationDto);
            return this.locationMapper.locationToCheckoutLocation(saved);
        } catch (ValidationException e) {
            LOGGER.warn("Cannot edit Location:" + e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
        } catch (NotFoundException e) {
            LOGGER.warn("Cannot edit Location:" + e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (ConflictException e) {
            LOGGER.warn("Cannot edit Location:" + e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
        }
    }


    @Secured("ROLE_USER")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{userId}/locations/{locationId}")
    @Operation(summary = "delete Location from user", security = @SecurityRequirement(name = "apiKey"))
    public void deleteUserLocation(@Valid @PathVariable Integer userId, @PathVariable Integer locationId) {
        LOGGER.info("/api/v1/users/{}/locations/{}", userId, locationId);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!userService.isUserAuthenticated(userId, auth)) {
            LOGGER.warn("Unauthorized delete");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized update");
        }
        try {
            this.userService.deleteUserLocation(userId, locationId);
        } catch (NotFoundException e) {
            LOGGER.warn("Cannot delete Location:" + e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }


    @Secured("ROLE_USER")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{id}/payment-details")
    @Operation(summary = "update Payment Detail from user", security = @SecurityRequirement(name = "apiKey"))
    public CheckoutPaymentDetail updateUserPaymentDetails(@Valid @PathVariable Integer id, @RequestBody SimplePaymentDetailDto paymentDetails,
                                                          Authentication auth) {
        if (!userService.isUserAuthenticated(id, auth)) {
            LOGGER.warn("Unauthorized update");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized update");
        }
        try {
            PaymentDetail saved = this.userService.addUserPaymentDetails(id, paymentDetails);
            return this.paymentDetailMapper.paymentDetailToCheckoutPaymentDetail(saved);
        } catch (NotFoundException e) {
            LOGGER.warn("Cannot create new payment detail: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (ValidationException e) {
            LOGGER.warn("Cannot create new payment detail:" + e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
        } catch (ConflictException e) {
            LOGGER.warn("Cannot add payment detail:" + e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
        }
    }

    @Secured("ROLE_USER")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}/payment-details")
    @Operation(summary = "edit existing Payment Detail from user", security = @SecurityRequirement(name = "apiKey"))
    public CheckoutPaymentDetail editUserPaymentDetails(@Valid @PathVariable Integer id, @RequestBody SimplePaymentDetailDto paymentDetails) {
        LOGGER.info("/api/v1/users/{}/payment-details", id);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!userService.isUserAuthenticated(id, auth)) {
            LOGGER.warn("Unauthorized update");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized update");
        }
        try {
            PaymentDetail saved = this.userService.editUserPaymentDetails(id, paymentDetails);
            return this.paymentDetailMapper.paymentDetailToCheckoutPaymentDetail(saved);
        } catch (NotFoundException e) {
            LOGGER.warn("Cannot edit payment detail: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (ValidationException e) {
            LOGGER.warn("Cannot edit payment detail:" + e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
        } catch (ConflictException e) {
            LOGGER.warn("Cannot edit payment detail:" + e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
        }
    }

    @Secured("ROLE_USER")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{userId}/payment-details/{paymentDetailsId}")
    @Operation(summary = "delete Payment Detail from user", security = @SecurityRequirement(name = "apiKey"))
    public void deleteUserPaymentDetails(@Valid @PathVariable Integer userId, @PathVariable Integer paymentDetailsId) {
        LOGGER.info("/api/v1/users/{}/payment-details/{}", userId, paymentDetailsId);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!userService.isUserAuthenticated(userId, auth)) {
            LOGGER.warn("Unauthorized delete");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized update");
        }
        try {
            this.userService.deleteUserPaymentDetails(userId, paymentDetailsId);
        } catch (NotFoundException e) {
            LOGGER.warn("Cannot delete payment detail: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @Secured("ROLE_USER")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    @Operation(summary = "get user data", security = @SecurityRequirement(name = "apiKey"))
    public UserProfileDto getUser(@Valid @PathVariable Integer id) {
        LOGGER.info("Get user {}", id);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!userService.isUserAuthenticated(id, authentication)) {
            LOGGER.warn("Unauthorized access");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized access");
        }
        try {
            ApplicationUser user = this.userService.getUser(id);
            return this.userMapper.applicationUserToProfileDto(user);
        } catch (NotFoundException e) {
            LOGGER.warn("Error getting user: " + e.getMessage());
            HttpStatus status = HttpStatus.NOT_FOUND;
            throw new ResponseStatusException(status, e.getMessage(), e);
        }
    }

    @Secured("ROLE_USER")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    @Operation(summary = "update user data", security = @SecurityRequirement(name = "apiKey"))
    public UserProfileDto updateUser(@Valid @PathVariable Integer id, @RequestBody UserProfileDto userProfileDto) {
        LOGGER.info("Update user {}", id);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!userService.isUserAuthenticated(id, authentication)) {
            LOGGER.warn("Unauthorized update");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized update");
        }
        try {
            ApplicationUser user = this.userService.updateUser(id, userProfileDto);
            return this.userMapper.applicationUserToProfileDto(user);
        } catch (NotFoundException e) {
            LOGGER.warn("Error updating user: " + e.getMessage());
            HttpStatus status = HttpStatus.NOT_FOUND;
            throw new ResponseStatusException(status, e.getMessage(), e);
        } catch (ValidationException e) {
            LOGGER.warn("Error updating user: " + e.getMessage());
            HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
            throw new ResponseStatusException(status, e.getMessage(), e);
        } catch (ConflictException e) {
            LOGGER.warn("Error updating user: " + e.getMessage());
            HttpStatus status = HttpStatus.CONFLICT;
            throw new ResponseStatusException(status, e.getMessage(), e);
        }
    }

    @Secured("ROLE_USER")
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}")
    @Operation(summary = "delete user", security = @SecurityRequirement(name = "apiKey"))
    public void deleteUser(@Valid @PathVariable Integer id) {
        LOGGER.info("Delete user {}", id);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!userService.isUserAuthenticated(id, authentication)) {
            LOGGER.warn("Unauthorized delete");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized delete");
        }
        try {
            this.userService.deleteUser(id);
        } catch (NotFoundException e) {
            LOGGER.warn("Error deleting user: " + e.getMessage());
            HttpStatus status = HttpStatus.NOT_FOUND;
            throw new ResponseStatusException(status, e.getMessage(), e);
        }
    }

    @Secured("ROLE_USER")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}/points")
    @Operation(summary = "get user points", security = @SecurityRequirement(name = "apiKey"))
    public Integer getUserPoints(@Valid @PathVariable Integer id) {
        LOGGER.info("Get user points {}", id);
        try {
            return this.userService.getUserPoints(id);
        } catch (NotFoundException e) {
            LOGGER.warn("Error getting user points: " + e.getMessage());
            HttpStatus status = HttpStatus.NOT_FOUND;
            throw new ResponseStatusException(status, e.getMessage(), e);
        }
    }

    @Secured("ROLE_USER")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/password")
    @Operation(summary = "change user password", security = @SecurityRequirement(name = "apiKey"))
    public void changePassword(@RequestBody UserPasswordChangeDto userPasswordChangeDto) {
        Integer authentication = (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LOGGER.info("Change password of {}", authentication);
        try {
            this.userService.changePassword(authentication, userPasswordChangeDto);
        } catch (NotFoundException e) {
            LOGGER.warn("Error finding user: " + e.getMessage());
            HttpStatus status = HttpStatus.NOT_FOUND;
            throw new ResponseStatusException(status, e.getMessage(), e);
        } catch (ValidationException e) {
            LOGGER.warn("Error changing password: " + e.getMessage());
            HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
            throw new ResponseStatusException(status, e.getMessage(), e);
        }
    }

}
