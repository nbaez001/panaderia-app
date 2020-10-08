import { NgModule } from '@angular/core';
import { CommonModule, DatePipe, DecimalPipe } from '@angular/common';

import { IntranetRoutingModule } from './intranet-routing.module';
import { HomeComponent } from './components/home/home.component';
import { MaterialModule } from '../material.module';
import { SharedModule } from '../shared.module';
import { NavbarComponent } from './components/shared/navbar/navbar.component';
import { LayoutModule } from '@angular/cdk/layout';
import { BdjProductoComponent } from './components/administracion/bdj-producto/bdj-producto.component';
import { RegProductoComponent } from './components/administracion/bdj-producto/reg-producto/reg-producto.component';
import { IconModule } from '../icon.module';
import { SharedService } from './services/shared.service';
import { BdjMaestraComponent } from './components/administracion/bdj-maestra/bdj-maestra.component';
import { RegMaestraComponent } from './components/administracion/bdj-maestra/reg-maestra/reg-maestra.component';
import { RegMaestraChildComponent } from './components/administracion/bdj-maestra/reg-maestra-child/reg-maestra-child.component';
import { MAT_DATE_LOCALE } from '@angular/material/core';
import { VentaComponent } from './components/venta/venta.component';
import { ConfirmDialogComponent } from './components/shared/confirm-dialog/confirm-dialog.component';
import { BdjComprobanteComponent } from './components/administracion/bdj-comprobante/bdj-comprobante.component';
import { RegComprobanteComponent } from './components/administracion/bdj-comprobante/reg-comprobante/reg-comprobante.component';
import { BdjPersonalComponent } from './components/personal/bdj-personal/bdj-personal.component';
import { BdjInsumosPersonalComponent } from './components/personal/bdj-insumos-personal/bdj-insumos-personal.component';
import { BdjHonorariosComponent } from './components/personal/bdj-honorarios/bdj-honorarios.component';
import { RegPersonalComponent } from './components/personal/bdj-personal/reg-personal/reg-personal.component';
import { RegInsumosPersonalComponent } from './components/personal/bdj-insumos-personal/reg-insumos-personal/reg-insumos-personal.component';
import { BdjTipoInsumoComponent } from './components/administracion/bdj-tipo-insumo/bdj-tipo-insumo.component';
import { RegTipoInsumoComponent } from './components/administracion/bdj-tipo-insumo/reg-tipo-insumo/reg-tipo-insumo.component';
import { RegHonorarioComponent } from './components/personal/bdj-honorarios/reg-honorario/reg-honorario.component';
import { BuscarPersonalComponent } from './components/personal/bdj-honorarios/reg-honorario/buscar-personal/buscar-personal.component';
import { PdfViewerComponent } from './components/shared/pdf-viewer/pdf-viewer.component';
import { NgxExtendedPdfViewerModule } from 'ngx-extended-pdf-viewer';
import { BdjRepVentasComponent } from './components/resportes/bdj-rep-ventas/bdj-rep-ventas.component';
import { BdjRepInsumosComponent } from './components/resportes/bdj-rep-insumos/bdj-rep-insumos.component';


@NgModule({
  entryComponents: [
    ConfirmDialogComponent,
    PdfViewerComponent,

    RegProductoComponent,
    RegMaestraComponent,
    RegMaestraChildComponent,
    RegComprobanteComponent,
    RegPersonalComponent,
    RegInsumosPersonalComponent,
    RegTipoInsumoComponent,
    RegHonorarioComponent,
  ],
  declarations: [
    ConfirmDialogComponent,
    PdfViewerComponent,
    RegProductoComponent,
    RegMaestraComponent,
    RegMaestraChildComponent,
    RegComprobanteComponent,
    RegPersonalComponent,
    RegInsumosPersonalComponent,
    RegTipoInsumoComponent,
    RegHonorarioComponent,

    HomeComponent,
    NavbarComponent,
    VentaComponent,
    BdjProductoComponent,
    BdjMaestraComponent,
    BdjComprobanteComponent,
    BdjPersonalComponent,
    BdjInsumosPersonalComponent,
    BdjHonorariosComponent,
    BdjTipoInsumoComponent,
    BuscarPersonalComponent,
    BdjRepVentasComponent,
    BdjRepInsumosComponent,
  ],
  imports: [
    CommonModule,
    IntranetRoutingModule,
    SharedModule,
    MaterialModule,
    LayoutModule,
    IconModule,
    NgxExtendedPdfViewerModule,
  ],
  providers: [
    ...SharedService,
    DatePipe,
    DecimalPipe,
    { provide: MAT_DATE_LOCALE, useValue: 'en-GB' },//DATEPICKER MUESTRA LA FECHA EN FORMATO DD/MM/YYYY
  ]
})
export class IntranetModule { }
