package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.createevent;

import java.util.List;

public class CreatePerformanceSectorDto {

    private Integer hallId;
    private Integer sectorId;

    private Double price;
    private String name;
    private Boolean standing;


    public Integer getHallId() {
        return hallId;
    }

    public void setHallId(Integer hallId) {
        this.hallId = hallId;
    }

    public Integer getSectorId() {
        return sectorId;
    }

    public void setSectorId(Integer sectorId) {
        this.sectorId = sectorId;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
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

    public List<String> validate() {
        List<String> errors = new java.util.ArrayList<>();
        if (price == null || price <= 0) {
            errors.add("Price must not be null and greater than 0");
        }
        if (name == null) {
            errors.add("Name must not be null");
        }
        if (standing == null) {
            errors.add("Standing must not be null");
        }

        return errors;
    }

    @Override
    public String toString() {
        return "CreatePerformanceSectorDto{"
            + "price=" + price
            + ", name='" + name + '\''
            + ", standing=" + standing
            + '}';
    }

    public static final class CreatePerformanceSectorDtoBuilder {
        private Integer hallId;
        private Integer sectorId;
        private Double price;
        private String name;
        private Boolean standing;

        private CreatePerformanceSectorDtoBuilder() {
        }

        public static CreatePerformanceSectorDtoBuilder aCreatePerformanceSectorDto() {
            return new CreatePerformanceSectorDtoBuilder();
        }

        public CreatePerformanceSectorDtoBuilder withHallId(Integer hallId) {
            this.hallId = hallId;
            return this;
        }

        public CreatePerformanceSectorDtoBuilder withSectorId(Integer sectorId) {
            this.sectorId = sectorId;
            return this;
        }

        public CreatePerformanceSectorDtoBuilder withPrice(Double price) {
            this.price = price;
            return this;
        }

        public CreatePerformanceSectorDtoBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public CreatePerformanceSectorDtoBuilder withStanding(Boolean standing) {
            this.standing = standing;
            return this;
        }

        public CreatePerformanceSectorDto build() {
            CreatePerformanceSectorDto createPerformanceSectorDto = new CreatePerformanceSectorDto();
            createPerformanceSectorDto.setHallId(hallId);
            createPerformanceSectorDto.setSectorId(sectorId);
            createPerformanceSectorDto.setPrice(price);
            createPerformanceSectorDto.setName(name);
            createPerformanceSectorDto.setStanding(standing);
            return createPerformanceSectorDto;
        }
    }
}
