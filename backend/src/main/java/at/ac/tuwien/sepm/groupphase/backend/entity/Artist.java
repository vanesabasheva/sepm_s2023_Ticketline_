package at.ac.tuwien.sepm.groupphase.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;

import java.util.Set;


@Entity
public class Artist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @ManyToMany
    @JoinTable(
        name = "artist_event",
        joinColumns = @JoinColumn(name = "artist_id"),
        inverseJoinColumns = @JoinColumn(name = "event_id")
    )
    private Set<Event> events;

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

    public Set<Event> getEvents() {
        return events;
    }

    public void setEvents(final Set<Event> events) {
        this.events = events;
    }


    public static final class ArtistBuilder {
        private Integer id;
        private String name;
        private Set<Event> events;

        private ArtistBuilder() {
        }

        public static ArtistBuilder aArtist() {
            return new ArtistBuilder();
        }

        public ArtistBuilder withId(Integer id) {
            this.id = id;
            return this;
        }

        public ArtistBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public ArtistBuilder withEvents(Set<Event> events) {
            this.events = events;
            return this;
        }

        public Artist build() {
            Artist artist = new Artist();
            artist.setId(id);
            artist.setName(name);
            artist.setEvents(events);
            return artist;
        }
    }

}
