import { Inject, Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { MaestraRequest } from '../dto/request/maestra.request';
import { Observable } from 'rxjs';
import { OutResponse } from '../dto/response/out.response';
import { MaestraResponse } from '../dto/response/maestra.response';
import { environment } from 'src/environments/environment';
import { MaestraBuscarRequest } from '../dto/request/maestra-buscar.request';
import { UsuarioService } from 'src/app/core/services/usuario.service';

@Injectable()
export class MaestraService {

  constructor(private http: HttpClient,
    @Inject(UsuarioService) private user: UsuarioService) { }

  public listarMaestra(request: MaestraBuscarRequest): Observable<OutResponse<MaestraResponse[]>> {
    let httpOptions = {
      headers: new HttpHeaders({
        'Content-type': 'application/json; charset=utf-8',
        authorization: 'Bearer ' + this.user.access_token
      })
    };
    return this.http.post<OutResponse<MaestraResponse[]>>(`${environment.WsPanaderiaBackend}/maestra/listarMaestra`, request, httpOptions);
  }

  public registrarMaestra(request: MaestraRequest): Observable<OutResponse<MaestraResponse>> {
    let httpOptions = {
      headers: new HttpHeaders({
        'Content-type': 'application/json; charset=utf-8',
        authorization: 'Bearer ' + this.user.access_token
      })
    };
    return this.http.post<OutResponse<MaestraResponse>>(`${environment.WsPanaderiaBackend}/maestra/registrarMaestra`, request, httpOptions);
  }

  public modificarMaestra(request: MaestraRequest): Observable<OutResponse<MaestraResponse>> {
    let httpOptions = {
      headers: new HttpHeaders({
        'Content-type': 'application/json; charset=utf-8',
        authorization: 'Bearer ' + this.user.access_token
      })
    };
    return this.http.post<OutResponse<MaestraResponse>>(`${environment.WsPanaderiaBackend}/maestra/modificarMaestra`, request, httpOptions);
  }

  public eliminarMaestra(request: MaestraRequest): Observable<OutResponse<MaestraResponse>> {
    let httpOptions = {
      headers: new HttpHeaders({
        'Content-type': 'application/json; charset=utf-8',
        authorization: 'Bearer ' + this.user.access_token
      })
    };
    return this.http.post<OutResponse<MaestraResponse>>(`${environment.WsPanaderiaBackend}/maestra/eliminarMaestra`, request, httpOptions);
  }
}
