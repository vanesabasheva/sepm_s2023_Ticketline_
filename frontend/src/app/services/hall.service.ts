import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Globals} from '../global/globals';
import {Observable} from 'rxjs';
import {Hall} from '../dtos/createEvent/hall';

@Injectable({
  providedIn: 'root'
})
export class HallService {

  constructor(private httpClient: HttpClient, private globals: Globals) {
  }


  public getHalls(): Observable<Hall[]> {
    return this.httpClient.get<Hall[]>(this.globals.backendUri + '/halls');
  }
}
