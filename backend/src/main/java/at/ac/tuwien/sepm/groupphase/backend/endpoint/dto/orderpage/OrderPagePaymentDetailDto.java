package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.orderpage;

public class OrderPagePaymentDetailDto {
    private Integer lastFourDigits;

    public Integer getLastFourDigits() {
        return lastFourDigits;
    }

    public void setLastFourDigits(Integer lastFourDigits) {
        this.lastFourDigits = lastFourDigits;
    }
}
