import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { DepartamentoRequest } from '../dto/request/departamento.request';
import { DistritoRequest } from '../dto/request/distrito.request';
import { ProvinciaRequest } from '../dto/request/provincia.request';
import { DepartamentoResponse } from '../dto/response/departamento.response';
import { DistritoResponse } from '../dto/response/distrito.response';
import { OutResponse } from '../dto/response/out.response';
import { PaisResponse } from '../dto/response/pais.response';
import { ProvinciaResponse } from '../dto/response/provincia.response';

@Injectable()
export class UbigeoService {

  constructor(private http: HttpClient) { }

  public listarPais(): Observable<OutResponse<PaisResponse[]>> {
    return this.http.post<OutResponse<PaisResponse[]>>(`${environment.WsPanaderiaBackend}/ubigeo/listarPais`, {});
  }

  public listarDepartamento(req: DepartamentoRequest): Observable<OutResponse<DepartamentoResponse[]>> {
    return this.http.post<OutResponse<DepartamentoResponse[]>>(`${environment.WsPanaderiaBackend}/ubigeo/listarDepartamento`, req);
  }

  public listarProvincia(req: ProvinciaRequest): Observable<OutResponse<ProvinciaResponse[]>> {
    return this.http.post<OutResponse<ProvinciaResponse[]>>(`${environment.WsPanaderiaBackend}/ubigeo/listarProvincia`, req);
  }

  public listarDistrito(req: DistritoRequest): Observable<OutResponse<DistritoResponse[]>> {
    return this.http.post<OutResponse<DistritoResponse[]>>(`${environment.WsPanaderiaBackend}/ubigeo/listarDistrito`, req);
  }
}
