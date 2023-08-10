import {ReservedTicket} from './ticket';

export class Reservation {
  id: number;
  exiprationTs: Date;
  cart: boolean;
  tickedId: number;
  userId: number;
}

export class DetailedReservation {
  id: number;
  expirationTs: Date;
  cart: boolean;
  ticket: ReservedTicket;
  userId: number;
  eventName: string;
}
