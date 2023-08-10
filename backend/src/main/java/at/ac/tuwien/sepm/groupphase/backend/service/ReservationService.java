package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Reservation;
import at.ac.tuwien.sepm.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepm.groupphase.backend.exception.UnauthorizedException;

import java.util.List;

public interface ReservationService {


    /**
     * Find all reservations by user id and cart status.
     * EntityGraph with ticket, ticket.seat, ticket.performance, ticket.performance.event
     *
     * @param id   the id of the user
     * @param cart if the reservation is in the cart or not
     * @return the list of reservations/the cart of the user
     */
    List<Reservation> findReservationsByUserIdAndCart(Integer id, Boolean cart);

    /**
     * Delete a reservation by its id.
     *
     * @param reservationId the id of the reservation
     */
    void deleteReservation(Integer reservationId, Integer userId) throws ConflictException, UnauthorizedException;
}
