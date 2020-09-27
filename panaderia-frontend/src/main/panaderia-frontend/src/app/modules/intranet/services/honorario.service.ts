import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { HonorarioBuscarRequest } from '../dto/request/honorario-buscar.request';
import { HonorarioRequest } from '../dto/request/honorario.request';
import { HonorarioResponse } from '../dto/response/honorario.response';
import { OutResponse } from '../dto/response/out.response';

@Injectable()
export class HonorarioService {

  constructor(private http: HttpClient) { }

  public listarHonorario(req: HonorarioBuscarRequest): Observable<OutResponse<HonorarioResponse[]>> {
    return this.http.post<OutResponse<HonorarioResponse[]>>(`${environment.WsPanaderiaBackend}/honorario/listarHonorario`, req);
  }

  public registrarHonorario(req: HonorarioRequest): Observable<OutResponse<HonorarioResponse>> {
    return this.http.post<OutResponse<HonorarioResponse>>(`${environment.WsPanaderiaBackend}/honorario/registrarHonorario`, req);
  }

  public modificarHonorario(req: HonorarioRequest): Observable<OutResponse<HonorarioResponse>> {
    return this.http.post<OutResponse<HonorarioResponse>>(`${environment.WsPanaderiaBackend}/honorario/modificarHonorario`, req);
  }

  public eliminarHonorario(req: HonorarioRequest): Observable<OutResponse<HonorarioResponse>> {
    return this.http.post<OutResponse<HonorarioResponse>>(`${environment.WsPanaderiaBackend}/honorario/eliminarHonorario`, req);
  }
}
