package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.news.DetailedNewsDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.news.SimpleNewsDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.NewsMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.News;
import at.ac.tuwien.sepm.groupphase.backend.exception.FatalException;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.service.NewsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.lang.invoke.MethodHandles;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/news")
public class NewsEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final NewsService newsService;
    private final NewsMapper newsMapper;

    public NewsEndpoint(NewsService newsService, NewsMapper newsMapper) {
        this.newsService = newsService;
        this.newsMapper = newsMapper;
    }


    @Secured("ROLE_USER")
    @GetMapping
    @Operation(summary = "Get list of news articles without details", security = @SecurityRequirement(name = "apiKey"))
    public List<SimpleNewsDto> findAll(Authentication authentication, @Valid boolean includingRead) {
        LOGGER.info("GET /api/v1/news?includingRead={}", includingRead);

        return newsMapper.newsToSimpleNewsDto(newsService.findAll((Integer) authentication.getPrincipal(), includingRead));

    }


    @Secured("ROLE_USER")
    @GetMapping(value = "/{id}")
    @Operation(summary = "Get detailed information about a specific message", security = @SecurityRequirement(name = "apiKey"))
    public DetailedNewsDto getDetailedNews(@Valid @PathVariable Integer id, Authentication authentication) {
        LOGGER.info("GET /api/v1/news/{}", id);
        try {
            News news = newsService.findOne(id, (Integer) authentication.getPrincipal());
            return newsMapper.newsToDetailedNewsDto(news);
        } catch (NotFoundException e) {
            LOGGER.warn("Unable to find news" + e.getMessage());
            HttpStatus status = HttpStatus.NOT_FOUND;
            throw new ResponseStatusException(status, e.getMessage(), e);
        }
    }


    @Secured("ROLE_ADMIN")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @Operation(summary = "Publish new news article", security = @SecurityRequirement(name = "apiKey"))
    public DetailedNewsDto create(@Valid @ModelAttribute DetailedNewsDto newsDto, @RequestParam(value = "image", required = false) MultipartFile image) {
        LOGGER.info("POST /api/v1/news body: {}", newsDto);

        try {
            return newsMapper.newsToDetailedNewsDto(
                newsService.publishNews(newsMapper.detailedNewsDtoToNews(newsDto), newsDto.getEventId(), image));
        } catch (NotFoundException e) {
            LOGGER.warn("Unable to find event" + e.getMessage());
            HttpStatus status = HttpStatus.NOT_FOUND;
            throw new ResponseStatusException(status, e.getMessage(), e);
        } catch (FatalException e) {
            LOGGER.warn("Unable to create news" + e.getMessage());
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
            throw new ResponseStatusException(status, e.getMessage(), e);
        } catch (ValidationException e) {
            LOGGER.warn("Unable to create news" + e.getMessage());
            HttpStatus status = HttpStatus.BAD_REQUEST;
            throw new ResponseStatusException(status, e.getMessage(), e);
        }

    }
}

