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
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;


import java.util.Set;


@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"row", "number", "sector_id"})})
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, name = "\"row\"")
    private Integer row;

    @Column(nullable = false)
    private Integer number;

    @OneToMany(mappedBy = "seat")
    private Set<Ticket> tickets;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sector_id", nullable = false)
    private Sector sector;

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public Integer getRow() {
        return row;
    }

    public void setRow(final Integer row) {
        this.row = row;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(final Integer number) {
        this.number = number;
    }

    public Set<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(final Set<Ticket> tickets) {
        this.tickets = tickets;
    }

    public Sector getSector() {
        return sector;
    }

    public void setSector(final Sector sector) {
        this.sector = sector;
    }


    public static final class SeatBuilder {
        private Integer id;
        private Integer row;
        private Integer number;
        private Set<Ticket> tickets;
        private Sector sector;


        private SeatBuilder() {
        }

        public static SeatBuilder aSeat() {
            return new SeatBuilder();
        }


        public SeatBuilder withId(Integer id) {
            this.id = id;
            return this;
        }


        public SeatBuilder withRow(Integer row) {
            this.row = row;
            return this;
        }


        public SeatBuilder withNumber(Integer number) {
            this.number = number;
            return this;
        }


        public SeatBuilder withTickets(Set<Ticket> tickets) {
            this.tickets = tickets;
            return this;
        }


        public SeatBuilder withSector(Sector sector) {
            this.sector = sector;
            return this;
        }


        public Seat build() {
            Seat seat = new Seat();
            seat.setId(id);
            seat.setRow(row);
            seat.setNumber(number);
            seat.setTickets(tickets);
            seat.setSector(sector);
            return seat;
        }
    }
}
