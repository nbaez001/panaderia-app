import { AutenticacionService } from '../../sesion/services/autenticacion.service';
import { ComprobanteService } from './comprobante.service';
import { MaestraService } from './maestra.service';
import { ProductoService } from './producto.service';
import { VentaService } from './venta.service';

export const SharedService = [
    MaestraService,
    AutenticacionService,
    ProductoService,
    VentaService,
    ComprobanteService,
];