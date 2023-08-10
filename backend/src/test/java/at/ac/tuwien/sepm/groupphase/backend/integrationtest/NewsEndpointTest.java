package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.news.DetailedNewsDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.news.SimpleNewsDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.NewsMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.News;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.NewsRepository;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;


import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class NewsEndpointTest implements TestData {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private NewsMapper newsMapper;

    @Autowired
    private JwtTokenizer jwtTokenizer;

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private NotUserRepository notUserRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private ApplicationUser admin;
    private News news;
    private Event event;

    @BeforeEach
    public void beforeEach() {
        newsRepository.deleteAll();
        eventRepository.deleteAll();
        notUserRepository.deleteAll();

        admin = ApplicationUser.UserBuilder.aUser()
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

        event = Event.EventBuilder.aEvent()
            .withId(1)
            .withName("Test Event")
            .withDescription("Test Description")
            .withLength(Duration.ofHours(1))
            .withType("Test Type")
            .build();

        news = News.NewsBuilder.aNews()
            .withTitle(TEST_NEWS_TITLE)
            .withSummary(TEST_NEWS_SUMMARY)
            .withContent(TEST_NEWS_TEXT)
            .withPublicationDate(TEST_NEWS_PUSBLISHED_AT_LOCALDATE)
            .withEvent(event)
            .build();

        notUserRepository.save(admin);
        eventRepository.save(event);
        newsRepository.save(news);
    }

    @Test
    public void givenNothing_whenFindAll_thenEmptyList() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get(MESSAGE_BASE_URI)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        List<SimpleNewsDto> simpleMessageDtos = Arrays.asList(objectMapper.readValue(response.getContentAsString(),
            SimpleNewsDto[].class));

        assertEquals(0, simpleMessageDtos.size());
    }


    @Test
    public void givenOneMessage_whenFindAll_thenListWithSizeOneAndMessageWithAllPropertiesExceptSummary()
        throws Exception {
        newsRepository.save(news);
        MvcResult mvcResult = this.mockMvc.perform(get(NEWS_BASE_URI)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        List<SimpleNewsDto> simpleNewsDtos = Arrays.asList(objectMapper.readValue(response.getContentAsString(),
            SimpleNewsDto[].class));

        assertEquals(1, simpleNewsDtos.size());
        SimpleNewsDto simpleNewsDto = simpleNewsDtos.get(0);
        assertAll(
            () -> assertEquals(news.getId(), simpleNewsDto.getId()),
            () -> assertEquals(TEST_NEWS_TITLE, simpleNewsDto.getTitle()),
            () -> assertEquals(TEST_NEWS_SUMMARY, simpleNewsDto.getSummary()),
            () -> assertEquals(TEST_NEWS_PUSBLISHED_AT_LOCALDATE, simpleNewsDto.getPublicationDate())
        );
    }

    @Test
    public void givenOneNews_whenFindById_thenNewsWithAllProperties() throws Exception {

        MvcResult mvcResult = this.mockMvc.perform(get(NEWS_BASE_URI + "/{id}", news.getId())
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertAll(
            () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
            () -> assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType())
        );

        DetailedNewsDto detailedNewsDto = objectMapper.readValue(response.getContentAsString(),
            DetailedNewsDto.class);

        News newsMapped = newsMapper.detailedNewsDtoToNews(detailedNewsDto);
        assertAll(
            () -> assertEquals(news.getId(), newsMapped.getId()),
            () -> assertEquals(news.getSummary(), newsMapped.getSummary()),
            () -> assertEquals(news.getContent(), newsMapped.getContent()),
            () -> assertEquals(news.getPublicationDate(), newsMapped.getPublicationDate()),
            () -> assertEquals(news.getEvent().getId(), newsMapped.getEvent().getId())
        );
    }

    @Test
    public void givenOneNews_whenFindByNonExistingId_then404() throws Exception {

        MvcResult mvcResult = this.mockMvc.perform(get(NEWS_BASE_URI + "/{id}", -1)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    public void givenNothing_whenPost_thenNewsWithAllSetPropertiesPlusIdAndPublishedDate() throws Exception {
        news.setPublicationDate(null);
        //DetailedNewsDto detailedNewsDto = newsMapper.newsToDetailedNewsDto(news);
        byte[] imageBytes = new byte[55];
        imageBytes[0] = 1;
        imageBytes[1] = 2;
        //MockMultipartFile mockFile = new MockMultipartFile("image", "filename.jpg", "image/jpeg", imageBytes);
        MockMultipartHttpServletRequestBuilder requestBuilder = (MockMultipartHttpServletRequestBuilder) MockMvcRequestBuilders.multipart(NEWS_BASE_URI)
            .param("title", news.getTitle())
            .param("summary", news.getSummary())
            .param("content", news.getContent())
            .param("eventId", String.valueOf(news.getEvent().getId()))
            .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES));

        MvcResult mvcResult = this.mockMvc.perform(requestBuilder)
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        DetailedNewsDto newsResponse = objectMapper.readValue(response.getContentAsString(),
            DetailedNewsDto.class);

        assertNotNull(newsResponse.getId());
        assertNotNull(newsResponse.getPublicationDate());
        //assertTrue(isNow(newsResponse.getPublicationDate()));
        assertEquals(LocalDate.now(), newsResponse.getPublicationDate());
        //Set generated properties to null to make the response comparable with the original input
        News newsMapped = newsMapper.detailedNewsDtoToNews(newsResponse);
        assertAll(
            () -> assertEquals(2, newsMapped.getId()),
            () -> assertEquals(news.getSummary(), newsMapped.getSummary()),
            () -> assertEquals(news.getContent(), newsMapped.getContent()),
            () -> assertEquals(LocalDate.now(), newsMapped.getPublicationDate()),
            () -> assertEquals(news.getEvent().getId(), newsMapped.getEvent().getId())
        );
    }

    @Test
    public void givenNothing_whenPostInvalid_then400() throws Exception {
        news.setTitle(null);
        news.setSummary(null);
        news.setContent(null);
        DetailedNewsDto detailedNewsDto = newsMapper.newsToDetailedNewsDto(news);
        String body = objectMapper.writeValueAsString(detailedNewsDto);
        MvcResult mvcResult = this.mockMvc.perform(post(NEWS_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertAll(
            () -> assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus())
        );
    }
}
