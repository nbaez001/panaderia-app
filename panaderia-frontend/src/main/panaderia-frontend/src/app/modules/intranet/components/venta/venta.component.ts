import { DatePipe, DecimalPipe } from '@angular/common';
import { Component, ElementRef, HostListener, Inject, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Observable, of } from 'rxjs';
import { KEY_CODES, MENSAJES } from 'src/app/common';
import { FormService } from 'src/app/core/services/form.service';
import { UsuarioService } from 'src/app/core/services/usuario.service';
import { ComprobanteBuscarRequest } from '../../dto/request/comprobante-buscar.request';
import { DetalleVentaRequest } from '../../dto/request/detalle-venta.request';
import { ProductoBuscarRequest } from '../../dto/request/producto-buscar.request';
import { ReporteVentaBuscarRequest } from '../../dto/request/reporte-venta-buscar.request';
import { VentaBuscarRequest } from '../../dto/request/venta-buscar.request';
import { VentaRequest } from '../../dto/request/venta.request';
import { ComprobanteResponse } from '../../dto/response/comprobante.response';
import { OutResponse } from '../../dto/response/out.response';
import { ProductoResponse } from '../../dto/response/producto.response';
import { VentaResponse } from '../../dto/response/venta.response';
import { ComprobanteService } from '../../services/comprobante.service';
import { ProductoService } from '../../services/producto.service';
import { ReporteService } from '../../services/reporte.service';
import { VentaService } from '../../services/venta.service';
import { ConfirmDialogComponent } from '../shared/confirm-dialog/confirm-dialog.component';

@Component({
  selector: 'app-venta',
  templateUrl: './venta.component.html',
  styleUrls: ['./venta.component.scss']
})
export class VentaComponent implements OnInit {
  comprobante: ComprobanteResponse;
  venta: boolean = false;
  pendConfirmacion: boolean = false;

  formularioGrp: FormGroup;
  formErrors: any;

  listaDetalleVenta: DetalleVentaRequest[] = [];

  listaProductos: ProductoResponse[];

  displayedColumns: string[];
  dataSource: MatTableDataSource<DetalleVentaRequest>;
  isLoading: boolean = false;

  columnsGrilla = [
    {
      columnDef: 'nombre',
      header: 'Nombre',
      cell: (m: DetalleVentaRequest) => `${m.nomProducto}`
    }, {
      columnDef: 'precio',
      header: 'Precio',
      cell: (m: DetalleVentaRequest) => (m.precio != null) ? `S/. ${this.decimalPipe.transform(m.precio, '1.2-2')}` : ''
    }, {
      columnDef: 'subtotal',
      header: 'Subtotal',
      cell: (m: DetalleVentaRequest) => (m.subtotal != null) ? `S/. ${this.decimalPipe.transform(m.subtotal, '1.2-2')}` : ''
    }];

  montoTotal: number;
  listaVentaResponse: VentaResponse[];
  displayedColumns2: string[];
  dataSource2: MatTableDataSource<VentaResponse>;
  isLoading2: boolean = false;

  columnsGrilla2 = [
    {
      columnDef: 'numeroVenta',
      header: 'Numero venta',
      cell: (m: VentaResponse) => `${m.serie ? m.serie : ''}-${m.numero ? m.numero : ''}`
    }, {
      columnDef: 'total',
      header: 'Total',
      cell: (m: VentaResponse) => `${m.total ? 'S/.' + this.decimalPipe.transform(m.total, '1.2-2') : ''}`
    }, {
      columnDef: 'fecha',
      header: 'Fecha',
      cell: (m: VentaResponse) => `${m.fecUsuarioCrea ? this.datePipe.transform(m.fecUsuarioCrea, 'dd/MM/yyyy') : ''}`
    }];

  listaTipoReporte: any[] = [{ id: 1, nombre: 'DIARIO' }, { id: 2, nombre: 'MENSUAL' }];

  @ViewChild(MatPaginator) paginator2: MatPaginator;
  @ViewChild(MatSort) sort2: MatSort;

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  @ViewChild('producto') producto: ElementRef;
  @ViewChild('subtotal') subtotal: ElementRef;

  constructor(private fb: FormBuilder,
    private dialog: MatDialog,
    private decimalPipe: DecimalPipe,
    private datePipe: DatePipe,
    // @Inject(MaestraService) private maestraService: MaestraService,
    @Inject(FormService) private formService: FormService,
    @Inject(UsuarioService) private user: UsuarioService,
    @Inject(ProductoService) private productoService: ProductoService,
    @Inject(VentaService) private ventaService: VentaService,
    @Inject(ComprobanteService) private comprobanteService: ComprobanteService,
    @Inject(ReporteService) private reporteService: ReporteService,
    private _snackBar: MatSnackBar) { }

  @HostListener('document:keydown', ['$event']) onKeydownHandler(event: KeyboardEvent) {
    if (!this.pendConfirmacion) {
      if (event.key === KEY_CODES.ESCAPE) {
        this.limpiarTodo();
      } else if (event.key == KEY_CODES.END) {
        this.finalizar();
      } else if (event.key == KEY_CODES.ENTER) {
        this.evaluarAcccion();
      }
    }
  }

  ngOnInit(): void {
    this.formularioGrp = this.fb.group({
      producto: ['', [Validators.required]],
      subtotal: ['', [Validators.required]],
    });
    this.formErrors = this.formService.buildFormErrors(this.formularioGrp, this.formErrors);
    this.formularioGrp.valueChanges.subscribe((val: any) => {
      this.formService.getValidationErrors(this.formularioGrp, this.formErrors, false);
    });

    this.formularioGrp.get('producto').valueChanges.subscribe(data => {
      const filterValue = (typeof data == 'string') ? data.toUpperCase() : null;
      if (filterValue) {
        this._buscarProducto(filterValue);
      }
    });

    setTimeout(() => {
      this.focusProductoControl();
    }, 0);

    this.inicializarVariables();
  }

  inicializarVariables(): void {
    this.definirTabla();
    this.definirTabla2();
    this.comboComprobante();
    this.listaVentas();
  }

  definirTabla(): void {
    this.displayedColumns = [];
    this.columnsGrilla.forEach(c => {
      this.displayedColumns.push(c.columnDef);
    });
    this.displayedColumns.unshift('id');
    this.displayedColumns.push('opt');
  }

  public cargarDatosTabla(): void {
    this.dataSource = new MatTableDataSource(this.listaDetalleVenta);
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  definirTabla2(): void {
    this.displayedColumns2 = [];
    this.columnsGrilla2.forEach(c => {
      this.displayedColumns2.push(c.columnDef);
    });
    this.displayedColumns2.unshift('id');
    this.displayedColumns2.push('opt');
  }

  public cargarDatosTabla2(): void {
    if (this.listaVentaResponse.length > 0) {
      this.dataSource2 = new MatTableDataSource(this.listaVentaResponse);
      this.dataSource2.paginator = this.paginator2;
      this.dataSource2.sort = this.sort2;
      this.calcularMontoTotal(this.listaVentaResponse);
    }
  }

  calcularMontoTotal(list: VentaResponse[]): void {
    this.montoTotal = 0.0;
    list.forEach(el => {
      this.montoTotal += el.total;
    })
  }

  listaVentas(): void {
    this.isLoading2 = true;

    let req = new VentaBuscarRequest();
    req.fecInicio = new Date();
    req.fecFin = new Date();

    console.log(req);

    this.ventaService.listarVenta(req).subscribe(
      (data: OutResponse<VentaResponse[]>) => {
        console.log(data);
        if (data.rCodigo == 0) {
          this.listaVentaResponse = data.rResult;
        } else {
          this._snackBar.open(data.rMensaje, null, { duration: 5000, horizontalPosition: 'right', verticalPosition: 'top', panelClass: ['warning-snackbar'] });
          this.listaVentaResponse = [];
        }
        this.cargarDatosTabla2();
        this.isLoading2 = false;
      },
      error => {
        console.log(error);
        this._snackBar.open(error.statusText, null, { duration: 5000, horizontalPosition: 'right', verticalPosition: 'top', panelClass: ['error-snackbar'] });
        this.isLoading2 = false;
      }
    );
  }

  private comboComprobante(): void {
    let req = new ComprobanteBuscarRequest();
    req.flgActual = 1;

    this.comprobanteService.listarComprobante(req).subscribe(
      (data: OutResponse<ComprobanteResponse[]>) => {
        console.log(data);
        if (data.rCodigo == 0) {
          this.comprobante = data.rResult[0];
        } else {
          console.log(data.rMensaje);
          this.comprobante = null;
        }
      }, error => {
        console.log(error);
        this.comprobante = null;
      }
    )
  }

  private _buscarProducto(value: any): void {
    let req = new ProductoBuscarRequest();
    req.nombre = value;
    this.productoService.listarProducto(req).subscribe(
      (data: OutResponse<ProductoResponse[]>) => {
        if (data.rCodigo == 0) {
          this.listaProductos = data.rResult;
        } else {
          console.log(data.rMensaje);
          this.listaProductos = [];
        }
      }, error => {
        console.log(error);
        this.listaProductos = [];
      }
    )
  }

  displayFn(obj) {
    return obj ? obj.nombre : undefined;
  }

  seleccionado(evt) {
    console.log(evt);
  }

  agregar(): void {
    if (this.formularioGrp.valid) {
      let mae = new DetalleVentaRequest();
      mae.id = 0;
      mae.idProducto = this.formularioGrp.get('producto').value.id;
      mae.nomProducto = this.formularioGrp.get('producto').value.nombre;
      mae.subtotal = this.formularioGrp.get('subtotal').value;
      mae.precio = this.formularioGrp.get('producto').value.precio;
      mae.cantidad = parseFloat((mae.subtotal / mae.precio).toFixed(2));
      mae.subtotal = this.formularioGrp.get('subtotal').value;
      mae.flagActivo = 1;

      console.log(mae);
      this.listaDetalleVenta.push(mae);
      this.cargarDatosTabla();
      this.limpiar();

      this.focusProductoControl();
    } else {
      this.formService.getValidationErrors(this.formularioGrp, this.formErrors, true);
    }
  }

  limpiar(): void {
    this.listaProductos = [];
    this.formService.setAsUntoched(this.formularioGrp, this.formErrors);
  }

  limpiarTodo() {
    this.listaDetalleVenta = [];
    this.limpiar();
    this.cargarDatosTabla();
  }

  finalizar(): void {
    if (this.comprobante) {
      if (this.listaDetalleVenta.length > 0) {
        this.pendConfirmacion = true;

        const dialogRef = this.dialog.open(ConfirmDialogComponent, {
          width: '300px',
          disableClose: true,
          data: {
            titulo: MENSAJES.INTRANET.MSG_CONFIRMACION,
            objeto: null
          }
        });

        dialogRef.afterClosed().subscribe((result) => {
          if (result == 1) {
            this.venta = true;

            let total = 0.0;
            this.listaDetalleVenta.forEach(val => {
              total += val.subtotal;
            });

            let req = new VentaRequest();
            req.idComprobante = this.comprobante.id;
            req.total = total;
            req.flgActivo = 1;
            req.idUsuarioCrea = this.user.getId;
            req.fecUsuarioCrea = new Date();

            req.listaDetalleVenta = this.listaDetalleVenta;

            this.ventaService.registrarVenta(req).subscribe(
              (data: OutResponse<VentaResponse>) => {
                console.log(data);
                if (data.rCodigo == 0) {
                  console.log('VENTA EXITOSA');
                  this._snackBar.open('VENTA EXITOSA', null, { duration: 5000, horizontalPosition: 'right', verticalPosition: 'top', panelClass: ['success-snackbar'] });

                  this.limpiarTodo();
                  this.comboComprobante();

                  this.listaVentaResponse.unshift(data.rResult);
                  this.cargarDatosTabla2();
                } else {
                  console.log(data.rMensaje);
                  this._snackBar.open(data.rMensaje, null, { duration: 5000, horizontalPosition: 'right', verticalPosition: 'top', panelClass: ['warning-snackbar'] });
                }
                this.venta = false;
              }, error => {
                console.log(error);
                this.venta = false;
                this._snackBar.open(error, null, { duration: 5000, horizontalPosition: 'right', verticalPosition: 'top', panelClass: ['warning-snackbar'] });
              }
            )
          }
          this.pendConfirmacion = false;
        });
      } else {
        this._snackBar.open('AGREGUE AL MENOS UN PRODUCTO', null, { duration: 5000, horizontalPosition: 'right', verticalPosition: 'top', panelClass: ['warning-snackbar'] });
      }
    } else {
      this._snackBar.open('CONFIGURE LA NUMERACION TICKETS EN EL MENU ADMINISTRACION', null, { duration: 5000, horizontalPosition: 'right', verticalPosition: 'top', panelClass: ['warning-snackbar'] });
    }
  }

  eliminar(obj: DetalleVentaRequest): void {
    let index = this.listaDetalleVenta.indexOf(obj);
    this.listaDetalleVenta.splice(index, 1);
    this.cargarDatosTabla();
  }

  focusProductoControl() {
    this.producto.nativeElement.focus();
  }

  focusSubtotalControl() {
    this.subtotal.nativeElement.focus();
  }

  evaluarAcccion(): void {
    let producto = this.formularioGrp.get('producto').value;
    let subtotal = this.formularioGrp.get('subtotal').value;
    console.log(typeof (producto));
    console.log(typeof (subtotal));


    if (producto) {
      if (typeof producto != 'string') {
        //PRODUCTO SELECCIONADO
        if (subtotal) {
          //SUBTOTAL INGRESADO
          this.agregar();
        } else {
          this.focusSubtotalControl();
        }
      } else {
        this.focusProductoControl();
      }
    } else {
      this.focusProductoControl();
    }
  }

  imprimir(row: VentaResponse): void {
    this.pendConfirmacion = true;
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      width: '300px',
      disableClose: true,
      data: {
        titulo: MENSAJES.INTRANET.MSG_CONFIRMACION,
        objeto: null
      }
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result == 1) {
        let req = new VentaRequest();
        req.id = row.id;

        this.ventaService.imprimirTicketVenta(req).subscribe(
          (data: OutResponse<any>) => {
            if (data.rCodigo == 0) {
              this._snackBar.open('IMPRESION EXITOSA', null, { duration: 5000, horizontalPosition: 'right', verticalPosition: 'top', panelClass: ['success-snackbar'] });
            } else {
              console.log(data.rMensaje);
              this._snackBar.open(data.rMensaje, null, { duration: 5000, horizontalPosition: 'right', verticalPosition: 'top', panelClass: ['warning-snackbar'] });
            }
          }, error => {
            console.log(error);
            this._snackBar.open(error, null, { duration: 5000, horizontalPosition: 'right', verticalPosition: 'top', panelClass: ['warning-snackbar'] });
          }
        )
      }
      this.pendConfirmacion = false;
    });
  }

}
