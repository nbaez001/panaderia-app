import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { ResponsiveRowsDirective } from '../core/directives/responsive-rows.directive';
import { FlexLayoutModule } from '@angular/flex-layout';
import { UppercasedDirective } from '../core/directives/uppercased.directive';



@NgModule({
  declarations: [
    ResponsiveRowsDirective,
    UppercasedDirective,
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    HttpClientModule,
    FlexLayoutModule,
  ],
  exports: [
    ResponsiveRowsDirective,
    UppercasedDirective,

    ReactiveFormsModule,
    HttpClientModule,
    FlexLayoutModule,
  ]
})
export class SharedModule { }
