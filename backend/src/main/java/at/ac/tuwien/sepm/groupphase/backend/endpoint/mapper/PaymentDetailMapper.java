package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimplePaymentDetailDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.checkout.CheckoutPaymentDetail;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.orderpage.OrderPagePaymentDetailDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.PaymentDetail;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public abstract class PaymentDetailMapper {


    public CheckoutPaymentDetail paymentDetailToCheckoutPaymentDetail(PaymentDetail paymentDetail) {
        CheckoutPaymentDetail checkoutPaymentDetail = new CheckoutPaymentDetail();
        checkoutPaymentDetail.setPaymentDetailId(paymentDetail.getId());
        String cardNumber = paymentDetail.getCardNumber();
        checkoutPaymentDetail.setLastFourDigits(Integer.parseInt(cardNumber.substring(cardNumber.length() - 4)));
        checkoutPaymentDetail.setCardHolder(paymentDetail.getCardHolder());
        checkoutPaymentDetail.setExpirationDate(paymentDetail.getExpirationDate());
        return checkoutPaymentDetail;
    }

    public abstract List<SimplePaymentDetailDto> paymentDetailListToSimplePaymentDetailDtoList(List<PaymentDetail> locations);

    public OrderPagePaymentDetailDto paymentDetailToOrderPagePaymentDetailDto(PaymentDetail paymentDetail) {
        OrderPagePaymentDetailDto orderPagePaymentDetailDto = new OrderPagePaymentDetailDto();
        String cardNumber = paymentDetail.getCardNumber();
        orderPagePaymentDetailDto.setLastFourDigits(Integer.parseInt(cardNumber.substring(cardNumber.length() - 4)));
        return orderPagePaymentDetailDto;
    }
}
