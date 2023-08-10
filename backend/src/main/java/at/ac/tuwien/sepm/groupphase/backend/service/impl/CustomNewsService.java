package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.News;
import at.ac.tuwien.sepm.groupphase.backend.exception.FatalException;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.NewsRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.NewsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CustomNewsService implements NewsService {


    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final NewsRepository newsRepository;
    private final EventRepository eventRepository;
    private final ResourceLoader resourceLoader;

    public CustomNewsService(NewsRepository newsRepository, EventRepository eventRepository, ResourceLoader resourceLoader) {
        this.newsRepository = newsRepository;
        this.eventRepository = eventRepository;
        this.resourceLoader = resourceLoader;
    }


    @Override
    public List<News> findAll(Integer userId, boolean includingRead) {
        LOGGER.debug("Find all news articles");
        if (includingRead) {
            return newsRepository.findNewsOrderByPublicationDateDesc();
        }
        return newsRepository.findNewsNotReadByUserOrderByPublicationDateDesc(userId);
    }

    @Override
    public News findOne(Integer id, Integer userId) {
        LOGGER.debug("Find news with id {}", id);
        Optional<News> message = newsRepository.findById(id);
        if (message.isPresent()) {
            newsRepository.markNewsAsReadByUser(id, userId);
            return message.get();
        } else {
            throw new NotFoundException(String.format("Could not find news article with id %s", id));
        }
    }

    private boolean isImage(MultipartFile file) {
        String[] allowedContentTypes = {"image/jpeg", "image/png", "image/jpg", "image/gif"};
        // Check if the file's content type is one of the allowed image types
        for (String contentType : allowedContentTypes) {
            if (contentType.equals(file.getContentType())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public News publishNews(News news, Integer eventId, MultipartFile image) throws ValidationException {
        LOGGER.debug("Publish new news article {}", news);
        news.setPublicationDate(LocalDate.now());
        news.setId(null);
        String filePath = null;
        news.setImagePath(null);

        if (image != null && !image.isEmpty()) {
            if (!isImage(image)) {
                LOGGER.warn("Unable to save image, wrong file type");
                throw new ValidationException("Validation error", List.of("Unable to save image, wrong file type"));
            }
            try {
                // get file extension of image
                String originalFilename = image.getOriginalFilename();
                String fileExtension = null;
                if (originalFilename != null && !originalFilename.isEmpty()) {
                    int dotIndex = originalFilename.lastIndexOf('.');
                    if (dotIndex > 0 && dotIndex < originalFilename.length() - 1) {
                        fileExtension = originalFilename.substring(dotIndex + 1);
                    }
                }
                // create image, overwrite newsDto.imagePath
                String uuid = UUID.randomUUID().toString();
                Resource resource = resourceLoader.getResource("classpath:");
                // get absolute path of /backend
                Path path = Paths.get(resource.getFile().getAbsolutePath()).getParent().getParent();
                String imgPath = "" + uuid + "." + fileExtension;
                filePath = path + "/src/newsImages/" + imgPath;
                File imgFile = new File(filePath);
                // create newsCover directory if it doesn't exist
                if (!imgFile.getParentFile().exists()) {
                    if (!imgFile.getParentFile().mkdirs()) {
                        LOGGER.warn("Unable to create directory for image");
                        throw new FatalException("Unable to create directory for image");
                    }
                }
                image.transferTo(imgFile);
                news.setImagePath(imgPath);
            } catch (IOException e) {
                LOGGER.warn("Unable to save image" + e.getMessage());
                throw new FatalException("Unable to save image");
            }
        }

        Optional<Event> event = eventRepository.findById(eventId);
        if (event.isPresent()) {
            news.setEvent(event.get());
        } else {
            // delete image if event not found
            if (image != null && !image.isEmpty() && filePath != null) {
                try {
                    File file = new File(filePath);
                    file.delete();
                } catch (Exception ex) {
                    LOGGER.warn("Unable to delete image" + ex.getMessage());
                }
            }
            throw new NotFoundException(String.format("Could not find event with id %s", eventId));
        }
        return newsRepository.save(news);
    }
}
