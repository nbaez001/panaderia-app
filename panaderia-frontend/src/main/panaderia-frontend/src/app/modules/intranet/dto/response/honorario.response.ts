import { PersonalResponse } from './personal.response';

export class HonorarioResponse {
    id: number;
    personal: PersonalResponse;
    monto: number;
    fechaInicio: Date;
    fechaFin: Date;
    fecha: Date;
    mes: number;
    anio: number;
    flgActivo: number;
    idUsuarioCrea: number;
    fecUsuarioCrea: Date;
    idUsuarioMod: number;
    fecUsuarioMod: Date;
}