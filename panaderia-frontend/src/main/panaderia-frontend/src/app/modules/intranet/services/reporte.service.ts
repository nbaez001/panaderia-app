import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { ReporteInsumoBuscarRequest } from '../dto/request/reporte-insumo-buscar.request';
import { ReporteVentaBuscarRequest } from '../dto/request/reporte-venta-buscar.request';
import { FileResponse } from '../dto/response/file.response';
import { OutResponse } from '../dto/response/out.response';
import { ReporteInsumoResponse } from '../dto/response/reporte-insumo.response';
import { ReporteVentaResponse } from '../dto/response/reporte-venta.response';

@Injectable()
export class ReporteService {

  constructor(private http: HttpClient) { }

  public validarArchivosReporte(): Observable<OutResponse<any>> {
    return this.http.post<OutResponse<any>>(`${environment.WsPanaderiaBackend}/reporte/validarReportes`, {});
  }

  public listarReporteInsumo(req: ReporteInsumoBuscarRequest): Observable<OutResponse<ReporteInsumoResponse[]>> {
    return this.http.post<OutResponse<ReporteInsumoResponse[]>>(`${environment.WsPanaderiaBackend}/reporte/listarReporteInsumo`, req);
  }

  public listarReporteVenta(req: ReporteVentaBuscarRequest): Observable<OutResponse<ReporteVentaResponse[]>> {
    return this.http.post<OutResponse<ReporteVentaResponse[]>>(`${environment.WsPanaderiaBackend}/reporte/listarReporteVenta`, req);
  }

  public generarReporteInsumoPDF(req: ReporteInsumoBuscarRequest): Observable<OutResponse<FileResponse>> {
    return this.http.post<OutResponse<FileResponse>>(`${environment.WsPanaderiaBackend}/reporte/generarReporteInsumoPDF`, req);
  }

  public generarReporteVentaPDF(req: ReporteVentaBuscarRequest): Observable<OutResponse<FileResponse>> {
    return this.http.post<OutResponse<FileResponse>>(`${environment.WsPanaderiaBackend}/reporte/generarReporteVentaPDF`, req);
  }

  public convertToBlobFromByte(fResp: FileResponse): Blob {
    const byteCharacters = atob(fResp.data);
    const byteNumbers = new Array(byteCharacters.length);

    for (let i = 0; i < byteCharacters.length; i++) {
      byteNumbers[i] = byteCharacters.charCodeAt(i);
    }

    const byteArray = new Uint8Array(byteNumbers);
    const blob = new Blob([byteArray], { type: fResp.type });
    const resultBlob: any = blob;
    resultBlob.lastModifiedDate = new Date();
    resultBlob.name = fResp.nombre;

    return blob;
  }

  public DownloadBlobFile(blob: Blob): void {
    console.log(blob);
    const link = document.createElement('a');
    link.target = '_blank';
    link.href = window.URL.createObjectURL(blob);
    link.click();
  }

}
