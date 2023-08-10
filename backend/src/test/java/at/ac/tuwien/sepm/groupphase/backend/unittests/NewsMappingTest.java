package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.news.DetailedNewsDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.news.SimpleNewsDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.NewsMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.News;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class NewsMappingTest implements TestData {

    private final News news = News.NewsBuilder.aNews()
        .withId(INT_ID)
        .withTitle(TEST_NEWS_TITLE)
        .withSummary(TEST_NEWS_SUMMARY)
        .withContent(TEST_NEWS_TEXT)
        .withPublicationDate(TEST_NEWS_PUSBLISHED_AT_LOCALDATE)
        .withImagePath(TEST_NEWS_IMAGE_PATH)
        .build();
    @Autowired
    private NewsMapper newsMapper;

    @Test
    public void givenNothing_whenMapDetailedNewsDtoToEntity_thenEntityHasAllProperties() {
        DetailedNewsDto detailedNewsDto = newsMapper.newsToDetailedNewsDto(news);
        assertAll(
            () -> assertEquals(INT_ID, detailedNewsDto.getId()),
            () -> assertEquals(TEST_NEWS_TITLE, detailedNewsDto.getTitle()),
            () -> assertEquals(TEST_NEWS_SUMMARY, detailedNewsDto.getSummary()),
            () -> assertEquals(TEST_NEWS_TEXT, detailedNewsDto.getContent()),
            () -> assertEquals(TEST_NEWS_PUSBLISHED_AT_LOCALDATE, detailedNewsDto.getPublicationDate()),
            () -> assertEquals(TEST_NEWS_IMAGE_PATH, detailedNewsDto.getImagePath())
        );
    }

    @Test
    public void givenNothing_whenMapListWithTwoMessageEntitiesToSimpleDto_thenGetListWithSizeTwoAndAllProperties() {
        List<News> newsArticles = new ArrayList<>();
        newsArticles.add(news);
        newsArticles.add(news);

        List<SimpleNewsDto> simpleNewsDtos = newsMapper.newsToSimpleNewsDto(newsArticles);
        assertEquals(2, simpleNewsDtos.size());
        SimpleNewsDto simpleMessageDto = simpleNewsDtos.get(0);
        assertAll(
            () -> assertEquals(INT_ID, simpleMessageDto.getId()),
            () -> assertEquals(TEST_NEWS_TITLE, simpleMessageDto.getTitle()),
            () -> assertEquals(TEST_NEWS_SUMMARY, simpleMessageDto.getSummary()),
            () -> assertEquals(TEST_NEWS_PUSBLISHED_AT_LOCALDATE, simpleMessageDto.getPublicationDate())
        );
    }


}
