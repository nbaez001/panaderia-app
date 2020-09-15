import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { SesionRoutingModule } from './sesion-routing.module';
import { LoginComponent } from './components/login/login.component';
import { MaterialModule } from '../material.module';
import { SharedModule } from '../shared.module';
import { SharedService } from '../intranet/services/shared.service';


@NgModule({
  declarations: [
    LoginComponent
  ],
  imports: [
    CommonModule,
    SesionRoutingModule,
    SharedModule,
    MaterialModule,
  ],
  providers:[
    ...SharedService
  ]
})
export class SesionModule { }
