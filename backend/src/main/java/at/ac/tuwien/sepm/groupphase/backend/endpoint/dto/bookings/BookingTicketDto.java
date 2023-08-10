package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.bookings;

import jakarta.validation.constraints.NotNull;

public class BookingTicketDto {
    @NotNull
    private Integer ticketId;
    @NotNull
    private Boolean reservation;

    public Integer getTicketId() {
        return ticketId;
    }

    public Boolean getReservation() {
        return reservation;
    }

    public void setTicketId(Integer ticketId) {
        this.ticketId = ticketId;
    }

    public void setReservation(Boolean reservation) {
        this.reservation = reservation;
    }
}
