package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.user.UserPasswordResetDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.user.UserPasswordResetRequestDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepm.groupphase.backend.exception.FatalException;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.NotUserRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.JwtTokenizer;
import at.ac.tuwien.sepm.groupphase.backend.service.PasswordResetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class CustomPasswordResetService implements PasswordResetService {


    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final NotUserRepository notUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenizer jwtTokenizer;

    private final MailService mailService;

    private final int tokenValidity = 15; //minutes


    public CustomPasswordResetService(NotUserRepository notUserRepository, PasswordEncoder passwordEncoder, JwtTokenizer jwtTokenizer, MailService mailService) {
        this.notUserRepository = notUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenizer = jwtTokenizer;
        this.mailService = mailService;
    }


    @Override
    public void sendResetToken(Integer id) throws ValidationException, NotFoundException {
        LOGGER.info("sendResetToken({})", id);
        ApplicationUser user = notUserRepository.findApplicationUserById(id);
        if (user == null) {
            throw new NotFoundException("User not found");
        }
        UserPasswordResetRequestDto request = new UserPasswordResetRequestDto();
        request.setEmail(user.getEmail());
        sendResetToken(request);
    }

    @Override
    public void sendResetToken(UserPasswordResetRequestDto request) throws ValidationException {
        LOGGER.info("sendResetToken({})", request);
        List<String> error = null;
        if (request == null || (error = request.validate()).size() > 0) {
            throw new ValidationException("UserPasswordResetRequestDto is not valid", error);
        }
        Optional<ApplicationUser> user = this.notUserRepository.findApplicationUsersByEmail(request.getEmail());

        if (user.isPresent()) {
            ApplicationUser applicationUser = user.get();
            //check if the user is already in the reset process
            if (applicationUser.getPasswordResetToken() != null && applicationUser.getPasswordResetTs() != null) {
                // If the user is already in the reset process -> resend the email
                if (LocalDateTime.now().isBefore(applicationUser.getPasswordResetTs())) {
                    String token = applicationUser.getPasswordResetToken();
                    //resend the email
                    mailService.sendPasswordResetEmail(applicationUser, token);
                    return;
                }
            }

            //Generate Token
            SecureRandom secureRandom = new SecureRandom();
            byte[] tokenBytes = new byte[32]; // Adjust the length of the token as needed
            secureRandom.nextBytes(tokenBytes);
            String token = Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);

            //Save Token
            applicationUser.setPasswordResetToken(token);
            //Set Token Expire Time
            applicationUser.setPasswordResetTs(LocalDateTime.now().plusMinutes(tokenValidity));
            this.notUserRepository.save(applicationUser);

            try {
                mailService.sendPasswordResetEmail(applicationUser, token);
            } catch (Exception e) {
                throw new FatalException(String.format("Problem with Mail Service. Could not send email to %s", applicationUser.getEmail()));
            }
        }
        //Do nothing if the user does not exist -> helps to not leak DB information
    }

    @Override
    public void updatePassword(String token, UserPasswordResetDto updatePasswordDto) throws ValidationException, ConflictException {
        LOGGER.info("updatePassword({})", updatePasswordDto);
        List<String> error = null;
        if (updatePasswordDto == null || (error = updatePasswordDto.validate()).size() > 0) {
            throw new ValidationException("UserPasswordResetRequestDto is not valid", error);
        }

        Optional<ApplicationUser> user = this.notUserRepository.getApplicationUserByPasswordResetToken(token);
        if (user.isPresent()) {
            ApplicationUser applicationUser = user.get();
            //check if the token is still valid
            if (applicationUser.getPasswordResetTs() != null && LocalDateTime.now().isBefore(applicationUser.getPasswordResetTs())) {
                //set the new password
                applicationUser.setPassword(passwordEncoder.encode(updatePasswordDto.getPassword()));
                //remove the token
                applicationUser.setPasswordResetToken(null);
                applicationUser.setPasswordResetTs(null);
                this.notUserRepository.save(applicationUser);
                return;
            } else {
                throw new ConflictException("Token", List.of("Token is not valid anymore"));
            }
        }
        throw new ConflictException("Token", List.of("not valid Token"));
        //Do nothing if token is invalid -> helps to not leak DB information
    }

}
