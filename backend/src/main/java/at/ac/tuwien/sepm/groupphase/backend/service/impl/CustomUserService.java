package at.ac.tuwien.sepm.groupphase.backend.service.impl;


import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.LocationDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimplePaymentDetailDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.checkout.CheckoutLocation;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.user.UserAdminDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.user.UserLoginDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.user.UserPasswordChangeDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.user.UserProfileDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.user.UserRegisterDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import at.ac.tuwien.sepm.groupphase.backend.entity.Order;
import at.ac.tuwien.sepm.groupphase.backend.entity.PaymentDetail;
import at.ac.tuwien.sepm.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepm.groupphase.backend.exception.FatalException;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.UnauthorizedException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.LocationRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.NotUserRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.OrderRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.PaymentDetailRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.JwtTokenizer;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CustomUserService implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final NotUserRepository notUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenizer jwtTokenizer;
    private final int maxFailedLogin = 5;

    private final LocationRepository locationRepository;
    private final PaymentDetailRepository paymentDetailRepository;
    private final OrderRepository orderRepository;

    private final UserMapper userMapper;

    @Autowired
    public CustomUserService(NotUserRepository notUserRepository, PasswordEncoder passwordEncoder, JwtTokenizer jwtTokenizer,
                             LocationRepository locationRepository, PaymentDetailRepository paymentDetailRepository, UserMapper userMapper,
                             OrderRepository orderRepository) {
        this.notUserRepository = notUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenizer = jwtTokenizer;
        this.paymentDetailRepository = paymentDetailRepository;
        this.locationRepository = locationRepository;
        this.orderRepository = orderRepository;
        this.userMapper = userMapper;
    }

    @Override
    public List<UserAdminDto> getAllUsers(boolean showLocked) {
        LOGGER.debug("Get all users");
        List<ApplicationUser> applicationUsers;
        if (showLocked) {
            applicationUsers = notUserRepository.findAllByLockedTrue();
        } else {
            applicationUsers = notUserRepository.findAll();
        }
        return userMapper.applicationUserToUserAdminDto(applicationUsers);
    }

    @Override
    public void setUserStatus(Integer id, Integer requester, boolean lock) throws UnauthorizedException, NotFoundException, ValidationException {
        LOGGER.debug("Toggle user status");
        List<String> validationErrors = new ArrayList<>();
        if (requester == null) {
            validationErrors.add("No requester given");
        }
        if (requester < 0) {
            validationErrors.add("Requester id must be positive");
        }
        ApplicationUser applicationUser = notUserRepository.getApplicationUserById(id);
        ApplicationUser requesterUser = notUserRepository.getApplicationUserById(requester);
        if (applicationUser == null) {
            throw new NotFoundException(String.format("Could not find the user with the id %d", id));
        }
        if (!requesterUser.getAdmin()) {
            throw new UnauthorizedException("Action not allowed",
                Collections.singletonList("Only admins can change the user status"));
        }
        if (id == null) {
            validationErrors.add("No id given");
        }
        if (id < 0) {
            validationErrors.add("Id must be positive");
        }
        if (applicationUser.getAdmin()) {
            validationErrors.add("Cannot change status of an admin");
        }
        if (applicationUser.getLocked() == lock) {
            validationErrors.add("User is already in the desired state");
        }
        if (!validationErrors.isEmpty()) {
            throw new ValidationException("Validation failed:", validationErrors);
        }
        notUserRepository.updateUserLocked(id, lock);
        notUserRepository.resetUserLoginAttempts(id);
    }

    /**
     * return the UserDetails of a given user via email address.
     *
     * @param email the email address
     * @return the UserDetails of the user
     * @throws UsernameNotFoundException if the user is not found
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        LOGGER.debug("Load all user by email");

        try {
            ApplicationUser applicationUser = findApplicationUserByEmail(email);

            List<GrantedAuthority> grantedAuthorities;
            if (applicationUser.getAdmin()) {
                grantedAuthorities = AuthorityUtils.createAuthorityList("ROLE_ADMIN", "ROLE_USER");
            } else {
                grantedAuthorities = AuthorityUtils.createAuthorityList("ROLE_USER");
            }
            return User.builder().username(applicationUser.getEmail())
                .password(applicationUser.getPassword())
                .authorities(grantedAuthorities)
                .accountLocked(applicationUser.getLocked()).build();
        } catch (NotFoundException e) {
            throw new UsernameNotFoundException(e.getMessage(), e);
        }
    }

    /**
     * returns the corresponding ApplicationUser to the given email address.
     *
     * @param email the email address .
     * @return the corresponding ApplicationUser.
     */
    @Override
    public ApplicationUser findApplicationUserByEmail(String email) {
        LOGGER.debug("Find application user by email");
        Optional<ApplicationUser> applicationUser;
        applicationUser = notUserRepository.findApplicationUsersByEmail(email);
        if (applicationUser.isPresent()) {
            return applicationUser.get();
        }
        throw new NotFoundException(String.format("Could not find the user with the email address %s", email));
    }

    @Override
    public boolean isUserAdmin(Integer userId) throws ValidationException, NotFoundException {
        LOGGER.debug("Check if user is admin");
        List<String> validationErrors = new ArrayList<>();
        if (userId == null) {
            validationErrors.add("Requester id is null");
        }
        if (userId < 0) {
            validationErrors.add("Requester id is negative");
        }
        ApplicationUser applicationUser = notUserRepository.getApplicationUserById(userId);
        if (applicationUser == null) {
            throw new NotFoundException(String.format("Could not find the user with the id %d", userId));
        }
        if (!validationErrors.isEmpty()) {
            throw new ValidationException("Failed to validate user", validationErrors);
        }
        return applicationUser.getAdmin();
    }

    /**
     * login user  with email and password.
     *
     * @param userLoginDto login credentials of the user
     * @return a jwt token if the login was successful
     */
    @Override
    public String login(UserLoginDto userLoginDto) throws BadCredentialsException, LockedException {
        UserDetails userDetails = loadUserByUsername(userLoginDto.getEmail());
        if (userDetails != null
            && userDetails.isAccountNonExpired()
            && userDetails.isAccountNonLocked()
            && userDetails.isCredentialsNonExpired()
        ) {
            List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
            LOGGER.info("roles: %s%n", Arrays.toString(roles.toArray()));

            if (!passwordEncoder.matches(userLoginDto.getPassword(), userDetails.getPassword())) {
                if (!roles.contains("ROLE_ADMIN")) {
                    LOGGER.info("user is not admin");
                    this.notUserRepository.findApplicationUsersByEmail(userLoginDto.getEmail()).ifPresent(user -> {
                        user.setFailedLogin(user.getFailedLogin() + 1);
                        this.notUserRepository.save(user);
                        LOGGER.info("failed login: %d%n", user.getFailedLogin());
                        if (user.getFailedLogin() >= maxFailedLogin) {
                            user.setLocked(true);
                            this.notUserRepository.save(user);
                            throw new LockedException(String.format("User %s is locked", user.getEmail()));
                        }

                    });
                }
                throw new BadCredentialsException("Username or password is incorrect");
            } else {

                return jwtTokenizer.getAuthToken(userDetails.getUsername(), roles);
            }
        }
        throw new LockedException(String.format("User %s is locked", userLoginDto.getEmail()));
    }

    /**
     * Register a new user.
     *
     * @param userRegisterDto user information for registration
     * @return success if the registration was successful
     */

    public String registerUser(UserRegisterDto userRegisterDto) throws ConflictException, ValidationException {
        List<String> error = new ArrayList<>();
        //check if userRegisterDto is valid and not null
        //validation happens in DTO with isValid() -> return List<String> with errors
        if (userRegisterDto == null || (error = userRegisterDto.validate()).size() > 0) {
            throw new ValidationException("UserRegisterDto is not valid", error);
        }
        LOGGER.trace("registerUser({})", userRegisterDto);
        //id is auto generated
        ApplicationUser applicationUser = ApplicationUser.UserBuilder.aUser()
            .withAdmin(userRegisterDto.getIsAdmin())
            .withEmail(userRegisterDto.getEmail())
            .withFirstName(userRegisterDto.getFirstName())
            .withLastName(userRegisterDto.getLastName())
            .withPassword(passwordEncoder.encode(userRegisterDto.getPassword()))
            .withPoints(0)
            .withLocked(false)
            .withFailedLogin(0)
            .build();
        //check if user with same email already exists
        if (notUserRepository.findApplicationUsersByEmail(userRegisterDto.getEmail()).isPresent()) {
            error.add(String.format("User with email: %s already exists", userRegisterDto.getEmail()));
            throw new ConflictException("Email Conflict", error);
        }
        ApplicationUser saved;
        //if save() throws an error then something dramatic happened
        saved = notUserRepository.save(applicationUser);
        //check if entity is the same as DTO (except password)
        if (saved.getEmail().equals(userRegisterDto.getEmail())
            && saved.getFirstName().equals(userRegisterDto.getFirstName())
            && saved.getLastName().equals(userRegisterDto.getLastName())) {
            LOGGER.trace(String.format("successful registration of user: %s", saved));
            return "success";
        } else {
            //should not happen
            throw new FatalException(
                String.format(
                    "Saved Entity does not match UserRegisterDto - Entity:{email: %s, firstName: %s, lastName: %s}, UserRegisterDto: %s",
                    saved.getEmail(),
                    saved.getFirstName(),
                    saved.getLastName(),
                    userRegisterDto));
        }
    }

    @Override
    public boolean isUserAuthenticated(Integer userId, Authentication auth) {
        LOGGER.trace("isUserAuthenticated({},{})", userId, auth);
        ApplicationUser user = notUserRepository.findApplicationUserById(userId);
        if (user == null) {
            throw new NotFoundException("Could not find User");
        }
        return user.getId().equals(auth.getPrincipal());
    }

    @Override
    public List<Location> getUserLocations(Integer userId) {
        LOGGER.trace("getUserLocations({})", userId);
        ApplicationUser user = notUserRepository.findApplicationUserById(userId);
        if (user == null) {
            throw new NotFoundException("Could not find User");
        }
        return locationRepository.findAllByUserId(userId);
    }

    @Override
    public Location addUserLocation(Integer id, LocationDto locationDto) throws ValidationException, ConflictException {
        LOGGER.trace("updateUserLocation({},{})", id, locationDto);
        List<String> error = new ArrayList<>();
        if (locationDto == null || (error = locationDto.validate()).size() > 0) {
            throw new ValidationException("LocationDto is not valid", error);
        }
        locationDto.validateApi();
        ApplicationUser user = notUserRepository.findApplicationUserById(id);
        if (user == null) {
            throw new NotFoundException("Could not find User");
        }
        List<Location> locations = locationRepository.findAllByUserId(id);
        if (!locations.isEmpty()) {
            for (Location loc : locations) {
                if (loc.getPostalCode().equals(locationDto.getPostalCode()) && loc.getCity().equals(locationDto.getCity())
                    && loc.getCountry().equals(locationDto.getCountry()) && loc.getStreet().equals(locationDto.getStreet())) {
                    error.add("Cannot add same location twice");
                    throw new ConflictException("Location already exists", error);
                }
            }
        }

        Location location = Location.LocationBuilder.aLocation()
            .withUser(user)
            .withPostalCode(locationDto.getPostalCode())
            .withCity(locationDto.getCity())
            .withCountry(locationDto.getCountry())
            .withStreet(locationDto.getStreet())
            .build();
        return this.locationRepository.save(location);
    }


    @Override
    public Location editUserLocation(Integer userId, CheckoutLocation locationDto) throws ValidationException, ConflictException {
        LOGGER.trace("editUserLocation({},{})", userId, locationDto);

        List<String> error = new ArrayList<>();
        if (locationDto == null || !(error = locationDto.validate()).isEmpty()) {
            throw new ValidationException("Location is not valid", error);
        }
        locationDto.validateApi();
        ApplicationUser user = notUserRepository.findApplicationUserById(userId);
        if (user == null) {
            throw new NotFoundException("Could not find User");
        }
        Location location = locationRepository.findLocationById(locationDto.getLocationId());
        if (location == null) {
            throw new NotFoundException("Could not find Location");
        }
        List<Location> locations = locationRepository.findAllByUserId(userId);
        if (!locations.isEmpty()) {
            for (Location loc : locations) {
                if (loc.getPostalCode().equals(locationDto.getPostalCode()) && loc.getCity().equals(locationDto.getCity())
                    && loc.getCountry().equals(locationDto.getCountry()) && loc.getStreet().equals(locationDto.getStreet())) {
                    error.add("Cannot add same location twice");
                    throw new ConflictException("Location already exists", error);
                }
            }
        }

        reusableLocationDelete(user, location);
        Location toSave = Location.LocationBuilder.aLocation()
            .withUser(user)
            .withPostalCode(locationDto.getPostalCode())
            .withCity(locationDto.getCity())
            .withCountry(locationDto.getCountry())
            .withStreet(locationDto.getStreet())
            .build();
        return this.locationRepository.save(toSave);
    }


    @Override
    public void deleteUserLocation(Integer userId, Integer locationId) {
        LOGGER.trace("deleteUserLocation({},{})", userId, locationId);
        ApplicationUser user = notUserRepository.findApplicationUserById(userId);
        if (user == null) {
            throw new NotFoundException("Could not find User");
        }
        Location location = locationRepository.findLocationById(locationId);
        if (location == null) {
            throw new NotFoundException("Could not find Location");
        }
        reusableLocationDelete(user, location);
    }

    private void reusableLocationDelete(ApplicationUser user, Location location) {
        location.setUser(null);
        Set<Location> locations = user.getLocations();
        locations.remove(location);
        locationRepository.save(location);
        notUserRepository.save(user);
    }


    @Override
    public List<PaymentDetail> getUserPaymentDetails(Integer userId) {
        LOGGER.trace("getUserPaymentDetails({})", userId);
        ApplicationUser user = notUserRepository.findApplicationUserById(userId);
        if (user == null) {
            throw new NotFoundException("Could not find User");
        }
        return paymentDetailRepository.findPaymentDetailsByUserId(userId);
    }

    @Override
    public PaymentDetail addUserPaymentDetails(Integer userId, SimplePaymentDetailDto paymentDetails) throws ValidationException, ConflictException {
        LOGGER.trace("addUserPaymentDetails({},{})", userId, paymentDetails);
        ApplicationUser user = notUserRepository.findApplicationUserById(userId);
        if (user == null) {
            throw new NotFoundException("Could not find User");
        }
        List<String> error = new ArrayList<>();
        if (paymentDetails == null || !(error = paymentDetails.validate()).isEmpty()) {
            throw new ValidationException("Payment Detail is not valid", error);
        }
        checkPaymentForDuplicates(userId, paymentDetails, error);
        PaymentDetail paymentDetail = PaymentDetail.PaymentDetailBuilder.aPaymentDetail()
            .withUser(user)
            .withCardNumber(paymentDetails.getCardNumber())
            .withCardHolder(paymentDetails.getCardHolder())
            .withExpirationDate(paymentDetails.getExpirationDate())
            .withCvv(paymentDetails.getCvv())
            .build();

        return reusablePaymentDetailSave(user, paymentDetail);
    }

    private PaymentDetail reusablePaymentDetailSave(ApplicationUser user, PaymentDetail paymentDetail) {
        PaymentDetail saved = this.paymentDetailRepository.save(paymentDetail);
        Set<PaymentDetail> userPaymentDetails = user.getPaymentDetails();
        userPaymentDetails.add(saved);
        user.setPaymentDetails(userPaymentDetails);
        return saved;
    }

    @Override
    public PaymentDetail editUserPaymentDetails(Integer userId, SimplePaymentDetailDto paymentDetails) throws ValidationException, ConflictException {
        LOGGER.trace("editUserPaymentDetails({},{})", userId, paymentDetails);
        ApplicationUser user = notUserRepository.findApplicationUserById(userId);
        if (user == null) {
            throw new NotFoundException("Could not find User");
        }
        List<String> error = new ArrayList<>();
        if (paymentDetails == null || !(error = paymentDetails.validate()).isEmpty()) {
            throw new ValidationException("PaymentDetails is not valid", error);
        }
        PaymentDetail paymentDetail = paymentDetailRepository.getPaymentDetailById(paymentDetails.getId());
        if (paymentDetail == null) {
            throw new NotFoundException("Could not find Payment Detail");
        }
        if (paymentDetail.getOrders().isEmpty()) {
            checkPaymentForDuplicates(userId, paymentDetails, error);
            paymentDetail.setCardHolder(paymentDetails.getCardHolder());
            paymentDetail.setCardNumber(paymentDetails.getCardNumber());
            paymentDetail.setCvv(paymentDetails.getCvv());
            paymentDetail.setExpirationDate(paymentDetails.getExpirationDate());

            return this.paymentDetailRepository.save(paymentDetail);
        } else {
            reusablePaymentDetailDelete(user, paymentDetail);
            PaymentDetail newPaymentDetail = PaymentDetail.PaymentDetailBuilder.aPaymentDetail()
                .withUser(user)
                .withCardNumber(paymentDetails.getCardNumber())
                .withCardHolder(paymentDetails.getCardHolder())
                .withExpirationDate(paymentDetails.getExpirationDate())
                .withCvv(paymentDetails.getCvv())
                .build();
            return reusablePaymentDetailSave(user, newPaymentDetail);
        }
    }

    private void checkPaymentForDuplicates(Integer userId, SimplePaymentDetailDto paymentDetails, List<String> error) throws ConflictException {
        List<PaymentDetail> paymentDetailList = paymentDetailRepository.findByUserId(userId);
        if (!paymentDetailList.isEmpty()) {
            for (PaymentDetail paymentDetail1 : paymentDetailList) {
                if (paymentDetail1.getCardNumber().equals(paymentDetails.getCardNumber()) && paymentDetail1.getCardHolder().equals(paymentDetails.getCardHolder())
                    && paymentDetail1.getExpirationDate().equals(paymentDetails.getExpirationDate()) && paymentDetail1.getCvv().equals(paymentDetails.getCvv())) {
                    error.add("Cannot add same payment detail twice");
                    throw new ConflictException("Payment Detail already exists", error);
                }
            }
        }
    }

    @Override
    public void deleteUserPaymentDetails(Integer userId, Integer paymentDetailsId) {
        LOGGER.trace("deleteUserPaymentDetails({},{})", userId, paymentDetailsId);
        ApplicationUser user = notUserRepository.findApplicationUserById(userId);
        if (user == null) {
            throw new NotFoundException("Could not find User");
        }

        PaymentDetail paymentDetail = paymentDetailRepository.getPaymentDetailById(paymentDetailsId);
        if (paymentDetail == null) {
            throw new NotFoundException("Could not find Payment Detail");
        }
        reusablePaymentDetailDelete(user, paymentDetail);
    }

    private void reusablePaymentDetailDelete(ApplicationUser user, PaymentDetail paymentDetail) {
        paymentDetail.setUser(null);
        Set<PaymentDetail> paymentDetails = user.getPaymentDetails();
        paymentDetails.remove(paymentDetail);
        paymentDetailRepository.save(paymentDetail);
        notUserRepository.save(user);
    }

    @Override
    public ApplicationUser getUser(Integer id) {
        LOGGER.trace("getUser({})", id);
        ApplicationUser user = notUserRepository.getApplicationUserById(id);
        if (user == null) {
            throw new NotFoundException("Could not find User");
        }
        return user;
    }

    @Override
    public ApplicationUser updateUser(Integer userId, UserProfileDto user) throws ValidationException, ConflictException {
        LOGGER.trace("updateUser({},{})", userId, user);
        ApplicationUser applicationUser = notUserRepository.getApplicationUserById(userId);
        if (applicationUser == null) {
            throw new NotFoundException("Could not find User");
        }
        List<String> error = new ArrayList<>();
        if (user == null || !(error = user.validate()).isEmpty()) {
            throw new ValidationException("User is not valid", error);
        }
        Optional<ApplicationUser> users = notUserRepository.findApplicationUsersByEmail(user.getEmail());
        if (users.isPresent() && !users.get().getId().equals(userId)) {
            error.add("Email already exists");
            throw new ConflictException("User is not valid", error);
        }
        applicationUser.setFirstName(user.getFirstName());
        applicationUser.setLastName(user.getLastName());
        applicationUser.setEmail(user.getEmail());
        return notUserRepository.save(applicationUser);
    }

    @Override
    public void deleteUser(Integer userId) {
        LOGGER.trace("deleteUser({})", userId);
        ApplicationUser user = notUserRepository.findApplicationUserById(userId);
        if (user == null) {
            throw new NotFoundException("Could not find User");
        }
        Set<Order> orders = user.getOrders();
        for (Order order : orders) {
            order.setUser(null);
            orderRepository.save(order);
        }
        Set<PaymentDetail> paymentDetails = user.getPaymentDetails();
        for (PaymentDetail paymentDetail : paymentDetails) {
            paymentDetail.setUser(null);
            paymentDetailRepository.save(paymentDetail);
        }
        Set<Location> locations = user.getLocations();
        for (Location location : locations) {
            location.setUser(null);
            locationRepository.save(location);
        }

        notUserRepository.delete(user);
    }

    @Override
    public Integer getUserPoints(Integer userId) {
        LOGGER.trace("getUserPoints({})", userId);
        ApplicationUser user = notUserRepository.findApplicationUserById(userId);
        if (user == null) {
            throw new NotFoundException("Could not find User");
        }
        return user.getPoints();
    }

    @Override
    public void changePassword(Integer id, UserPasswordChangeDto userPasswordChangeDto) throws ValidationException, NotFoundException {
        LOGGER.trace("changePassword({},{})", id, userPasswordChangeDto);
        ApplicationUser user = notUserRepository.findApplicationUserById(id);
        if (user == null) {
            throw new NotFoundException("Could not find User");
        }
        List<String> error = new ArrayList<>();
        if (userPasswordChangeDto == null || !(error = userPasswordChangeDto.validate()).isEmpty()) {
            throw new ValidationException("Invalid data", error);
        }
        if (passwordEncoder.matches(userPasswordChangeDto.getOldPassword(), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(userPasswordChangeDto.getNewPassword1()));
            user.setFailedLogin(0);
            notUserRepository.save(user);
        } else {
            if (!user.getAdmin()) {
                error.add("Old password is not correct");
                user.setFailedLogin(user.getFailedLogin() + 1);
                if (user.getFailedLogin() >= maxFailedLogin) {
                    user.setLocked(true);
                    notUserRepository.save(user);
                    throw new ValidationException("Old password is not correct",
                        List.of("Your account has been locked due to too many failed attempts. "
                            + "Please contact the administrator to unlock your account"));
                } else if (user.getFailedLogin() >= maxFailedLogin - 1) {
                    notUserRepository.save(user);
                    throw new ValidationException("Old password is not correct",
                        List.of("You have one more attempt before your account is locked"));
                }
                notUserRepository.save(user);
                throw new ValidationException("Old password is not correct", error);
            } else {
                error.add("Old password is not correct");
                notUserRepository.save(user);
                throw new ValidationException("Old password is not correct", error);
            }
        }
    }
}
