package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.MerchandiseOrdered;
import at.ac.tuwien.sepm.groupphase.backend.entity.PerformanceSector;
import at.ac.tuwien.sepm.groupphase.backend.entity.Sector;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.entity.Transaction;
import at.ac.tuwien.sepm.groupphase.backend.repository.MerchandiseOrderedRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.OrderRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.PerformanceSectorRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TicketRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TransactionRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Profile("generateData")
@Component
@DependsOn({"orderDataGenerator", "merchandiseOrderedDataGeneration", "merchandiseDataGenerator", "performanceSectorDataGenerator"})
public class TransactionDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final OrderRepository orderRepository;
    private final TransactionRepository transactionRepository;
    private final MerchandiseOrderedRepository merchandiseOrderedRepository;
    private final TicketRepository ticketRepository;
    private final PerformanceSectorRepository performanceSectorRepository;


    public TransactionDataGenerator(OrderRepository orderRepository, TransactionRepository transactionRepository,
                                    MerchandiseOrderedRepository merchandiseOrderedRepository, TicketRepository ticketRepository,
                                    PerformanceSectorRepository performanceSectorRepository) {
        this.orderRepository = orderRepository;
        this.transactionRepository = transactionRepository;
        this.merchandiseOrderedRepository = merchandiseOrderedRepository;
        this.ticketRepository = ticketRepository;
        this.performanceSectorRepository = performanceSectorRepository;
    }


    @PostConstruct
    private void generateOrder() {
        if (transactionRepository.findAll().size() > 0) {
            LOGGER.debug("transaction already generated");
        } else {
            LOGGER.debug("generating transaction entries");

            List<Transaction> transactionList = new ArrayList<>();
            orderRepository.findAll().forEach(order -> {
                BigDecimal price = BigDecimal.valueOf(0);
                int points = 0;
                List<MerchandiseOrdered> merchandiseOrderedList = merchandiseOrderedRepository.findByOrderId(order.getId());

                for (MerchandiseOrdered merchandiseOrdered : merchandiseOrderedList) {
                    price = price.add(merchandiseOrdered.getMerchandise().getPrice()
                        .multiply(BigDecimal.valueOf(merchandiseOrdered.getQuantity())));
                    if (merchandiseOrdered.getPoints()) {
                        points += merchandiseOrdered.getMerchandise().getPointsReward() * merchandiseOrdered.getQuantity();
                    }
                }

                List<Ticket> ticketList = ticketRepository.findByOrderId(order.getId());
                for (Ticket ticket : ticketList) {
                    Sector sector = ticket.getSeat().getSector();
                    PerformanceSector performanceSector =
                        performanceSectorRepository.findBySectorIdAndPerformanceId(sector.getId(), ticket.getPerformance().getId());
                    price = price.add(performanceSector.getPrice());
                    points += performanceSector.getPointsReward();

                }
                Transaction transaction = Transaction.TransactionBuilder.aTransaction()
                    .withOrder(order)
                    .withDeductedAmount(price)
                    .withDeductedPoints(points)
                    .withTransactionTs(LocalDateTime.now())
                    .build();

                transactionList.add(transaction);
            });
            transactionRepository.saveAll(transactionList);
        }
    }
}