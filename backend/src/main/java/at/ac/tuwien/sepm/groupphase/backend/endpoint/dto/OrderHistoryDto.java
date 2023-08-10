package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class OrderHistoryDto {
    private Integer id;
    private LocalDateTime orderTs;
    private Boolean cancelled;
    private List<OrderTicketDto> tickets;
    private List<OrderMerchandiseDto> merchandises;
    private BigDecimal totalPrice;
    private Integer totalPoints;

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

    public List<OrderTicketDto> getTickets() {
        return tickets;
    }

    public void setTickets(List<OrderTicketDto> tickets) {
        this.tickets = tickets;
    }

    public List<OrderMerchandiseDto> getMerchandises() {
        return merchandises;
    }

    public void setMerchandises(List<OrderMerchandiseDto> merchandises) {
        this.merchandises = merchandises;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(Integer totalPoints) {
        this.totalPoints = totalPoints;
    }

    public static final class OrderHistoryDtoBuilder {
        private Integer id;
        private LocalDateTime orderTs;
        private Boolean cancelled;
        private List<OrderTicketDto> tickets;
        private List<OrderMerchandiseDto> merchandises;
        private BigDecimal totalPrice;
        private Integer totalPoints;

        private OrderHistoryDtoBuilder() {
        }

        public static OrderHistoryDtoBuilder aOrderHistoryDto() {
            return new OrderHistoryDtoBuilder();
        }

        public OrderHistoryDtoBuilder withId(Integer id) {
            this.id = id;
            return this;
        }

        public OrderHistoryDtoBuilder withOrderTs(LocalDateTime orderTs) {
            this.orderTs = orderTs;
            return this;
        }

        public OrderHistoryDtoBuilder withCancelled(Boolean cancelled) {
            this.cancelled = cancelled;
            return this;
        }

        public OrderHistoryDtoBuilder withTickets(List<OrderTicketDto> tickets) {
            this.tickets = tickets;
            return this;
        }

        public OrderHistoryDtoBuilder withMerchandises(List<OrderMerchandiseDto> merchandises) {
            this.merchandises = merchandises;
            return this;
        }

        public OrderHistoryDtoBuilder withTotalPrice(BigDecimal totalPrice) {
            this.totalPrice = totalPrice;
            return this;
        }

        public OrderHistoryDtoBuilder withTotalPoints(Integer totalPoints) {
            this.totalPoints = totalPoints;
            return this;
        }


        public OrderHistoryDto build() {
            OrderHistoryDto orderHistoryDto = new OrderHistoryDto();
            orderHistoryDto.setId(id);
            orderHistoryDto.setOrderTs(orderTs);
            orderHistoryDto.setCancelled(cancelled);
            orderHistoryDto.setTickets(tickets);
            orderHistoryDto.setMerchandises(merchandises);
            orderHistoryDto.setTotalPrice(totalPrice);
            orderHistoryDto.setTotalPoints(totalPoints);
            return orderHistoryDto;
        }
    }
}
