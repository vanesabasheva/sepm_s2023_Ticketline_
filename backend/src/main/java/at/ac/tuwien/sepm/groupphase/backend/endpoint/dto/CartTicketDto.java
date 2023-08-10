package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;


import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CartTicketDto {

    private Integer id;
    private int seatRow;
    private int seatNumber;
    private String sectorName;
    private boolean standing;
    private LocalDateTime date;
    private String eventName;
    private String hallName;
    private String locationCity;
    private String locationStreet;
    private BigDecimal price;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getSeatRow() {
        return seatRow;
    }

    public void setSeatRow(int seatRow) {
        this.seatRow = seatRow;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getSectorName() {
        return sectorName;
    }

    public void setSectorName(String sectorName) {
        this.sectorName = sectorName;
    }

    public boolean isStanding() {
        return standing;
    }

    public void setStanding(boolean standing) {
        this.standing = standing;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getHallName() {
        return hallName;
    }

    public void setHallName(String hallName) {
        this.hallName = hallName;
    }

    public String getLocationCity() {
        return locationCity;
    }

    public void setLocationCity(String locationCity) {
        this.locationCity = locationCity;
    }

    public String getLocationStreet() {
        return locationStreet;
    }

    public void setLocationStreet(String locationStreet) {
        this.locationStreet = locationStreet;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }


    public static final class CartTicketDtoBuilder {
        private Integer id;
        private int seatRow;
        private int seatNumber;
        private String sectorName;
        private boolean standing;
        private LocalDateTime date;
        private String eventName;
        private String hallName;
        private String locationCity;
        private String locationStreet;
        private BigDecimal price;

        private CartTicketDtoBuilder() {
        }

        public static CartTicketDto.CartTicketDtoBuilder aCartTicketDto() {
            return new CartTicketDto.CartTicketDtoBuilder();
        }

        public CartTicketDto.CartTicketDtoBuilder withId(Integer id) {
            this.id = id;
            return this;
        }

        public CartTicketDto.CartTicketDtoBuilder withSeatRow(int seatRow) {
            this.seatRow = seatRow;
            return this;
        }

        public CartTicketDto.CartTicketDtoBuilder withSeatNumber(int seatNumber) {
            this.seatNumber = seatNumber;
            return this;
        }

        public CartTicketDto.CartTicketDtoBuilder withSectorName(String sectorName) {
            this.sectorName = sectorName;
            return this;
        }

        public CartTicketDto.CartTicketDtoBuilder withStanding(boolean standing) {
            this.standing = standing;
            return this;
        }

        public CartTicketDto.CartTicketDtoBuilder withDate(LocalDateTime date) {
            this.date = date;
            return this;
        }

        public CartTicketDto.CartTicketDtoBuilder withEventName(String eventName) {
            this.eventName = eventName;
            return this;
        }

        public CartTicketDto.CartTicketDtoBuilder withHallName(String hallName) {
            this.hallName = hallName;
            return this;
        }

        public CartTicketDto.CartTicketDtoBuilder withLocationCity(String locationCity) {
            this.locationCity = locationCity;
            return this;
        }

        public CartTicketDto.CartTicketDtoBuilder withLocationStreet(String locationStreet) {
            this.locationStreet = locationStreet;
            return this;
        }

        public CartTicketDto.CartTicketDtoBuilder withPrice(BigDecimal price) {
            this.price = price;
            return this;
        }


        public CartTicketDto build() {
            CartTicketDto cartTicketDto = new CartTicketDto();
            cartTicketDto.setId(id);
            cartTicketDto.setSeatRow(seatRow);
            cartTicketDto.setSeatNumber(seatNumber);
            cartTicketDto.setSectorName(sectorName);
            cartTicketDto.setStanding(standing);
            cartTicketDto.setDate(date);
            cartTicketDto.setEventName(eventName);
            cartTicketDto.setHallName(hallName);
            cartTicketDto.setLocationCity(locationCity);
            cartTicketDto.setLocationStreet(locationStreet);
            cartTicketDto.setPrice(price);
            return cartTicketDto;
        }
    }
}
