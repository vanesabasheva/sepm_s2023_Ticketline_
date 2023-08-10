package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.checkout;

import java.util.List;

public class CheckoutDetailsDto {
    private List<CheckoutLocation> locations;
    private List<CheckoutPaymentDetail> paymentDetails;

    public List<CheckoutLocation> getLocations() {
        return locations;
    }

    public void setLocations(List<CheckoutLocation> locations) {
        this.locations = locations;
    }

    public List<CheckoutPaymentDetail> getPaymentDetails() {
        return paymentDetails;
    }

    public void setPaymentDetails(List<CheckoutPaymentDetail> paymentDetails) {
        this.paymentDetails = paymentDetails;
    }


}
