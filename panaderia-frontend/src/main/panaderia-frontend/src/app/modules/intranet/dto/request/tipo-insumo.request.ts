export class TipoInsumoRequest {
    id: number;
    idtUnidadMedida: number;
    nomUnidadMedida: number;
    nombre: string;
    codigo: string;
    observacion: string;
    flgActivo: number;

    idUsuarioCrea: number;
    fecUsuarioCrea: Date;
    idUsuarioMod: number;
    fecUsuarioMod: Date;
}