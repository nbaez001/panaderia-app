import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { BdjProductoComponent } from './components/administracion/bdj-producto/bdj-producto.component';
import { BdjMaestraComponent } from './components/administracion/bdj-maestra/bdj-maestra.component';
import { VentaComponent } from './components/venta/venta.component';


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
        path: 'administracion/productos',
        component: BdjProductoComponent,
        data: { title: 'Productos' }
      }, {
        path: 'administracion/maestras',
        component: BdjMaestraComponent,
        data: { title: 'Parametros' }
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(intranetRoutes)],
  exports: [RouterModule]
})
export class IntranetRoutingModule { }
