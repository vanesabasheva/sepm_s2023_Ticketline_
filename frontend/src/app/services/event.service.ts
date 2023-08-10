import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {Artist} from '../dtos/createEvent/artist';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Globals} from '../global/globals';
import {Event} from '../dtos/createEvent/event';
import {LocationSearch} from '../dtos/location';
import {CreateEvent} from '../dtos/createEvent/createEvent';


@Injectable({
  providedIn: 'root'
})
export class EventService {

  private baseUri: string = this.globals.backendUri + '/search';


  constructor(
    private http: HttpClient,
    private globals: Globals
  ) {
  }

  /**
   * Get all artists with name like the specified one
   *
   * @param artistName the sequence of letters of which the artists' names should consist
   * @return an observable list of the found artists
   */
  searchArtistsByName(artistName: string): Observable<Artist[]> {
    const params = new HttpParams()
      .set('name', artistName);
    return this.http.get<Artist[]>(this.baseUri + '/artists', {params});
  }

  /**
   * Get the events of the specified artist
   *
   * @param artistId the id of the artist, whose events should be fetched
   * @param artistName the name of the artist, whose events should be fetched
   * @return an observable list of the found events
   */
  searchEventsByArtistName(artistId: number, artistName: string): Observable<Event[]> {
    const params = new HttpParams()
      .set('id', artistId)
      .set('name', artistName);
    return this.http.get<Event[]>(this.baseUri + '/events-by-artist', {params});
  }

  /**
   * Get the locations with the specified parameters
   *
   * @param params the parameters of the locations that should be fetched
   * @return an observable list of the found locations
   */
  searchLocations(params: HttpParams): Observable<LocationSearch[]> {
    return this.http.get<LocationSearch[]>(this.baseUri + '/locations', {params});
  }

  /**
   * Get the events with the specified parameters
   *
   * @param params the parameters of the events that should be fetched
   * @return an observable list of the found events
   */
  searchEvents(params: HttpParams): Observable<Event[]> {
    return this.http.get<Event[]>(this.baseUri + '/events', {params});
  }

  createEvent(event: CreateEvent): Observable<Event> {
    return this.http.post<Event>(this.globals.backendUri + '/events', event);
  }
}
