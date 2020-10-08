import { Component, Inject, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { NgxSpinnerService } from 'ngx-spinner';
import { MENSAJES } from 'src/app/common';
import { FormService } from 'src/app/core/services/form.service';
import { TipoInsumoBuscarRequest } from '../../../dto/request/tipo-insumo-buscar.request';
import { TipoInsumoRequest } from '../../../dto/request/tipo-insumo.request';
import { FileResponse } from '../../../dto/response/file.response';
import { OutResponse } from '../../../dto/response/out.response';
import { TipoInsumoResponse } from '../../../dto/response/tipo-insumo.response';
import { InsumoService } from '../../../services/insumo.service';
import { MaestraService } from '../../../services/maestra.service';
import { ReporteService } from '../../../services/reporte.service';
import { ConfirmDialogComponent } from '../../shared/confirm-dialog/confirm-dialog.component';
import { RegTipoInsumoComponent } from './reg-tipo-insumo/reg-tipo-insumo.component';

@Component({
  selector: 'app-bdj-tipo-insumo',
  templateUrl: './bdj-tipo-insumo.component.html',
  styleUrls: ['./bdj-tipo-insumo.component.scss']
})
export class BdjTipoInsumoComponent implements OnInit {
  exportar: boolean = false;
  formularioGrp: FormGroup;
  formErrors: any;

  displayedColumns: string[];
  dataSource: MatTableDataSource<TipoInsumoResponse>;
  isLoading: boolean = false;

  listaTipoInsumo: TipoInsumoResponse[] = [];
  columnsGrilla = [
    {
      columnDef: 'codigo',
      header: 'Codigo',
      cell: (tipoInsumo: TipoInsumoResponse) => `${tipoInsumo.codigo ? tipoInsumo.codigo : ''}`
    }, {
      columnDef: 'nombre',
      header: 'Nombre',
      cell: (tipoInsumo: TipoInsumoResponse) => `${tipoInsumo.nombre ? tipoInsumo.nombre : ''}`
    },
    {
      columnDef: 'unidadMedida',
      header: 'Unidad Medida',
      cell: (tipoInsumo: TipoInsumoResponse) => `${tipoInsumo.nomUnidadMedida ? tipoInsumo.nomUnidadMedida : ''}`
    },
    {
      columnDef: 'observacion',
      header: 'Observacion',
      cell: (tipoInsumo: TipoInsumoResponse) => `${tipoInsumo.observacion ? tipoInsumo.observacion : ''}`
    }
  ];

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor(private fb: FormBuilder,
    public dialog: MatDialog,
    private _snackBar: MatSnackBar,
    private spinner: NgxSpinnerService,
    @Inject(FormService) private formService: FormService,
    @Inject(ReporteService) private reporteService: ReporteService,
    @Inject(MaestraService) private maestraService: MaestraService,
    @Inject(InsumoService) private insumoService: InsumoService,) { }

  ngOnInit(): void {
    // this.spinnerService.show();

    this.formularioGrp = this.fb.group({
      indicio: ['', []],
      fechaInicio: ['', []],
      fechaFin: ['', []],
    });
    this.formErrors = this.formService.buildFormErrors(this.formularioGrp, this.formErrors);
    this.formularioGrp.valueChanges.subscribe((val: any) => {
      this.formService.getValidationErrors(this.formularioGrp, this.formErrors, false);
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
    this.buscar();
  }

  cargarDatosTabla(): void {
    this.dataSource = null;
    if (this.listaTipoInsumo.length > 0) {
      this.dataSource = new MatTableDataSource(this.listaTipoInsumo);
      this.dataSource.paginator = this.paginator;
      this.dataSource.sort = this.sort;
    }
  }

  buscar(): void {
    let request = new TipoInsumoBuscarRequest();

    this.dataSource = null;

    this.isLoading = true;
    this.insumoService.listarTipoInsumo(request).subscribe(
      (data: OutResponse<TipoInsumoResponse[]>) => {
        if (data.rCodigo == 0) {
          this.listaTipoInsumo = data.rResult;

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
    const dialogRef = this.dialog.open(RegTipoInsumoComponent, {
      width: '600px',
      data: { titulo: MENSAJES.INTRANET.BANDEJA_INSUMO.TIPO_INSUMO.REGISTRAR.TITLE, objeto: obj }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.listaTipoInsumo.unshift(result);
        this.cargarDatosTabla();
      }
    });
  }

  exportarExcel(): void {
    this.exportar = true;

    let req = new TipoInsumoBuscarRequest();

    this.insumoService.reporteXlsxListarTipoInsumo(req).subscribe(
      (data: OutResponse<FileResponse>) => {
        if (data.rCodigo == 0) {
          let blob = this.reporteService.convertToBlobFromByte(data.rResult);
          this.reporteService.DownloadBlobFile(blob);
        } else {
          this._snackBar.open(data.rMensaje, null, { duration: 10000, horizontalPosition: 'right', verticalPosition: 'top', panelClass: ['error-snackbar'] });
        }
        this.exportar = false;
      },
      error => {
        console.log(error);
        this._snackBar.open(error.statusText, null, { duration: 10000, horizontalPosition: 'right', verticalPosition: 'top', panelClass: ['error-snackbar'] });
        this.exportar = false;
      }
    );
  }

  editar(obj: TipoInsumoResponse): void {
    let index = this.listaTipoInsumo.indexOf(obj);
    const dialogRef = this.dialog.open(RegTipoInsumoComponent, {
      width: '600px',
      data: { title: MENSAJES.INTRANET.BANDEJA_INSUMO.TIPO_INSUMO.MODIFICAR.TITLE, objeto: obj }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.listaTipoInsumo.splice(index, 1);
        this.listaTipoInsumo.unshift(result);
        this.cargarDatosTabla();
      }
    });
  }

  eliminar(obj: TipoInsumoResponse): void {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      width: '300px',
      data: {
        titulo: MENSAJES.INTRANET.MSG_CONFIRMACION,
        objeto: null
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result == 1) {
        this.spinner.show();
        let index = this.listaTipoInsumo.indexOf(obj);

        let req = new TipoInsumoRequest();
        req.id = obj.id;

        this.insumoService.eliminarTipoInsumo(req).subscribe(
          (data: OutResponse<TipoInsumoResponse>) => {
            if (data.rCodigo == 0) {
              this.listaTipoInsumo.splice(index, 1);
              this.cargarDatosTabla();
              this.spinner.hide();
            } else {
              this._snackBar.open(data.rMensaje, null, { duration: 5000, horizontalPosition: 'right', verticalPosition: 'top', panelClass: ['warning-snackbar'] });
              this.spinner.hide();
            }
          }, error => {
            this._snackBar.open(error, null, { duration: 5000, horizontalPosition: 'right', verticalPosition: 'top', panelClass: ['error-snackbar'] });
            this.spinner.hide();
          }
        );
      }
    });
  }


}
