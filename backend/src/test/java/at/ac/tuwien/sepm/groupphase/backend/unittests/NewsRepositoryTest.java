package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.News;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.NewsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
// This test slice annotation is used instead of @SpringBootTest to load only repository beans instead of
// the entire application context
@DataJpaTest
@ActiveProfiles("test")
public class NewsRepositoryTest implements TestData {

    @Autowired
    private NewsRepository newsRepository;
    @Autowired
    private EventRepository eventRepository;

    @Test
    public void givenNothing_whenSaveNews_thenFindListWithOneElementAndFindNewsById() {
        Event event = Event.EventBuilder.aEvent()
            .withId(1)
            .withDescription("Test description")
            .withLength(Duration.ofHours(1))
            .withName("Test name")
            .withType("Test type")
            .build();

        eventRepository.save(event);

        News news = News.NewsBuilder.aNews()
            .withTitle(TEST_NEWS_TITLE)
            .withSummary(TEST_NEWS_SUMMARY)
            .withContent(TEST_NEWS_TEXT)
            .withPublicationDate(TEST_NEWS_PUSBLISHED_AT_LOCALDATE)
            .withEvent(event)
            .build();

        newsRepository.save(news);

        assertAll(
            () -> assertEquals(1, newsRepository.findAll().size()),
            () -> assertNotNull(newsRepository.findById(news.getId()))
        );
    }

}
