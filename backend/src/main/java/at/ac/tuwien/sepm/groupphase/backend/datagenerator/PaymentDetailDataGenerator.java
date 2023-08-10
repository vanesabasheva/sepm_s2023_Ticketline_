package at.ac.tuwien.sepm.groupphase.backend.datagenerator;


import at.ac.tuwien.sepm.groupphase.backend.entity.PaymentDetail;
import at.ac.tuwien.sepm.groupphase.backend.repository.NotUserRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.PaymentDetailRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.time.LocalDate;
import java.util.Set;

@Profile("generateData")
@Component
@DependsOn({"userDataGenerator"})
public class PaymentDetailDataGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());


    private final PaymentDetailRepository paymentDetailRepository;
    private final NotUserRepository notUserRepository;

    public PaymentDetailDataGenerator(PaymentDetailRepository paymentDetailRepository, NotUserRepository notUserRepository) {
        this.paymentDetailRepository = paymentDetailRepository;
        this.notUserRepository = notUserRepository;
    }


    @PostConstruct
    public void generatePaymentDetail() {
        if (paymentDetailRepository.findAll().size() > 0) {
            LOGGER.debug("payment detail already generated");
        } else {
            LOGGER.debug("generating payment detail entries");
            notUserRepository.findAll().forEach(user -> {
                PaymentDetail paymentDetail = PaymentDetail.PaymentDetailBuilder.aPaymentDetail()
                    .withCardNumber("123456789")
                    .withCardHolder(user.getFirstName() + " " + user.getLastName())
                    .withCvv(123)
                    .withExpirationDate(LocalDate.now().plusDays(100))
                    .withUser(user)
                    .build();
                paymentDetail = paymentDetailRepository.save(paymentDetail);
                user.setPaymentDetails(Set.of(paymentDetail));
                notUserRepository.save(user);
            });

        }
    }
}
