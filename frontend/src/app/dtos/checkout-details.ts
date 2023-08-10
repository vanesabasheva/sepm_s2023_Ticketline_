import { CheckoutLocation } from './location';
import { CheckoutPaymentDetail } from './payment-detail';

export class CheckoutDetails {
  paymentDetails: CheckoutPaymentDetail[];
  locations: CheckoutLocation[];
}
