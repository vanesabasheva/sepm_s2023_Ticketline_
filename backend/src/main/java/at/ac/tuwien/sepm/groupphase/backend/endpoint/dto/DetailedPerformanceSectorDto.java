package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

public class DetailedPerformanceSectorDto {

    private Double price;
    private Integer pointsReward;
    private String name;
    private Boolean standing;


    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getPointsReward() {
        return pointsReward;
    }

    public void setPointsReward(Integer pointsReward) {
        this.pointsReward = pointsReward;
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

    public static final class DetailedPerformanceSectorDtoBuilder {

        private Double price;
        private Integer pointsReward;
        private String name;
        private Boolean standing;


        private DetailedPerformanceSectorDtoBuilder() {
        }

        public static DetailedPerformanceSectorDtoBuilder aPerformanceSectorDto() {
            return new DetailedPerformanceSectorDtoBuilder();
        }

        public DetailedPerformanceSectorDtoBuilder withPrice(Double price) {
            this.price = price;
            return this;
        }

        public DetailedPerformanceSectorDtoBuilder withPointsReward(Integer pointsReward) {
            this.pointsReward = pointsReward;
            return this;
        }

        public DetailedPerformanceSectorDtoBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public DetailedPerformanceSectorDtoBuilder withStanding(Boolean standing) {
            this.standing = standing;
            return this;
        }


        public DetailedPerformanceSectorDto build() {
            DetailedPerformanceSectorDto detailedPerformanceSectorDto = new DetailedPerformanceSectorDto();
            detailedPerformanceSectorDto.setPrice(price);
            detailedPerformanceSectorDto.setPointsReward(pointsReward);
            detailedPerformanceSectorDto.setName(name);
            detailedPerformanceSectorDto.setStanding(standing);
            return detailedPerformanceSectorDto;
        }
    }
}
