import { PersonalResponse } from './personal.response';
import { TipoInsumoResponse } from './tipo-insumo.response';

export class ReporteInsumoResponse {
    anio: string;
    mes: string;
    fecha: string;
    idPersonal: number;
    nomPersonal: string;
    idTipoInsumo: number;
    nomTipoInsumo: string;
    nomUnidadMedida: string;
    suma: number;
}