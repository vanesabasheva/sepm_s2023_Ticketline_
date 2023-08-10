package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import java.time.LocalDateTime;
import java.util.Map;

public class DetailedPerformanceDto {
    private Integer id;
    private LocalDateTime dateTime;
    private String hallName;
    private String eventName;
    private LocationDto location;
    private Map<Integer, DetailedPerformanceSectorDto> performanceSector;
    private PerformanceTicketDto[][] tickets;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getHallName() {
        return hallName;
    }

    public void setHallName(String hallName) {
        this.hallName = hallName;
    }

    public LocationDto getLocation() {
        return location;
    }

    public void setLocation(LocationDto location) {
        this.location = location;
    }

    public Map<Integer, DetailedPerformanceSectorDto> getPerformanceSector() {
        return performanceSector;
    }

    public void setPerformanceSector(Map<Integer, DetailedPerformanceSectorDto> performanceSector) {
        this.performanceSector = performanceSector;
    }

    public PerformanceTicketDto[][] getTickets() {
        return tickets;
    }

    public void setTickets(PerformanceTicketDto[][] tickets) {
        this.tickets = tickets;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }


    public static final class DetailedPerformanceDtoBuilder {
        private Integer id;
        private LocalDateTime timestamp;
        private String eventName;
        private String hallName;
        private LocationDto location;
        private Map<Integer, DetailedPerformanceSectorDto> performanceSector;

        private PerformanceTicketDto[][] performanceTickets;


        private DetailedPerformanceDtoBuilder() {
        }

        public static DetailedPerformanceDtoBuilder aDetailedPerformanceDto() {
            return new DetailedPerformanceDtoBuilder();
        }

        public DetailedPerformanceDtoBuilder withId(Integer id) {
            this.id = id;
            return this;
        }

        public DetailedPerformanceDtoBuilder withTimestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public DetailedPerformanceDtoBuilder withHallName(String hallName) {
            this.hallName = hallName;
            return this;
        }

        public DetailedPerformanceDtoBuilder withLocation(LocationDto location) {
            this.location = location;
            return this;
        }

        public DetailedPerformanceDtoBuilder withPerformanceSector(Map<Integer, DetailedPerformanceSectorDto> performanceSector) {
            this.performanceSector = performanceSector;
            return this;
        }

        public DetailedPerformanceDtoBuilder withPerformanceTickets(PerformanceTicketDto[][] seats) {
            this.performanceTickets = seats;
            return this;
        }

        public DetailedPerformanceDtoBuilder withEventName(String eventName) {
            this.eventName = eventName;
            return this;
        }

        public DetailedPerformanceDto build() {
            DetailedPerformanceDto detailedPerformanceDto = new DetailedPerformanceDto();
            detailedPerformanceDto.setId(id);
            detailedPerformanceDto.setDateTime(timestamp);
            detailedPerformanceDto.setEventName(eventName);
            detailedPerformanceDto.setHallName(hallName);
            detailedPerformanceDto.setLocation(location);
            detailedPerformanceDto.setPerformanceSector(performanceSector);
            detailedPerformanceDto.setTickets(performanceTickets);
            return detailedPerformanceDto;
        }
    }

}
