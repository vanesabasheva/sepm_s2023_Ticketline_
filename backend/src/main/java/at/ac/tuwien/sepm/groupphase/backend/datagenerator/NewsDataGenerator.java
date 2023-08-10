package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.News;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.NewsRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Profile("generateData")
@Component
@DependsOn({"eventDataGenerator"})
public class NewsDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final String TEST_NEWS_TITLE = " News about ";
    private static final String TEST_NEWS_SUMMARY =
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, "
            + "quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.";
    private static final int NUMBER_OF_NEWS_TO_GENERATE_FOR_EACH_EVENT = 3;
    private static final String TEST_NEWS_TEXT =
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim "
            + "veniam, "
            + "quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate "
            + "velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia "
            + "deserunt mollit anim id est laborum.";

    private final NewsRepository newsRepository;
    private final EventRepository eventRepository;

    public NewsDataGenerator(NewsRepository newsRepository, EventRepository eventRepository) {
        this.newsRepository = newsRepository;
        this.eventRepository = eventRepository;
    }

    @PostConstruct
    private void generateNews() {
        if (newsRepository.findAll().size() > 0) {
            LOGGER.debug("news already generated");
        } else {
            LOGGER.debug("news {} news entries", NUMBER_OF_NEWS_TO_GENERATE_FOR_EACH_EVENT);
            List<News> newsList = new ArrayList<>();
            String[] imgPaths = {"cat.png", "cat_black.png", "turtle.png", "duck.jpg"};
            eventRepository.findAll().forEach(event -> {
                for (int i = 1; i <= NUMBER_OF_NEWS_TO_GENERATE_FOR_EACH_EVENT; i++) {
                    News news = News.NewsBuilder.aNews()
                        .withSummary(TEST_NEWS_SUMMARY + event.getName() + "_" + i)
                        .withContent(TEST_NEWS_TEXT + event.getName() + "_" + i)
                        .withPublicationDate(LocalDate.of(2023, 4, i))
                        .withTitle(event.getName() + " is starting! " + i)
                        .withEvent(event)
                        .withImagePath(imgPaths[(int) (Math.random() * 4)])
                        .build();
                    newsList.add(news);
                }
            });
            LOGGER.debug("saving news {}", newsList);
            newsRepository.saveAll(newsList);
        }
    }

}
