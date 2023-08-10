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
public class Hall {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @OneToMany(mappedBy = "hall")
    private Set<Performance> performances;

    @OneToMany(mappedBy = "hall")
    private Set<Sector> sectors;

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

    public Location getLocation() {
        return location;
    }

    public void setLocation(final Location location) {
        this.location = location;
    }

    public Set<Performance> getPerformances() {
        return performances;
    }

    public void setPerformances(final Set<Performance> performances) {
        this.performances = performances;
    }

    public Set<Sector> getSectors() {
        return sectors;
    }

    public void setSectors(final Set<Sector> sectors) {
        this.sectors = sectors;
    }


    public static final class HallBuilder {
        private Integer id;
        private String name;
        private Location location;
        private Set<Performance> performances;
        private Set<Sector> sectors;

        private HallBuilder() {
        }

        public static HallBuilder aHall() {
            return new HallBuilder();
        }

        public HallBuilder withId(Integer id) {
            this.id = id;
            return this;
        }

        public HallBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public HallBuilder withLocation(Location location) {
            this.location = location;
            return this;
        }

        public HallBuilder withPerformances(Set<Performance> performances) {
            this.performances = performances;
            return this;
        }

        public HallBuilder withSectors(Set<Sector> sectors) {
            this.sectors = sectors;
            return this;
        }

        public Hall build() {
            Hall hall = new Hall();
            hall.setId(id);
            hall.setName(name);
            hall.setLocation(location);
            hall.setPerformances(performances);
            hall.setSectors(sectors);
            return hall;
        }
    }
}
