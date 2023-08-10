package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import java.time.Duration;

public class DetailedEventDto {
    private Integer id;
    private String name;
    private String type;
    private Duration length;
    private String description;
    private String artists;

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

    public Duration getLength() {
        return length;
    }

    public void setLength(Duration length) {
        this.length = length;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getArtists() {
        return artists;
    }

    public void setArtists(String artists) {
        this.artists = artists;
    }

    public static final class DetailedEventDtoBuilder {
        private Integer id;
        private String name;
        private String type;
        private Duration length;
        private String description;
        private String artists;

        private DetailedEventDtoBuilder() {
        }

        public static DetailedEventDtoBuilder aDetailedEventDto() {
            return new DetailedEventDtoBuilder();
        }

        public DetailedEventDtoBuilder withId(Integer id) {
            this.id = id;
            return this;
        }

        public DetailedEventDtoBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public DetailedEventDtoBuilder withType(String type) {
            this.type = type;
            return this;
        }

        public DetailedEventDtoBuilder withLength(Duration length) {
            this.length = length;
            return this;
        }

        public DetailedEventDtoBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public DetailedEventDtoBuilder withArtists(String artists) {
            this.artists = artists;
            return this;
        }

        public DetailedEventDto build() {
            DetailedEventDto detailedEventDto = new DetailedEventDto();
            detailedEventDto.setId(id);
            detailedEventDto.setName(name);
            detailedEventDto.setType(type);
            detailedEventDto.setLength(length);
            detailedEventDto.setDescription(description);
            detailedEventDto.setArtists(artists);
            return detailedEventDto;
        }
    }
}
