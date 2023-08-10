package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.user.UserPasswordResetDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.user.UserPasswordResetRequestDto;
import at.ac.tuwien.sepm.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.service.PasswordResetService;
import jakarta.annotation.security.PermitAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
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
@RequestMapping(value = "/api/v1")
public class PasswordResetEndpoint {


    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final PasswordResetService passwordResetService;


    public PasswordResetEndpoint(PasswordResetService passwordResetService) {
        this.passwordResetService = passwordResetService;
    }

    /**
     * This method is used to send the reset token to the user.
     *
     * @param passwordResetRequestDto containing the email and password of the user.
     */
    @PermitAll
    @PostMapping("/resets")
    @ResponseStatus(HttpStatus.OK)
    public void sendResetToken(@RequestBody UserPasswordResetRequestDto passwordResetRequestDto) {
        LOGGER.info(String.format("POST /api/v1/resets email: %s", passwordResetRequestDto.getEmail()));
        try {
            this.passwordResetService.sendResetToken(passwordResetRequestDto);
        } catch (ValidationException e) {
            LOGGER.info("Invalid DtO: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
        }
    }


    /**
     * This method is used to update the password of the user with the given token.
     *
     * @param passwordResetDto contains token and new Password
     */
    @PermitAll
    @PutMapping("/resets/{token}")
    @ResponseStatus(HttpStatus.OK)
    public void updatePassword(@PathVariable String token, @RequestBody UserPasswordResetDto passwordResetDto) {
        LOGGER.info(String.format("PUT /api/v1/resets/%s", token));
        try {
            this.passwordResetService.updatePassword(token, passwordResetDto);
        } catch (ValidationException e) {
            LOGGER.info("Invalid Request: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
        } catch (ConflictException e) {
            LOGGER.info("Token: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
        }
    }


}
