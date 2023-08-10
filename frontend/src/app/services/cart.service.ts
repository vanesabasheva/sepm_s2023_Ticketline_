import { Injectable } from '@angular/core';
import { CartTicket } from '../dtos/ticket';
import {  Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Globals } from '../global/globals';
import { CheckoutPaymentDetail } from '../dtos/payment-detail';
import { Booking } from '../dtos/booking';
import { CheckoutDetails } from '../dtos/checkout-details';
import { CheckoutLocation } from '../dtos/location';
import { BookingMerchandise, Merchandise } from '../dtos/merchandise';
import { Cart } from '../dtos/cart';
import { OrderResponse } from '../dtos/order';

export interface DialogData {
  paymentDetails: CheckoutPaymentDetail[];
  locations: CheckoutLocation[];
}

@Injectable({
  providedIn: 'root',
})
export class CartService {
  items: CartTicket[];
  cart: Observable<Cart>;

  private cartBaseUri: string = this.globals.backendUri;
  constructor(private http: HttpClient, private globals: Globals) {}

  /**
   * Get the tickets in the cart of the specified user
   *
   * @param id the id of the user, whose cart should be fetched
   * @return an observable list of the tickets in the cart of the user
   */
  getCartTickets(id: number): Observable<Cart> {
    return this.http.get<Cart>(this.cartBaseUri + '/users/' + id + '/cart');
  }
  /**
   * Remove a ticket from the specified user's cart
   *
   *  @param userId the id of the user, from whose cart the ticket should be deleted
   *  @param ticketId the id of the ticket that should be removed from the cart
   *  TODO: still a mock, implement backend
   */
  removeTicketFromCart(userId: number, ticketId: number): Observable<any> {
    return this.http.delete(
      this.cartBaseUri + '/users/' + userId + '/cart/tickets/' + ticketId
    );
  }

  buy(booking: Booking): Observable<OrderResponse> {
    return this.http.post<OrderResponse>(
      this.cartBaseUri + '/bookings',
      booking
    );
  }

  getCheckoutDetails(userId: number): Observable<CheckoutDetails> {
    return this.http.get<any>(
      this.cartBaseUri + '/users/' + userId + '/checkout-details'
    );
  }

  getMerchInfo(
    bookingMerchandises: BookingMerchandise[]
  ): Observable<Merchandise[]> {
    return this.http.post<Merchandise[]>(
      this.cartBaseUri + '/merch',
      bookingMerchandises
    );
  }
}
