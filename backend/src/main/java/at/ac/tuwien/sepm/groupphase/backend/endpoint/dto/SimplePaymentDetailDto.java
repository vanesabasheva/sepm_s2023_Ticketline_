package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SimplePaymentDetailDto {
    private Integer id;
    private String cardNumber;
    private String cardHolder;
    private Integer cvv;
    private LocalDate expirationDate;
    private Integer user;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public Integer getCvv() {
        return cvv;
    }

    public void setCvv(Integer cvv) {
        this.cvv = cvv;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Integer getUserId() {
        return user;
    }

    public void setUserId(Integer userId) {
        this.user = userId;
    }

    public List<String> validate() {
        List<String> errors = new ArrayList<>();
        if (cardNumber == null || cardHolder == null || cardHolder.isBlank() || cvv == null || expirationDate == null || user == null) {
            errors.add("All fields must be filled");
        }
        if (cardNumber != null && !cardNumber.isBlank()) {
            if (cardNumber.length() != 16) {
                errors.add("Card number must be 16 digits long");
            }

            if (!cardNumber.matches("^\\d+$")) {
                errors.add("Card number must be numeric");
            }
        }
        if (cvv != null && (cvv.toString().length() != 3)) {
            errors.add("CVV must be 3 digits long");

        }
        if (expirationDate != null && (expirationDate.isBefore(LocalDate.now()))) {
            errors.add("Expiration date must be in the future");

        }

        return errors;

    }


}
