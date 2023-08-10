import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Globals } from '../global/globals';
import { Observable } from 'rxjs';
import { Merchandise } from '../dtos/merchandise';

@Injectable({
  providedIn: 'root',
})
export class MerchandiseService {
  private merchandiseBaseUri: string = this.globals.backendUri + '/merch';

  constructor(private httpClient: HttpClient, private globals: Globals) {}

  public getMerchandise(withPoints: boolean): Observable<Merchandise[]> {
    console.log(this.merchandiseBaseUri + ', ' + withPoints);
    if (withPoints) {
      const params = new HttpParams().set('withPoints', 'true');
      return this.httpClient.get<Merchandise[]>(this.merchandiseBaseUri, {
        params,
      });
    } else {
      return this.httpClient.get<Merchandise[]>(this.merchandiseBaseUri);
    }
  }
}
