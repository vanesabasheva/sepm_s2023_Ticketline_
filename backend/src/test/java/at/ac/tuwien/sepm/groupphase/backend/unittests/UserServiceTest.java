package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimplePaymentDetailDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.checkout.CheckoutLocation;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.user.UserLoginDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.user.UserPasswordChangeDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.user.UserProfileDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.user.UserRegisterDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import at.ac.tuwien.sepm.groupphase.backend.entity.Order;
import at.ac.tuwien.sepm.groupphase.backend.entity.PaymentDetail;
import at.ac.tuwien.sepm.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.LocationRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.NotUserRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.OrderRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.PaymentDetailRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class UserServiceTest {


    @Autowired
    private NotUserRepository repository;
    @Autowired
    private UserService service;
    @Autowired
    private PaymentDetailRepository paymentDetailRepository;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    private int emailCounter = 0;
    private char nextFirstNameChar = 'a';
    private char nextLastNameChar = 'A';

    private UserLoginDto notExistingUser;
    private UserRegisterDto shortPasswordWithNoUppercaseWithNoNumber;
    private UserRegisterDto passwordWithNoUppercaseWithNoNumber;
    private UserRegisterDto passwordWithNoUppercase;
    private UserRegisterDto correctPassword;
    private UserRegisterDto noEmail;
    private UserRegisterDto noValues;
    private UserRegisterDto dtoIsNull;
    private UserRegisterDto nameIsNonLetter;
    private UserRegisterDto noPassword;
    private UserRegisterDto emailIsNotValid;
    private UserRegisterDto emailIsNotValid1;
    private UserRegisterDto emailIsNotValid2;
    private UserRegisterDto emailIsNotValid3;
    private UserRegisterDto emailIsNotValid4;
    private UserRegisterDto validEmail;
    private UserRegisterDto validEmail1;
    private UserRegisterDto checkEqualDtoAndEntity;
    private UserRegisterDto newUser;
    private UserLoginDto newUserLogin;
    private UserLoginDto falsePasswordLogin;
    private UserRegisterDto existingEmail;
    private UserProfileDto updateUser;
    private UserProfileDto invalidUser;
    private UserProfileDto invalidUser1;
    private UserProfileDto invalidUser2;
    private UserProfileDto emptyUser;
    private ApplicationUser applicationUser;
    private ApplicationUser applicationUserPassword;
    private ApplicationUser user2;
    private PaymentDetail paymentDetail;
    private SimplePaymentDetailDto updatePaymentDetail;
    private SimplePaymentDetailDto invalidPaymentDetail;
    private SimplePaymentDetailDto invalidCardHolderPaymentDetail;
    private SimplePaymentDetailDto invalidCardNumberPaymentDetail;
    private SimplePaymentDetailDto invalidCvvPaymentDetail;
    private SimplePaymentDetailDto invalidExpirationDatePaymentDetail;
    private Order order;
    private Location location;
    private CheckoutLocation updateLocation;
    private CheckoutLocation invalidLocation;
    private CheckoutLocation invalidStreetLocation;
    private CheckoutLocation invalidCityLocation;
    private CheckoutLocation invalidCountryLocation;
    private CheckoutLocation invalidPostalCodeLocation;

    @AfterEach
    void tearDown() {
        locationRepository.deleteAll();
        paymentDetailRepository.deleteAll();
        repository.deleteAll();
        orderRepository.deleteAll();
    }

    private String generateEmail() {
        return String.format("newEmail%d@email.com", emailCounter++);
    }

    private String generateFirstName() {
        if (nextFirstNameChar > 'z') {
            nextFirstNameChar = 'a';
        }
        return String.format("Firstname%c", nextFirstNameChar);
    }

    private String generateLastName() {
        if (nextLastNameChar > 'Z') {
            nextLastNameChar = 'A';
        }
        return String.format("Lastname%c", nextLastNameChar);
    }


    @Test
    void loginAsNotExistingUserTest() {
        assertThrows(UsernameNotFoundException.class, () -> this.service.login(notExistingUser));
    }

    @Test
    void shortPasswordWithNoUppercaseWithNoNumberTest() {
        ValidationException exception = assertThrows(ValidationException.class, () -> this.service.registerUser(shortPasswordWithNoUppercaseWithNoNumber));
        String expectedText = "Invalid password format";
        assertTrue(exception.getMessage().contains(expectedText));
    }

    @Test
    void passwordWithNoUppercaseWithNoNumberTest() {
        ValidationException exception = assertThrows(ValidationException.class, () -> this.service.registerUser(passwordWithNoUppercaseWithNoNumber));
        String expectedText = "Invalid password format";
        assertTrue(exception.getMessage().contains(expectedText));
    }

    @Test
    void passwordWithNoUppercaseTest() {
        ValidationException exception = assertThrows(ValidationException.class, () -> this.service.registerUser(passwordWithNoUppercase));
        String expectedText = "Invalid password format";
        assertTrue(exception.getMessage().contains(expectedText));
    }

    @Test
    void correctPasswordTest() {
        assertDoesNotThrow(() -> this.service.registerUser(correctPassword));
    }

    @Test
    void noEmailTest() {
        ValidationException exception = assertThrows(ValidationException.class, () -> this.service.registerUser(noEmail));
        String expectedText = "All fields are required";
        assertTrue(exception.getMessage().contains(expectedText));
    }

    @Test
    void noValuesTest() {
        ValidationException exception = assertThrows(ValidationException.class, () -> this.service.registerUser(noValues));
        String expectedText = "All fields are required";
        assertTrue(exception.getMessage().contains(expectedText));
    }

    @Test
    void dtoIsNullTest() {
        ValidationException exception = assertThrows(ValidationException.class, () -> this.service.registerUser(dtoIsNull));
        String expectedText = "UserRegisterDto is not valid";
        assertTrue(exception.getMessage().contains(expectedText));
    }

    @Test
    void nameIsNonLetterTest() {
        ValidationException exception = assertThrows(ValidationException.class, () -> this.service.registerUser(nameIsNonLetter));
        String expectedText = "Invalid first name format";
        String expectedText1 = "Invalid last name format";
        assertTrue(exception.getMessage().contains(expectedText));
        assertTrue(exception.getMessage().contains(expectedText1));
    }

    @Test
    void noPasswordTest() {
        ValidationException exception = assertThrows(ValidationException.class, () -> this.service.registerUser(noPassword));
        String expectedText = "All fields are required";
        assertTrue(exception.getMessage().contains(expectedText));
    }

    @Test
    void emailIsNotValidTest() {
        ValidationException exception = assertThrows(ValidationException.class, () -> this.service.registerUser(emailIsNotValid));
        String expectedText = "Invalid email format";
        assertTrue(exception.getMessage().contains(expectedText));
    }

    @Test
    void emailIsNotValid1Test() {
        ValidationException exception = assertThrows(ValidationException.class, () -> this.service.registerUser(emailIsNotValid1));
        String expectedText = "Invalid email format";
        assertTrue(exception.getMessage().contains(expectedText));
    }

    @Test
    void emailIsNotValid2Test() {
        ValidationException exception = assertThrows(ValidationException.class, () -> this.service.registerUser(emailIsNotValid2));
        String expectedText = "Invalid email format";
        assertTrue(exception.getMessage().contains(expectedText));
    }

    @Test
    void emailIsNotValid3Test() {
        ValidationException exception = assertThrows(ValidationException.class, () -> this.service.registerUser(emailIsNotValid3));
        String expectedText = "Invalid email format";
        assertTrue(exception.getMessage().contains(expectedText));
    }

    @Test
    void emailIsNotValid4Test() {
        ValidationException exception = assertThrows(ValidationException.class, () -> this.service.registerUser(emailIsNotValid4));
        String expectedText = "Invalid email format";
        assertTrue(exception.getMessage().contains(expectedText));
    }

    @Test
    void validEmailTest() {
        assertDoesNotThrow(() -> this.service.registerUser(validEmail));
    }

    @Test
    void validEmail1Test() {
        assertDoesNotThrow(() -> this.service.registerUser(validEmail1));
    }

    @Test
    void checkEqualDtoAndEntityTest() {
        assertDoesNotThrow(() -> this.service.registerUser(checkEqualDtoAndEntity));

        ApplicationUser user = this.service.findApplicationUserByEmail(checkEqualDtoAndEntity.getEmail());
        assertAll(
            () -> assertEquals(checkEqualDtoAndEntity.getEmail(), user.getEmail()),
            () -> assertEquals(checkEqualDtoAndEntity.getFirstName(), user.getFirstName()),
            () -> assertEquals(checkEqualDtoAndEntity.getLastName(), user.getLastName()),
            () -> assertTrue(passwordEncoder.matches(checkEqualDtoAndEntity.getPassword(), user.getPassword())),
            () -> assertNotNull(user.getId()),
            () -> assertFalse(user.getAdmin()),
            () -> assertEquals(0, (int) user.getPoints()),
            () -> assertFalse(user.getLocked())
        );

    }

    @Test
    void registerAndLoginTest() {
        assertDoesNotThrow(() -> this.service.registerUser(newUser));
        assertDoesNotThrow(() -> this.service.login(newUserLogin));
    }

    @Test
    void registerWithExistingEmailTest() {
        assertDoesNotThrow(() -> this.service.registerUser(newUser));
        assertDoesNotThrow(() -> this.service.login(newUserLogin));
        assertThrows(ConflictException.class, () -> this.service.registerUser(existingEmail));
    }

    //------------------------PASSWORD CHANGE------------------------//
    @Test
    void changePasswordWithValidInformationShouldChangePassword() {
        UserPasswordChangeDto data = new UserPasswordChangeDto();
        data.setOldPassword("Password123");
        data.setNewPassword1("EpicPassword123");
        data.setNewPassword2("EpicPassword123");
        assertDoesNotThrow(() -> this.service.changePassword(2, data));
        data.setOldPassword("EpicPassword123");
        data.setNewPassword1("Password123");
        data.setNewPassword2("Password123");
        assertDoesNotThrow(() -> this.service.changePassword(2, data));
    }

    @Test
    void changePasswordWithNotMatchingNewPasswordShouldThrowValidationError() {
        UserPasswordChangeDto data = new UserPasswordChangeDto();
        data.setOldPassword("Password123");
        data.setNewPassword1("IsEverybodyGoingCrazy1");
        data.setNewPassword2("MoralPanic1");
        ValidationException error = assertThrows(ValidationException.class, () -> this.service.changePassword(2, data));
        assertTrue(error.getMessage().contains("New passwords do not match"));
    }

    @Test
    void changePasswordFailAttemptsTooOftenShouldLockUser() {
        UserPasswordChangeDto data = new UserPasswordChangeDto();
        data.setOldPassword("YouKnowMeTooWell");
        data.setNewPassword1("HoneyWhiskey4ever");
        data.setNewPassword2("HoneyWhiskey4ever");
        for (int i = 0; i < 6; i++) {
            ValidationException error = assertThrows(ValidationException.class, () -> this.service.changePassword(2, data));
            assertTrue(error.getMessage().contains("Old password is not correct"));
            if (i == 4) {
                assertTrue(error.getMessage().contains("Your account has been locked due to too many failed attempts."));
            } else if (i == 5) {
                assertTrue(error.getMessage().contains("Your account has been locked"));
            }
        }
        ApplicationUser user = this.repository.getApplicationUserById(2);
        assertTrue(user.getLocked());
    }


    //------------------------LOCK ACCOUNT------------------------//
    @Test
    void failedLoginUserTest() {
        ApplicationUser user = ApplicationUser.UserBuilder.aUser()
            .withId(99)
            .withAdmin(false)
            .withFirstName("user")
            .withLastName("user")
            .withEmail("user@email.com")
            .withPassword(passwordEncoder.encode("password"))
            .withPoints(0)
            .withLocked(false)
            .withFailedLogin(0)
            .build();
        this.repository.save(user);
        assertThrows(BadCredentialsException.class, () -> this.service.login(
            UserLoginDto.UserLoginDtoBuilder.anUserLoginDto()
                .withEmail(user.getEmail())
                .withPassword("falsePassword123")
                .build()
        ));
        assertEquals(1, this.service.findApplicationUserByEmail(user.getEmail()).getFailedLogin());
    }

    @Test
    void failedLoginAdminTest() {
        ApplicationUser admin = ApplicationUser.UserBuilder.aUser()
            .withId(-99)
            .withAdmin(true)
            .withFirstName("admin")
            .withLastName("admin")
            .withEmail("user@email.com")
            .withPassword(passwordEncoder.encode("password"))
            .withPoints(0)
            .withLocked(false)
            .withFailedLogin(0)
            .build();
        this.repository.save(admin);
        assertThrows(BadCredentialsException.class, () -> this.service.login(
            UserLoginDto.UserLoginDtoBuilder.anUserLoginDto()
                .withEmail(admin.getEmail())
                .withPassword("falsePassword123")
                .build()

        ));
        assertEquals(0, this.service.findApplicationUserByEmail(admin.getEmail()).getFailedLogin());

    }

    @Test
    void increaseFailedLoginTest() {
        assertDoesNotThrow(() -> this.service.registerUser(newUser));
        assertThrows(BadCredentialsException.class, () -> this.service.login(falsePasswordLogin));
        assertEquals(1, this.service.findApplicationUserByEmail(newUser.getEmail()).getFailedLogin());
        assertThrows(BadCredentialsException.class, () -> this.service.login(falsePasswordLogin));
        assertEquals(2, this.service.findApplicationUserByEmail(newUser.getEmail()).getFailedLogin());
        assertThrows(BadCredentialsException.class, () -> this.service.login(falsePasswordLogin));
        assertEquals(3, this.service.findApplicationUserByEmail(newUser.getEmail()).getFailedLogin());
        assertThrows(BadCredentialsException.class, () -> this.service.login(falsePasswordLogin));
        assertEquals(4, this.service.findApplicationUserByEmail(newUser.getEmail()).getFailedLogin());
        assertThrows(LockedException.class, () -> this.service.login(falsePasswordLogin));
        assertEquals(5, this.service.findApplicationUserByEmail(newUser.getEmail()).getFailedLogin());
        //more logins shouldn't increase "failedLogin"
        assertThrows(LockedException.class, () -> this.service.login(falsePasswordLogin));
        assertEquals(5, this.service.findApplicationUserByEmail(newUser.getEmail()).getFailedLogin());
        assertEquals(true, this.service.findApplicationUserByEmail(newUser.getEmail()).getLocked());
        assertThrows(LockedException.class, () -> this.service.login(falsePasswordLogin));
        assertEquals(5, this.service.findApplicationUserByEmail(newUser.getEmail()).getFailedLogin());
        assertEquals(true, this.service.findApplicationUserByEmail(newUser.getEmail()).getLocked());
    }

    @Test
    void DoNotIncreaseFailedLoginForAdminTest() {
        ApplicationUser admin = ApplicationUser.UserBuilder.aUser()
            .withId(-99)
            .withAdmin(true)
            .withFirstName("admin")
            .withLastName("admin")
            .withEmail("user@email.com")
            .withPassword(passwordEncoder.encode("password"))
            .withPoints(0)
            .withLocked(false)
            .withFailedLogin(0)
            .build();
        this.repository.save(admin);

        UserLoginDto adminLogin = UserLoginDto.UserLoginDtoBuilder.anUserLoginDto()
            .withEmail(admin.getEmail())
            .withPassword("falsePassword123")
            .build();


        //admins should never be locked or have failedLogin > 0
        assertThrows(BadCredentialsException.class, () -> this.service.login(adminLogin));
        assertEquals(0, this.service.findApplicationUserByEmail(adminLogin.getEmail()).getFailedLogin());
        assertThrows(BadCredentialsException.class, () -> this.service.login(adminLogin));
        assertEquals(0, this.service.findApplicationUserByEmail(adminLogin.getEmail()).getFailedLogin());
        assertThrows(BadCredentialsException.class, () -> this.service.login(adminLogin));
        assertEquals(0, this.service.findApplicationUserByEmail(adminLogin.getEmail()).getFailedLogin());
        assertThrows(BadCredentialsException.class, () -> this.service.login(adminLogin));
        assertEquals(0, this.service.findApplicationUserByEmail(adminLogin.getEmail()).getFailedLogin());
        assertThrows(BadCredentialsException.class, () -> this.service.login(adminLogin));
        assertEquals(0, this.service.findApplicationUserByEmail(adminLogin.getEmail()).getFailedLogin());
    }


    //------------------------GET USER------------------------//
    @Test
    void getUser_ShouldReturnUser() {
        ApplicationUser user = service.getUser(1);

        assertThat(user)
            .isNotNull()
            .extracting(ApplicationUser::getId, ApplicationUser::getFirstName, ApplicationUser::getLastName, ApplicationUser::getEmail)
            .containsExactly(1, user.getFirstName(), user.getLastName(), user.getEmail());
    }

    @Test
    void getUserWithNotExistingId_ShouldThrowNotFoundException() {
        assertThrows(NotFoundException.class, () -> service.getUser(99));
    }

    //------------------------UPDATE USER------------------------//
    @Test
    void updateUser_ShouldUpdateUser() throws ValidationException, ConflictException {
        ApplicationUser user = service.updateUser(1, updateUser);

        assertThat(user)
            .isNotNull()
            .extracting(ApplicationUser::getId, ApplicationUser::getFirstName, ApplicationUser::getLastName, ApplicationUser::getEmail)
            .containsExactly(1, updateUser.getFirstName(), updateUser.getLastName(), updateUser.getEmail());
    }

    @Test
    void updateUserWithNotExistingId_ShouldThrowNotFoundException() {
        assertThrows(NotFoundException.class, () -> service.updateUser(99, updateUser));
    }

    @Test
    void updateUserWithInvalidDto_ShouldThrowValidationException() {
        assertThrows(ValidationException.class, () -> service.updateUser(1, invalidUser));
    }

    @Test
    void updateUserWithNullDto_ShouldThrowValidationException() {
        assertThrows(ValidationException.class, () -> service.updateUser(1, null));
    }

    @Test
    void updateUserWithEmptyDto_ShouldThrowValidationException() {
        assertThrows(ValidationException.class, () -> service.updateUser(1, emptyUser));
    }

    @Test
    void updateUserWithInvalidEmail_ShouldThrowValidationException() {
        assertThrows(ValidationException.class, () -> service.updateUser(1, invalidUser1));
    }

    @Test
    void updateUserWithInvalidName_ShouldThrowValidationException() {
        assertThrows(ValidationException.class, () -> service.updateUser(1, invalidUser2));
    }

    //------------------------DELETE USER------------------------//
    @Test
    void deleteUser_ShouldDeleteUser() {
        service.deleteUser(1);

        assertThrows(NotFoundException.class, () -> service.getUser(1));
    }

    @Test
    void deleteUser_ShouldNotDeleteOrder() {
        Order order = applicationUser.getOrders().iterator().next();
        service.deleteUser(applicationUser.getId());

        assertThat(orderRepository.findById(order.getId())).isPresent();
    }

    @Test
    void deleteUser_ShouldNotDeletePaymentDetails() {
        PaymentDetail paymentDetail = user2.getPaymentDetails().iterator().next();
        service.deleteUser(user2.getId());

        assertThat(paymentDetailRepository.findById(paymentDetail.getId())).isPresent();
    }

    @Test
    void deleteUser_ShouldNotDeleteLocation() {
        Location location1 = user2.getLocations().iterator().next();
        service.deleteUser(user2.getId());

        assertThat(locationRepository.findById(location1.getId())).isPresent();
    }

    @Test
    void deleteUserWithNotExistingId_ShouldThrowNotFoundException() {
        assertThrows(NotFoundException.class, () -> service.deleteUser(99));
    }

    //------------------------UPDATE PAYMENT DETAILS------------------------//
    @Test
    void editPaymentDetail_ShouldUpdate() throws ValidationException, ConflictException {
        PaymentDetail paymentDetail = service.editUserPaymentDetails(user2.getId(), updatePaymentDetail);

        assertThat(paymentDetail)
            .isNotNull()
            .extracting(PaymentDetail::getId, PaymentDetail::getCardNumber, PaymentDetail::getCardHolder, PaymentDetail::getExpirationDate, PaymentDetail::getCvv)
            .containsExactly(updatePaymentDetail.getId(), updatePaymentDetail.getCardNumber(), updatePaymentDetail.getCardHolder(),
                updatePaymentDetail.getExpirationDate(), updatePaymentDetail.getCvv());
    }

    @Test
    void editPaymentDetailWithNotExistingUserId_ShouldThrowNotFoundException() {
        assertThrows(NotFoundException.class, () -> service.addUserPaymentDetails(99, updatePaymentDetail));
    }

    @Test
    void editPaymentDetailWithInvalidDto_ShouldThrowValidationException() {
        assertThrows(ValidationException.class, () -> service.addUserPaymentDetails(user2.getId(), invalidPaymentDetail));
    }

    @Test
    void editPaymentDetailWithNullDto_ShouldThrowValidationException() {
        assertThrows(ValidationException.class, () -> service.addUserPaymentDetails(user2.getId(), null));
    }

    @Test
    void editPaymentDetailWithInvalidCardNumber_ShouldThrowValidationException() {
        assertThrows(ValidationException.class, () -> service.addUserPaymentDetails(user2.getId(), invalidCardNumberPaymentDetail));
    }

    @Test
    void editPaymentDetailWithInvalidCardHolder_ShouldThrowValidationException() {
        assertThrows(ValidationException.class, () -> service.addUserPaymentDetails(user2.getId(), invalidCardHolderPaymentDetail));
    }

    @Test
    void editPaymentDetailWithInvalidExpirationDate_ShouldThrowValidationException() {
        assertThrows(ValidationException.class, () -> service.addUserPaymentDetails(user2.getId(), invalidExpirationDatePaymentDetail));
    }

    @Test
    void editPaymentDetailWithInvalidCvv_ShouldThrowValidationException() {
        assertThrows(ValidationException.class, () -> service.addUserPaymentDetails(user2.getId(), invalidCvvPaymentDetail));
    }


    //------------------------DELETE PAYMENT DETAILS------------------------//
    @Test
    void deletePaymentDetails_ShouldDelete() {
        service.deleteUserPaymentDetails(user2.getId(), paymentDetail.getId());

        PaymentDetail deletedPaymentDetail = paymentDetailRepository.getPaymentDetailById(paymentDetail.getId());
        ApplicationUser user = repository.findApplicationUserById(user2.getId());

        assertThat(user.getPaymentDetails()).isEmpty();
        assertThat(deletedPaymentDetail.getUser()).isNull();
    }

    @Test
    void deletePaymentDetailsWithNotExistingUserId_ShouldThrowNotFoundException() {
        assertThrows(NotFoundException.class, () -> service.deleteUserPaymentDetails(99, paymentDetail.getId()));
    }

    @Test
    void deletePaymentDetailsWithNotExistingPaymentDetailId_ShouldThrowNotFoundException() {
        assertThrows(NotFoundException.class, () -> service.deleteUserPaymentDetails(user2.getId(), 99));
    }

    //------------------------UPDATE LOCATIONS------------------------//
    @Test
    void editLocation_ShouldUpdate() throws ValidationException, ConflictException {
        Location updatedLocation = service.editUserLocation(user2.getId(), updateLocation);


        assertThat(updatedLocation)
            .isNotNull()
            .extracting(Location::getStreet, Location::getCity, Location::getPostalCode, Location::getCountry)
            .containsExactly(updateLocation.getStreet(), updateLocation.getCity(), updateLocation.getPostalCode(),
                updateLocation.getCountry());
    }

    @Test
    void editLocationWithNotExistingUserId_ShouldThrowNotFoundException() {
        assertThrows(NotFoundException.class, () -> service.editUserLocation(99, updateLocation));
    }

    @Test
    void editLocationWithInvalidDto_ShouldThrowValidationException() {
        assertThrows(ValidationException.class, () -> service.editUserLocation(user2.getId(), invalidLocation));
    }

    @Test
    void editLocationWithNullDto_ShouldThrowValidationException() {
        assertThrows(ValidationException.class, () -> service.editUserLocation(user2.getId(), null));
    }

    @Test
    void editLocationWithInvalidStreet_ShouldThrowValidationException() {
        assertThrows(ValidationException.class, () -> service.editUserLocation(user2.getId(), invalidStreetLocation));
    }

    @Test
    void editLocationWithInvalidCity_ShouldThrowValidationException() {
        assertThrows(ValidationException.class, () -> service.editUserLocation(user2.getId(), invalidCityLocation));
    }

    @Test
    void editLocationWithInvalidPostalCode_ShouldThrowValidationException() {
        assertThrows(ValidationException.class, () -> service.editUserLocation(user2.getId(), invalidPostalCodeLocation));
    }

    @Test
    void editLocationWithInvalidCountry_ShouldThrowValidationException() {
        assertThrows(ValidationException.class, () -> service.editUserLocation(user2.getId(), invalidCountryLocation));
    }


    //------------------------DELETE LOCATIONS------------------------//
    @Test
    void deleteLocation_ShouldDelete() {
        service.deleteUserLocation(user2.getId(), location.getId());

        Location deletedLocation = locationRepository.findLocationById(location.getId());
        ApplicationUser user = repository.findApplicationUserById(user2.getId());

        assertThat(deletedLocation.getUser()).isNull();
        assertThat(user.getLocations()).isEmpty();
    }

    @Test
    void deleteLocationWithNotExistingUserId_ShouldThrowNotFoundException() {
        assertThrows(NotFoundException.class, () -> service.deleteUserLocation(99, location.getId()));
    }

    @Test
    void deleteLocationWithNotExistingLocationId_ShouldThrowNotFoundException() {
        assertThrows(NotFoundException.class, () -> service.deleteUserLocation(user2.getId(), 99));
    }

    //------------------------POINTS------------------------//
    @Test
    void getPoints_ShouldReturnPoints() {
        int points = service.getUserPoints(applicationUser.getId());

        assertThat(points).isEqualTo(0);
    }

    @Test
    void getPointsWithNotExistingUserId_ShouldThrowNotFoundException() {
        assertThrows(NotFoundException.class, () -> service.getUserPoints(99));
    }

    //------------------------SETUP Method------------------------//
    @BeforeEach
    public void setup() {
        final String CORRECT_PASSWORD = "CorrectPassword123";
        final String FALSE_PASSWORD = "FalsePassword123";

        notExistingUser = new UserLoginDto();
        notExistingUser.setEmail("test@email.com");
        notExistingUser.setPassword(CORRECT_PASSWORD);

        applicationUserPassword = new ApplicationUser();
        applicationUserPassword.setFirstName("Diyar");
        applicationUserPassword.setLastName("Turan");
        applicationUserPassword.setEmail("diyar@mail.com");
        applicationUserPassword.setPassword(passwordEncoder.encode("Password123"));
        applicationUserPassword.setId(44);
        applicationUserPassword.setLocked(false);
        applicationUserPassword.setAdmin(false);
        applicationUserPassword.setPoints(0);
        applicationUserPassword.setFailedLogin(0);
        repository.save(applicationUserPassword);

        this.applicationUserPassword = new ApplicationUser();
        this.applicationUserPassword.setId(44);
        this.applicationUserPassword.setFirstName("Diyar");
        this.applicationUserPassword.setLastName("Turan");
        this.applicationUserPassword.setEmail("diyar1@mail.com");
        this.applicationUserPassword.setPassword("Password123");
        this.applicationUserPassword.setLocked(false);
        this.applicationUserPassword.setAdmin(false);
        this.applicationUserPassword.setPoints(0);
        this.applicationUserPassword.setFailedLogin(0);


        repository.save(this.applicationUserPassword);

        // CHECK PASSWORD
        shortPasswordWithNoUppercaseWithNoNumber = new UserRegisterDto();
        shortPasswordWithNoUppercaseWithNoNumber.setEmail(generateEmail());
        shortPasswordWithNoUppercaseWithNoNumber.setFirstName("Hermann");
        shortPasswordWithNoUppercaseWithNoNumber.setLastName("Stoss");
        shortPasswordWithNoUppercaseWithNoNumber.setPassword("weak");

        passwordWithNoUppercaseWithNoNumber = new UserRegisterDto();
        passwordWithNoUppercaseWithNoNumber.setEmail(generateEmail());
        passwordWithNoUppercaseWithNoNumber.setFirstName("Lucca");
        passwordWithNoUppercaseWithNoNumber.setLastName("Dukic");
        passwordWithNoUppercaseWithNoNumber.setPassword("weakpassword");

        passwordWithNoUppercase = new UserRegisterDto();
        passwordWithNoUppercase.setEmail(generateEmail());
        passwordWithNoUppercase.setFirstName("Diyar");
        passwordWithNoUppercase.setLastName("Turan");
        passwordWithNoUppercase.setPassword("weakpassword123");

        correctPassword = new UserRegisterDto();
        correctPassword.setEmail(generateEmail());
        correctPassword.setFirstName("Theo");
        correctPassword.setLastName("Kretz");
        correctPassword.setPassword(CORRECT_PASSWORD);

        // CHECK MANDATORY VALUES -> Validation Check
        noEmail = new UserRegisterDto();
        noEmail.setEmail("");
        noEmail.setFirstName("Vanesa");
        noEmail.setLastName("Basheva");
        noEmail.setPassword(CORRECT_PASSWORD);

        noValues = new UserRegisterDto();
        noValues.setEmail("");
        noValues.setFirstName("");
        noValues.setLastName("");
        noValues.setPassword("");

        dtoIsNull = null;

        nameIsNonLetter = new UserRegisterDto();
        nameIsNonLetter.setEmail(generateEmail());
        nameIsNonLetter.setFirstName("123");
        nameIsNonLetter.setLastName("123");
        nameIsNonLetter.setPassword(CORRECT_PASSWORD);

        noPassword = new UserRegisterDto();
        noPassword.setEmail(generateEmail());
        noPassword.setFirstName(generateFirstName());
        noPassword.setLastName(generateLastName());
        noPassword.setPassword("");

        //CHECK EMAIL
        emailIsNotValid = new UserRegisterDto();
        emailIsNotValid.setEmail("email");
        emailIsNotValid.setFirstName(generateFirstName());
        emailIsNotValid.setLastName(generateLastName());
        emailIsNotValid.setPassword(CORRECT_PASSWORD);

        emailIsNotValid1 = new UserRegisterDto();
        emailIsNotValid1.setEmail("email@");
        emailIsNotValid1.setFirstName(generateFirstName());
        emailIsNotValid1.setLastName(generateLastName());
        emailIsNotValid1.setPassword(CORRECT_PASSWORD);

        emailIsNotValid2 = new UserRegisterDto();
        emailIsNotValid2.setEmail("email@test");
        emailIsNotValid2.setFirstName(generateFirstName());
        emailIsNotValid2.setLastName(generateLastName());
        emailIsNotValid2.setPassword(CORRECT_PASSWORD);

        emailIsNotValid3 = new UserRegisterDto();
        emailIsNotValid3.setEmail("email@test.");
        emailIsNotValid3.setFirstName(generateFirstName());
        emailIsNotValid3.setLastName(generateLastName());
        emailIsNotValid3.setPassword(CORRECT_PASSWORD);

        emailIsNotValid4 = new UserRegisterDto();
        emailIsNotValid4.setEmail("email@&*&%*&.%#%^");
        emailIsNotValid4.setFirstName(generateFirstName());
        emailIsNotValid4.setLastName(generateLastName());
        emailIsNotValid4.setPassword(CORRECT_PASSWORD);

        validEmail = new UserRegisterDto();
        validEmail.setEmail("hermann.stoss@tuwien.ac.at");
        validEmail.setFirstName(generateFirstName());
        validEmail.setLastName(generateLastName());
        validEmail.setPassword(CORRECT_PASSWORD);

        validEmail1 = new UserRegisterDto();
        validEmail1.setEmail("e12122539@student.tuwien.ac.at");
        validEmail1.setFirstName(generateFirstName());
        validEmail1.setLastName(generateLastName());
        validEmail1.setPassword(CORRECT_PASSWORD);

        //CHECK IF DTO AND ENTITY ARE EQUAL

        checkEqualDtoAndEntity = new UserRegisterDto();
        checkEqualDtoAndEntity.setEmail(generateEmail());
        checkEqualDtoAndEntity.setFirstName(generateFirstName());
        checkEqualDtoAndEntity.setLastName(generateLastName());
        checkEqualDtoAndEntity.setPassword(CORRECT_PASSWORD);

        //CHECK IF YOU CAN LOGIN WITH NEW CREATED USER
        newUser = new UserRegisterDto();
        newUser.setEmail(checkEqualDtoAndEntity.getEmail());
        newUser.setFirstName(generateFirstName());
        newUser.setLastName(generateLastName());
        newUser.setPassword(CORRECT_PASSWORD);

        newUserLogin = new UserLoginDto();
        newUserLogin.setEmail(newUser.getEmail());
        newUserLogin.setPassword(newUser.getPassword());

        //LOGIN EXISTING USER WITH FALSE PASSWORD

        existingEmail = new UserRegisterDto();
        existingEmail.setEmail(newUser.getEmail());
        existingEmail.setFirstName(generateFirstName());
        existingEmail.setLastName(generateLastName());
        existingEmail.setPassword(CORRECT_PASSWORD);

        falsePasswordLogin = new UserLoginDto();
        falsePasswordLogin.setEmail(newUser.getEmail());
        falsePasswordLogin.setPassword(FALSE_PASSWORD);

        //UPDATE USER
        updateUser = new UserProfileDto();
        updateUser.setFirstName("Hermann");
        updateUser.setLastName("Stoss");
        updateUser.setEmail("hermann@email.com");

        applicationUser = new ApplicationUser();
        applicationUser.setFirstName("Diyar");
        applicationUser.setLastName("Turan");
        applicationUser.setEmail(generateEmail());
        applicationUser.setPassword("Password123");
        applicationUser.setId(123);
        applicationUser.setLocked(false);
        applicationUser.setAdmin(false);
        applicationUser.setPoints(0);
        applicationUser.setFailedLogin(0);
        this.repository.save(applicationUser);

        //USER
        updateUser = new UserProfileDto();
        updateUser.setFirstName("Hermann");
        updateUser.setLastName("Stoss");
        updateUser.setEmail("hermann@email.com");

        invalidUser = new UserProfileDto();
        invalidUser.setFirstName("Hermann123");
        invalidUser.setLastName("Stoss123");
        invalidUser.setEmail("dsasd");

        emptyUser = new UserProfileDto();

        invalidUser1 = new UserProfileDto();
        invalidUser1.setFirstName("Hermann");
        invalidUser1.setLastName("Stoss");
        invalidUser1.setEmail("dadasd");

        invalidUser2 = new UserProfileDto();
        invalidUser2.setFirstName("Hermann123");
        invalidUser2.setLastName("Stoss123");
        invalidUser2.setEmail("hermann@stoss.com");

        this.applicationUser = new ApplicationUser();
        this.applicationUser.setId(1);
        this.applicationUser.setFirstName("Diyar");
        this.applicationUser.setLastName("Turan");
        this.applicationUser.setEmail(generateEmail());
        this.applicationUser.setPassword("Password123");
        this.applicationUser.setLocked(false);
        this.applicationUser.setAdmin(false);
        this.applicationUser.setPoints(0);
        this.applicationUser.setFailedLogin(0);


        repository.save(this.applicationUser);

        //DELETE USER
        Order order = new Order();
        order.setId(1);
        order.setOrderTs(LocalDateTime.now());
        order.setCancelled(false);
        orderRepository.save(order);

        order.setUser(this.applicationUser);
        Set<Order> orders = new HashSet<>();
        orders.add(order);
        this.applicationUser.setOrders(orders);


        //PAYMENT DETAIL
        user2 = new ApplicationUser();
        user2.setId(2);
        user2.setFirstName("Theo");
        user2.setLastName("Kretz");
        user2.setEmail("kretz@mail.com");
        user2.setPassword(passwordEncoder.encode("Password123"));
        user2.setLocked(false);
        user2.setAdmin(false);
        user2.setPoints(0);
        user2.setFailedLogin(0);
        repository.save(user2);

        paymentDetail = new PaymentDetail();
        paymentDetail.setCardHolder("Hermann Stoss");
        paymentDetail.setCardNumber("1234567891234567");
        paymentDetail.setCvv(123);
        paymentDetail.setExpirationDate(LocalDate.of(2025, 12, 12));
        paymentDetailRepository.save(paymentDetail);

        paymentDetail.setUser(user2);
        Set<PaymentDetail> paymentDetailSet = new HashSet<>();
        paymentDetailSet.add(paymentDetail);
        user2.setPaymentDetails(paymentDetailSet);


        updatePaymentDetail = new SimplePaymentDetailDto();
        updatePaymentDetail.setId(paymentDetail.getId());
        updatePaymentDetail.setCardHolder("Theo Kretz");
        updatePaymentDetail.setCardNumber("1234123412341234");
        updatePaymentDetail.setCvv(333);
        updatePaymentDetail.setExpirationDate(LocalDate.of(2035, 12, 12));
        updatePaymentDetail.setUserId(user2.getId());

        invalidPaymentDetail = new SimplePaymentDetailDto();
        invalidPaymentDetail.setId(paymentDetail.getId());
        invalidPaymentDetail.setCardHolder("");
        invalidPaymentDetail.setCardNumber("123");
        invalidPaymentDetail.setCvv(33);
        invalidPaymentDetail.setExpirationDate(LocalDate.of(2015, 12, 12));
        invalidPaymentDetail.setUserId(user2.getId());

        invalidCardHolderPaymentDetail = new SimplePaymentDetailDto();
        invalidCardHolderPaymentDetail.setId(paymentDetail.getId());
        invalidCardHolderPaymentDetail.setCardHolder("");
        invalidCardHolderPaymentDetail.setCardNumber("1234123412341234");
        invalidCardHolderPaymentDetail.setCvv(333);
        invalidCardHolderPaymentDetail.setExpirationDate(LocalDate.of(2035, 12, 12));
        invalidCardHolderPaymentDetail.setUserId(user2.getId());

        invalidCardNumberPaymentDetail = new SimplePaymentDetailDto();
        invalidCardNumberPaymentDetail.setId(paymentDetail.getId());
        invalidCardNumberPaymentDetail.setCardHolder("Theo Kretz");
        invalidCardNumberPaymentDetail.setCardNumber("123");
        invalidCardNumberPaymentDetail.setCvv(333);
        invalidCardNumberPaymentDetail.setExpirationDate(LocalDate.of(2035, 12, 12));
        invalidCardNumberPaymentDetail.setUserId(user2.getId());

        invalidCvvPaymentDetail = new SimplePaymentDetailDto();
        invalidCvvPaymentDetail.setId(paymentDetail.getId());
        invalidCvvPaymentDetail.setCardHolder("Theo Kretz");
        invalidCvvPaymentDetail.setCardNumber("1234123412341234");
        invalidCvvPaymentDetail.setCvv(33);
        invalidCvvPaymentDetail.setExpirationDate(LocalDate.of(2035, 12, 12));
        invalidCvvPaymentDetail.setUserId(user2.getId());

        invalidExpirationDatePaymentDetail = new SimplePaymentDetailDto();
        invalidExpirationDatePaymentDetail.setId(paymentDetail.getId());
        invalidExpirationDatePaymentDetail.setCardHolder("Theo Kretz");
        invalidExpirationDatePaymentDetail.setCardNumber("1234123412341234");
        invalidExpirationDatePaymentDetail.setCvv(333);
        invalidExpirationDatePaymentDetail.setExpirationDate(LocalDate.of(2015, 12, 12));
        invalidExpirationDatePaymentDetail.setUserId(user2.getId());


        //LOCATION
        location = new Location();
        location.setCity("Vienna");
        location.setCountry("AT");
        location.setStreet("Wiedner Hauptstrasse 1");
        location.setPostalCode(1040);
        locationRepository.save(location);

        location.setUser(user2);
        Set<Location> locationSet = new HashSet<>();
        locationSet.add(location);
        user2.setLocations(locationSet);


        updateLocation = new CheckoutLocation();
        updateLocation.setLocationId(location.getId());
        updateLocation.setCity("Munich");
        updateLocation.setCountry("DE");
        updateLocation.setStreet("Deutsche Strasse 4");
        updateLocation.setPostalCode(12345);

        invalidLocation = new CheckoutLocation();
        invalidLocation.setLocationId(location.getId());
        invalidLocation.setCity("");
        invalidLocation.setCountry("GER");
        invalidLocation.setStreet("");
        invalidLocation.setPostalCode(1234545);

        invalidCityLocation = new CheckoutLocation();
        invalidCityLocation.setLocationId(location.getId());
        invalidCityLocation.setCity("");
        invalidCityLocation.setCountry("AT");
        invalidCityLocation.setStreet("Strasse 4");
        invalidCityLocation.setPostalCode(12345);

        invalidCountryLocation = new CheckoutLocation();
        invalidCountryLocation.setLocationId(location.getId());
        invalidCountryLocation.setCity("Munich");
        invalidCountryLocation.setCountry("GER");
        invalidCountryLocation.setStreet("Strasse 4");
        invalidCountryLocation.setPostalCode(12345);

        invalidStreetLocation = new CheckoutLocation();
        invalidStreetLocation.setLocationId(location.getId());
        invalidStreetLocation.setCity("Vienna");
        invalidStreetLocation.setCountry("AT");
        invalidStreetLocation.setStreet("");
        invalidStreetLocation.setPostalCode(12345);

        invalidPostalCodeLocation = new CheckoutLocation();
        invalidPostalCodeLocation.setLocationId(location.getId());
        invalidPostalCodeLocation.setCity("Vienna");
        invalidPostalCodeLocation.setCountry("AT");
        invalidPostalCodeLocation.setStreet("Strasse 4");
        invalidPostalCodeLocation.setPostalCode(1234545);


    }

}
