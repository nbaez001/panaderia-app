import { PersonalRequest } from './personal.request';
import { TipoInsumoRequest } from './tipo-insumo.request';

export class InsumoRequest {
    id: number;
    personal: PersonalRequest;
    tipoInsumo: TipoInsumoRequest;
    cantidad: number;
    fecha: Date;
    flgActivo: number;

    idUsuarioCrea: number;
    fecUsuarioCrea: Date;
    idUsuarioMod: number;
    fecUsuarioMod: Date;

    constructor() {
        this.personal = new PersonalRequest();
        this.tipoInsumo = new TipoInsumoRequest();
    }
}