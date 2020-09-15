export class DetalleVentaRequest {
    id: number;
    idProducto: number;
    nomProducto: string;
    idVenta: number;
    cantidad: number;
    precio: number;
    subtotal: number;
    flagActivo: number;
    idUsuarioCrea: number;
    fecUsuarioCrea: Date;
    idUsuarioMod: number;
    fecUsuarioMod: Date;
}