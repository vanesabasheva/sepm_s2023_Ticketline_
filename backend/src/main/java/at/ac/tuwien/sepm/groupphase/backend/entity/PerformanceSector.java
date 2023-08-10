package at.ac.tuwien.sepm.groupphase.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.math.BigDecimal;


@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"performance_id", "sector_id"})})
public class PerformanceSector {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, precision = 7, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer pointsReward;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "performance_id", nullable = false)
    private Performance performance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sector_id", nullable = false)
    private Sector sector;

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

    public Integer getPointsReward() {
        return pointsReward;
    }

    public void setPointsReward(final Integer pointsReward) {
        this.pointsReward = pointsReward;
    }

    public Performance getPerformance() {
        return performance;
    }

    public void setPerformance(final Performance performance) {
        this.performance = performance;
    }

    public Sector getSector() {
        return sector;
    }

    public void setSector(final Sector sector) {
        this.sector = sector;
    }


    @Override
    public String toString() {
        return "PerformanceSector{"
            +
            "id=" + id
            + ", price=" + price
            + ", pointsReward=" + pointsReward
            + ", performance=" + performance
            + ", sector=" + sector
            + '}';
    }

    public static final class PerformanceSectorBuilder {
        private Integer id;
        private BigDecimal price;
        private Integer pointsReward;
        private Performance performance;
        private Sector sector;

        private PerformanceSectorBuilder() {
        }

        public static PerformanceSectorBuilder aPerformanceSector() {
            return new PerformanceSectorBuilder();
        }

        public PerformanceSectorBuilder withId(Integer id) {
            this.id = id;
            return this;
        }

        public PerformanceSectorBuilder withPrice(BigDecimal price) {
            this.price = price;
            return this;
        }

        public PerformanceSectorBuilder withPointsReward(Integer pointsReward) {
            this.pointsReward = pointsReward;
            return this;
        }

        public PerformanceSectorBuilder withPerformance(Performance performance) {
            this.performance = performance;
            return this;
        }

        public PerformanceSectorBuilder withSector(Sector sector) {
            this.sector = sector;
            return this;
        }

        public PerformanceSector build() {
            PerformanceSector performanceSector = new PerformanceSector();
            performanceSector.setId(id);
            performanceSector.setPrice(price);
            performanceSector.setPointsReward(pointsReward);
            performanceSector.setPerformance(performance);
            performanceSector.setSector(sector);
            return performanceSector;
        }
    }
}

