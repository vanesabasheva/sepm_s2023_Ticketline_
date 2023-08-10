package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface SeatRepository extends JpaRepository<Seat, Integer> {

    /**
     * Find all seat entries by sector id.
     *
     * @return list of seat entries that match sector id
     */
    List<Seat> findAllBySectorId(int sectorId);

    /**
     * Find all seat entries by sector ids.
     *
     * @return list of seat entries that match sector ids
     */
    List<Seat> findAllBySectorIdIn(List<Integer> sectorIds);
}
