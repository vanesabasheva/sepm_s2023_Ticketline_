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

import java.util.Set;


@Entity
public class Sector {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Boolean standing;

    @OneToMany(mappedBy = "sector")
    private Set<Seat> seats;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hall_id", nullable = false)
    private Hall hall;

    @OneToMany(mappedBy = "sector")
    private Set<PerformanceSector> performanceSectors;

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Boolean getStanding() {
        return standing;
    }

    public void setStanding(final Boolean standing) {
        this.standing = standing;
    }

    public Set<Seat> getSeats() {
        return seats;
    }

    public void setSeats(final Set<Seat> seats) {
        this.seats = seats;
    }

    public Hall getHall() {
        return hall;
    }

    public void setHall(final Hall hall) {
        this.hall = hall;
    }

    public Set<PerformanceSector> getPerformanceSectors() {
        return performanceSectors;
    }

    public void setPerformanceSectors(final Set<PerformanceSector> performanceSectors) {
        this.performanceSectors = performanceSectors;
    }


    public static final class SectorBuilder {

        private Integer id;
        private String name;
        private Boolean standing;
        private Set<Seat> seats;
        private Hall hall;
        private Set<PerformanceSector> performanceSectors;

        private SectorBuilder() {
        }

        public static SectorBuilder aSector() {
            return new SectorBuilder();
        }

        public SectorBuilder withId(Integer id) {
            this.id = id;
            return this;
        }

        public SectorBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public SectorBuilder withStanding(Boolean standing) {
            this.standing = standing;
            return this;
        }

        public SectorBuilder withSeats(Set<Seat> seats) {
            this.seats = seats;
            return this;
        }

        public SectorBuilder withHall(Hall hall) {
            this.hall = hall;
            return this;
        }

        public SectorBuilder withPerformanceSectors(Set<PerformanceSector> performanceSectors) {
            this.performanceSectors = performanceSectors;
            return this;
        }

        public Sector build() {
            Sector sector = new Sector();
            sector.setId(id);
            sector.setName(name);
            sector.setStanding(standing);
            sector.setSeats(seats);
            sector.setHall(hall);
            sector.setPerformanceSectors(performanceSectors);
            return sector;
        }
    }
}

