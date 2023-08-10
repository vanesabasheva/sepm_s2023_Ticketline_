package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Transaction;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    /**
     * get transaction by id.
     *
     * @param id id of transaction
     * @return transaction
     */
    @EntityGraph(attributePaths = {
        "order",
        "order.deliveryAddress",
        "order.user"
    })
    Transaction getTransactionById(Integer id);
}
