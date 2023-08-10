package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.News;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface NewsRepository extends JpaRepository<News, Integer> {


    /**
     * Find all unread news entries ordered by published at date (descending).
     *
     * @param userId the id of the user
     * @return ordered list of al news entries
     */
    @Query(value = "SELECT n.* FROM News n LEFT OUTER JOIN READ_NEWS RN on n.ID = RN.NEWS_ID AND RN.USER_ID = :userId WHERE USER_ID IS NULL ORDER BY PUBLICATION_DATE DESC;", nativeQuery = true)
    List<News> findNewsNotReadByUserOrderByPublicationDateDesc(@Param("userId") Integer userId);

    /**
     * Find all news entries ordered by published at date (descending).
     *
     * @return ordered list of al news entries
     */
    @Query("SELECT n FROM News n ORDER BY n.publicationDate DESC")
    List<News> findNewsOrderByPublicationDateDesc();


    /**
     * Find a single news entry by id and set it to read.
     *
     * @param newsId the id of the news entry
     * @param userId the id of the user
     */
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO read_news(news_id, user_id) SELECT :newsId, :userId WHERE NOT EXISTS (SELECT 1 FROM read_news WHERE news_id = :newsId AND user_id = :userId)", nativeQuery = true)
    void markNewsAsReadByUser(@Param("newsId") Integer newsId, @Param("userId") Integer userId);

}



