package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.user.UserAdminDto;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.UnauthorizedException;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.user.UserRegisterDto;
import at.ac.tuwien.sepm.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.service.PasswordResetService;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import java.lang.invoke.MethodHandles;
import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping(value = "/api/v1")
public class AdminEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final UserService userService;
    private final PasswordResetService passwordResetService;

    @Autowired
    public AdminEndpoint(UserService userService, PasswordResetService passwordResetService) {
        this.userService = userService;
        this.passwordResetService = passwordResetService;
    }

    @Secured("ROLE_ADMIN")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/userlist")
    @Operation(summary = "get all users", security = @SecurityRequirement(name = "apiKey"))
    public List<UserAdminDto> getAllUsers(@RequestParam boolean locked) {
        LOGGER.info(String.format("GET /api/v1/userlist: get all users (locked: %s)", locked));
        return userService.getAllUsers(locked);
    }

    @Secured("ROLE_ADMIN")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/users/{id}/reset-password")
    @Operation(summary = "reset user password", security = @SecurityRequirement(name = "apiKey"))
    public void resetUserPassword(@PathVariable("id") Integer id) {
        LOGGER.info(String.format("POST /api/v1/users/%s/resetpassword: reset user (ID: %s) password", id, id));
        try {
            long var = System.currentTimeMillis();
            passwordResetService.sendResetToken(id);
            System.out.println("Time taken (ENDPOINT): " + (System.currentTimeMillis() - var));
        } catch (NotFoundException e) {
            LOGGER.info("User not found:" + e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (ValidationException e) {
            LOGGER.info("Validation error:" + e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @Secured("ROLE_ADMIN")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/users/{id}/lock")
    @Operation(summary = "lock user", security = @SecurityRequirement(name = "apiKey"))
    public void lockUser(@PathVariable("id") Integer id) {
        LOGGER.info(String.format("POST /api/v1/users/%s/lock: lock user %s", id, id));
        try {
            Integer authentication = (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            userService.setUserStatus(id, authentication, true);
        } catch (UnauthorizedException e) {
            LOGGER.info("Access forbidden: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        } catch (NotFoundException e) {
            LOGGER.info("User not found:" + e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (ValidationException e) {
            LOGGER.info("Validation error: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
        }
    }

    @Secured("ROLE_ADMIN")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/users/{id}/open")
    @Operation(summary = "unlock user", security = @SecurityRequirement(name = "apiKey"))
    public void unlockUser(@PathVariable("id") Integer id) {
        LOGGER.info(String.format("POST /api/v1/users/%s/open: unlock user %s", id, id));
        try {
            Integer authentication = (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            userService.setUserStatus(id, authentication, false);
        } catch (UnauthorizedException e) {
            LOGGER.info("Access forbidden: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        } catch (NotFoundException e) {
            LOGGER.info("User not found:" + e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (ValidationException e) {
            LOGGER.info("Validation error: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
        }
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/admins")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create new user", security = @SecurityRequirement(name = "apiKey"))
    public String registerAdminUser(@RequestBody UserRegisterDto userRegisterDto) {
        LOGGER.info(String.format("POST /api/v1/admins user: %s", userRegisterDto.toString()));
        try {
            Integer authentication = (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            boolean check;
            try {
                check = userService.isUserAdmin(authentication);
            } catch (ValidationException e) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Requester ID invalid:", e);
            } catch (NotFoundException e) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requester ID not found:", e);
            }
            if (!check) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You're forbidden to create an admin user");
            }
            return userService.registerUser(userRegisterDto);
        } catch (ConflictException e) {
            LOGGER.info("Conflict with DB: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
        } catch (ValidationException e) {
            LOGGER.info("Request was formatted incorrectly:" + e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
        }
    }
}
