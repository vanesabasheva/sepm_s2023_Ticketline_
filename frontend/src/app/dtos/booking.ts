import { BookingMerchandise } from './merchandise';
import { BookingTicket } from './ticket';

export class Booking {
  tickets: BookingTicket[];
  merchandise: BookingMerchandise[];
  paymentDetailId: number;
  locationId: number;
}
