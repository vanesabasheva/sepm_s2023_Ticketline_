export class PaymentDetail {
  id: number;
  userId: number;
  cardHolder: string;
  cardNumber: string;
  cvv: number;
  expirationDate: Date;
}

export class CheckoutPaymentDetail {
  paymentDetailId: number;
  lastFourDigits: number;
  cardHolder: string;
  expirationDate: Date;
}

export class OrderPagePaymentDetail {
  lastFourDigits: number;
}
