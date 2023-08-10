package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.Reservation;
import at.ac.tuwien.sepm.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.UnauthorizedException;
import at.ac.tuwien.sepm.groupphase.backend.repository.ReservationRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.ReservationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

@Service
public class CustomReservationService implements ReservationService {


    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final ReservationRepository reservationRepository;


    public CustomReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }


    @Override
    public List<Reservation> findReservationsByUserIdAndCart(Integer id, Boolean cart) {
        LOGGER.debug("Find reservations with user id {} and cart {}", id, cart);
        List<Reservation> reservations = reservationRepository.findAllWithNamedReservationByUserIdAndCart(id, cart);
        if (!cart) {
            reservations.removeIf(reservation -> java.time.LocalDateTime.now().isAfter(reservation.getExpirationTs()));
        }
        return reservations;
    }


    @Override
    public void deleteReservation(Integer reservationId, Integer userId) throws ConflictException, UnauthorizedException {
        LOGGER.debug("Delete reservation with id {}", reservationId);
        Reservation reservation = reservationRepository.findReservationById(reservationId);
        if (reservation == null) {
            throw new NotFoundException("Could not find Reservation");
        }
        List<String> conflictMsg = new ArrayList<>();
        // check if reservation is cart item
        if (reservation.getCart()) {
            conflictMsg.add("Reservation is a cart item, please remove it from the cart");
        }
        // check if reservation is expired
        if (java.time.LocalDateTime.now().isAfter(reservation.getExpirationTs())) {
            conflictMsg.add("Reservation is already expired");
        }
        if (!conflictMsg.isEmpty()) {
            throw new ConflictException("Could not delete reservation", conflictMsg);
        }
        // check if reservation belongs to user
        if (!reservation.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("Could not delete reservation", List.of("Reservation does not belong to user"));
        }
        reservationRepository.deleteById(reservationId);
    }
}
