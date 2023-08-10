package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Reservation;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.repository.NotUserRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ReservationRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TicketRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static at.ac.tuwien.sepm.groupphase.backend.datagenerator.UserDataGenerator.NUMBER_OF_USERS_TO_GENERATE;

@Profile("generateData")
@Component
@DependsOn({"ticketDataGenerator", "userDataGenerator", "orderDataGenerator"})
public class ReservationDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final TicketRepository ticketRepository;
    private final ReservationRepository reservationRepository;
    private final NotUserRepository notUserRepository;

    public ReservationDataGenerator(TicketRepository ticketRepository, ReservationRepository reservationRepository, NotUserRepository notUserRepository) {
        this.ticketRepository = ticketRepository;
        this.reservationRepository = reservationRepository;
        this.notUserRepository = notUserRepository;
    }


    @PostConstruct
    private void generateReservations() {
        if (reservationRepository.findAll().size() > 0) {
            LOGGER.debug("reservation already generated");
        } else {
            List<Ticket> tickets = ticketRepository.findAllByOrderIsNull();
            LOGGER.debug("generating reservation entries");
            boolean ticketInCart = false;
            for (int i = 1; i < tickets.size(); i++) {
                if (Math.random() < 0.30) {
                    Optional<ApplicationUser> user = notUserRepository.findById(i % NUMBER_OF_USERS_TO_GENERATE);
                    if (user.isEmpty()) {
                        LOGGER.debug("user {} not found", i);
                        continue;
                    }

                    Reservation reservation = Reservation.ReservationBuilder.aReservation()
                        .withUser(user.get())
                        .withTicket(tickets.get(i))
                        .withExpirationTs(LocalDateTime.now().plusDays(100))
                        .withCart(ticketInCart = !ticketInCart)
                        .build();
                    LOGGER.debug("saving reservation {}", reservation);
                    reservationRepository.save(reservation);
                }
            }
        }
    }
}
