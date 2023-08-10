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
import org.mapstruct.Mapper;

import java.time.LocalDateTime;
import java.util.Set;


@Entity
public class Performance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private LocalDateTime datetime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hall_id", nullable = false)
    private Hall hall;

    @OneToMany(mappedBy = "performance")
    private Set<Ticket> tickets;

    @OneToMany(mappedBy = "performance")
    private Set<PerformanceSector> performanceSectors;

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public LocalDateTime getDatetime() {
        return datetime;
    }

    public void setDatetime(final LocalDateTime datetime) {
        this.datetime = datetime;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(final Event event) {
        this.event = event;
    }

    public Hall getHall() {
        return hall;
    }

    public void setHall(final Hall hall) {
        this.hall = hall;
    }

    public Set<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(final Set<Ticket> tickets) {
        this.tickets = tickets;
    }

    public Set<PerformanceSector> getPerformanceSectors() {
        return performanceSectors;
    }

    public void setPerformanceSectors(final Set<PerformanceSector> performanceSectors) {
        this.performanceSectors = performanceSectors;
    }


    @Override
    public String toString() {
        return "Performance{"
            +
            "id=" + id
            + ", datetime=" + datetime
            + ", event=" + event
            + ", hall=" + hall
            + ", tickets=" + tickets
            + ", sectors=" + performanceSectors
            + '}';
    }

    public static final class PerformanceBuilder {
        private Integer id;
        private LocalDateTime datetime;
        private Event event;
        private Hall hall;
        private Set<Ticket> tickets;
        private Set<PerformanceSector> performanceSectors;

        public PerformanceBuilder() {
        }

        public static PerformanceBuilder aPerformance() {
            return new PerformanceBuilder();
        }

        public PerformanceBuilder withId(Integer id) {
            this.id = id;
            return this;
        }

        public PerformanceBuilder withDatetime(LocalDateTime datetime) {
            this.datetime = datetime;
            return this;
        }

        public PerformanceBuilder withEvent(Event event) {
            this.event = event;
            return this;
        }

        public PerformanceBuilder withHall(Hall hall) {
            this.hall = hall;
            return this;
        }

        public PerformanceBuilder withTickets(Set<Ticket> tickets) {
            this.tickets = tickets;
            return this;
        }

        public PerformanceBuilder withperformanceSectors(Set<PerformanceSector> performanceSectors) {
            this.performanceSectors = performanceSectors;
            return this;
        }

        public Performance build() {
            Performance performance = new Performance();
            performance.setId(id);
            performance.setDatetime(datetime);
            performance.setEvent(event);
            performance.setHall(hall);
            performance.setTickets(tickets);
            performance.setPerformanceSectors(performanceSectors);
            return performance;
        }

    }
}

