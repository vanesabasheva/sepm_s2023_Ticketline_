package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.hall.DetailedHallDto;

import java.time.LocalDateTime;

public class DetailedOrderPerformanceDto {

    private Integer id;
    private LocalDateTime datetime;
    private DetailedEventDto event;
    private DetailedHallDto hall;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getDatetime() {
        return datetime;
    }

    public void setDatetime(LocalDateTime datetime) {
        this.datetime = datetime;
    }

    public DetailedEventDto getEvent() {
        return event;
    }

    public void setEvent(DetailedEventDto event) {
        this.event = event;
    }

    public DetailedHallDto getHall() {
        return hall;
    }

    public void setHall(DetailedHallDto hall) {
        this.hall = hall;
    }

    public static final class DetailedOrderPerformanceDtoBuilder {
        private Integer id;
        private LocalDateTime datetime;
        private DetailedEventDto event;
        private DetailedHallDto hall;

        private DetailedOrderPerformanceDtoBuilder() {
        }

        public static DetailedOrderPerformanceDtoBuilder aDetailedOrderPerformanceDto() {
            return new DetailedOrderPerformanceDtoBuilder();
        }

        public DetailedOrderPerformanceDtoBuilder withId(Integer id) {
            this.id = id;
            return this;
        }

        public DetailedOrderPerformanceDtoBuilder withDatetime(LocalDateTime datetime) {
            this.datetime = datetime;
            return this;
        }

        public DetailedOrderPerformanceDtoBuilder withEvent(DetailedEventDto event) {
            this.event = event;
            return this;
        }

        public DetailedOrderPerformanceDtoBuilder withHall(DetailedHallDto hall) {
            this.hall = hall;
            return this;
        }


        public DetailedOrderPerformanceDto build() {
            DetailedOrderPerformanceDto detailedOrderPerformanceDto = new DetailedOrderPerformanceDto();
            detailedOrderPerformanceDto.setId(id);
            detailedOrderPerformanceDto.setDatetime(datetime);
            detailedOrderPerformanceDto.setEvent(event);
            detailedOrderPerformanceDto.setHall(hall);
            return detailedOrderPerformanceDto;
        }
    }
}
