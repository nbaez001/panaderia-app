import { PersonalResponse } from './personal.response';
import { TipoInsumoResponse } from './tipo-insumo.response';

export class InsumoResponse {
    id: number;
    personal: PersonalResponse;
    tipoInsumo: TipoInsumoResponse;
    cantidad: number;
    fecha: Date;
    flgActivo: number;

    idUsuarioCrea: number;
    fecUsuarioCrea: Date;
    idUsuarioMod: number;
    fecUsuarioMod: Date;
}