package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.search;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.LocationDto;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

public class PerformanceSearchDto {
    private Integer id;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime dateTime;
    private String hallName;
    private String eventName;
    private String price;
    private LocationDto location;
    private String imagePath;

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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public static final class PerformanceSearchDtoBuilder {
        private Integer id;
        private LocalDateTime timestamp;
        private String eventName;
        private String hallName;
        private LocationDto location;
        private String price;
        private String imagePath;

        private PerformanceSearchDtoBuilder() {
        }

        public static PerformanceSearchDtoBuilder aPerformanceSearchDtoBuilder() {
            return new PerformanceSearchDtoBuilder();
        }

        public PerformanceSearchDto.PerformanceSearchDtoBuilder withId(Integer id) {
            this.id = id;
            return this;
        }

        public PerformanceSearchDto.PerformanceSearchDtoBuilder withTimestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public PerformanceSearchDto.PerformanceSearchDtoBuilder withHallName(String hallName) {
            this.hallName = hallName;
            return this;
        }

        public PerformanceSearchDto.PerformanceSearchDtoBuilder withLocation(LocationDto location) {
            this.location = location;
            return this;
        }

        public PerformanceSearchDto.PerformanceSearchDtoBuilder withPrice(String price) {
            this.price = price;
            return this;
        }

        public PerformanceSearchDto.PerformanceSearchDtoBuilder withEventName(String eventName) {
            this.eventName = eventName;
            return this;
        }

        public PerformanceSearchDto.PerformanceSearchDtoBuilder withImagePath(String imagePath) {
            this.imagePath = imagePath;
            return this;
        }

        public PerformanceSearchDto build() {
            PerformanceSearchDto performanceSearchDto = new PerformanceSearchDto();
            performanceSearchDto.setId(id);
            performanceSearchDto.setDateTime(timestamp);
            performanceSearchDto.setEventName(eventName);
            performanceSearchDto.setHallName(hallName);
            performanceSearchDto.setLocation(location);
            performanceSearchDto.setPrice(price);
            performanceSearchDto.setImagePath(imagePath);
            return performanceSearchDto;
        }

    }
}
