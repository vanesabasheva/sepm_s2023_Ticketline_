package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.News;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface NewsService {


    /**
     * Find all news entries ordered by published at date (descending).
     *
     * @param userId        the id of the user
     * @param includingRead whether to include read news articles
     * @return ordered list of al news entries
     */
    List<News> findAll(Integer userId, boolean includingRead);


    /**
     * Find a single news entry by id and mark is as read.
     *
     * @param id the id of the message entry
     * @return the news entry
     */
    News findOne(Integer id, Integer userId);

    /**
     * Publish a single news entry.
     *
     * @param news    to publish
     * @param eventId the id of the event
     * @param image   the image of the news entry
     * @return published news entry
     * @throws ValidationException if the image is not of type jpg, jpeg, gif or png
     */
    News publishNews(News news, Integer eventId, MultipartFile image) throws ValidationException;
}
