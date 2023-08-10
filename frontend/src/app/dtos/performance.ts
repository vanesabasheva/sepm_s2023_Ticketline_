import { PerformanceTicket as PerformanceTicket } from './ticket';
import { Sector as Sector } from './sector';
import { Location } from './location';

export class Performance {
  id: number;
  eventName: string;
  hallName: string;
  location: Location;
  dateTime: Date;
  tickets: PerformanceTicket[][];
  performanceSector: Map<number, Sector>;
}

export class PerformanceSearch {
  id: number;
  eventName: string;
  hallName: string;
  location: Location;
  dateTime: Date;
  price: number;
  imagePath: string;
}
