import {Injectable} from '@angular/core';
import {AuthRequest} from '../dtos/authentication/auth-request';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {tap} from 'rxjs/operators';
// @ts-ignore
import jwt_decode from 'jwt-decode';
import {Globals} from '../global/globals';
import {RegisterRequest} from '../dtos/authentication/user-registration';
import {PasswordResetRequest} from '../dtos/passwordReset/passwordResetRequest';
import {PasswordUpdate} from '../dtos/passwordReset/passwordUpdate';
import {CookieService} from 'ngx-cookie-service';

@Injectable({
  providedIn: 'root',
})
export class AuthService {

  private authBaseUri: string = this.globals.backendUri;

  constructor(private httpClient: HttpClient,
              private globals: Globals,
              private cookie: CookieService
  ) {
  }

  /**
   * Login in the user. If it was successful, a valid JWT token will be stored
   *
   * @param authRequest User data
   */
  loginUser(authRequest: AuthRequest): Observable<string> {
    return this.httpClient
      .post(this.authBaseUri + '/authentication', authRequest, {
        responseType: 'text',
      })
      .pipe(tap((authResponse: string) => this.setToken(authResponse)));
  }

  createUser(registerRequest: RegisterRequest, isAdmin: boolean) {
    if (!isAdmin) {
      return this.httpClient.post(this.authBaseUri + '/users', registerRequest, {
        responseType: 'text',
      });
    } else {
      return this.httpClient.post(this.authBaseUri + '/admins', registerRequest, {
        responseType: 'text',
      });
    }
  }

  /**
   * Check if a valid JWT token is saved in the localStorage
   */
  isLoggedIn() {
    return (
      !!this.getToken() &&
      this.getTokenExpirationDate(this.getToken()).valueOf() >
      new Date().valueOf()
    );
  }

  logoutUser() {
    console.log('Logout');
    localStorage.removeItem('authToken');
    this.cookie.delete('merchandise');
  }

  getToken() {
    return localStorage.getItem('authToken');
  }

  /**
   * Returns the user role based on the current token
   */
  getUserRole() {
    if (this.getToken() != null) {
      const decoded: any = jwt_decode(this.getToken());
      const authInfo: string[] = decoded.rol;
      if (authInfo.includes('ROLE_ADMIN')) {
        return 'ADMIN';
      } else if (authInfo.includes('ROLE_USER')) {
        return 'USER';
      }
    }
    return 'UNDEFINED';
  }

  getUserId(): number {
    if (this.getToken() != null) {
      const decoded: any = jwt_decode(this.getToken());
      const authInfo: string = decoded.user;
      if (authInfo == null || authInfo === '' || authInfo === undefined) {
        return -1;
      }
      return Number(authInfo);
    }
    return -1;
  }

  sendResetPasswordEmail(email: PasswordResetRequest) {
    return this.httpClient.post(
      this.authBaseUri + '/resets', email
    );
  }

  updatePassword(token: string, password: PasswordUpdate) {
    return this.httpClient.put(
      this.authBaseUri + '/resets/' + token, password);
  }


  private setToken(authResponse: string) {
    localStorage.setItem('authToken', authResponse);
  }

  private getTokenExpirationDate(token: string): Date {
    const decoded: any = jwt_decode(token);
    if (decoded.exp === undefined) {
      return null;
    }

    const date = new Date(0);
    date.setUTCSeconds(decoded.exp);
    return date;
  }
}
