package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.orderpage;

import java.math.BigDecimal;

public class OrderPageTransactionDto {
    private Integer id;
    private BigDecimal deductedAmount;
    private Integer deductedPoints;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getDeductedAmount() {
        return deductedAmount;
    }

    public void setDeductedAmount(BigDecimal deductedAmount) {
        this.deductedAmount = deductedAmount;
    }

    public Integer getDeductedPoints() {
        return deductedPoints;
    }

    public void setDeductedPoints(Integer deductedPoints) {
        this.deductedPoints = deductedPoints;
    }
}
