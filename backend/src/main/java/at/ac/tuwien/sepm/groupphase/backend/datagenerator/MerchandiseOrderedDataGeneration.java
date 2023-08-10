package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.Merchandise;
import at.ac.tuwien.sepm.groupphase.backend.entity.MerchandiseOrdered;
import at.ac.tuwien.sepm.groupphase.backend.repository.MerchandiseOrderedRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.MerchandiseRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.OrderRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Profile("generateData")
@Component
@DependsOn({"orderDataGenerator", "merchandiseDataGenerator"})
public class MerchandiseOrderedDataGeneration {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final MerchandiseRepository merchandiseRepository;
    private final OrderRepository orderRepository;
    private final MerchandiseOrderedRepository merchandiseOrderedRepository;

    public MerchandiseOrderedDataGeneration(MerchandiseRepository merchandiseRepository, OrderRepository orderRepository,
                                            MerchandiseOrderedRepository merchandiseOrderedRepository) {
        this.merchandiseRepository = merchandiseRepository;
        this.orderRepository = orderRepository;
        this.merchandiseOrderedRepository = merchandiseOrderedRepository;
    }

    @PostConstruct
    private void generateMerchandiseOrdered() {
        if (merchandiseOrderedRepository.findAll().size() > 0) {
            LOGGER.debug("merchandise ordered already generated");
        } else {
            LOGGER.debug("generating merchandise ordered entries");

            List<Merchandise> merchandise = merchandiseRepository.findAll();
            Merchandise[] merchArray = merchandise.toArray(new Merchandise[0]);
            int merchSize = merchArray.length;
            AtomicInteger counter = new AtomicInteger();
            Set<MerchandiseOrdered> merchandiseOrdered = new HashSet<>();
            orderRepository.findAll().forEach(order -> {
                merchandiseOrdered.add(new MerchandiseOrdered.MerchandiseOrderedBuilder()
                    .withMerchandise(merchArray[counter.getAndIncrement() % merchSize])
                    .withOrder(order)
                    .withQuantity(3)
                    .withPoints(true)
                    .build());
                merchandiseOrdered.add(new MerchandiseOrdered.MerchandiseOrderedBuilder()
                    .withMerchandise(merchArray[counter.getAndIncrement() % merchSize])
                    .withOrder(order)
                    .withQuantity(2)
                    .withPoints(true)
                    .build());
            });
            LOGGER.debug("saving merchandiseOrdered {}", merchandiseOrdered);
            merchandiseOrderedRepository.saveAll(merchandiseOrdered);

        }
    }
}
