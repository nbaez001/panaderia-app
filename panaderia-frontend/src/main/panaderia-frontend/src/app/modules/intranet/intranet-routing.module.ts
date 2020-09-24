import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { BdjProductoComponent } from './components/administracion/bdj-producto/bdj-producto.component';
import { BdjMaestraComponent } from './components/administracion/bdj-maestra/bdj-maestra.component';
import { VentaComponent } from './components/venta/venta.component';
import { BdjComprobanteComponent } from './components/administracion/bdj-comprobante/bdj-comprobante.component';
import { BdjInsumosPersonalComponent } from './components/personal/bdj-insumos-personal/bdj-insumos-personal.component';
import { BdjHonorariosComponent } from './components/personal/bdj-honorarios/bdj-honorarios.component';
import { BdjPersonalComponent } from './components/personal/bdj-personal/bdj-personal.component';


const intranetRoutes: Routes = [
  {
    path: '',
    children: [
      {
        path: '',
        redirectTo: 'home',
        pathMatch: 'full'
      }, {
        path: 'home',
        component: HomeComponent,
        data: { title: 'Home' }
      }, {
        path: 'venta',
        component: VentaComponent,
        data: { title: 'Venta' }
      }, {
        path: 'administracion',
        children: [
          {
            path: '',
            redirectTo: 'productos',
            pathMatch: 'full'
          },
          {
            path: 'productos',
            component: BdjProductoComponent,
            data: { title: 'Productos' }
          }, {
            path: 'comprobantes',
            component: BdjComprobanteComponent,
            data: { title: 'Comprobantes' }
          }, {
            path: 'maestras',
            component: BdjMaestraComponent,
            data: { title: 'Parametros' }
          }
        ]
      }, {
        path: 'personal',
        children: [
          {
            path: '',
            redirectTo: 'insumos',
            pathMatch: 'full'
          }, {
            path: 'insumos',
            component: BdjInsumosPersonalComponent,
            data: { title: 'Insumos' }
          }, {
            path: 'honorarios',
            component: BdjHonorariosComponent,
            data: { title: 'Honorarios' }
          }, {
            path: 'persona',
            component: BdjPersonalComponent,
            data: { title: 'Personal' }
          }
        ]
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(intranetRoutes)],
  exports: [RouterModule]
})
export class IntranetRoutingModule { }
