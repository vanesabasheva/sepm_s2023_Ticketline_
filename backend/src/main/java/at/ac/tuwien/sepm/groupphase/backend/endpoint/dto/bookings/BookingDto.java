package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.bookings;

import jakarta.validation.Valid;

import java.util.List;

public class BookingDto {
    @Valid
    private List<BookingTicketDto> tickets;
    @Valid
    private List<BookingMerchandiseDto> merchandise;
    private Integer locationId;
    private Integer paymentDetailId;

    public List<BookingTicketDto> getTickets() {
        return tickets;
    }

    public void setTickets(List<BookingTicketDto> tickets) {
        this.tickets = tickets;
    }

    public List<BookingMerchandiseDto> getMerchandise() {
        return merchandise;
    }

    public void setMerchandise(List<BookingMerchandiseDto> merchandise) {
        this.merchandise = merchandise;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public Integer getPaymentDetailId() {
        return paymentDetailId;
    }

    public void setPaymentDetailId(Integer paymentDetailId) {
        this.paymentDetailId = paymentDetailId;
    }
}
