import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { InsumoBuscarRequest } from '../dto/request/insumo-buscar.request';
import { InsumoRequest } from '../dto/request/insumo.request';
import { TipoInsumoBuscarRequest } from '../dto/request/tipo-insumo-buscar.request';
import { InsumoResponse } from '../dto/response/insumo.response';
import { OutResponse } from '../dto/response/out.response';
import { TipoInsumoResponse } from '../dto/response/tipo-insumo.response';

@Injectable()
export class InsumoService {

  constructor(private http: HttpClient) { }

  public listarTipoInsumo(req: TipoInsumoBuscarRequest): Observable<OutResponse<TipoInsumoResponse[]>> {
    return this.http.post<OutResponse<TipoInsumoResponse[]>>(`${environment.WsPanaderiaBackend}/insumo/listarTipoInsumo`, req);
  }

  public listarInsumo(req: InsumoBuscarRequest): Observable<OutResponse<InsumoResponse[]>> {
    return this.http.post<OutResponse<InsumoResponse[]>>(`${environment.WsPanaderiaBackend}/insumo/listarInsumo`, req);
  }

  public eliminarInsumo(req: InsumoRequest): Observable<OutResponse<InsumoResponse>> {
    return this.http.post<OutResponse<InsumoResponse>>(`${environment.WsPanaderiaBackend}/insumo/listarInsumo`, req);
  }
}
