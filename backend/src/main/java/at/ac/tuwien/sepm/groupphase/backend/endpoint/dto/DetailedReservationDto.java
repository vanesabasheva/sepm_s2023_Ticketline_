package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import java.time.LocalDateTime;

public class DetailedReservationDto {
    private Integer id;
    private LocalDateTime expirationTs;
    private Boolean cart;
    private TicketDto ticket;
    private String eventName;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getExpirationTs() {
        return expirationTs;
    }

    public void setExpirationTs(LocalDateTime expirationTs) {
        this.expirationTs = expirationTs;
    }

    public Boolean getCart() {
        return cart;
    }

    public void setCart(Boolean cart) {
        this.cart = cart;
    }

    public TicketDto getTicket() {
        return ticket;
    }

    public void setTicket(TicketDto ticket) {
        this.ticket = ticket;
    }


    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }
}
