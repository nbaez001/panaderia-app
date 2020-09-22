import { DatePipe } from '@angular/common';
import { Component, Inject, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { NgxSpinnerService } from 'ngx-spinner';
import { MENSAJES, TABLAS_MAESTRA } from 'src/app/common';
import { FormService } from 'src/app/core/services/form.service';
import { ComprobanteBuscarRequest } from '../../../dto/request/comprobante-buscar.request';
import { MaestraBuscarRequest } from '../../../dto/request/maestra-buscar.request';
import { ComprobanteResponse } from '../../../dto/response/comprobante.response';
import { MaestraResponse } from '../../../dto/response/maestra.response';
import { OutResponse } from '../../../dto/response/out.response';
import { ComprobanteService } from '../../../services/comprobante.service';
import { MaestraService } from '../../../services/maestra.service';
import { ConfirmDialogComponent } from '../../shared/confirm-dialog/confirm-dialog.component';
import { RegComprobanteComponent } from './reg-comprobante/reg-comprobante.component';

@Component({
  selector: 'app-bdj-comprobante',
  templateUrl: './bdj-comprobante.component.html',
  styleUrls: ['./bdj-comprobante.component.scss']
})
export class BdjComprobanteComponent implements OnInit {
  exportar = false;
  index: number;

  listaTipoComprobante: MaestraResponse[];

  listaComprobanteResponse: ComprobanteResponse[];

  displayedColumns: string[];
  dataSource: MatTableDataSource<ComprobanteResponse>;
  isLoading: boolean = false;

  formularioGrp: FormGroup;
  formErrors: any;

  columnsGrilla = [
    {
      columnDef: 'nombre',
      header: 'Nombre',
      cell: (m: ComprobanteResponse) => `${m.nombre}`,
      numero: 2
    }, {
      columnDef: 'serie',
      header: 'Serie',
      cell: (m: ComprobanteResponse) => `${m.serie}`,
      numero: 3
    }, {
      columnDef: 'numero',
      header: 'Numero',
      cell: (m: ComprobanteResponse) => (m.numero != null) ? `${m.numero}` : '',
      numero: 4
    }, {
      columnDef: 'nomTipoComprobante',
      header: 'Tipo comprobante',
      cell: (m: ComprobanteResponse) => (m.nomTipoComprobante != null) ? `${m.nomTipoComprobante}` : '',
      numero: 5
    }, {
      columnDef: 'flgActual',
      header: 'Actual',
      cell: (m: ComprobanteResponse) => `${m.flgActual}`,
      numero: 6
    }, {
      columnDef: 'flgUsado',
      header: '¿Se generaron numeros?',
      cell: (m: ComprobanteResponse) => (m.flgUsado != null) ? `${m.flgUsado == 1 ? 'SI' : 'NO'}` : '',
      numero: 7
    }, {
      columnDef: 'idUsuarioCrea',
      header: 'Usuario creador',
      cell: (m: ComprobanteResponse) => `${m.idUsuarioCrea}`,
      numero: 8
    }, {
      columnDef: 'fecUsuarioCrea',
      header: 'Fecha creacion',
      cell: (m: ComprobanteResponse) => this.datePipe.transform(m.fecUsuarioCrea, 'dd/MM/yyyy'),
      numero: 9
    }];

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor(private fb: FormBuilder,
    public dialog: MatDialog,
    private spinner: NgxSpinnerService,
    @Inject(ComprobanteService) private comprobanteService: ComprobanteService,
    @Inject(MaestraService) private maestraService: MaestraService,
    @Inject(FormService) private formService: FormService,
    private _snackBar: MatSnackBar,
    private datePipe: DatePipe) { }

  ngOnInit() {
    this.formularioGrp = this.fb.group({
      tipoComprobante: ['', []],
      fecInicio: ['', []],
      fecFin: ['', []],
    });
    this.formErrors = this.formService.buildFormErrors(this.formularioGrp, this.formErrors);
    this.formularioGrp.valueChanges.subscribe((val: any) => {
      this.formService.getValidationErrors(this.formularioGrp, this.formErrors, false);
    });

    this.listaComprobanteResponse = [];

    this.definirTabla();
    this.inicializarVariables();
  }

  inicializarVariables(): void {
    this.comboTipoComprobante();
    this.buscar();
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
    if (this.listaComprobanteResponse.length > 0) {
      this.dataSource = new MatTableDataSource(this.listaComprobanteResponse);
      this.dataSource.paginator = this.paginator;
      this.dataSource.sort = this.sort;
    }
  }

  comboTipoComprobante(): void {
    let req = new MaestraBuscarRequest();
    req.idTabla = TABLAS_MAESTRA.TIPO_COMPROBANTE.ID;

    this.maestraService.listarMaestra(req).subscribe(
      (data: OutResponse<MaestraResponse[]>) => {
        if (data.rCodigo == 0) {
          this.listaTipoComprobante = data.rResult;
          this.listaTipoComprobante.unshift(JSON.parse(JSON.stringify({ id: 0, nombre: 'TODOS' })));

          this.formularioGrp.get('tipoComprobante').setValue(this.listaTipoComprobante[0]);
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
    this.dataSource = null;
    this.isLoading = true;

    let req = new ComprobanteBuscarRequest();
    req.idtTipoComprobante = this.formularioGrp.get('tipoComprobante').value ? this.formularioGrp.get('tipoComprobante').value.id : null;
    req.fecInicio = this.formularioGrp.get('fecInicio').value;
    req.fecFin = this.formularioGrp.get('fecFin').value;

    this.comprobanteService.listarComprobante(req).subscribe(
      (data: OutResponse<ComprobanteResponse[]>) => {
        if (data.rCodigo == 0) {
          console.log(data.rResult);
          this.listaComprobanteResponse = data.rResult;
        } else {
          this._snackBar.open(data.rMensaje, null, { duration: 5000, horizontalPosition: 'right', verticalPosition: 'top', panelClass: ['warning-snackbar'] });
          this.listaComprobanteResponse = [];
        }
        this.cargarDatosTabla();
        this.isLoading = false;
      },
      error => {
        console.log(error);
        this._snackBar.open(error.statusText, null, { duration: 5000, horizontalPosition: 'right', verticalPosition: 'top', panelClass: ['error-snackbar'] });
        this.isLoading = false;
      }
    );
  }

  exportarExcel() {
    this.exportar = true;

    setTimeout(() => {
      this.exportar = false;
    }, 2000);
  }

  registrar(obj: ComprobanteResponse) {
    const dialogRef = this.dialog.open(RegComprobanteComponent, {
      width: '600px',
      disableClose: false,
      data: {
        titulo: MENSAJES.INTRANET.BANDEJA_COMPROBANTES.COMPROBANTE.REGISTRAR.TITLE,
        objeto: null
      }
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.listaComprobanteResponse.unshift(result);
        this.cargarDatosTabla();
      }
    });
  }

  modificar(obj: ComprobanteResponse) {
    let index = this.listaComprobanteResponse.indexOf(obj);
    const dialogRef = this.dialog.open(RegComprobanteComponent, {
      width: '600px',
      disableClose: false,
      data: {
        titulo: MENSAJES.INTRANET.BANDEJA_COMPROBANTES.COMPROBANTE.MODIFICAR.TITLE,
        objeto: obj
      }
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.listaComprobanteResponse.splice(index, 1);
        this.listaComprobanteResponse.unshift(result);
        this.cargarDatosTabla();
      }
    });
  }

  eliminar(obj): void {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      width: '300px',
      disableClose: false,
      data: {
        titulo: MENSAJES.INTRANET.MSG_CONFIRMACION,
        objeto: null
      }
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result == 1) {
        this.spinner.show();

        this.index = this.listaComprobanteResponse.indexOf(obj);

        this.comprobanteService.eliminarComprobante(obj).subscribe(
          (data: OutResponse<ComprobanteResponse>) => {
            if (data.rCodigo == 0) {
              this.listaComprobanteResponse.splice(this.index, 1);
              this.cargarDatosTabla();
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
      }
    });
  }

  establecerActual(obj: ComprobanteResponse, event: any): void {
    event.preventDefault();
    if (obj.flgActual == 0) {
      const dialogRef = this.dialog.open(ConfirmDialogComponent, {
        width: '300px',
        disableClose: false,
        data: {
          titulo: MENSAJES.INTRANET.BANDEJA_COMPROBANTES.COMPROBANTE.MSG_ESTABLECER_ACTUAL,
          objeto: null
        }
      });

      dialogRef.afterClosed().subscribe((result) => {
        if (result == 1) {
          this.spinner.show();
          this.index = this.listaComprobanteResponse.indexOf(obj);

          this.comprobanteService.establecerComprobanteActual(obj).subscribe(
            (data: OutResponse<ComprobanteResponse>) => {
              if (data.rCodigo == 0) {
                this.listaComprobanteResponse.forEach(el => {
                  if (el.id == obj.id) {
                    el.flgActual = 1;
                  } else {
                    el.flgActual = 0;
                  }
                });
                this.dataSource = null;
                this.cargarDatosTabla();
              } else {
                this._snackBar.open(data.rMensaje, null, { duration: 5000, horizontalPosition: 'right', verticalPosition: 'top', panelClass: ['error-snackbar'] });
              }
              this.spinner.hide();
            }, error => {
              console.error(error);
              this.spinner.hide();
              this._snackBar.open(error.statusText, null, { duration: 5000, horizontalPosition: 'right', verticalPosition: 'top', panelClass: ['error-snackbar'] });
            }
          )
        }
      });
    }
  }
}
