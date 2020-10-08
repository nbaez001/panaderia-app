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
import { MaestraBuscarRequest } from '../../../dto/request/maestra-buscar.request';
import { PersonalBuscarRequest } from '../../../dto/request/personal-buscar.request';
import { FileResponse } from '../../../dto/response/file.response';
import { MaestraResponse } from '../../../dto/response/maestra.response';
import { OutResponse } from '../../../dto/response/out.response';
import { PersonalResponse } from '../../../dto/response/personal.response';
import { MaestraService } from '../../../services/maestra.service';
import { PersonalService } from '../../../services/personal.service';
import { ReporteService } from '../../../services/reporte.service';
import { ConfirmDialogComponent } from '../../shared/confirm-dialog/confirm-dialog.component';
import { RegPersonalComponent } from './reg-personal/reg-personal.component';

@Component({
  selector: 'app-bdj-personal',
  templateUrl: './bdj-personal.component.html',
  styleUrls: ['./bdj-personal.component.scss']
})
export class BdjPersonalComponent implements OnInit {
  exportar = false;

  listaTipoDocumento: MaestraResponse[];
  listaPersonalResponse: PersonalResponse[];

  displayedColumns: string[];
  dataSource: MatTableDataSource<PersonalResponse>;
  isLoading: boolean = false;

  formularioGrp: FormGroup;
  formErrors: any;
  columnsGrilla = [
    {
      columnDef: 'cargo',
      header: 'Cargo',
      cell: (m: PersonalResponse) => `${m.cargo}`
    }, {
      columnDef: 'nombre',
      header: 'Nombre',
      cell: (m: PersonalResponse) => `${m.persona.nombre}`
    }, {
      columnDef: 'apellidos',
      header: 'Apellidos',
      cell: (m: PersonalResponse) => `${m.persona.apePaterno ? m.persona.apePaterno : ''} ${m.persona.apeMaterno ? m.persona.apeMaterno : ''}`
    }, {
      columnDef: 'nomTipoDocumento',
      header: 'Tipo documento',
      cell: (m: PersonalResponse) => `${m.persona.nomTipoDocumento ? m.persona.nomTipoDocumento : ''}`
    }, {
      columnDef: 'nroDocumento',
      header: 'Nro Documento',
      cell: (m: PersonalResponse) => `${m.persona.nroDocumento ? m.persona.nroDocumento : ''}`
    }, {
      columnDef: 'direccionDomicilio',
      header: 'Dir. Domicilio',
      cell: (m: PersonalResponse) => `${m.persona.direccionDomicilio ? m.persona.direccionDomicilio : ''}`
    }, {
      columnDef: 'nomDistrito',
      header: 'Distrito',
      cell: (m: PersonalResponse) => `${m.persona.nomDistrito ? m.persona.nomDistrito : ''}`
    }, {
      columnDef: 'nomProvincia',
      header: 'Provincia',
      cell: (m: PersonalResponse) => `${m.persona.nomProvincia ? m.persona.nomProvincia : ''}`
    }, {
      columnDef: 'nomDepartamento',
      header: 'Departamento',
      cell: (m: PersonalResponse) => `${m.persona.nomDepartamento ? m.persona.nomDepartamento : ''}`
    }, {
      columnDef: 'nomPais',
      header: 'Pais',
      cell: (m: PersonalResponse) => `${m.persona.nomPais ? m.persona.nomPais : ''}`
    }];

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor(private fb: FormBuilder,
    public dialog: MatDialog,
    private spinner: NgxSpinnerService,
    @Inject(ReporteService) private reporteService: ReporteService,
    @Inject(PersonalService) private personalService: PersonalService,
    @Inject(MaestraService) private maestraService: MaestraService,
    @Inject(FormService) private formService: FormService,
    private _snackBar: MatSnackBar,
    private datePipe: DatePipe) { }

  ngOnInit() {
    this.formularioGrp = this.fb.group({
      tipoDocumento: ['', []],
      nroDocumento: ['', []],
      nombre: ['', []],
    });
    this.formErrors = this.formService.buildFormErrors(this.formularioGrp, this.formErrors);

    this.definirTabla();
    this.inicializarVariables();
  }

  inicializarVariables(): void {
    this.comboTipoDocumento();
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
    if (this.listaPersonalResponse.length > 0) {
      this.dataSource = new MatTableDataSource(this.listaPersonalResponse);
      this.dataSource.paginator = this.paginator;
      this.dataSource.sort = this.sort;
    }
  }

  comboTipoDocumento(): void {
    let req = new MaestraBuscarRequest();
    req.idTabla = TABLAS_MAESTRA.TIPO_DOCUMENTO.ID;

    this.maestraService.listarMaestra(req).subscribe(
      (data: OutResponse<MaestraResponse[]>) => {
        if (data.rCodigo == 0) {
          this.listaTipoDocumento = data.rResult;
          this.listaTipoDocumento.unshift(JSON.parse(JSON.stringify({ id: 0, nombre: 'TODOS' })));

          this.formularioGrp.get('tipoDocumento').setValue(this.listaTipoDocumento[0]);
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

    let req = new PersonalBuscarRequest();
    req.idtTipoDocumento = this.formularioGrp.get('tipoDocumento').value ? this.formularioGrp.get('tipoDocumento').value.id : null;
    req.nroDocumento = this.formularioGrp.get('nroDocumento').value;
    req.nombre = this.formularioGrp.get('nombre').value;

    this.personalService.listarPersonal(req).subscribe(
      (data: OutResponse<PersonalResponse[]>) => {
        if (data.rCodigo == 0) {
          console.log(data.rResult);
          this.listaPersonalResponse = data.rResult;
        } else {
          this._snackBar.open(data.rMensaje, null, { duration: 5000, horizontalPosition: 'right', verticalPosition: 'top', panelClass: ['warning-snackbar'] });
          this.listaPersonalResponse = [];
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

    let req = new PersonalBuscarRequest();
    req.idtTipoDocumento = this.formularioGrp.get('tipoDocumento').value ? this.formularioGrp.get('tipoDocumento').value.id : null;
    req.nroDocumento = this.formularioGrp.get('nroDocumento').value;
    req.nombre = this.formularioGrp.get('nombre').value;

    this.personalService.reporteXlsxListarPersonal(req).subscribe(
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

  registrar(obj: PersonalResponse) {
    const dialogRef = this.dialog.open(RegPersonalComponent, {
      width: '600px',
      disableClose: false,
      data: {
        titulo: MENSAJES.INTRANET.BANDEJA_PERSONAL.PERSONAL.REGISTRAR.TITLE,
        objeto: null
      }
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.listaPersonalResponse.unshift(result);
        this.cargarDatosTabla();
      }
    });
  }

  modificar(obj: PersonalResponse) {
    let index = this.listaPersonalResponse.indexOf(obj);
    const dialogRef = this.dialog.open(RegPersonalComponent, {
      width: '600px',
      disableClose: false,
      data: {
        titulo: MENSAJES.INTRANET.BANDEJA_PERSONAL.PERSONAL.MODIFICAR.TITLE,
        objeto: obj
      }
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.listaPersonalResponse.splice(index, 1);
        this.listaPersonalResponse.unshift(result);
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

        let index = this.listaPersonalResponse.indexOf(obj);

        this.personalService.eliminarPersonal(obj).subscribe(
          (data: OutResponse<PersonalResponse>) => {
            if (data.rCodigo == 0) {
              this.listaPersonalResponse.splice(index, 1);
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

}
