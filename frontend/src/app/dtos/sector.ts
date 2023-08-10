import {Hall} from './createEvent/hall';

export class Sector {
  sectorId?: number;
  name: string;
  standing: boolean;
  hall?: Hall;
  hallId?: number;
  price?: number;
  pointsReward?: number;
}
