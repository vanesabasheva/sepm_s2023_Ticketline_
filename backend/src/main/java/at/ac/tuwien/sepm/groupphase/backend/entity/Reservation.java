package at.ac.tuwien.sepm.groupphase.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

import java.time.LocalDateTime;


@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private LocalDateTime expirationTs;

    @Column(nullable = false)
    private Boolean cart;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id", nullable = false, unique = true)
    private Ticket ticket;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private ApplicationUser user;


    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public LocalDateTime getExpirationTs() {
        return expirationTs;
    }

    public void setExpirationTs(final LocalDateTime expirationTs) {
        this.expirationTs = expirationTs;
    }

    public Boolean getCart() {
        return cart;
    }

    public void setCart(final Boolean cart) {
        this.cart = cart;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(final Ticket ticket) {
        this.ticket = ticket;
    }

    public ApplicationUser getUser() {
        return user;
    }

    public void setUser(final ApplicationUser user) {
        this.user = user;
    }


    public static final class ReservationBuilder {
        private Integer id;
        private LocalDateTime expirationTs;
        private Boolean cart;
        private Ticket ticket;
        private ApplicationUser user;

        private ReservationBuilder() {
        }

        public static ReservationBuilder aReservation() {
            return new ReservationBuilder();
        }

        public ReservationBuilder withId(Integer id) {
            this.id = id;
            return this;
        }

        public ReservationBuilder withExpirationTs(LocalDateTime expirationTs) {
            this.expirationTs = expirationTs;
            return this;
        }

        public ReservationBuilder withCart(Boolean cart) {
            this.cart = cart;
            return this;
        }

        public ReservationBuilder withTicket(Ticket ticket) {
            this.ticket = ticket;
            return this;
        }

        public ReservationBuilder withUser(ApplicationUser user) {
            this.user = user;
            return this;
        }

        public Reservation build() {
            Reservation reservation = new Reservation();
            reservation.setId(id);
            reservation.setExpirationTs(expirationTs);
            reservation.setCart(cart);
            reservation.setTicket(ticket);
            reservation.setUser(user);
            return reservation;
        }
    }
}
