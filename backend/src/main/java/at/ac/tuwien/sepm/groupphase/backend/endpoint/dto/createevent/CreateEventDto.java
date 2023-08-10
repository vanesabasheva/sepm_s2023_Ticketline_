package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.createevent;

import java.util.List;

public class CreateEventDto {

    private String name;
    private String type;
    private Integer length;
    private String description;
    private String artists;
    private String imagePath;


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

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
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

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public List<String> validate() {
        List<String> errors = new java.util.ArrayList<>();
        if (name == null) {
            errors.add("Name must not be null");
        }
        if (type == null) {
            errors.add("Type must not be null");
        }
        if (length == null) {
            errors.add("Length must not be null");
        } else if (length <= 0) {
            errors.add("Length must be greater than 0");
        }

        if (description == null || description.isBlank() || description.isEmpty()) {
            errors.add("Description must not be null");
        }
        if (artists == null) {
            errors.add("Artists must not be null");
        } else {
            String regex = "^([A-Za-z0-9 ]+)(?:;[A-Za-z0-9 ]+)*$";
            if (!artists.matches(regex)) {
                errors.add("Artists string is not correctly formatted");
            }
        }
        //create regex "Artist 1; Artist 2; Artist 3"
        if (imagePath == null) {
            errors.add("ImagePath must not be null");
        }

        return errors;
    }

    @Override
    public String toString() {
        return "CreateEventDto{"
            + "name='" + name + '\''
            + ", type='" + type + '\''
            + ", length=" + length
            + ", description='" + description
            + '\'' + ", artists='" + artists + '\''
            + ", imagePath='" + imagePath + '\''
            + '}';
    }

    public static final class CreateEventDtoBuilder {
        private String name;
        private String type;
        private Integer length;
        private String description;
        private String artists;
        private String imagePath;

        private CreateEventDtoBuilder() {
        }

        public static CreateEventDtoBuilder aCreateEventDto() {
            return new CreateEventDtoBuilder();
        }

        public CreateEventDtoBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public CreateEventDtoBuilder withType(String type) {
            this.type = type;
            return this;
        }

        public CreateEventDtoBuilder withLength(Integer length) {
            this.length = length;
            return this;
        }

        public CreateEventDtoBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public CreateEventDtoBuilder withArtists(String artists) {
            this.artists = artists;
            return this;
        }

        public CreateEventDtoBuilder withImagePath(String imagePath) {
            this.imagePath = imagePath;
            return this;
        }

        public CreateEventDto build() {
            CreateEventDto createEventDto = new CreateEventDto();
            createEventDto.setName(name);
            createEventDto.setType(type);
            createEventDto.setLength(length);
            createEventDto.setDescription(description);
            createEventDto.setArtists(artists);
            createEventDto.setImagePath(imagePath);
            return createEventDto;
        }
    }
}
