package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.user.UserPasswordResetDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.user.UserPasswordResetRequestDto;
import at.ac.tuwien.sepm.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;

public interface PasswordResetService {

    /**
     * send a reset token to the user. If the user is already in the reset process, resend the email.
     *
     * @param id the id of the user.
     * @throws ValidationException if the request is not valid.
     */
    void sendResetToken(Integer id) throws ValidationException, NotFoundException;

    /**
     * send a reset token to the user. If the user is already in the reset process, resend the email.
     *
     * @param request the email of the user.
     * @throws ValidationException if the request is not valid.
     */
    void sendResetToken(UserPasswordResetRequestDto request) throws ValidationException;


    /**
     * updates a users password if the token is valid.
     *
     * @param token             the token of the user.
     * @param updatePasswordDto the new password of the user.
     * @throws ValidationException if the request is not valid.
     * @throws ConflictException   if the token is not valid.
     */
    void updatePassword(String token, UserPasswordResetDto updatePasswordDto) throws ValidationException, ConflictException;


}
