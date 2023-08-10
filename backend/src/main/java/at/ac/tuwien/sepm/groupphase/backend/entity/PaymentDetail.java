package at.ac.tuwien.sepm.groupphase.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import java.time.LocalDate;
import java.util.Set;

@Entity
public class PaymentDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @Column(nullable = false)
    private String cardNumber;

    @Column(nullable = false)
    private String cardHolder;

    @Column(nullable = false)
    private Integer cvv;

    @Column(nullable = false)
    private LocalDate expirationDate;

    @OneToMany(mappedBy = "paymentDetail")
    private Set<Order> orders;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private ApplicationUser user;


    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(final String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(final String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public Integer getCvv() {
        return cvv;
    }

    public void setCvv(final Integer cvv) {
        this.cvv = cvv;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(final LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(final Set<Order> orders) {
        this.orders = orders;
    }

    public ApplicationUser getUser() {
        return user;
    }

    public void setUser(final ApplicationUser user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "PaymentDetail{"
            + "id=" + id
            + ", cardNumber=" + cardNumber
            + ", cardHolder='" + cardHolder + '\''
            + ", cvv=" + cvv
            + ", expirationDate=" + expirationDate
            + ", orders=" + orders
            + ", user=" + user
            + '}';
    }

    public static final class PaymentDetailBuilder {
        private Integer id;
        private String cardNumber;
        private String cardHolder;
        private Integer cvv;
        private LocalDate expirationDate;
        private Set<Order> orders;
        private ApplicationUser user;

        private PaymentDetailBuilder() {
        }

        public static PaymentDetailBuilder aPaymentDetail() {
            return new PaymentDetailBuilder();
        }

        public PaymentDetailBuilder withId(Integer id) {
            this.id = id;
            return this;
        }

        public PaymentDetailBuilder withCardNumber(String cardNumber) {
            this.cardNumber = cardNumber;
            return this;
        }

        public PaymentDetailBuilder withCardHolder(String cardHolder) {
            this.cardHolder = cardHolder;
            return this;
        }

        public PaymentDetailBuilder withCvv(Integer cvv) {
            this.cvv = cvv;
            return this;
        }

        public PaymentDetailBuilder withExpirationDate(LocalDate expirationDate) {
            this.expirationDate = expirationDate;
            return this;
        }

        public PaymentDetailBuilder withOrders(Set<Order> orders) {
            this.orders = orders;
            return this;
        }

        public PaymentDetailBuilder withUser(ApplicationUser user) {
            this.user = user;
            return this;
        }

        public PaymentDetail build() {
            PaymentDetail paymentDetail = new PaymentDetail();
            paymentDetail.setId(id);
            paymentDetail.setCardNumber(cardNumber);
            paymentDetail.setCardHolder(cardHolder);
            paymentDetail.setCvv(cvv);
            paymentDetail.setExpirationDate(expirationDate);
            paymentDetail.setOrders(orders);
            paymentDetail.setUser(user);
            return paymentDetail;
        }
    }

}

