package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Hall;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface HallRepository extends JpaRepository<Hall, Integer> {

    /**
     * find hall by id.
     *
     * @param id id of hall
     * @return hall
     */
    Optional<Hall> findById(Integer id);

}

