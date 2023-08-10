package at.ac.tuwien.sepm.groupphase.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;

import java.time.Duration;
import java.util.Set;


@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column
    private String type;

    @Column(nullable = false)
    private Duration length;

    @Column
    private String description;

    @Column(length = 200)
    private String imagePath;

    @ManyToMany(mappedBy = "events")
    private Set<Artist> artists;

    @OneToMany(mappedBy = "event")
    private Set<Performance> performances;

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

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

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public Duration getLength() {
        return length;
    }

    public void setLength(final Duration length) {
        this.length = length;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public Set<Artist> getArtists() {
        return artists;
    }

    public void setArtists(final Set<Artist> artists) {
        this.artists = artists;
    }

    public Set<Performance> getPerformances() {
        return performances;
    }

    public void setPerformances(final Set<Performance> performances) {
        this.performances = performances;
    }


    public static final class EventBuilder {
        private Integer id;
        private String name;
        private String type;
        private Duration length;
        private String description;
        private Set<Artist> artists;
        private Set<Performance> performances;
        private String imagePath;

        private EventBuilder() {
        }

        public static EventBuilder aEvent() {
            return new EventBuilder();
        }

        public EventBuilder withId(Integer id) {
            this.id = id;
            return this;
        }

        public EventBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public EventBuilder withType(String type) {
            this.type = type;
            return this;
        }

        public EventBuilder withLength(Duration length) {
            this.length = length;
            return this;
        }

        public EventBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public EventBuilder withArtists(Set<Artist> artists) {
            this.artists = artists;
            return this;
        }

        public EventBuilder withPerformances(Set<Performance> performances) {
            this.performances = performances;
            return this;
        }

        public EventBuilder withImagePath(String imagePath) {
            this.imagePath = imagePath;
            return this;
        }

        public Event build() {
            Event event = new Event();
            event.setId(id);
            event.setName(name);
            event.setType(type);
            event.setLength(length);
            event.setDescription(description);
            event.setArtists(artists);
            event.setPerformances(performances);
            event.setImagePath(imagePath);
            return event;
        }
    }
}
