import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http'
import { Observable } from 'rxjs';
import { AuthenticationToken } from '../entity/authentication-token';
import { AuthenticationRequest } from '../entity/authentication-request';
import { RegistrationRequest } from '../entity/registration-request';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
  private apiServerUrl = environment.apiServerUrl;

  constructor(private http: HttpClient) { }

  public authenticate(request: AuthenticationRequest): Observable<AuthenticationToken> {
    return this.http.post<AuthenticationToken>(`${this.apiServerUrl}/authenticate`, request);
  }

  public register(request: RegistrationRequest): Observable<AuthenticationToken> {
    return this.http.post<AuthenticationToken>(`${this.apiServerUrl}/register`, request);
  }
}