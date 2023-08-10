package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.hall;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.LocationDto;

public class DetailedHallDto {
    private String name;
    private LocationDto location;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocationDto getLocation() {
        return location;
    }

    public void setLocation(LocationDto location) {
        this.location = location;
    }

    public static final class DetailedHallDtoBuilder {
        private String name;
        private LocationDto location;

        private DetailedHallDtoBuilder() {
        }

        public static DetailedHallDtoBuilder aDetailedHallDto() {
            return new DetailedHallDtoBuilder();
        }

        public DetailedHallDtoBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public DetailedHallDtoBuilder withLocation(LocationDto location) {
            this.location = location;
            return this;
        }

        public DetailedHallDto build() {
            DetailedHallDto detailedHallDto = new DetailedHallDto();
            detailedHallDto.setName(name);
            detailedHallDto.setLocation(location);
            return detailedHallDto;
        }
    }
}
