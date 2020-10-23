import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { VentaBuscarRequest } from '../dto/request/venta-buscar.request';
import { VentaRequest } from '../dto/request/venta.request';
import { OutResponse } from '../dto/response/out.response';
import { VentaResponse } from '../dto/response/venta.response';

@Injectable()
export class VentaService {

  constructor(private http: HttpClient) { }

  public listarVenta(req: VentaBuscarRequest): Observable<OutResponse<VentaResponse[]>> {
    return this.http.post<OutResponse<VentaResponse[]>>(`${environment.WsPanaderiaBackend}/venta/listarVenta`, req);
  }

  public registrarVenta(req: VentaRequest): Observable<OutResponse<VentaResponse>> {
    return this.http.post<OutResponse<VentaResponse>>(`${environment.WsPanaderiaBackend}/venta/registrarVenta`, req);
  }

  public imprimirTicketVenta(req: VentaRequest): Observable<OutResponse<VentaResponse>> {
    return this.http.post<OutResponse<VentaResponse>>(`${environment.WsPanaderiaBackend}/venta/imprimirTicketVenta`, req);
  }
}
