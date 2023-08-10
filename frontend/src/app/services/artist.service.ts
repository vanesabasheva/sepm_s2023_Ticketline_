import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Globals} from '../global/globals';
import {Observable} from 'rxjs';
import {Artist} from '../dtos/createEvent/artist';

@Injectable({
  providedIn: 'root'
})
export class ArtistService {

  constructor(private httpClient: HttpClient, private globals: Globals) {
  }

  public getArtists(): Observable<Artist[]> {
    return this.httpClient.get<Artist[]>(this.globals.backendUri + '/artists');
  }
}
