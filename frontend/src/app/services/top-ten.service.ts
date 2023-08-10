import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Globals} from '../global/globals';
import {EventTicketCount} from '../dtos/topTen/topTen';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TopTenService {

  private newsBaseUri: string = this.globals.backendUri + '/top-ten';

  constructor(private httpClient: HttpClient, private globals: Globals) {
  }

  /**
   * Loads all top-ten events by category from the backend
   */
  getTopTenEvents(): Observable<EventTicketCount[]> {
    return this.httpClient.get<EventTicketCount[]>(this.newsBaseUri);
  }
}
