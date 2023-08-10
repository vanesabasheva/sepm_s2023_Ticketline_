package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.PerformanceSector;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;


public interface PerformanceSectorRepository extends JpaRepository<PerformanceSector, Long> {

    /**
     * Find all performance sectors by performance id.
     *
     * @return list of al performance sector entries that match the performance id
     */
    List<PerformanceSector> findAllByPerformanceId(int performanceId);


    /**
     * Find all performance sectors by performance id.
     *
     * @return list of al performance sector entries that match the performance id
     */
    Set<PerformanceSector> findAllSetByPerformanceId(int performanceId);

    /**
     * find performanceSector by sector and performance id.
     *
     * @param sectorId      id of the sector
     * @param performanceId id of the performance
     * @return the looked up performanceSector
     */
    PerformanceSector findBySectorIdAndPerformanceId(Integer sectorId, Integer performanceId);

}
