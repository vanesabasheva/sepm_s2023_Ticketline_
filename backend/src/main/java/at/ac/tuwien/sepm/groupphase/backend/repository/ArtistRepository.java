package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface ArtistRepository extends JpaRepository<Artist, Integer> {

    /**
     * Find all events and LEFT JOIN them (eager fetch).
     *
     * @return list of all artists
     */
    @Query("SELECT a FROM Artist a LEFT JOIN FETCH a.events")
    List<Artist> findAllFetchEvents();

    /**
     * Find all artists with name LIKE the given.
     *
     * @return list of all found artist
     */
    List<Artist> findByNameContainingIgnoreCase(String name);

    /**
     * find artist by name.
     *
     * @param name name of artist
     * @return artist
     */
    Optional<Artist> findByName(String name);

    /**
     * find all artists by event id.
     *
     * @param id id of event
     * @return list of artists
     */
    @EntityGraph(attributePaths = {"events"})
    List<Artist> findAllByEventsId(Integer id);


}

