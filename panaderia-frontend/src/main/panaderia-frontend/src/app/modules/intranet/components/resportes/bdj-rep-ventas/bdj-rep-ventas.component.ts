import { DatePipe, DecimalPipe } from '@angular/common';
import { Component, Inject, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MAT_MOMENT_DATE_ADAPTER_OPTIONS, MomentDateAdapter } from '@angular/material-moment-adapter';
import { DateAdapter, MAT_DATE_FORMATS, MAT_DATE_LOCALE } from '@angular/material/core';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import * as _moment from 'moment';
import { NgxSpinnerService } from 'ngx-spinner';
import { FormService } from 'src/app/core/services/form.service';
import { ProductoBuscarRequest } from '../../../dto/request/producto-buscar.request';
import { ReporteVentaBuscarRequest } from '../../../dto/request/reporte-venta-buscar.request';
import { OutResponse } from '../../../dto/response/out.response';
import { ProductoResponse } from '../../../dto/response/producto.response';
import { ReporteVentaResponse } from '../../../dto/response/reporte-venta.response';
import { ProductoService } from '../../../services/producto.service';
import { ReporteService } from '../../../services/reporte.service';
import { MY_FORMATS } from '../bdj-rep-insumos/bdj-rep-insumos.component';
import { defaultFormat as _rollupMoment, Moment } from 'moment';
import { MatDatepicker } from '@angular/material/datepicker';
import { UsuarioService } from 'src/app/core/services/usuario.service';
import { FileResponse } from '../../../dto/response/file.response';
import { PdfViewerComponent } from '../../shared/pdf-viewer/pdf-viewer.component';

@Component({
  selector: 'app-bdj-rep-ventas',
  templateUrl: './bdj-rep-ventas.component.html',
  styleUrls: ['./bdj-rep-ventas.component.scss'],
  providers: [
    // `MomentDateAdapter` can be automatically provided by importing `MomentDateModule` in your
    // application's root module. We provide it at the component level here, due to limitations of
    // our example generation script.
    {
      provide: DateAdapter,
      useClass: MomentDateAdapter,
      deps: [MAT_DATE_LOCALE, MAT_MOMENT_DATE_ADAPTER_OPTIONS]
    },
    { provide: MAT_DATE_FORMATS, useValue: MY_FORMATS },
  ],
})
export class BdjRepVentasComponent implements OnInit { 
  exportar = false;
  mesFinM: Date;

  listaTipoReporte: any[] = [{ id: 1, nombre: 'DIARIO' }, { id: 2, nombre: 'MENSUAL' }];

  listaProductoResponse: ProductoResponse[];

  listaReporteVentaResponse: ReporteVentaResponse[];
  displayedColumns: string[];
  dataSource: MatTableDataSource<ReporteVentaResponse>;
  isLoading: boolean = false;

  formularioGrp: FormGroup;
  formErrors: any;
  columnsGrilla = [
    {
      columnDef: 'anio',
      header: 'Año',
      cell: (m: ReporteVentaResponse) => `${m.anio ? m.anio : ''}`
    }, {
      columnDef: 'mes',
      header: 'Mes',
      cell: (m: ReporteVentaResponse) => `${m.mes ? m.mes : ''}`
    }, {
      columnDef: 'fecha',
      header: 'Fecha',
      cell: (m: ReporteVentaResponse) => `${m.fecha ? m.fecha : ''}`
    }, {
      columnDef: 'nomProducto',
      header: 'Producto',
      cell: (m: ReporteVentaResponse) => `${m.nomProducto ? m.nomProducto : ''}`
    }, {
      columnDef: 'nomUnidadMedida',
      header: 'Unidad medida',
      cell: (m: ReporteVentaResponse) => `${m.nomUnidadMedida ? m.nomUnidadMedida : ''}`
    }, {
      columnDef: 'cantidad',
      header: 'Cantidad',
      cell: (m: ReporteVentaResponse) => `${m.cantidad ? this.decimalPipe.transform(m.cantidad, '1.1-1') : ''}`
    }, {
      columnDef: 'suma',
      header: 'Monto total',
      cell: (m: ReporteVentaResponse) => `${m.suma ? ('S/. ' + this.decimalPipe.transform(m.suma, '1.2-2')) : ''}`
    }];

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor(private fb: FormBuilder,
    public dialog: MatDialog,
    private spinner: NgxSpinnerService,
    @Inject(UsuarioService) private usuarioService: UsuarioService,
    @Inject(ProductoService) private productoService: ProductoService,
    @Inject(ReporteService) private reporteService: ReporteService,
    @Inject(FormService) private formService: FormService,
    private _snackBar: MatSnackBar,
    private datePipe: DatePipe,
    private decimalPipe: DecimalPipe,
  ) { }

  ngOnInit() {
    this.formularioGrp = this.fb.group({
      tipoReporte: [{ value: this.listaTipoReporte[0], disabled: false }, []],
      tipoInsumo: ['', []],
      producto: ['', []],
      fecInicio: [{ value: new Date(), disabled: false }, [Validators.required]],
      fecFin: [{ value: new Date(), disabled: false }, [Validators.required]],
      mesInicio: [{ value: _moment(), disabled: false }, []],
      mesFin: [{ value: _moment(), disabled: false }, []],
      mesFinM: [{ value: '', disabled: false }, []],
      mesInicioM: [{ value: '', disabled: false }, []],
    });
    this.formErrors = this.formService.buildFormErrors(this.formularioGrp, this.formErrors);

    this.formularioGrp.get('mesFin').valueChanges.subscribe((val: any) => {
      if (val) {
        this.formularioGrp.get('mesFinM').setValue(this.datePipe.transform(val, 'MM/yyyy'));
      }
    });

    this.formularioGrp.get('mesInicio').valueChanges.subscribe((val: any) => {
      if (val) {
        this.formularioGrp.get('mesInicioM').setValue(this.datePipe.transform(val, 'MM/yyyy'));
      }
    });

    this.formularioGrp.get('tipoReporte').valueChanges.subscribe((val: any) => {
      this.changeValidators();
    });

    this.definirTabla();
    this.inicializarVariables();
  }

  inicializarVariables(): void {
    this.comboProducto();
    this.buscar();
  }

  changeValidators(): void {
    let idTipoReporte = this.formularioGrp.get('tipoReporte').value.id;

    if (idTipoReporte == 1) {
      this.formularioGrp.controls['fecInicio'].clearValidators();
      this.formularioGrp.controls['fecInicio'].setValidators([Validators.required]);
      this.formularioGrp.controls['fecInicio'].updateValueAndValidity();

      this.formularioGrp.controls['fecFin'].clearValidators();
      this.formularioGrp.controls['fecFin'].setValidators([Validators.required]);
      this.formularioGrp.controls['fecFin'].updateValueAndValidity();

      this.formularioGrp.controls['mesFinM'].clearValidators();
      this.formularioGrp.controls['mesFinM'].updateValueAndValidity();

      this.formularioGrp.controls['mesInicioM'].clearValidators();
      this.formularioGrp.controls['mesInicioM'].updateValueAndValidity();
    } else {
      this.formularioGrp.controls['mesFinM'].clearValidators();
      this.formularioGrp.controls['mesFinM'].setValidators([Validators.required]);
      this.formularioGrp.controls['mesFinM'].updateValueAndValidity();

      this.formularioGrp.controls['mesInicioM'].clearValidators();
      this.formularioGrp.controls['mesInicioM'].setValidators([Validators.required]);
      this.formularioGrp.controls['mesInicioM'].updateValueAndValidity();

      this.formularioGrp.controls['fecInicio'].clearValidators();
      this.formularioGrp.controls['fecInicio'].updateValueAndValidity();

      this.formularioGrp.controls['fecFin'].clearValidators();
      this.formularioGrp.controls['fecFin'].updateValueAndValidity();
    }
  }

  definirTabla(idTipoReporte?: number): void {
    this.displayedColumns = [];
    this.columnsGrilla.forEach(c => {
      this.displayedColumns.push(c.columnDef);
    });
    this.displayedColumns.unshift('id');

    if (idTipoReporte == 1) {
      let index = this.displayedColumns.indexOf('anio');
      if (index > -1) {
        this.displayedColumns.splice(index, 1);
      }

      index = this.displayedColumns.indexOf('mes');
      if (index > -1) {
        this.displayedColumns.splice(index, 1);
      }
    } else {
      if (!this.displayedColumns.includes('mes')) {
        this.displayedColumns.unshift('mes')
      }
      if (!this.displayedColumns.includes('anio')) {
        this.displayedColumns.unshift('anio')
      }
    }
  }

  public cargarDatosTabla(): void {
    if (this.listaReporteVentaResponse.length > 0) {
      this.dataSource = new MatTableDataSource(this.listaReporteVentaResponse);
      this.dataSource.paginator = this.paginator;
      this.dataSource.sort = this.sort;
    }
  }

  comboProducto(): void {
    let req = new ProductoBuscarRequest();

    this.productoService.listarProducto(req).subscribe(
      (data: OutResponse<ProductoResponse[]>) => {
        if (data.rCodigo == 0) {
          this.listaProductoResponse = data.rResult;
          this.listaProductoResponse.unshift(JSON.parse(JSON.stringify({ id: 0, nombre: 'TODOS' })));

          this.formularioGrp.get('producto').setValue(this.listaProductoResponse[0]);
        } else {
          this._snackBar.open(data.rMensaje, null, { duration: 5000, horizontalPosition: 'right', verticalPosition: 'top', panelClass: ['error-snackbar'] });
        }
      }, error => {
        console.error(error);
        this._snackBar.open(error.statusText, null, { duration: 5000, horizontalPosition: 'right', verticalPosition: 'top', panelClass: ['error-snackbar'] });
      }
    )
  }

  buscar(): void {
    if (this.formularioGrp.valid) {
      this.dataSource = null;
      this.isLoading = true;

      let req = new ReporteVentaBuscarRequest();
      req.idTipoReporte = this.formularioGrp.get('tipoReporte').value.id;
      req.idProducto = this.formularioGrp.get('producto').value.id;
      if (req.idTipoReporte == 1) {
        req.fecInicio = this.formularioGrp.get('fecInicio').value;
        req.fecFin = this.formularioGrp.get('fecFin').value;
      } else {
        req.fecInicio = this.formularioGrp.get('mesInicio').value;
        req.fecFin = this.formularioGrp.get('mesFin').value;
      }
      console.log(req);

      this.reporteService.listarReporteVenta(req).subscribe(
        (data: OutResponse<ReporteVentaResponse[]>) => {
          console.log(data);
          if (data.rCodigo == 0) {
            this.listaReporteVentaResponse = data.rResult;
          } else {
            this._snackBar.open(data.rMensaje, null, { duration: 5000, horizontalPosition: 'right', verticalPosition: 'top', panelClass: ['warning-snackbar'] });
            this.listaReporteVentaResponse = [];
          }
          this.definirTabla(this.formularioGrp.get('tipoReporte').value.id);
          this.cargarDatosTabla();
          this.isLoading = false;
        },
        error => {
          console.log(error);
          this._snackBar.open(error.statusText, null, { duration: 5000, horizontalPosition: 'right', verticalPosition: 'top', panelClass: ['error-snackbar'] });
          this.isLoading = false;
        }
      );
    } else {
      this.formService.getValidationErrors(this.formularioGrp, this.formErrors, true);
    }
  }

  exportarExcel() {
    if (this.formularioGrp.valid) {
      this.spinner.show();

      let req = new ReporteVentaBuscarRequest();
      req.idTipoReporte = this.formularioGrp.get('tipoReporte').value.id;
      req.idProducto = this.formularioGrp.get('producto').value.id;
      if (req.idTipoReporte == 1) {
        req.fecInicio = this.formularioGrp.get('fecInicio').value;
        req.fecFin = this.formularioGrp.get('fecFin').value;
      } else {
        req.fecInicio = this.formularioGrp.get('mesInicio').value;
        req.fecFin = this.formularioGrp.get('mesFin').value;
      }
      req.user = this.usuarioService.getNombre + ' ' + this.usuarioService.getApeMaterno;
      console.log(req);

      this.reporteService.generarReporteVentaPDF(req).subscribe(
        (data: OutResponse<FileResponse>) => {
          if (data.rCodigo == 0) {
            const dialogRef = this.dialog.open(PdfViewerComponent, {
              width: '800px',
              disableClose: true,
              panelClass: 'dialog-no-padding',
              data: {
                titulo: '',
                objeto: data
              }
            });

            dialogRef.afterClosed().subscribe((result) => {
            });
          } else {
            this._snackBar.open(data.rMensaje, null, { duration: 5000, horizontalPosition: 'right', verticalPosition: 'top', panelClass: ['error-snackbar'] });
          }
          this.spinner.hide();
        }, error => {
          console.error(error);
          this._snackBar.open(error.statusText, null, { duration: 5000, horizontalPosition: 'right', verticalPosition: 'top', panelClass: ['error-snackbar'] });
          this.spinner.hide();
        }
      )
    } else {
      this.formService.getValidationErrors(this.formularioGrp, this.formErrors, true);
    }
  }

  chosenYearHandler(normalizedYear: Moment, ctrlName: string) {
    const ctrlValue = this.formularioGrp.get(ctrlName).value;
    ctrlValue.year(normalizedYear.year());
    this.formularioGrp.get(ctrlName).setValue(ctrlValue);
  }

  chosenMonthHandler(normalizedMonth: Moment, datepicker: MatDatepicker<Moment>, ctrlName: string) {
    const ctrlValue = this.formularioGrp.get(ctrlName).value;
    ctrlValue.month(normalizedMonth.month());
    ctrlValue.date(1);
    this.formularioGrp.get(ctrlName).setValue(ctrlValue);
    datepicker.close();
  }

}
