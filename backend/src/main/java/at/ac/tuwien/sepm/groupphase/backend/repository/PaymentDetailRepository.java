package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.PaymentDetail;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface PaymentDetailRepository extends JpaRepository<PaymentDetail, Integer> {

    /**
     * Find paymentDetail by id.
     *
     * @param id the id of the paymentDetail
     * @return the paymentDetail
     */
    @EntityGraph(attributePaths = {
        "orders",
    })
    PaymentDetail getPaymentDetailById(Integer id);

    /**
     * Find all paymentDetails by userid.
     *
     * @param id user id
     * @return list of paymentDetails
     */
    List<PaymentDetail> findByUserId(Integer id);

    /**
     * find paymentDetails by userId.
     *
     * @param userId id of the user
     * @return the looked up paymentDetails
     */
    List<PaymentDetail> findPaymentDetailsByUserId(Integer userId);
}
