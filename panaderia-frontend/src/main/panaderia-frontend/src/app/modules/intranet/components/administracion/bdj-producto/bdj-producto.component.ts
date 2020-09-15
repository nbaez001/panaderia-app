import { Component, Inject, OnInit, ViewChild } from '@angular/core';
import { FormGroup, FormBuilder } from '@angular/forms';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatDialog } from '@angular/material/dialog';
import { ProductoResponse } from '../../../dto/response/producto.response';
import { RegProductoComponent } from './reg-producto/reg-producto.component';
import { MENSAJES } from 'src/app/common';
import { MaestraBuscarRequest } from '../../../dto/request/maestra-buscar.request';
import { TABLAS_MAESTRA } from "src/app/common";
import { MaestraService } from '../../../services/maestra.service';
import { MaestraResponse } from '../../../dto/response/maestra.response';
import { OutResponse } from '../../../dto/response/out.response';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ProductoService } from '../../../services/producto.service';
import { ProductoBuscarRequest } from '../../../dto/request/producto-buscar.request';
import { DatePipe, DecimalPipe } from '@angular/common';

@Component({
  selector: 'app-bdj-producto',
  templateUrl: './bdj-producto.component.html',
  styleUrls: ['./bdj-producto.component.scss']
})
export class BdjProductoComponent implements OnInit {
  // listaUnidadMedida: MaestraResponse[] = [];

  formularioGrp: FormGroup;
  messages = {};
  formErrors = {};

  displayedColumns: string[];
  dataSource: MatTableDataSource<ProductoResponse>;
  isLoading: boolean = false;

  listaProductos: ProductoResponse[] = [];
  columnsGrilla = [
    {
      columnDef: 'nombre',
      header: 'Nombre',
      cell: (producto: ProductoResponse) => `${(producto.nombre) ? producto.nombre : ''}`
    },
    {
      columnDef: 'precio',
      header: 'Precio',
      cell: (producto: ProductoResponse) => `${(producto.precio) ? this.decimalPipe.transform(producto.precio, '1.2-2') : ''}`
    }
  ];

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor(private fb: FormBuilder, public dialog: MatDialog,
    private decimalPipe: DecimalPipe,
    private _snackBar: MatSnackBar,
    @Inject(MaestraService) private maestraService: MaestraService,
    @Inject(ProductoService) private productoService: ProductoService,) { }

  ngOnInit(): void {
    // this.spinnerService.show();

    this.formularioGrp = this.fb.group({
      indicio: ['', []],
      fechaInicio: ['', []],
      fechaFin: ['', []],
    });

    this.definirTabla();
    this.inicializarVariables();
  }

  definirTabla(): void {
    this.displayedColumns = [];
    this.columnsGrilla.forEach(c => {
      this.displayedColumns.push(c.columnDef);
    });
    this.displayedColumns.unshift('id');
    this.displayedColumns.push('opt');
  }

  public inicializarVariables(): void {
    // this.comboUnidadMedida();
    this.buscar();
    // this.spinnerService.hide();
  }

  cargarDatosTabla(): void {
    this.dataSource = null;
    if (this.listaProductos.length > 0) {
      this.dataSource = new MatTableDataSource(this.listaProductos);
      this.dataSource.paginator = this.paginator;
      this.dataSource.sort = this.sort;
    }
  }

  // comboUnidadMedida(): void {
  //   let maestra = new MaestraBuscarRequest();
  //   maestra.idTabla = TABLAS_MAESTRA.UNIDAD_MEDIDA.ID;
  //   this.maestraService.listarMaestra(maestra).subscribe(
  //     (data: OutResponse<MaestraResponse[]>) => {
  //       if (data.rCodigo == 0) {
  //         this.listaUnidadMedida = data.result;
  //       } else {
  //         this._snackBar.open(data.rMensaje, null, { duration: 5000, horizontalPosition: 'right', verticalPosition: 'top', panelClass: ['warning-snackbar'] });
  //       }
  //     }, error => {
  //       console.log(error);
  //       this._snackBar.open(error, null, { duration: 5000, horizontalPosition: 'right', verticalPosition: 'top', panelClass: ['error-snackbar'] });
  //     }
  //   );
  // }

  buscar(): void {
    let request = new ProductoBuscarRequest();

    this.dataSource = null;

    this.isLoading = true;
    this.productoService.listarProducto(request).subscribe(
      (data: OutResponse<ProductoResponse[]>) => {
        if (data.rCodigo == 0) {
          this.listaProductos = data.result;

          this.cargarDatosTabla();
          this.isLoading = false;
        } else {
          this._snackBar.open(data.rMensaje, null, { duration: 5000, horizontalPosition: 'right', verticalPosition: 'top', panelClass: ['warning-snackbar'] });
          this.isLoading = false;
        }
      }, error => {
        this._snackBar.open(error, null, { duration: 5000, horizontalPosition: 'right', verticalPosition: 'top', panelClass: ['error-snackbar'] });
        this.isLoading = false;
      }
    );
  }

  registrar(obj): void {
    console.log('MODAL');
    console.log(obj);
    const dialogRef = this.dialog.open(RegProductoComponent, {
      width: '600px',
      data: { title: MENSAJES.INTRANET.BANDEJA_PRODUCTOS.PRODUCTO.REGISTRAR.TITLE, objeto: obj }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.listaProductos.unshift(result);
        this.cargarDatosTabla();
      }
    });
  }

  exportarExcel(): void {

  }

  editar(p: any): void {

  }

}
