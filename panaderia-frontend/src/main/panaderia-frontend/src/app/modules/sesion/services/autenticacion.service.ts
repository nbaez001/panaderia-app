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

  // saveToken(token: Oauth2Response) {
  //   const expireDate = new Date().getTime() + (1000 * token.expires_in);
  //   Cookie.set('access_token_fc', token.access_token, expireDate);
  //   Cookie.set('inforegioncodigo', token.inforegioncodigo, expireDate);
  //   Cookie.set('idusuario', token.id_usuario, expireDate);
  //   Cookie.set('idrol', token.nombre_rol, expireDate);
  //   Cookie.set('vnombreusuario', token.infonombre, expireDate);
  //   Cookie.set('vnombreregion', token.inforegion, expireDate);
  // }

  // existeToken(): void {
  //   const token = Cookie.get('access_token_fc');
  //   console.log('esto es el token');
  //   console.log(token);

  //   if (token == null || typeof (token) === 'undefined') {
  //     console.log('por aqui paso');
  //     this.router.navigate(['/login']);
  //   }

  // }

  // salir() {
  //   Cookie.delete('access_token_fc');
  //   Cookie.delete('inforegioncodigo');
  //   Cookie.delete('idusuario');
  //   Cookie.delete('idrol');
  //   Cookie.delete('vnombreusuario');
  //   Cookie.delete('vnombreregion');
  //   this.router.navigate(['/login']);
  // }
}
