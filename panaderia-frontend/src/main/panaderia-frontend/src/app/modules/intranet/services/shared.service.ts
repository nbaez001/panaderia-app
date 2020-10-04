import { AutenticacionService } from '../../sesion/services/autenticacion.service';
import { ComprobanteService } from './comprobante.service';
import { HonorarioService } from './honorario.service';
import { InsumoService } from './insumo.service';
import { MaestraService } from './maestra.service';
import { PersonaService } from './persona.service';
import { PersonalService } from './personal.service';
import { ProductoService } from './producto.service';
import { ReporteService } from './reporte.service';
import { UbigeoService } from './ubigeo.service';
import { VentaService } from './venta.service';

export const SharedService = [
    MaestraService,
    AutenticacionService,
    ProductoService,
    VentaService,
    ComprobanteService,
    PersonaService,
    PersonalService,
    UbigeoService,
    InsumoService,
    HonorarioService,
    ReporteService,
];