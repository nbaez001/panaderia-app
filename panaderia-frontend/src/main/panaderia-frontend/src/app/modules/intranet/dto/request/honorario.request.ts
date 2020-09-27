import { HonorarioInsumoRequest } from './honorario-insumo.request';
import { PersonalRequest } from './personal.request';

export class HonorarioRequest {
    id: number;
    personal: PersonalRequest;
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

    listaHonorarioInsumo: HonorarioInsumoRequest;

    constructor() {
        this.personal = new PersonalRequest();
    }
}