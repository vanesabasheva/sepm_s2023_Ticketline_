package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


/**
 * The interface Performance repository.
 */
public interface PerformanceRepository extends JpaRepository<Performance, Integer> {

    /**
     * find all performances by id.
     *
     * @param id id of performance
     * @return performance
     */
    Optional<Performance> findAllPerformancesById(Integer id);

    /**
     * find all performances by event id.
     *
     * @param id id of event
     * @return performance
     */
    @EntityGraph(attributePaths = {
        "event", "hall", "event.artists", "hall.location", "performanceSectors"
    })
    List<Performance> findAllByEvent_Id(Integer id);

    /**
     * find all performances by hall id.
     *
     * @param id id of hall
     * @return performance
     */
    @EntityGraph(attributePaths = {
        "event", "hall", "hall.location"
    })
    @Query("SELECT a FROM Performance a WHERE a.hall.location.id = :id")
    List<Performance> findAllByHallLocation_Id(@Param("id") Integer id);


    /**
     * filter performances by given parameters.
     *
     * @param dateTimeFrom start date of performance
     * @param dateTimeTill end date of performance
     * @param eventName    name of event
     * @param hallName     name of hall
     * @param lowerPrice   lower price of performance
     * @param upperPrice   upper price of performance
     * @return list of performances
     */
    @EntityGraph(attributePaths = {
        "event", "hall", "hall.location", "performanceSectors"
    })
    @Query("SELECT a FROM Performance a INNER JOIN PerformanceSector PS ON a.id=PS.id"
        + " INNER JOIN Seat S ON PS.sector.id = S.sector.id"
        + " WHERE (S.id NOT IN (SELECT T.seat FROM Ticket T INNER JOIN Reservation R ON T.id = R.ticket.id) "
        + " AND S.id NOT IN (SELECT T.seat FROM Ticket T WHERE T.order IS NOT NULL))"
        + " AND (:dateTimeFrom is null OR a.datetime >= :dateTimeFrom AND a.datetime <= :dateTimeTill)"
        + " AND (:eventName is null OR :eventName = '' OR UPPER(a.event.name) LIKE UPPER(CONCAT('%', :eventName, '%')))"
        + " AND (:hallName is null OR :hallName = '' OR UPPER(a.hall.name) LIKE UPPER(CONCAT('%', :hallName, '%')))"
        + " AND (:lowerPrice is null OR (PS.price >= :lowerPrice AND PS.price <= :upperPrice))")
    List<Performance> findAllPerformancesWithParams(@Param("dateTimeFrom") LocalDateTime dateTimeFrom,
                                                    @Param("dateTimeTill") LocalDateTime dateTimeTill,
                                                    @Param("eventName") String eventName,
                                                    @Param("hallName") String hallName,
                                                    @Param("lowerPrice") BigDecimal lowerPrice,
                                                    @Param("upperPrice") BigDecimal upperPrice);
}

