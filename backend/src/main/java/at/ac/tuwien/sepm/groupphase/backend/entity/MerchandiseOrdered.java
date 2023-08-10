package at.ac.tuwien.sepm.groupphase.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;


@Entity
public class MerchandiseOrdered {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Boolean points;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchandise_id", nullable = false)
    private Merchandise merchandise;

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(final Integer quantity) {
        this.quantity = quantity;
    }

    public Boolean getPoints() {
        return points;
    }

    public void setPoints(final Boolean points) {
        this.points = points;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(final Order order) {
        this.order = order;
    }

    public Merchandise getMerchandise() {
        return merchandise;
    }

    public void setMerchandise(final Merchandise merchandise) {
        this.merchandise = merchandise;
    }


    public static final class MerchandiseOrderedBuilder {
        private Integer id;
        private Integer quantity;
        private Boolean points;
        private Order order;
        private Merchandise merchandise;

        public MerchandiseOrderedBuilder() {
        }

        public static MerchandiseOrderedBuilder aMerchandiseOrdered() {
            return new MerchandiseOrderedBuilder();
        }

        public MerchandiseOrderedBuilder withId(Integer id) {
            this.id = id;
            return this;
        }

        public MerchandiseOrderedBuilder withQuantity(Integer quantity) {
            this.quantity = quantity;
            return this;
        }

        public MerchandiseOrderedBuilder withPoints(Boolean points) {
            this.points = points;
            return this;
        }

        public MerchandiseOrderedBuilder withOrder(Order order) {
            this.order = order;
            return this;
        }

        public MerchandiseOrderedBuilder withMerchandise(Merchandise merchandise) {
            this.merchandise = merchandise;
            return this;
        }

        public MerchandiseOrdered build() {
            MerchandiseOrdered merchandiseOrdered = new MerchandiseOrdered();
            merchandiseOrdered.setId(id);
            merchandiseOrdered.setQuantity(quantity);
            merchandiseOrdered.setPoints(points);
            merchandiseOrdered.setOrder(order);
            merchandiseOrdered.setMerchandise(merchandise);
            return merchandiseOrdered;
        }
    }


    @Override
    public String toString() {
        return "MerchandiseOrdered{"
            +
            "id=" + id
            +
            ", quantity=" + quantity
            +
            ", points=" + points
            +
            ", order=" + order
            +
            ", merchandise=" + merchandise
            +
            '}';
    }
}

