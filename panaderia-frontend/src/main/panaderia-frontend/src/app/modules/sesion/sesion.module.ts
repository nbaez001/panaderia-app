import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { SesionRoutingModule } from './sesion-routing.module';
import { LoginComponent } from './components/login/login.component';
import { MaterialModule } from '../material.module';
import { SharedModule } from '../shared.module';


@NgModule({
  declarations: [
    LoginComponent
  ],
  imports: [
    CommonModule,
    SesionRoutingModule,
    SharedModule,
    MaterialModule,
  ]
})
export class SesionModule { }
