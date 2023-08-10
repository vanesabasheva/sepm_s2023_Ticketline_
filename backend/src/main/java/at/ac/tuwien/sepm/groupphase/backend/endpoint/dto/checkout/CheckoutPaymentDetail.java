package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.checkout;

import java.time.LocalDate;

public class CheckoutPaymentDetail {
    private Integer paymentDetailId;
    private Integer lastFourDigits;
    private String cardHolder;
    private LocalDate expirationDate;

    public Integer getPaymentDetailId() {
        return paymentDetailId;
    }

    public void setPaymentDetailId(Integer paymentDetailId) {
        this.paymentDetailId = paymentDetailId;
    }

    public Integer getLastFourDigits() {
        return lastFourDigits;
    }

    public void setLastFourDigits(Integer lastFourDigits) {
        this.lastFourDigits = lastFourDigits;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }
}
