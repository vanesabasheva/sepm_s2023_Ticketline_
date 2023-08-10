package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.orderpage;

import java.math.BigDecimal;

public class OrderPageMerchandiseDto {
    private Integer id;
    private Integer quantity;
    private BigDecimal price;
    private Integer pointsPrice;
    private boolean points;
    private String title;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getPointsPrice() {
        return pointsPrice;
    }

    public void setPointsPrice(Integer pointsPrice) {
        this.pointsPrice = pointsPrice;
    }

    public boolean isPoints() {
        return points;
    }

    public void setPoints(boolean points) {
        this.points = points;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
