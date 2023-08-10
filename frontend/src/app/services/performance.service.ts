import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import { Observable } from 'rxjs';
import { Globals } from '../global/globals';
import {Performance, PerformanceSearch} from '../dtos/performance';

@Injectable({
  providedIn: 'root',
})
export class PerformanceService {
  private performanceBaseUri: string =
    this.globals.backendUri + '/performances';

  constructor(private httpClient: HttpClient, private globals: Globals) {
  }

  /**
   * Get the performance with the specified id
   *
   * @param id the id of the performance that should be fetched
   * @return an observable of the performance
   */
  public getPerformanceById(id: number): Observable<Performance> {
    return this.httpClient.get<Performance>(this.performanceBaseUri + '/' + id);
  }

  /**
   * Get the tickets in the cart of the specified user
   *
   * @param id the id of the event, whose performances should be fetched
   * @return an observable list of the performances of the event
   */
  public getPerformancesOfEventWithId(id: number): Observable<Performance[]> {
    return this.httpClient.get<Performance[]>(
      this.performanceBaseUri + '/events/' + id
    );
  }

  /**
   * Get the tickets in the cart of the specified user
   *
   * @param id the id of the location, whose performances should be fetched
   * @return an observable list of the found performances on this location
   */
  getPerformancesOnLocationById(id: number): Observable<Performance[]> {
    return this.httpClient.get<Performance[]>(
      this.performanceBaseUri + '/locations/' + id
    );
  }

  /**
   * Get the performances with the specified parameters
   *
   * @param params the parameters of the events that should be fetched
   * @return an observable list of the found performances
   */
  searchPerformances(params: HttpParams): Observable<PerformanceSearch[]> {
    return this.httpClient.get<PerformanceSearch[]>(this.globals.backendUri + '/search/performances', {params});
  }
}
