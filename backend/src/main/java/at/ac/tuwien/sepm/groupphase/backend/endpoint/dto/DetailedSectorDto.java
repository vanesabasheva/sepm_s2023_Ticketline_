package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.hall.DetailedHallDto;

public class DetailedSectorDto {
    private String name;
    private Boolean standing;
    private DetailedHallDto hall;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getStanding() {
        return standing;
    }

    public void setStanding(Boolean standing) {
        this.standing = standing;
    }

    public DetailedHallDto getHall() {
        return hall;
    }

    public void setHall(DetailedHallDto hall) {
        this.hall = hall;
    }

    public static final class DetailedSectorDtoBuilder {
        private String name;
        private Boolean standing;
        private DetailedHallDto hall;

        private DetailedSectorDtoBuilder() {
        }

        public static DetailedSectorDtoBuilder aDetailedSectorDto() {
            return new DetailedSectorDtoBuilder();
        }

        public DetailedSectorDtoBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public DetailedSectorDtoBuilder withStanding(Boolean standing) {
            this.standing = standing;
            return this;
        }

        public DetailedSectorDtoBuilder withHall(DetailedHallDto hall) {
            this.hall = hall;
            return this;
        }

        public DetailedSectorDto build() {
            DetailedSectorDto detailedSectorDto = new DetailedSectorDto();
            detailedSectorDto.setName(name);
            detailedSectorDto.setStanding(standing);
            detailedSectorDto.setHall(hall);
            return detailedSectorDto;
        }
    }
}
