import { Injectable } from '@angular/core';
import { Globals } from '../global/globals';
import { HttpClient } from '@angular/common/http';
import {BehaviorSubject, Observable} from 'rxjs';
import { Reservation } from '../dtos/reservation';
import { SimpleTicket } from '../dtos/ticket';
import { Order } from '../dtos/order';
import { CheckoutLocation, Location } from '../dtos/location';
import { CheckoutPaymentDetail, PaymentDetail } from '../dtos/payment-detail';
import {UserProfile} from '../dtos/user-profile';
import {PasswordChange} from '../dtos/passwordReset/passwordChange';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  pointsHelper: Observable<number>;
  userPoints: BehaviorSubject<number> = new BehaviorSubject<number>(0);
  private userBaseUri: string = this.globals.backendUri + '/users/';

  constructor(private httpClient: HttpClient, private globals: Globals) {}

  addTicketsToCart(
    id: number,
    ticketIds: SimpleTicket[]
  ): Observable<Reservation> {
    return this.httpClient.post<any>(
      this.userBaseUri + id + '/cart/tickets',
      ticketIds
    );
  }

  /**
   * Get all orders of the specified user
   *
   * @param id the id of the user whose orders should be fetched
   * @return an Observable List of all orders of the user
   */
  getOrders(id: number): Observable<Order[]> {
    return this.httpClient.get<Order[]>(
      this.userBaseUri + id + '/order-history'
    );
  }

  /**
   * Create a location for a user
   *
   * @param id the user id
   * @param location the location to create
   * @returns the created location
   */
  createLocation(id: number, location: Location): Observable<CheckoutLocation> {
    return this.httpClient.post<any>(
      this.userBaseUri + id + '/locations',
      location
    );
  }

  /**
   * Update a location of a user
   *
   * @param id the user id
   * @param location the location to update
   */
  updateLocation(id: number, location: CheckoutLocation): Observable<CheckoutLocation> {
    return this.httpClient.put<any>(
        this.userBaseUri + id + '/locations',
        location
    );
  }

  /**
   * Delete a location of a user
   *
   * @param id user id
   * @param locationId of deleted location
   */
    deleteLocation(id: number, locationId: number): Observable<CheckoutLocation> {
        return this.httpClient.delete<any>(
            this.userBaseUri + id + '/locations' + '/' + locationId
        );
    }
  /**
   * Create a payment detail for a user
   *
   * @param id the user id
   * @param paymentDetail the payment detail to create
   * @returns the created payment detail
   */
  createPaymentDetail(
    id: number,
    paymentDetail: PaymentDetail
  ): Observable<CheckoutPaymentDetail> {
    return this.httpClient.post<any>(
      this.userBaseUri + id + '/payment-details',
      paymentDetail
    );
  }

  /**
   * Update a payment detail of a user
   *
   * @param id
   * @param paymentDetail
   */
  updatePaymentDetail(
    id: number,
    paymentDetail: PaymentDetail
    ): Observable<CheckoutPaymentDetail> {
    return this.httpClient.put<any>(
      this.userBaseUri + id + '/payment-details',
      paymentDetail
    );
  }

  /**
   * Delete a payment detail of a user
   *
   * @param id
   * @param paymentDetailId
   */
  deletePaymentDetail(
      id: number,
      paymentDetailId: number
  ): Observable<CheckoutPaymentDetail> {
    return this.httpClient.delete<any>(
        this.userBaseUri + id + '/payment-details' + '/' + paymentDetailId
    );
  }

  /**
   * Get the user with the specified id
   *
   * @param id
   * @return an Observable of the user
   */
   getUser(id: number): Observable<UserProfile> {
    return this.httpClient.get<UserProfile>(this.userBaseUri + id);
  }

  /**
   * Edit the user with the specified id
   *
   * @param id
   * @param user
   * @return an Observable of the edited user
   */
   editUser(id: number, user: UserProfile): Observable<UserProfile> {
    return this.httpClient.put<UserProfile>(this.userBaseUri + id, user);
  }

  /**
   * Delete the user with the specified id
   *
   *
   * @param id
   * @return an Observable of the deleted user
   */
  deleteUser(id: number): Observable<UserProfile> {
    return this.httpClient.delete<UserProfile>(this.userBaseUri + id);
  }

  /**
   * Get the points of the user with the specified id
   *
   * @param id
   * @return an Observable of the points of the user
   */
  getUserPoints(id: number): Observable<number> {
    this.pointsHelper = this.httpClient.get<number>(this.userBaseUri + id + '/points');
    this.pointsHelper.subscribe((points) => {
        this.userPoints.next(points);
    });
    return this.userPoints;
  }
  changePassword(data: PasswordChange): Observable<any> {
    return this.httpClient.post<any>(this.userBaseUri + 'password', data);
  }
}
