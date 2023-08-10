package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import java.time.LocalDateTime;
import java.util.Set;

public class DetailedOrderDto {
    private Integer id;
    private LocalDateTime orderTs;
    private Boolean cancelled;
    private Set<DetailedTicketDto> tickets;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getOrderTs() {
        return orderTs;
    }

    public void setOrderTs(LocalDateTime orderTs) {
        this.orderTs = orderTs;
    }

    public Boolean getCancelled() {
        return cancelled;
    }

    public void setCancelled(Boolean cancelled) {
        this.cancelled = cancelled;
    }

    public Set<DetailedTicketDto> getTickets() {
        return tickets;
    }

    public void setTickets(Set<DetailedTicketDto> tickets) {
        this.tickets = tickets;
    }

    public static final class DetailedOrderDtoBuilder {
        private Integer id;
        private LocalDateTime orderTs;
        private Boolean cancelled;
        private Set<DetailedTicketDto> tickets;

        private DetailedOrderDtoBuilder() {}

        public static DetailedOrderDtoBuilder aDetailedOrderDto() {
            return new DetailedOrderDtoBuilder();
        }

        public DetailedOrderDtoBuilder withId(Integer id) {
            this.id = id;
            return this;
        }

        public DetailedOrderDtoBuilder withOrderTs(LocalDateTime orderTs) {
            this.orderTs = orderTs;
            return this;
        }

        public DetailedOrderDtoBuilder withCancelled(Boolean cancelled) {
            this.cancelled = cancelled;
            return this;
        }

        public DetailedOrderDtoBuilder withTickets(Set<DetailedTicketDto> tickets) {
            this.tickets = tickets;
            return this;
        }

        public DetailedOrderDto build() {
            DetailedOrderDto detailedOrderDto = new DetailedOrderDto();
            detailedOrderDto.setId(id);
            detailedOrderDto.setOrderTs(orderTs);
            detailedOrderDto.setCancelled(cancelled);
            detailedOrderDto.setTickets(tickets);
            return detailedOrderDto;
        }
    }
}
