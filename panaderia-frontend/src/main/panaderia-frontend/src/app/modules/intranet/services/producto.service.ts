import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { ProductoBuscarRequest } from '../dto/request/producto-buscar.request';
import { ProductoRequest } from '../dto/request/producto.request';
import { OutResponse } from '../dto/response/out.response';
import { ProductoResponse } from '../dto/response/producto.response';

@Injectable()
export class ProductoService {
  constructor(private http: HttpClient) { }

  public listarProducto(req: ProductoBuscarRequest): Observable<OutResponse<ProductoResponse[]>> {
    return this.http.post<OutResponse<ProductoResponse[]>>(`${environment.WsPanaderiaBackend}/producto/listarProducto`, req);
  }

  public registrarProducto(req: ProductoRequest): Observable<OutResponse<ProductoResponse>> {
    return this.http.post<OutResponse<ProductoResponse>>(`${environment.WsPanaderiaBackend}/producto/registrarProducto`, req);
  }

  public modificarProducto(req: ProductoRequest): Observable<OutResponse<ProductoResponse>> {
    return this.http.post<OutResponse<ProductoResponse>>(`${environment.WsPanaderiaBackend}/producto/modificarProducto`, req);
  }

  public eliminarProducto(req: ProductoRequest): Observable<OutResponse<ProductoResponse>> {
    return this.http.post<OutResponse<ProductoResponse>>(`${environment.WsPanaderiaBackend}/producto/eliminarProducto`, req);
  }
}
