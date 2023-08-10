package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Sector;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SectorRepository extends JpaRepository<Sector, Integer> {


    /**
     * Find all sectors by hall id.
     *
     * @param hallId the hall id
     * @return the sector list
     */
    List<Sector> findAllByHallId(Integer hallId);
    
}
