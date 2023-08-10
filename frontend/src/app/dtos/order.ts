import { OrderPageTicket, OrderTicket } from './ticket';
import { OrderMerchandise, OrderPageMerchandise } from './merchandise';
import { Location } from './location';
import { OrderPagePaymentDetail } from './payment-detail';
import { OrderPageTransactionDto as OrderPageTransaction } from './transaction';

export class Order {
  id: number;
  orderTs: Date;
  cancelled: boolean;
  tickets: OrderTicket[];
  merchandises: OrderMerchandise[];
  totalPrice: number;
  totalPoints: number;
}

export class OrderResponse {
  id: number;
}

export class OrderPage {
  orderTs: Date;
  location: Location;
  cancelled: boolean;
  paymentDetail: OrderPagePaymentDetail;
  tickets: OrderPageTicket[];
  merchandise: OrderPageMerchandise[];
  transactions: OrderPageTransaction[];
}
