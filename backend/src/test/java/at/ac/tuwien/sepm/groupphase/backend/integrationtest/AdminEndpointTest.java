package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.user.UserAdminDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.user.UserRegisterDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.repository.NotUserRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.JwtTokenizer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AdminEndpointTest {

    private final String ADMIN_URL = "/api/v1";
    private final List<String> ADMIN_ROLES = new ArrayList<>() {
        {
            add("ROLE_ADMIN");
            add("ROLE_USER");
        }
    };

    private final List<String> USER_ROLES = new ArrayList<>() {
        {
            add("ROLE_USER");
        }
    };

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private NotUserRepository notUserRepository;

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenizer jwtTokenizer;

    @Autowired
    private ObjectMapper objectMapper;


    private ApplicationUser unlockedUser;
    private ApplicationUser lockedUser;
    private ApplicationUser admin;
    private ApplicationUser admin2;
    private ApplicationUser testUser1;
    private ApplicationUser testUser2;
    private ApplicationUser testUser3;

    @BeforeEach
    public void beforeEach() {
        notUserRepository.deleteAll();
        unlockedUser = ApplicationUser.UserBuilder.aUser()
            .withId(1)
            .withAdmin(false)
            .withFirstName("Average")
            .withLastName("User")
            .withEmail("averageuser@email.com")
            .withPassword(passwordEncoder.encode("averagepassword"))
            .withPoints(0)
            .withFailedLogin(0)
            .withLocked(false)
            .build();

        lockedUser = ApplicationUser.UserBuilder.aUser()
            .withId(2)
            .withAdmin(false)
            .withFirstName("Standard")
            .withLastName("User")
            .withEmail("standarduser@email.com")
            .withPassword(passwordEncoder.encode("standardpassword"))
            .withPoints(0)
            .withFailedLogin(0)
            .withLocked(true)
            .build();

        admin = ApplicationUser.UserBuilder.aUser()
            .withId(3)
            .withAdmin(true)
            .withFirstName("Chad")
            .withLastName("Admin")
            .withEmail("chadadmin@email.com")
            .withPassword(passwordEncoder.encode("chadpassword"))
            .withPoints(0)
            .withFailedLogin(0)
            .withLocked(false)
            .build();

        admin2 = ApplicationUser.UserBuilder.aUser()
            .withId(4)
            .withAdmin(true)
            .withFirstName("Chad")
            .withLastName("Admin")
            .withEmail("chadadmin2@email.com")
            .withPassword(passwordEncoder.encode("chadpassword"))
            .withPoints(0)
            .withFailedLogin(0)
            .withLocked(false)
            .build();

        testUser1 = ApplicationUser.UserBuilder.aUser()
            .withId(10)
            .withAdmin(false)
            .withFirstName("Conor")
            .withLastName("Mason")
            .withEmail("conormason@nbt.uk")
            .withPassword(passwordEncoder.encode("amsterdam"))
            .withPoints(0)
            .withFailedLogin(0)
            .withLocked(false)
            .build();

        testUser2 = ApplicationUser.UserBuilder.aUser()
            .withId(11)
            .withAdmin(false)
            .withFirstName("Wolf")
            .withLastName("Alice")
            .withEmail("wolfalice@heartstopper.com")
            .withPassword(passwordEncoder.encode("dontdeletethekisses"))
            .withPoints(0)
            .withFailedLogin(0)
            .withLocked(false)
            .build();

        testUser3 = ApplicationUser.UserBuilder.aUser()
            .withId(12)
            .withAdmin(false)
            .withFirstName("Arctic")
            .withLastName("Monkeys")
            .withEmail("arcticmonkeys@am.uk")
            .withPassword(passwordEncoder.encode("doiwannaknow"))
            .withPoints(0)
            .withFailedLogin(0)
            .withLocked(false)
            .build();

        notUserRepository.save(unlockedUser);
        notUserRepository.save(lockedUser);
        notUserRepository.save(admin);
        notUserRepository.save(admin2);
        notUserRepository.save(testUser1);
        notUserRepository.save(testUser2);
        notUserRepository.save(testUser3);
    }

    @Test
    public void adminRequestsAllLockedUsersShouldReturnAllLockedUsersTotalCountOne() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get(ADMIN_URL + "/userlist?locked=true")
            .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken("chadadmin@email.com", ADMIN_ROLES)))
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        List<UserAdminDto> result = Arrays.asList(objectMapper.readValue(response.getContentAsString(), UserAdminDto[].class));
        assertEquals(1, result.size());
        assertTrue(result.get(0).getLocked());
        assertEquals(2, result.get(0).getId());
        assertAll(
            () -> assertEquals("Standard", result.get(0).getFirstName()),
            () -> assertEquals("User", result.get(0).getLastName()),
            () -> assertEquals("standarduser@email.com", result.get(0).getEmail())
        );
    }

    @Test
    public void adminRequestsAllUsersShouldReturnAllUsersCountSeven() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get(ADMIN_URL + "/userlist?locked=false")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken("chadadmin@email.com", ADMIN_ROLES)))
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        List<UserAdminDto> result = Arrays.asList(objectMapper.readValue(response.getContentAsString(), UserAdminDto[].class));
        assertEquals(7, result.size());
        for (UserAdminDto user : result) {
            if (user.getId() == 1) {
                assertAll(
                    () -> assertEquals("Average", user.getFirstName()),
                    () -> assertEquals("User", user.getLastName()),
                    () -> assertEquals("averageuser@email.com", user.getEmail()),
                    () -> assertFalse(user.getAdmin()),
                    () -> assertFalse(user.getLocked())
                );
            } else if (user.getId() == 10) {
                assertAll(
                    () -> assertEquals("Conor", user.getFirstName()),
                    () -> assertEquals("Mason", user.getLastName()),
                    () -> assertEquals("conormason@nbt.uk", user.getEmail()),
                    () -> assertFalse(user.getAdmin()),
                    () -> assertFalse(user.getLocked())
                );
            }
        }
    }

    @Test
    public void userRequestsAllUsersShouldReturn403Forbidden() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get(ADMIN_URL + "/userlist?locked=false")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken("averageuser@email.com", USER_ROLES)))
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
    }

    @Test
    public void adminRequestsAllUsersWithInvalidRequestShouldReturnError() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get(ADMIN_URL + "/userlist?locked=null")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(admin.getEmail(), ADMIN_ROLES)))
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    public void adminRequestsLockingOfAnotherAdminShouldReturnValidationError() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(post(ADMIN_URL + "/users/4/lock")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken("chadadmin@email.com", ADMIN_ROLES)))
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.getStatus());

        String errorMessage = response.getContentAsString();
        assertTrue(errorMessage.contains("Cannot change status of an admin"));
    }

    @Test
    public void adminRequestsLockingOfUserShouldSucceed() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(post(ADMIN_URL + "/users/1/lock")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(admin.getEmail(), ADMIN_ROLES)))
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());

        ApplicationUser user = notUserRepository.findById(1).orElseThrow();
        assertTrue(user.getLocked());
    }

    @Test
    public void adminRequestsUnlockingOfUserShouldSucceed() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(post(ADMIN_URL + "/users/2/open")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(admin.getEmail(), ADMIN_ROLES)))
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());

        ApplicationUser user = notUserRepository.findById(2).orElseThrow();
        assertFalse(user.getLocked());
    }

    @Test
    public void adminRequestUnlockingOfNonexistentUserShouldReturnError() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(post(ADMIN_URL + "/users/100/open")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(admin.getEmail(), ADMIN_ROLES)))
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    public void adminRequestUnlockingOfAlreadyUnlockedUserShouldReturnError() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(post(ADMIN_URL + "/users/1/open")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(admin.getEmail(), ADMIN_ROLES)))
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains("User is already in the desired state"));
    }

    @Test
    public void adminRequestsCreationOfNewAdminUserShouldSucceed() throws Exception {
        UserRegisterDto requestBody = UserRegisterDto.UserRegisterDtoBuilder.anUserRegisterDto()
            .withEmail("joebiden@gov.us")
            .withFirstName("Joe")
            .withLastName("Biden")
            .withPassword("iLoveIceCream0")
            .withIsAdmin(true)
            .build();
        MvcResult mvcResult = this.mockMvc.perform(post(ADMIN_URL + "/admins")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(admin.getEmail(), ADMIN_ROLES))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains("success"));

        ApplicationUser user = notUserRepository.findApplicationUsersByEmail("joebiden@gov.us").orElseThrow();
        assertAll(
            () -> assertEquals("Joe", user.getFirstName()),
            () -> assertEquals("Biden", user.getLastName()),
            () -> assertTrue(user.getAdmin())
        );
    }

    @Test
    public void userRequestsCreationOfNewAdminUserShouldReturn403Forbidden() throws Exception {
        UserRegisterDto requestBody = UserRegisterDto.UserRegisterDtoBuilder.anUserRegisterDto()
            .withEmail("volodymyr.zelenskyy@gov.ua")
            .withFirstName("Volodymyr")
            .withLastName("Zelenskyy")
            .withPassword("SlavaUkarini0")
            .withIsAdmin(true)
            .build();
        MvcResult mvcResult = this.mockMvc.perform(post(ADMIN_URL + "/admins")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(unlockedUser.getEmail(), USER_ROLES))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
    }

    @Test
    public void adminRequestsCreationOfNewUserShouldSucceed() throws Exception {
        UserRegisterDto requestBody = UserRegisterDto.UserRegisterDtoBuilder.anUserRegisterDto()
            .withEmail("olaf.scholz@bundeskanzler.de")
            .withFirstName("Olaf")
            .withLastName("Scholz")
            .withPassword("SPD4ever")
            .withIsAdmin(false)
            .build();
        MvcResult mvcResult = this.mockMvc.perform(post(ADMIN_URL + "/admins")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(admin.getEmail(), ADMIN_ROLES))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody)))
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains("success"));

        ApplicationUser user = notUserRepository.findApplicationUsersByEmail("olaf.scholz@bundeskanzler.de").orElseThrow();
        assertAll(
            () -> assertEquals("Olaf", user.getFirstName()),
            () -> assertEquals("Scholz", user.getLastName()),
            () -> assertFalse(user.getAdmin())
        );
    }
}
