import { TipoInsumoResponse } from '../response/tipo-insumo.response';

export class HonorarioDetalleRequest {
    id: number;
    idHonorario: number;
    idInsumo: number;
    flgActivo: number;

    tipoInsumo: TipoInsumoResponse;
    cantidad: number;
    tarifa: number;
    subtotal: number;
}