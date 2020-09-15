import { DetalleVentaRequest } from './detalle-venta.request';

export class VentaRequest {
    id: number;
    serie: string;
    numero: string;
    total: number;
    flgActivo: number;
    idUsuarioCrea: number;
    fecUsuarioCrea: Date;
    idUsuarioMod: number;
    fecUsuarioMod: Date;

    listaDetalleVenta: DetalleVentaRequest[];
}