package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.topten;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class EventTicketCountDto {

    @NotNull(message = "Event must have an id")
    private Integer id;
    @NotNull(message = "Event must have a name")
    private String name;
    @NotNull(message = "Event must have a type")
    private String type;
    @Size(min = 0, message = "Event must have a positive ticketCount")
    private Long ticketCount;

    private String imagePath;

    public EventTicketCountDto() {
    }

    // needed for custom query of EventRepository.findTopTenEventsByTicketCount()
    public EventTicketCountDto(Integer id, String name, String type, Long ticketCount, String imagePath) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.ticketCount = ticketCount;
        this.imagePath = imagePath;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getTicketCount() {
        return ticketCount;
    }

    public void setTicketCount(Long ticketCount) {
        this.ticketCount = ticketCount;
    }


    public static final class EventTicketCountDtoBuilder {
        private Integer id;
        private String name;
        private String type;
        private Long ticketCount;
        private String imagePath;

        private EventTicketCountDtoBuilder() {
        }


        public static EventTicketCountDtoBuilder aEventTicketCountDto() {
            return new EventTicketCountDtoBuilder();
        }

        public EventTicketCountDtoBuilder withId(Integer id) {
            this.id = id;
            return this;
        }

        public EventTicketCountDtoBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public EventTicketCountDtoBuilder withType(String type) {
            this.type = type;
            return this;
        }

        public EventTicketCountDtoBuilder withTicketCount(Long ticketCount) {
            this.ticketCount = ticketCount;
            return this;
        }

        public EventTicketCountDtoBuilder withImagePath(String imagePath) {
            this.imagePath = imagePath;
            return this;
        }

        public EventTicketCountDto build() {
            EventTicketCountDto eventTicketCountDto = new EventTicketCountDto();
            eventTicketCountDto.setId(id);
            eventTicketCountDto.setName(name);
            eventTicketCountDto.setType(type);
            eventTicketCountDto.setTicketCount(ticketCount);
            eventTicketCountDto.setImagePath(imagePath);
            return eventTicketCountDto;
        }
    }
}
