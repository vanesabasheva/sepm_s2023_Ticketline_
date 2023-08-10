package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.checkout.CheckoutDetailsDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.checkout.CheckoutLocation;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.checkout.CheckoutPaymentDetail;
import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import at.ac.tuwien.sepm.groupphase.backend.entity.PaymentDetail;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public abstract class CheckoutMapper {

    @Autowired
    private PaymentDetailMapper paymentDetailMapper;

    public CheckoutDetailsDto generateCheckoutDetailsDto(List<PaymentDetail> paymentDetails, List<Location> locations) {
        CheckoutDetailsDto checkoutDetailsDto = new CheckoutDetailsDto();
        List<CheckoutPaymentDetail> checkoutPaymentDetails = new ArrayList<>();
        for (PaymentDetail paymentDetail : paymentDetails) {
            checkoutPaymentDetails.add(paymentDetailMapper.paymentDetailToCheckoutPaymentDetail(paymentDetail));
        }
        List<CheckoutLocation> checkoutLocations = new ArrayList<>();
        for (Location location : locations) {
            CheckoutLocation checkoutLocation = new CheckoutLocation();
            checkoutLocation.setLocationId(location.getId());
            checkoutLocation.setStreet(location.getStreet());
            checkoutLocation.setCity(location.getCity());
            checkoutLocation.setCountry(location.getCountry());
            checkoutLocation.setPostalCode(location.getPostalCode());
            checkoutLocations.add(checkoutLocation);
        }
        checkoutDetailsDto.setPaymentDetails(checkoutPaymentDetails);
        checkoutDetailsDto.setLocations(checkoutLocations);
        return checkoutDetailsDto;
    }
}
