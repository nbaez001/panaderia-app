import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { InsumoBuscarRequest } from '../dto/request/insumo-buscar.request';
import { InsumoRequest } from '../dto/request/insumo.request';
import { TipoInsumoBuscarRequest } from '../dto/request/tipo-insumo-buscar.request';
import { TipoInsumoRequest } from '../dto/request/tipo-insumo.request';
import { FileResponse } from '../dto/response/file.response';
import { InsumoResponse } from '../dto/response/insumo.response';
import { OutResponse } from '../dto/response/out.response';
import { TipoInsumoResponse } from '../dto/response/tipo-insumo.response';

@Injectable()
export class InsumoService {

  constructor(private http: HttpClient) { }

  public listarTipoInsumo(req: TipoInsumoBuscarRequest): Observable<OutResponse<TipoInsumoResponse[]>> {
    return this.http.post<OutResponse<TipoInsumoResponse[]>>(`${environment.WsPanaderiaBackend}/insumo/listarTipoInsumo`, req);
  }

  public registrarTipoInsumo(req: TipoInsumoRequest): Observable<OutResponse<TipoInsumoResponse>> {
    return this.http.post<OutResponse<TipoInsumoResponse>>(`${environment.WsPanaderiaBackend}/insumo/registrarTipoInsumo`, req);
  }

  public modificarTipoInsumo(req: TipoInsumoRequest): Observable<OutResponse<TipoInsumoResponse>> {
    return this.http.post<OutResponse<TipoInsumoResponse>>(`${environment.WsPanaderiaBackend}/insumo/modificarTipoInsumo`, req);
  }

  public eliminarTipoInsumo(req: TipoInsumoRequest): Observable<OutResponse<TipoInsumoResponse>> {
    return this.http.post<OutResponse<TipoInsumoResponse>>(`${environment.WsPanaderiaBackend}/insumo/eliminarTipoInsumo`, req);
  }



  public listarInsumo(req: InsumoBuscarRequest): Observable<OutResponse<InsumoResponse[]>> {
    return this.http.post<OutResponse<InsumoResponse[]>>(`${environment.WsPanaderiaBackend}/insumo/listarInsumo`, req);
  }

  public registrarInsumo(req: InsumoRequest): Observable<OutResponse<InsumoResponse>> {
    return this.http.post<OutResponse<InsumoResponse>>(`${environment.WsPanaderiaBackend}/insumo/registrarInsumo`, req);
  }

  public modificarInsumo(req: InsumoRequest): Observable<OutResponse<InsumoResponse>> {
    return this.http.post<OutResponse<InsumoResponse>>(`${environment.WsPanaderiaBackend}/insumo/modificarInsumo`, req);
  }

  public eliminarInsumo(req: InsumoRequest): Observable<OutResponse<InsumoResponse>> {
    return this.http.post<OutResponse<InsumoResponse>>(`${environment.WsPanaderiaBackend}/insumo/eliminarInsumo`, req);
  }

  public reporteXlsxListarInsumo(req: InsumoBuscarRequest): Observable<OutResponse<FileResponse>> {
    return this.http.post<OutResponse<FileResponse>>(`${environment.WsPanaderiaBackend}/insumo/reporteXlsxListarInsumo`, req);
  }
}
