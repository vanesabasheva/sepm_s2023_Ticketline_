import { Injectable } from '@angular/core';
import {Observable} from 'rxjs';
import {User} from '../dtos/user';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Globals} from '../global/globals';

@Injectable({
  providedIn: 'root'
})
export class AdminService {
  private userBaseUri: string = this.globals.backendUri;
  constructor(private httpClient: HttpClient, private globals: Globals) {}

  getAdminUsers(locked: boolean): Observable<User[]> {
    const params = new HttpParams().set('locked', locked.toString());
    return this.httpClient.get<User[]>(
      this.userBaseUri + '/userlist', {params}
    );
  }

  resetPassword(id: number): Observable<User> {
    return this.httpClient.post<null>(
      this.userBaseUri + '/users/' + id + '/reset-password', null
    );
  }

  lockUser(id: number): Observable<User> {
    return this.httpClient.post<null>(
      this.userBaseUri + '/users/' + id + '/lock', null
    );
  }
  unlockUser(id: number): Observable<User> {
    return this.httpClient.post<null>(
      this.userBaseUri + '/users/' + id + '/open', null
    );
  }
}
