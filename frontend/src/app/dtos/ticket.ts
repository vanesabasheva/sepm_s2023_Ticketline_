import { SimpleSeat } from './seat';

export class PerformanceTicket {
  ticketId: number;
  reserved: boolean;
  row: number;
  number: number;
  sectorId: number;
}

export class SimpleTicket {
  id: number;
}

export class ReservedTicket {
  id: number;
  seat: SimpleSeat;
}
export class OrderTicket {
  id: number;
  price: number;
  artists: string;
  eventName: string;
  datetime: Date;
}
export class BookingTicket {
  ticketId: number;
  reservation: boolean;
}

export class CartTicket {
  id: number;
  seatRow: number;
  seatNumber: number;
  sectorName: string;
  standing: boolean;
  date: Date;
  eventName: string;
  hallName: string;
  locationCity: string;
  locationStreet: string;
  price: number;
  reservation: boolean;
  userPoints: number;
}

export class OrderPageTicket {
  id: number;
  price: number;
  number: number;
  row: number;
  sectorName: string;
  standing: boolean;
  eventName: string;
  hallName: string;
  performanceStart: Date;
  artistNames: string[];
}
