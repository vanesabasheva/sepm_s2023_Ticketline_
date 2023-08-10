package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.MerchandiseOrdered;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface MerchandiseOrderedRepository extends JpaRepository<MerchandiseOrdered, Integer> {

    /**
     * Find all merchandise ordered by order id.
     * With Eager fetching of merchandise.
     *
     * @param id order id
     * @return list of merchandise ordered
     */
    @EntityGraph(attributePaths = {
        "merchandise"})
    List<MerchandiseOrdered> findByOrderId(Integer id);
}

