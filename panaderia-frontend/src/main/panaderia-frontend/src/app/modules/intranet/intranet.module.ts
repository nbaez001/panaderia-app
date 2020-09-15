import { NgModule } from '@angular/core';
import { CommonModule, DatePipe, DecimalPipe } from '@angular/common';

import { IntranetRoutingModule } from './intranet-routing.module';
import { HomeComponent } from './components/home/home.component';
import { MaterialModule } from '../material.module';
import { SharedModule } from '../shared.module';
import { NavbarComponent } from './components/navbar/navbar.component';
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


@NgModule({
  entryComponents: [
    RegProductoComponent,
    RegMaestraComponent,
    RegMaestraChildComponent,
    ConfirmDialogComponent,
  ],
  declarations: [
    HomeComponent,
    NavbarComponent,
    BdjProductoComponent,
    BdjMaestraComponent,
    ConfirmDialogComponent,

    RegProductoComponent,
    RegMaestraComponent,
    RegMaestraChildComponent,
    VentaComponent,
    
  ],
  imports: [
    CommonModule,
    IntranetRoutingModule,
    SharedModule,
    MaterialModule,
    LayoutModule,
    IconModule,
  ],
  providers: [
    ...SharedService,
    DatePipe,
    DecimalPipe,
    { provide: MAT_DATE_LOCALE, useValue: 'en-GB' },//DATEPICKER MUESTRA LA FECHA EN FORMATO DD/MM/YYYY
  ]
})
export class IntranetModule { }
