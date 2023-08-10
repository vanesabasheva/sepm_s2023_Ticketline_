package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    /**
     * Find all orders by user id.
     * Furthermore, the following attributes are loaded eagerly: "tickets", "merchandiseOrdered", "tickets.performance",
     * "merchandiseOrdered.merchandise", "tickets.seat", "tickets.performance.event",
     * "tickets.performance.performanceSectors", "tickets.seat.sector.performanceSectors",
     * "tickets.performance.event.artists", "tickets.seat.sector" and "transactions".
     *
     * @param id user id
     * @return list of orders by the user
     */
    @EntityGraph(attributePaths = {
        "tickets", "merchandiseOrdered", "tickets.performance", "merchandiseOrdered.merchandise", "tickets.seat",
        "tickets.performance.event", "tickets.performance.performanceSectors",
        "tickets.seat.sector.performanceSectors",
        "tickets.performance.event.artists", "tickets.seat.sector", "transactions"
    })
    List<Order> getAllOrdersByUserId(Integer id);


    /**
     * get order by id.
     *
     * @param id id of order
     * @return order
     */
    @EntityGraph(attributePaths = {
        "tickets",
        "transactions",
        "user"
    })
    Order getOrderById(Integer id);

    /**
     * get order by id.
     *
     * @param id id of order
     * @return order
     */
    @EntityGraph(attributePaths = {
        "tickets",
        "tickets.performance",
        "tickets.performance.performanceSectors",
        "tickets.seat",
        "tickets.seat.sector",
        "transactions",
        "merchandiseOrdered",
        "merchandiseOrdered.merchandise",
        "user"
    })
    Order getOrderHereById(Integer id);

    /**
     * get order by id.
     *
     * @param id id of order
     * @return order
     */
    @EntityGraph(attributePaths = {
        "deliveryAddress",
        "paymentDetail",
        "tickets",
        "tickets.performance",
        "tickets.performance.performanceSectors",
        "tickets.performance.event",
        "tickets.performance.event.artists",
        "tickets.performance.hall",
        "tickets.seat",
        "tickets.seat.sector",
        "transactions",
        "merchandiseOrdered",
        "merchandiseOrdered.merchandise",
        "user",
        "transactions",
    })
    Order getOrderNowById(Integer id);

}

