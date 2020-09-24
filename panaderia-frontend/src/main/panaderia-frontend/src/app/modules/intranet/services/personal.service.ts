import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { PersonalBuscarRequest } from '../dto/request/personal-buscar.request';
import { PersonalRequest } from '../dto/request/personal.request';
import { OutResponse } from '../dto/response/out.response';
import { PersonalResponse } from '../dto/response/personal.response';

@Injectable()
export class PersonalService {

  constructor(private http: HttpClient) { }

  public listarPersonal(req: PersonalBuscarRequest): Observable<OutResponse<PersonalResponse[]>> {
    return this.http.post<OutResponse<PersonalResponse[]>>(`${environment.WsPanaderiaBackend}/personal/listarPersonal`, req);
  }

  public registrarPersonal(req: PersonalRequest): Observable<OutResponse<PersonalResponse>> {
    return this.http.post<OutResponse<PersonalResponse>>(`${environment.WsPanaderiaBackend}/personal/registrarPersonal`, req);
  }

  public modificarPersonal(req: PersonalRequest): Observable<OutResponse<PersonalResponse>> {
    return this.http.post<OutResponse<PersonalResponse>>(`${environment.WsPanaderiaBackend}/personal/modificarPersonal`, req);
  }

  public eliminarPersonal(req: PersonalRequest): Observable<OutResponse<PersonalResponse>> {
    return this.http.post<OutResponse<PersonalResponse>>(`${environment.WsPanaderiaBackend}/personal/eliminarPersonal`, req);
  }
}
