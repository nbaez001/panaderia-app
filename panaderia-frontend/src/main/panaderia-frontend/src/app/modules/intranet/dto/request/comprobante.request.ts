export class ComprobanteRequest {
    id: number;
    idtTipoComprobante: number;
    nomTipoComprobante: string;
    nombre: string;
    serie: number;
    numero: number;
    longSerie: number;
    longNumero: number;
    flgActual: number;
    flgActivo: number;
    flgUsado: number;
    idUsuarioCrea: number;
    fecUsuarioCrea: Date;
    idUsuarioMod: number;
    fecUsuarioMod: Date;
}