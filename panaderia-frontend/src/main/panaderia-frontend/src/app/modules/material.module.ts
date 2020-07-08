import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { HttpClientModule } from '@angular/common/http';
import { ReactiveFormsModule } from '@angular/forms';


@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    MatButtonModule,

    ReactiveFormsModule,
    HttpClientModule,
  ],
  exports: [
    MatButtonModule,
    ReactiveFormsModule,
    HttpClientModule,
  ]
})
export class MaterialModule { }
