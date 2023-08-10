package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.Merchandise;
import at.ac.tuwien.sepm.groupphase.backend.repository.MerchandiseRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;

@Profile("generateData")
@Component
public class MerchandiseDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final int NUMBER_OF_MERCHANDISE_TO_GENERATE = 20;
    private static final String TEST_MERCHANDISE_TITLE = "Merchandise";
    private static final String TEST_MERCHANDISE_DESCRIPTION = "Merchandise Description";


    private final MerchandiseRepository merchandiseRepository;

    public MerchandiseDataGenerator(MerchandiseRepository merchandiseRepository) {
        this.merchandiseRepository = merchandiseRepository;
    }


    @PostConstruct
    private void generateMerchandise() {
        if (merchandiseRepository.findAll().size() > 0) {
            LOGGER.debug("merchandise already generated");
        } else {
            LOGGER.debug("generating {} merchandise entries", NUMBER_OF_MERCHANDISE_TO_GENERATE);
            for (int i = 0; i < NUMBER_OF_MERCHANDISE_TO_GENERATE; i++) {
                Merchandise merchandise = Merchandise.MerchandiseBuilder.aMerchandise()
                    .withTitle(TEST_MERCHANDISE_TITLE + " " + i)
                    .withDescription(TEST_MERCHANDISE_DESCRIPTION + " " + i)
                    .withPrice(BigDecimal.valueOf(9.99 + i * 10))
                    .withPointsReward(9 + i * 10)
                    .withPointsPrice((10 + i * 10) * 4)
                    .withImagePath((i % 20) + ".png")
                    .build();
                LOGGER.debug("saving merchandise {}", merchandise);
                merchandiseRepository.save(merchandise);
            }
        }
    }
}