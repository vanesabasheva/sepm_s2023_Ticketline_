package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.createevent;

public class SectorDto {
    private Integer sectorId;
    private String name;
    private Boolean standing;
    private Integer hallId;


    public Integer getSectorId() {
        return sectorId;
    }

    public void setSectorId(Integer sectorId) {
        this.sectorId = sectorId;
    }

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

    public Integer getHallId() {
        return hallId;
    }

    public void setHallId(Integer hallId) {
        this.hallId = hallId;
    }

    public static final class SectorDtoBuilder {
        private Integer sectorId;
        private String name;
        private Boolean standing;
        private Integer hallId;

        private SectorDtoBuilder() {
        }

        public static SectorDtoBuilder aSectorDto() {
            return new SectorDtoBuilder();
        }

        public SectorDtoBuilder withSectorId(Integer sectorId) {
            this.sectorId = sectorId;
            return this;
        }

        public SectorDtoBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public SectorDtoBuilder withStanding(Boolean standing) {
            this.standing = standing;
            return this;
        }

        public SectorDtoBuilder withHallId(Integer hallId) {
            this.hallId = hallId;
            return this;
        }

        public SectorDto build() {
            SectorDto sectorDto = new SectorDto();
            sectorDto.setSectorId(sectorId);
            sectorDto.setName(name);
            sectorDto.setStanding(standing);
            sectorDto.setHallId(hallId);
            return sectorDto;
        }
    }
}
