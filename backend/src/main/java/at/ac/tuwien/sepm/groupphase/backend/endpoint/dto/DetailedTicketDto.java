package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import java.time.LocalDateTime;

public class DetailedTicketDto {
    private Integer id;
    private DetailedOrderPerformanceDto performance;
    private DetailedSeatDto seat;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public DetailedOrderPerformanceDto getPerformance() {
        return performance;
    }

    public void setPerformance(DetailedOrderPerformanceDto performance) {
        this.performance = performance;
    }

    public DetailedSeatDto getSeat() {
        return seat;
    }

    public void setSeat(DetailedSeatDto seat) {
        this.seat = seat;
    }

    public static final class DetailedTicketDtoBuilder {
        private Integer id;
        private DetailedOrderPerformanceDto performance;
        private DetailedSeatDto seat;

        private DetailedTicketDtoBuilder() {}

        public static DetailedTicketDtoBuilder aDetailedTicketDto() {
            return new DetailedTicketDtoBuilder();
        }

        public DetailedTicketDtoBuilder withId(Integer id) {
            this.id = id;
            return this;
        }

        public DetailedTicketDtoBuilder withPerformance(DetailedOrderPerformanceDto performance) {
            this.performance = performance;
            return this;
        }

        public DetailedTicketDtoBuilder withSeat(DetailedSeatDto seat) {
            this.seat = seat;
            return this;
        }

        public DetailedTicketDto build() {
            DetailedTicketDto detailedTicketDto = new DetailedTicketDto();
            detailedTicketDto.setId(id);
            detailedTicketDto.setPerformance(performance);
            detailedTicketDto.setSeat(seat);
            return detailedTicketDto;
        }
    }
}
