package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Merchandise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface MerchandiseRepository extends JpaRepository<Merchandise, Integer> {


    /**
     * find all merchandise with points.
     *
     * @return list of merchandise
     */
    List<Merchandise> findAllMerchandiseWithPoints();

}

