package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

public class DetailedSeatDto {
    private Integer row;
    private Integer number;
    private DetailedSectorDto sector;

    public Integer getRow() {
        return row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public DetailedSectorDto getSector() {
        return sector;
    }

    public void setSector(DetailedSectorDto sector) {
        this.sector = sector;
    }

    public static final class DetailedSeatDtoBuilder {
        private Integer row;
        private Integer number;
        private DetailedSectorDto sector;

        private DetailedSeatDtoBuilder() {}

        public static DetailedSeatDtoBuilder aDetailedSeatDto() {
            return new DetailedSeatDtoBuilder();
        }

        public DetailedSeatDtoBuilder withRow(Integer row) {
            this.row = row;
            return this;
        }

        public DetailedSeatDtoBuilder withNumber(Integer number) {
            this.number = number;
            return this;
        }

        public DetailedSeatDtoBuilder withSector(DetailedSectorDto sector) {
            this.sector = sector;
            return this;
        }

        public DetailedSeatDto build() {
            DetailedSeatDto detailedSeatDto = new DetailedSeatDto();
            detailedSeatDto.setRow(row);
            detailedSeatDto.setNumber(number);
            detailedSeatDto.setSector(sector);
            return detailedSeatDto;
        }
    }
}
