package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;


import java.math.BigDecimal;

public class MerchandiseFilterDto {


    private BigDecimal minPrice;
    private BigDecimal maxPrice;

    private Integer pointsPrice;

    private Integer pointsReward;

    private String title;


    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public BigDecimal getMaxPrice() {
        return maxPrice;
    }

    public Integer getPointsPrice() {
        return pointsPrice;
    }

    public Integer getPointsReward() {
        return pointsReward;
    }

    public String getTitle() {
        return title;
    }
}
