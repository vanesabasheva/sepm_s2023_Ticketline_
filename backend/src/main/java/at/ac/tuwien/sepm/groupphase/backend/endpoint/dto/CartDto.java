package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import java.util.List;

public class CartDto {
    private Integer userId;
    private int userPoints;
    private List<CartTicketDto> tickets;


    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public List<CartTicketDto> getTickets() {
        return tickets;
    }

    public void setTickets(List<CartTicketDto> tickets) {
        this.tickets = tickets;
    }

    public void setUserPoints(int userPoints) {
        this.userPoints = userPoints;
    }

    public int getUserPoints() {
        return userPoints;
    }

    public static final class CartDtoBuilder {
        private Integer userId;
        private int userPoints;
        private List<CartTicketDto> tickets;

        private CartDtoBuilder() {
        }

        public static CartDto.CartDtoBuilder aCartDto() {
            return new CartDto.CartDtoBuilder();
        }

        public CartDto.CartDtoBuilder withUserId(Integer userId) {
            this.userId = userId;
            return this;
        }

        public CartDto.CartDtoBuilder withTickets(List<CartTicketDto> tickets) {
            this.tickets = tickets;
            return this;
        }

        public CartDto.CartDtoBuilder withUserPoints(int userPoints) {
            this.userPoints = userPoints;
            return this;
        }

        public CartDto build() {
            CartDto cartDto = new CartDto();
            cartDto.setUserId(userId);
            cartDto.setUserPoints(userPoints);
            cartDto.setTickets(tickets);
            return cartDto;
        }
    }
}
