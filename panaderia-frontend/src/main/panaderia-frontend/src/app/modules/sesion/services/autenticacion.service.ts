import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Oauth2Response } from '../dto/request/oauth2-response';
import { UsuarioRequest } from '../dto/request/usuario-request';
import { environment } from 'src/environments/environment';
import { Router } from '@angular/router';
import { Cookie } from 'ng2-cookies';

@Injectable()
export class AutenticacionService {

  constructor(private http: HttpClient, private router: Router) { }

  public oauth2Token(req: UsuarioRequest): Observable<Oauth2Response> {
    const params = new URLSearchParams();
    params.append('username', req.usuario);
    params.append('password', req.contrasenia);
    params.append('grant_type', 'password');

    const headers = new HttpHeaders({
      'Content-type': 'application/x-www-form-urlencoded; charset=utf-8',
      Authorization: 'Basic ' + btoa(environment.clientId + ':' + environment.clientSecret)
    });

    return this.http.post<Oauth2Response>(`${environment.WsPanaderiaAuthorizer}/oauth/token`, params.toString(), { headers });
  }

  public refreshOauth2Token(req: Oauth2Response): Observable<Oauth2Response> {
    const params = new URLSearchParams();
    params.append('refresh_token', req.refresh_token);
    params.append('grant_type', 'refresh_token');

    const headers = new HttpHeaders({
      'Content-type': 'application/x-www-form-urlencoded; charset=utf-8',
      Authorization: 'Basic ' + btoa(environment.clientId + ':' + environment.clientSecret)
    });

    return this.http.post<Oauth2Response>(`${environment.WsPanaderiaAuthorizer}/oauth/token`, params.toString(), { headers });
  }

  saveToken(token: Oauth2Response) {
    const expireDate = token.expires_in/3600;
    Cookie.set('refresh_token', token.refresh_token, expireDate);
  }

  existeToken(): any {
    const token = Cookie.get('refresh_token');
    return token;
  }

  salir() {
    Cookie.delete('refresh_token');
    this.router.navigate(['/sesion/login']);
  }
}
