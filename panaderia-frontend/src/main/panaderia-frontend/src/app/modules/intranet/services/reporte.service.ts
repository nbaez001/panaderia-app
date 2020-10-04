import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { ReporteInsumoBuscarRequest } from '../dto/request/reporte-insumo-buscar.request';
import { ReporteVentaBuscarRequest } from '../dto/request/reporte-venta-buscar.request';
import { OutResponse } from '../dto/response/out.response';
import { ReporteInsumoResponse } from '../dto/response/reporte-insumo.response';
import { ReporteVentaResponse } from '../dto/response/reporte-venta.response';

@Injectable()
export class ReporteService {

  constructor(private http: HttpClient) { }

  public listarReporteInsumo(req: ReporteInsumoBuscarRequest): Observable<OutResponse<ReporteInsumoResponse[]>> {
    return this.http.post<OutResponse<ReporteInsumoResponse[]>>(`${environment.WsPanaderiaBackend}/reporte/listarReporteInsumo`, req);
  }

  public listarReporteVenta(req: ReporteVentaBuscarRequest): Observable<OutResponse<ReporteVentaResponse[]>> {
    return this.http.post<OutResponse<ReporteVentaResponse[]>>(`${environment.WsPanaderiaBackend}/reporte/listarReporteVenta`, req);
  }
}
