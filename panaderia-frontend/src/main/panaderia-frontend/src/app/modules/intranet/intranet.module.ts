import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { IntranetRoutingModule } from './intranet-routing.module';
import { HomeComponent } from './components/home/home.component';
import { MaterialModule } from '../material.module';
import { SharedModule } from '../shared.module';
import { NavbarComponent } from './components/navbar/navbar.component';
import { LayoutModule } from '@angular/cdk/layout';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';
import { BdjProductoComponent } from './components/administracion/bdj-producto/bdj-producto.component';
import { RegProductoComponent } from './components/administracion/bdj-producto/reg-producto/reg-producto.component';


@NgModule({
  entryComponents: [
    RegProductoComponent,
  ],
  declarations: [
    HomeComponent,
    NavbarComponent,
    BdjProductoComponent,

    RegProductoComponent,
  ],
  imports: [
    CommonModule,
    IntranetRoutingModule,
    SharedModule,
    MaterialModule,
    LayoutModule,
    MatToolbarModule,
    MatButtonModule,
    MatSidenavModule,
    MatIconModule,
    MatListModule,
  ]
})
export class IntranetModule { }
