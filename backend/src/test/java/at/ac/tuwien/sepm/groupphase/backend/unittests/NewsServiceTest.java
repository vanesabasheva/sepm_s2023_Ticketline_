package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.News;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.NewsRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.NewsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class NewsServiceTest {
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private at.ac.tuwien.sepm.groupphase.backend.repository.NotUserRepository notUserRepository;
    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private NewsService newsService;

    private ApplicationUser user;
    private ApplicationUser user2;
    private Event event;
    private Event event2;
    private News news1;
    private News news2;

    @BeforeEach
    public void beforeAll() {
        event = new Event();
        event.setName("Event 1");
        event.setLength(Duration.ofHours(1));
        eventRepository.save(event);

        event2 = new Event();
        event2.setName("Event 2");
        event2.setLength(Duration.ofHours(1));
        eventRepository.save(event2);

        news1 = News.NewsBuilder.aNews()
            .withId(1)
            .withTitle("News1")
            .withSummary("Summary1")
            .withContent("Content1")
            .withEvent(event)
            .withPublicationDate(LocalDate.now())
            .build();

        news2 = News.NewsBuilder.aNews()
            .withId(2)
            .withTitle("News2")
            .withSummary("Summary2")
            .withContent("Content2")
            .withEvent(event2)
            .withPublicationDate(LocalDate.now())
            .build();

        this.user = new ApplicationUser();
        this.user.setId(1);
        this.user.setEmail("hallo@12334");
        this.user.setAdmin(false);
        this.user.setFirstName("Vanesa");
        this.user.setLastName("Besheva");
        this.user.setPassword("Password");
        this.user.setLocked(false);
        this.user.setPoints(10000);

        this.user2 = new ApplicationUser();
        this.user.setId(2);
        this.user2.setEmail("tschau@12334");
        this.user2.setAdmin(false);
        this.user2.setFirstName("Vanesa");
        this.user2.setLastName("Besheva");
        this.user2.setPassword("Password");
        this.user2.setLocked(false);
        this.user2.setPoints(10000);

        notUserRepository.save(user);
        notUserRepository.save(user2);

        newsRepository.save(news1);
        newsRepository.save(news2);
    }

    @Test
    void getAllNewsExpectOne() {
        newsRepository.markNewsAsReadByUser(news1.getId(), user.getId());
        List<News> list = newsService.findAll(user.getId(), false);
        assertEquals(1, list.size());
    }

    @Test
    void getAllNews() {
        newsRepository.markNewsAsReadByUser(news1.getId(), user.getId());
        List<News> list = newsService.findAll(user.getId(), true);
        assertEquals(2, list.size());
    }

    @Test
    void getAllNewsExpectTwo() {
        List<News> list = newsService.findAll(user2.getId(), false);
        assertEquals(2, list.size());
    }

    @Test
    void getAllReadNewsExpectOne() {
        List<News> list = newsService.findAll(user2.getId(), false);
        assertEquals(2, list.size());
        newsService.findOne(news1.getId(), user2.getId());
        list = newsService.findAll(2, false);
        assertEquals(1, list.size());
    }
}
