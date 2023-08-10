import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Globals } from '../global/globals';
import { DetailedReservation } from '../dtos/reservation';

@Injectable({
  providedIn: 'root',
})
export class ReservationService {
  private reservationBaseUri: string = this.globals.backendUri;

  constructor(private httpClient: HttpClient, private globals: Globals) {}

  getReservationsByUserId(userId: number): Observable<DetailedReservation[]> {
    return this.httpClient.get<DetailedReservation[]>(
      this.reservationBaseUri + '/users/' + userId + '/reservations'
    );
  }

  deleteReservation(id: number): Observable<any> {
    return this.httpClient.delete<any>(
      this.reservationBaseUri + '/reservations/' + id
    );
  }
}
