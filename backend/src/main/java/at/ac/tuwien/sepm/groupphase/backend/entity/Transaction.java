package at.ac.tuwien.sepm.groupphase.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal deductedAmount;

    @Column(nullable = false)
    private Integer deductedPoints;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(nullable = false)
    private LocalDateTime transactionTs;

    @Lob
    @Column
    private String receipt;

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public BigDecimal getDeductedAmount() {
        return deductedAmount;
    }

    public void setDeductedAmount(final BigDecimal deductedAmount) {
        this.deductedAmount = deductedAmount;
    }

    public Integer getDeductedPoints() {
        return deductedPoints;
    }

    public void setDeductedPoints(final Integer deductedPoints) {
        this.deductedPoints = deductedPoints;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(final Order order) {
        this.order = order;
    }

    public LocalDateTime getTransactionTs() {
        return transactionTs;
    }

    public void setTransactionTs(LocalDateTime transactionTs) {
        this.transactionTs = transactionTs;
    }

    public String getReceipt() {
        return receipt;
    }

    public void setReceipt(String receipt) {
        this.receipt = receipt;
    }

    @Override
    public String toString() {
        return "Transaction{"
            + "id=" + id
            + ", deductedAmount=" + deductedAmount
            + ", deductedPoints=" + deductedPoints
            + ", order=" + order
            + ", transactionTs=" + transactionTs
            + '}';
    }

    public static final class TransactionBuilder {
        private Integer id;
        private BigDecimal deductedAmount;
        private Integer deductedPoints;
        private Order order;
        private LocalDateTime transactionTs;
        private String receipt;

        private TransactionBuilder() {
        }

        public static TransactionBuilder aTransaction() {
            return new TransactionBuilder();
        }

        public TransactionBuilder withId(Integer id) {
            this.id = id;
            return this;
        }

        public TransactionBuilder withDeductedAmount(BigDecimal deductedAmount) {
            this.deductedAmount = deductedAmount;
            return this;
        }

        public TransactionBuilder withDeductedPoints(Integer deductedPoints) {
            this.deductedPoints = deductedPoints;
            return this;
        }

        public TransactionBuilder withOrder(Order order) {
            this.order = order;
            return this;
        }

        public TransactionBuilder withTransactionTs(LocalDateTime transactionTs) {
            this.transactionTs = transactionTs;
            return this;
        }

        public TransactionBuilder withReceipt(String receipt) {
            this.receipt = receipt;
            return this;
        }

        public Transaction build() {
            Transaction transaction = new Transaction();
            transaction.setId(id);
            transaction.setDeductedAmount(deductedAmount);
            transaction.setDeductedPoints(deductedPoints);
            transaction.setOrder(order);
            transaction.setTransactionTs(transactionTs);
            transaction.setReceipt(receipt);
            return transaction;
        }
    }

}

