package at.ac.tuwien.sepm.groupphase.backend.service;


import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.LocationDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimplePaymentDetailDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.checkout.CheckoutLocation;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.user.UserAdminDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.user.UserLoginDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.user.UserPasswordChangeDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.user.UserProfileDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.user.UserRegisterDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import at.ac.tuwien.sepm.groupphase.backend.entity.PaymentDetail;
import at.ac.tuwien.sepm.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.UnauthorizedException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

/**
 * The interface User service.
 */
public interface UserService extends UserDetailsService {

    /**
     * Get all users.
     *
     * @param showLocked if true, only locked users are returned
     * @return list of users
     */
    List<UserAdminDto> getAllUsers(boolean showLocked);

    /**
     * Set user status.
     *
     * @param id        the id of the user to be edited
     * @param requester the id of the user who requested the operation
     * @param lock      specifies if the operation should be locking or unlocking
     * @throws UnauthorizedException if the user is not authorized to perform this operation
     * @throws NotFoundException     if the user is not found
     * @throws ValidationException   if the user is already locked/unlocked or if the user is an admin
     */
    void setUserStatus(Integer id, Integer requester, boolean lock) throws UnauthorizedException, NotFoundException, ValidationException;


    /**
     * Find a user in the context of Spring Security based on the email address
     * <br>
     * For more information have a look at this tutorial:
     * https://www.baeldung.com/spring-security-authentication-with-a-database
     *
     * @param email the email address
     * @return a Spring Security user
     * @throws UsernameNotFoundException is thrown if the specified user does not exists
     */
    @Override
    UserDetails loadUserByUsername(String email) throws UsernameNotFoundException;

    /**
     * Find an application user based on the email address.
     *
     * @param email the email address
     * @return a application user
     */
    ApplicationUser findApplicationUserByEmail(String email);

    /**
     * Checks if a user is an admin.
     *
     * @param userId the id
     * @return true if the user is an admin; false otherwise
     * @throws ValidationException if the id is not valid
     * @throws NotFoundException   if the user does not exist
     */
    boolean isUserAdmin(Integer userId) throws ValidationException, NotFoundException;

    /**
     * Log in a user.
     *
     * @param userLoginDto login credentials
     * @return the JWT, if successful
     * @throws BadCredentialsException the bad credentials exception
     * @throws LockedException         the locked exception
     */
    String login(UserLoginDto userLoginDto) throws BadCredentialsException, LockedException;

    /**
     * register a user.
     *
     * @param userRegisterDto the user DTO to register.
     * @return "success" if successful
     * @throws ConflictException   if the email is already ued
     * @throws ValidationException if the DTO is not valid
     */
    String registerUser(UserRegisterDto userRegisterDto) throws ConflictException, ValidationException;

    /**
     * isUserAuthenticated makes sure that a requests authentication matches the given user id.
     *
     * @param userId the user id
     * @param auth   the authentication of the request
     * @return true if the user is authenticated; false otherwise
     */
    boolean isUserAuthenticated(Integer userId, Authentication auth);

    /**
     * getUserLocations returns all locations of a user.
     *
     * @param userId the id of the user
     * @return a list of locations
     */
    List<Location> getUserLocations(Integer userId);

    /**
     * updateUserLocation creates a new location for a user.
     *
     * @param id          the id of the user
     * @param locationDto the location to create
     * @return the created location
     * @throws ValidationException if the DTO is not valid
     */
    Location addUserLocation(Integer id, LocationDto locationDto) throws ValidationException, ConflictException;


    /**
     * editUserLocation edits a existing location of a user.
     *
     * @param userId      the id of the user
     * @param locationDto the location to edit
     * @return the edited location
     * @throws ValidationException if the DTO is not valid
     */
    Location editUserLocation(Integer userId, CheckoutLocation locationDto) throws ValidationException, ConflictException;

    /**
     * deleteUserLocation deletes a location of a user.
     *
     * @param userId     the id of the user
     * @param locationId the id of the location to delete
     */
    void deleteUserLocation(Integer userId, Integer locationId);

    /**
     * getUserPaymentDetails returns all payment details of a user.
     *
     * @param userId the id of the user
     * @return a list of payment details
     */
    List<PaymentDetail> getUserPaymentDetails(Integer userId);

    /**
     * updateUserPaymentDetails creates a new payment detail for a user.
     *
     * @param userId         the id of the user
     * @param paymentDetails the payment detail to create
     * @return the created payment detail
     * @throws ValidationException if the DTO is not valid
     */
    PaymentDetail addUserPaymentDetails(Integer userId, SimplePaymentDetailDto paymentDetails) throws ValidationException, ConflictException;

    /**
     * editUserPaymentDetails edits a payment detail of a user.
     *
     * @param userId         the id of the user
     * @param paymentDetails the payment detail to edit
     * @return the edited payment detail
     * @throws ValidationException if the DTO is not valid
     */
    PaymentDetail editUserPaymentDetails(Integer userId, SimplePaymentDetailDto paymentDetails) throws ValidationException, ConflictException;

    /**
     * deleteUserPaymentDetails deletes a payment detail of a user.
     *
     * @param userId           the id of the user
     * @param paymentDetailsId the id of the payment detail to delete
     */
    void deleteUserPaymentDetails(Integer userId, Integer paymentDetailsId);

    /**
     * getUser returns the user with the given id.
     *
     * @param userId the id of the user
     * @return the user
     */
    ApplicationUser getUser(Integer userId);


    /**
     * Update user.
     *
     * @param userId the user id
     * @param user   the user data to update
     * @return the updated application user
     * @throws ValidationException the validation exception
     */
    ApplicationUser updateUser(Integer userId, UserProfileDto user) throws ValidationException, ConflictException;


    /**
     * Delete user.
     *
     * @param userId the user id
     */
    void deleteUser(Integer userId);

    /**
     * Gets user points.
     *
     * @param userId the user id
     * @return the user points
     */
    Integer getUserPoints(Integer userId);

    /**
     * Change password.
     *
     * @param id                    the id
     * @param userPasswordChangeDto the user password change dto
     * @throws ValidationException if any of the password requirements fail
     * @throws NotFoundException   if the user is not found
     */
    void changePassword(Integer id, UserPasswordChangeDto userPasswordChangeDto) throws ValidationException, NotFoundException;
}
