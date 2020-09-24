import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { ApiPersonaBuscarRequest } from '../dto/request/api-persona-buscar.request';
import { ApiOutResponse } from '../dto/response/api-out.response';
import { ApiPersonaResponse } from '../dto/response/api-persona.response';

@Injectable()
export class PersonaService {

  constructor(private http: HttpClient) { }

  public buscarPersona(req: ApiPersonaBuscarRequest): Observable<ApiOutResponse<ApiPersonaResponse>> {
    return this.http.post<ApiOutResponse<ApiPersonaResponse>>(`${environment.WsReniec}`, req);
  }
}
