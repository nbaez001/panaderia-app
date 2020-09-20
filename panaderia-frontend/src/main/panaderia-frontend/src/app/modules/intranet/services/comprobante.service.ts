import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { ComprobanteBuscarRequest } from '../dto/request/comprobante-buscar.request';
import { ComprobanteRequest } from '../dto/request/comprobante.request';
import { ComprobanteResponse } from '../dto/response/comprobante.response';
import { OutResponse } from '../dto/response/out.response';

@Injectable()
export class ComprobanteService {

  constructor(private http: HttpClient) { }

  public listarComprobante(req: ComprobanteBuscarRequest): Observable<OutResponse<ComprobanteResponse[]>> {
    return this.http.post<OutResponse<ComprobanteResponse[]>>(`${environment.WsPanaderiaBackend}/comprobante/listarComprobante`, req);
  }

  public registrarComprobante(req: ComprobanteRequest): Observable<OutResponse<ComprobanteResponse>> {
    return this.http.post<OutResponse<ComprobanteResponse>>(`${environment.WsPanaderiaBackend}/comprobante/registrarComprobante`, req);
  }

  public modificarComprobante(req: ComprobanteRequest): Observable<OutResponse<ComprobanteResponse>> {
    return this.http.post<OutResponse<ComprobanteResponse>>(`${environment.WsPanaderiaBackend}/comprobante/modificarComprobante`, req);
  }

  public eliminarComprobante(req: ComprobanteRequest): Observable<OutResponse<ComprobanteResponse>> {
    return this.http.post<OutResponse<ComprobanteResponse>>(`${environment.WsPanaderiaBackend}/comprobante/eliminarComprobante`, req);
  }
}
