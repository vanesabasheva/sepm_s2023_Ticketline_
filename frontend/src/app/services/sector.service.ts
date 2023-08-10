import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Globals} from '../global/globals';
import {Sector} from '../dtos/sector';

@Injectable({
  providedIn: 'root'
})
export class SectorService {

  private baseUri: string = this.globals.backendUri + '/sectors';


  constructor(
    private http: HttpClient,
    private globals: Globals
  ) {
  }

  public getSectorFromHall(id: number) {
    return this.http.get<Sector[]>(this.baseUri + '/' + id);
  }

}
