export class BookingMerchandise {
  id: number;
  quantity: number;
  buyWithPoints?: boolean;
  constructor(id: number, quantity: number, buyWithPoints?: boolean) {
    this.id = id;
    this.quantity = quantity;
    if (buyWithPoints) {
      this.buyWithPoints = buyWithPoints;
    } else {
      this.buyWithPoints = false;
    }
  }
}

export class Merchandise {
  id: number;
  price: number;
  pointsPrice?: number;
  pointsReward?: number;
  title: string;
  description?: string;
  imagePath?: string;
  quantity?: number;
  buyWithPoints?: boolean;
}

export class OrderMerchandise {
  id: number;
  itemName: string;
  quantity: number;
  price: number;
}

export class OrderPageMerchandise {
  id: number;
  quantity: number;
  price: number;
  pointsPrice: number;
  points: boolean;
  title: string;
}
