package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Order;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.repository.NotUserRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.OrderRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TicketRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Profile("generateData")
@Component
@DependsOn({"userDataGenerator", "paymentDetailDataGenerator", "locationDataGenerator", "ticketDataGenerator"})
public class OrderDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());


    private final NotUserRepository notUserRepository;

    private final OrderRepository orderRepository;

    private final TicketRepository ticketRepository;


    public OrderDataGenerator(NotUserRepository notUserRepository, OrderRepository orderRepository, TicketRepository ticketRepository) {
        this.notUserRepository = notUserRepository;
        this.orderRepository = orderRepository;
        this.ticketRepository = ticketRepository;
    }

    @PostConstruct
    @Transactional
    private void generateOrder() {
        if (orderRepository.findAll().size() > 0) {
            LOGGER.debug("order already generated");
        } else {
            LOGGER.debug("generating order entries");
            List<Ticket> tickets = ticketRepository.findAllByOrderIsNullAndReservationIsNull();
            Collections.shuffle(tickets); // Shuffle the list of tickets

            List<ApplicationUser> users = notUserRepository.findAllWithLocationsAndPaymentDetails();
            int numTicketsPerOrder = 4; // Number of tickets to be assigned to each order
            int totalOrders = (tickets.size() / numTicketsPerOrder) / 50;
            int ticketsAssigned = 0;

            for (int i = 0; i < totalOrders; i++) {
                ApplicationUser user = users.get(i % users.size()); // Select user in a round-robin fashion
                Set<Ticket> boughtTickets = new HashSet<>();

                for (int j = 0; j < numTicketsPerOrder && ticketsAssigned < tickets.size(); j++) {
                    Ticket ticket = tickets.get(ticketsAssigned++);
                    boughtTickets.add(ticket);
                }

                if (boughtTickets.isEmpty()) {
                    continue;
                }

                Order order = Order.OrderBuilder.aOrder()
                    .setOrderTs(LocalDateTime.now())
                    .setCancelled(false)
                    .setUser(user)
                    .setDeliveryAddress(user.getLocations().iterator().next())
                    .setPaymentDetail(user.getPaymentDetails().iterator().next())
                    .setTickets(boughtTickets)
                    .build();
                for (Ticket t : boughtTickets) {
                    t.setOrder(order);
                }

                LOGGER.debug("Saving order: {}", order);
                orderRepository.save(order);
                ticketRepository.saveAll(boughtTickets);
            }
        }
    }
}