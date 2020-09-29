import { InsumoResponse } from '../response/insumo.response';
import { HonorarioDetalleRequest } from './honorario-detalle.request';
import { PersonalRequest } from './personal.request';

export class HonorarioRequest {
    id: number;
    personal: PersonalRequest;
    monto: number;
    fechaInicio: Date;
    fechaFin: Date;
    fecha: Date;
    flgActivo: number;
    idUsuarioCrea: number;
    fecUsuarioCrea: Date;
    idUsuarioMod: number;
    fecUsuarioMod: Date;

    listaInsumo: InsumoResponse[];
    listaHonorarioDetalle: HonorarioDetalleRequest[];

    constructor() {
        this.personal = new PersonalRequest();
    }
}