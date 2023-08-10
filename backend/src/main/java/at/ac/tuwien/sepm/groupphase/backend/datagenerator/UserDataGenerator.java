package at.ac.tuwien.sepm.groupphase.backend.datagenerator;


import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import at.ac.tuwien.sepm.groupphase.backend.repository.LocationRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.NotUserRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


@Profile("generateData")
@Component
@DependsOn({"locationDataGenerator"})
public class UserDataGenerator {


    public static final int NUMBER_OF_USERS_TO_GENERATE = 20;
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final String TEST_FIRST_NAME = "First Name";
    private static final String TEST_LAST_NAME = "Last Name";
    private static final String TEST_E_MAIL = "test@mail.com";
    private static final String TEST_PASSWORD = "password";
    private static final Integer TEST_POINTS = 50;
    private static final int TEST_LOCATION_ID = 5;

    private final PasswordEncoder passwordEncoder;

    private final NotUserRepository notUserRepository;
    private final LocationRepository locationRepository;


    public UserDataGenerator(NotUserRepository notUserRepository, LocationRepository locationRepository, PasswordEncoder passwordEncoder) {
        this.notUserRepository = notUserRepository;
        this.locationRepository = locationRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    private void generateUsers() {
        if (notUserRepository.findAll().size() > 0) {
            LOGGER.debug("users already generated");
        } else {
            LOGGER.debug("generating {} user entries", NUMBER_OF_USERS_TO_GENERATE);
            for (int i = 1; i < NUMBER_OF_USERS_TO_GENERATE; i++) {

                Optional<Location> location = locationRepository.findById(TEST_LOCATION_ID + i);
                if (location.isEmpty()) {
                    LOGGER.debug("location {} not found", TEST_LOCATION_ID + i);
                } else {
                    Set<Location> locationSet = new HashSet<>();
                    locationSet.add(location.get());
                    ApplicationUser user = ApplicationUser.UserBuilder.aUser()
                        .withAdmin(false)
                        .withFirstName(TEST_FIRST_NAME + " " + i)
                        .withLastName(TEST_LAST_NAME + " " + i)
                        .withEmail(i + TEST_E_MAIL)
                        .withPassword(passwordEncoder.encode(TEST_PASSWORD + i))
                        .withPoints(TEST_POINTS + i)
                        .withLocked(false)
                        .withFailedLogin(0)
                        .withLocations(locationSet)
                        .build();

                    LOGGER.debug("saving user {}", user);
                    notUserRepository.save(user);
                    if (locationSet.iterator().hasNext()) {
                        locationSet.iterator().next().setUser(user);
                        locationRepository.save(locationSet.iterator().next());
                    }
                }
            }

            ApplicationUser admin = ApplicationUser.UserBuilder.aUser()
                .withId(-1)
                .withAdmin(true)
                .withFirstName("Admin")
                .withLastName("Admin")
                .withEmail("admin@email.com")
                .withPassword(passwordEncoder.encode("password"))
                .withPoints(0)
                .withLocked(false)
                .withFailedLogin(0)
                .build();
            notUserRepository.save(admin);

        }

    }

}
