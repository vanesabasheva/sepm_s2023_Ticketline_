package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.search;

import jakarta.validation.constraints.Size;

public class EventSearchDto {
    private Integer id;
    private String name;
    private String type;
    private String length;
    private String description;
    private String artists;
    @Size(max = 200)
    private String imagePath;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
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

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
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

    public static final class EventSearchDtoBuilder {
        private Integer id;
        private String name;
        private String type;
        private String length;
        private String description;
        private String imagePath;
        private String artists;

        private EventSearchDtoBuilder() {
        }

        public static EventSearchDto.EventSearchDtoBuilder aEventSearchDto() {
            return new EventSearchDto.EventSearchDtoBuilder();
        }

        public EventSearchDto.EventSearchDtoBuilder withId(Integer id) {
            this.id = id;
            return this;
        }

        public EventSearchDto.EventSearchDtoBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public EventSearchDto.EventSearchDtoBuilder withType(String type) {
            this.type = type;
            return this;
        }

        public EventSearchDto.EventSearchDtoBuilder withLength(String length) {
            this.length = length;
            return this;
        }

        public EventSearchDto.EventSearchDtoBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public EventSearchDto.EventSearchDtoBuilder withArtists(String artists) {
            this.artists = artists;
            return this;
        }

        public EventSearchDto.EventSearchDtoBuilder withImagePath(String imagePath) {
            this.imagePath = imagePath;
            return this;
        }

        public EventSearchDto build() {
            EventSearchDto eventSearchDto = new EventSearchDto();
            eventSearchDto.setId(id);
            eventSearchDto.setName(name);
            eventSearchDto.setType(type);
            eventSearchDto.setLength(length);
            eventSearchDto.setDescription(description);
            eventSearchDto.setArtists(artists);
            eventSearchDto.setImagePath(imagePath);
            return eventSearchDto;
        }
    }
}
