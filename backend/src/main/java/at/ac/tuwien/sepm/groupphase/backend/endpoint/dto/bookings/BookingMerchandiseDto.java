package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.bookings;

import jakarta.validation.constraints.NotNull;

public class BookingMerchandiseDto {
    @NotNull
    private Integer id;
    @NotNull
    private Integer quantity;
    @NotNull
    private Boolean buyWithPoints;

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

    public Boolean getBuyWithPoints() {
        return buyWithPoints;
    }

    public void setBuyWithPoints(Boolean buyWithPoints) {
        this.buyWithPoints = buyWithPoints;
    }

    @Override
    public String toString() {
        return "BookingMerchandiseDto{"
            + "merchandiseId=" + id
            + ", quantity=" + quantity
            + '}';
    }
}
