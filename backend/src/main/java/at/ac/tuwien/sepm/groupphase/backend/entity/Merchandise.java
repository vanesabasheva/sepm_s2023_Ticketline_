package at.ac.tuwien.sepm.groupphase.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.math.BigDecimal;
import java.util.Set;


@Entity
public class Merchandise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, precision = 7, scale = 2)
    private BigDecimal price;

    @Column
    private Integer pointsPrice;

    @Column(nullable = false)
    private Integer pointsReward;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column
    private String imagePath;


    @OneToMany(mappedBy = "merchandise")
    private Set<MerchandiseOrdered> merchandiseMerchandiseOrdereds;


    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(final BigDecimal price) {
        this.price = price;
    }

    public Integer getPointsPrice() {
        return pointsPrice;
    }

    public void setPointsPrice(final Integer pointsPrice) {
        this.pointsPrice = pointsPrice;
    }

    public Integer getPointsReward() {
        return pointsReward;
    }

    public void setPointsReward(final Integer pointsReward) {
        this.pointsReward = pointsReward;
    }

    public Set<MerchandiseOrdered> getMerchandiseMerchandiseOrdereds() {
        return merchandiseMerchandiseOrdereds;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setMerchandiseMerchandiseOrdereds(
        final Set<MerchandiseOrdered> merchandiseMerchandiseOrdereds) {
        this.merchandiseMerchandiseOrdereds = merchandiseMerchandiseOrdereds;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public static final class MerchandiseBuilder {
        private Integer id;
        private BigDecimal price;
        private Integer pointsPrice;
        private Integer pointsReward;
        private String title;
        private String description;
        private String imagePath;
        private Set<MerchandiseOrdered> merchandiseMerchandiseOrdereds;

        private MerchandiseBuilder() {
        }

        public static MerchandiseBuilder aMerchandise() {
            return new MerchandiseBuilder();
        }

        public MerchandiseBuilder withId(Integer id) {
            this.id = id;
            return this;
        }

        public MerchandiseBuilder withPrice(BigDecimal price) {
            this.price = price;
            return this;
        }

        public MerchandiseBuilder withPointsPrice(Integer pointsPrice) {
            this.pointsPrice = pointsPrice;
            return this;
        }

        public MerchandiseBuilder withPointsReward(Integer pointsReward) {
            this.pointsReward = pointsReward;
            return this;
        }

        public MerchandiseBuilder withTitle(String title) {
            this.title = title;
            return this;
        }

        public MerchandiseBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public MerchandiseBuilder withMerchandiseMerchandiseOrdereds(Set<MerchandiseOrdered> merchandiseMerchandiseOrdereds) {
            this.merchandiseMerchandiseOrdereds = merchandiseMerchandiseOrdereds;
            return this;
        }

        public MerchandiseBuilder withImagePath(String imagePath) {
            this.imagePath = imagePath;
            return this;
        }

        public Merchandise build() {
            Merchandise merchandise = new Merchandise();
            merchandise.setId(id);
            merchandise.setPrice(price);
            merchandise.setPointsPrice(pointsPrice);
            merchandise.setPointsReward(pointsReward);
            merchandise.setTitle(title);
            merchandise.setDescription(description);
            merchandise.setImagePath(imagePath);
            merchandise.setMerchandiseMerchandiseOrdereds(merchandiseMerchandiseOrdereds);
            return merchandise;
        }
    }

    @Override
    public String toString() {
        return "Merchandise{"
            +
            "id=" + id
            +
            ", price=" + price
            +
            ", pointsPrice=" + pointsPrice
            +
            ", pointsReward=" + pointsReward
            +
            ", merchandiseMerchandiseOrdereds=" + merchandiseMerchandiseOrdereds
            +
            '}';
    }
}

