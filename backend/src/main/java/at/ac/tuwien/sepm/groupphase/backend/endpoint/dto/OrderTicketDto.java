package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderTicketDto {
    private Integer id;
    private BigDecimal price;
    private String artists;
    private String eventName;
    private LocalDateTime datetime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getArtists() {
        return artists;
    }

    public void setArtists(String artists) {
        this.artists = artists;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public LocalDateTime getDatetime() {
        return datetime;
    }

    public void setDatetime(LocalDateTime datetime) {
        this.datetime = datetime;
    }

    public static final class OrderTicketDtoBuilder {
        private Integer id;
        private BigDecimal price;
        private String artists;
        private String eventName;
        private LocalDateTime datetime;

        private OrderTicketDtoBuilder() {
        }

        public static OrderTicketDto.OrderTicketDtoBuilder aOrderTicketDto() {
            return new OrderTicketDto.OrderTicketDtoBuilder();
        }

        public OrderTicketDto.OrderTicketDtoBuilder withId(Integer id) {
            this.id = id;
            return this;
        }

        public OrderTicketDto.OrderTicketDtoBuilder withPrice(BigDecimal price) {
            this.price = price;
            return this;
        }

        public OrderTicketDto.OrderTicketDtoBuilder withArtists(String artists) {
            this.artists = artists;
            return this;
        }

        public OrderTicketDto.OrderTicketDtoBuilder withEventName(String eventName) {
            this.eventName = eventName;
            return this;
        }

        public OrderTicketDto.OrderTicketDtoBuilder withDatetime(LocalDateTime datetime) {
            this.datetime = datetime;
            return this;
        }

        public OrderTicketDto build() {
            OrderTicketDto orderTicketDto = new OrderTicketDto();
            orderTicketDto.setId(id);
            orderTicketDto.setPrice(price);
            orderTicketDto.setArtists(artists);
            orderTicketDto.setEventName(eventName);
            orderTicketDto.setDatetime(datetime);
            return orderTicketDto;
        }
    }

}
