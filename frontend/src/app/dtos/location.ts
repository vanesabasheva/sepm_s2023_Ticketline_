export class Location {
  postalCode: number;
  street: string;
  city: string;
  country: string;
}

export class CheckoutLocation {
  locationId: number;
  postalCode: number;
  street: string;
  city: string;
  country: string;
}

export class LocationSearch {
  id: number;
  postalCode?: number;
  street?: string;
  city?: string;
  country?: string;
}
