import { DatePipe, DecimalPipe } from '@angular/common';
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
import { HonorarioBuscarRequest } from '../../../dto/request/honorario-buscar.request';
import { HonorarioRequest } from '../../../dto/request/honorario.request';
import { PersonalBuscarRequest } from '../../../dto/request/personal-buscar.request';
import { FileResponse } from '../../../dto/response/file.response';
import { HonorarioResponse } from '../../../dto/response/honorario.response';
import { OutResponse } from '../../../dto/response/out.response';
import { PersonalResponse } from '../../../dto/response/personal.response';
import { HonorarioService } from '../../../services/honorario.service';
import { PersonalService } from '../../../services/personal.service';
import { ConfirmDialogComponent } from '../../shared/confirm-dialog/confirm-dialog.component';
import { PdfViewerComponent } from '../../shared/pdf-viewer/pdf-viewer.component';
import { RegHonorarioComponent } from './reg-honorario/reg-honorario.component';

@Component({
  selector: 'app-bdj-honorarios',
  templateUrl: './bdj-honorarios.component.html',
  styleUrls: ['./bdj-honorarios.component.scss']
})
export class BdjHonorariosComponent implements OnInit {
  exportar = false;

  listaPersonalResponse: PersonalResponse[] = [];

  listaHonorarioResponse: HonorarioResponse[];
  displayedColumns: string[];
  dataSource: MatTableDataSource<HonorarioResponse>;
  isLoading: boolean = false;

  formularioGrp: FormGroup;
  formErrors: any;
  columnsGrilla = [
    {
      columnDef: 'personal',
      header: 'Personal',
      cell: (m: HonorarioResponse) => `${m.personal.persona.nombre ? m.personal.persona.nombre : ''} ${m.personal.persona.apePaterno ? m.personal.persona.apePaterno : ''} ${m.personal.persona.apeMaterno ? m.personal.persona.apeMaterno : ''}`
    }, {
      columnDef: 'monto',
      header: 'Monto',
      cell: (m: HonorarioResponse) => `${m.monto ? this.decimalPipe.transform(m.monto, '1.2-2') : ''}`
    }, {
      columnDef: 'fechaInicio',
      header: 'Fecha inicio calculo',
      cell: (m: HonorarioResponse) => `${m.fechaInicio ? this.datePipe.transform(m.fechaInicio, 'dd/MM/yyyy') : ''}`
    }, {
      columnDef: 'fechaFin',
      header: 'Fecha fin calculo',
      cell: (m: HonorarioResponse) => `${m.fechaFin ? this.datePipe.transform(m.fechaFin, 'dd/MM/yyyy') : ''}`
    }, {
      columnDef: 'fecha',
      header: 'Fecha pago',
      cell: (m: HonorarioResponse) => `${m.fecha ? this.datePipe.transform(m.fecha, 'dd/MM/yyyy') : ''}`
    }];

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor(private fb: FormBuilder,
    public dialog: MatDialog,
    private spinner: NgxSpinnerService,
    @Inject(PersonalService) private personalService: PersonalService,
    @Inject(HonorarioService) private honorarioService: HonorarioService,
    @Inject(FormService) private formService: FormService,
    private _snackBar: MatSnackBar,
    private datePipe: DatePipe,
    private decimalPipe: DecimalPipe,
  ) { }

  ngOnInit() {
    this.formularioGrp = this.fb.group({
      personal: ['', []],
      fecInicio: ['', []],
      fecFin: ['', []],
    });
    this.formErrors = this.formService.buildFormErrors(this.formularioGrp, this.formErrors);

    this.definirTabla();
    this.inicializarVariables();
  }

  inicializarVariables(): void {
    this.comboPersonal();
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
    if (this.listaHonorarioResponse.length > 0) {
      this.dataSource = new MatTableDataSource(this.listaHonorarioResponse);
      this.dataSource.paginator = this.paginator;
      this.dataSource.sort = this.sort;
    }
  }

  buscar(): void {
    this.dataSource = null;
    this.isLoading = true;

    let req = new HonorarioBuscarRequest();
    req.fecInicio = this.formularioGrp.get('fecInicio').value;
    req.fecFin = this.formularioGrp.get('fecFin').value;

    this.honorarioService.listarHonorario(req).subscribe(
      (data: OutResponse<HonorarioResponse[]>) => {
        if (data.rCodigo == 0) {
          this.listaHonorarioResponse = data.rResult;
        } else {
          this._snackBar.open(data.rMensaje, null, { duration: 5000, horizontalPosition: 'right', verticalPosition: 'top', panelClass: ['warning-snackbar'] });
          this.listaHonorarioResponse = [];
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

  comboPersonal(): void {
    let req = new PersonalBuscarRequest();

    this.personalService.listarPersonal(req).subscribe(
      (data: OutResponse<PersonalResponse[]>) => {
        if (data.rCodigo == 0) {
          this.listaPersonalResponse = data.rResult;
          this.listaPersonalResponse.unshift(JSON.parse(JSON.stringify({ id: 0, persona: { nombre: 'TODOS', apePaterno: '' } })));

          this.listaPersonalResponse.forEach(el => {
            el.nombreCompleto = el.persona.nombre + ' ' + el.persona.apePaterno;
          });

          this.formularioGrp.get('personal').setValue(this.listaPersonalResponse[0]);
        } else {
          this._snackBar.open(data.rMensaje, null, { duration: 5000, horizontalPosition: 'right', verticalPosition: 'top', panelClass: ['error-snackbar'] });
        }
      }, error => {
        console.error(error);
        this._snackBar.open(error.statusText, null, { duration: 5000, horizontalPosition: 'right', verticalPosition: 'top', panelClass: ['error-snackbar'] });
      }
    )
  }

  exportarExcel() {
    this.exportar = true;

    setTimeout(() => {
      this.exportar = false;
    }, 2000);
  }

  registrar(obj: HonorarioResponse) {
    const dialogRef = this.dialog.open(RegHonorarioComponent, {
      width: '800px',
      disableClose: false,
      data: {
        titulo: MENSAJES.INTRANET.BANDEJA_HONORARIO.HONORARIO.REGISTRAR.TITLE,
        objeto: null
      }
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.listaHonorarioResponse.unshift(result);
        this.cargarDatosTabla();
      }
    });
  }

  modificar(obj: HonorarioResponse) {
    let index = this.listaHonorarioResponse.indexOf(obj);
    const dialogRef = this.dialog.open(RegHonorarioComponent, {
      width: '600px',
      disableClose: false,
      data: {
        titulo: MENSAJES.INTRANET.BANDEJA_HONORARIO.HONORARIO.MODIFICAR.TITLE,
        objeto: obj
      }
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.listaHonorarioResponse.splice(index, 1);
        this.listaHonorarioResponse.unshift(result);
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

        let index = this.listaHonorarioResponse.indexOf(obj);
        let req = new HonorarioRequest();
        req.id = obj.id;

        this.honorarioService.eliminarHonorario(req).subscribe(
          (data: OutResponse<HonorarioResponse>) => {
            if (data.rCodigo == 0) {
              this.listaHonorarioResponse.splice(index, 1);
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

  verRecibo(obj: HonorarioResponse): void {
    this.spinner.show();

    let req = new HonorarioRequest();
    req.id = obj.id;

    this.honorarioService.reporteHonorario(req).subscribe(
      (data: OutResponse<FileResponse>) => {
        if (data.rCodigo == 0) {
          const dialogRef = this.dialog.open(PdfViewerComponent, {
            width: '400px',
            disableClose: true,
            panelClass: 'dialog-no-padding',
            data: {
              titulo: MENSAJES.INTRANET.BANDEJA_HONORARIO.HONORARIO.RESUMEN_HONORARIO.TITLE,
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
  }

}
