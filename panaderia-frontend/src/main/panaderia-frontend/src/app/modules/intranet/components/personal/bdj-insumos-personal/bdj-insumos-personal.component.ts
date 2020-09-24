import { DatePipe } from '@angular/common';
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
import { InsumoBuscarRequest } from '../../../dto/request/insumo-buscar.request';
import { InsumoRequest } from '../../../dto/request/insumo.request';
import { TipoInsumoBuscarRequest } from '../../../dto/request/tipo-insumo-buscar.request';
import { InsumoResponse } from '../../../dto/response/insumo.response';
import { OutResponse } from '../../../dto/response/out.response';
import { PersonalResponse } from '../../../dto/response/personal.response';
import { TipoInsumoResponse } from '../../../dto/response/tipo-insumo.response';
import { InsumoService } from '../../../services/insumo.service';
import { PersonalService } from '../../../services/personal.service';
import { ConfirmDialogComponent } from '../../shared/confirm-dialog/confirm-dialog.component';
import { RegInsumosPersonalComponent } from './reg-insumos-personal/reg-insumos-personal.component';

@Component({
  selector: 'app-bdj-insumos-personal',
  templateUrl: './bdj-insumos-personal.component.html',
  styleUrls: ['./bdj-insumos-personal.component.scss']
})
export class BdjInsumosPersonalComponent implements OnInit {
  exportar = false;

  listaPersonalResponse: PersonalResponse[];
  listaTipoInsumoResponse: TipoInsumoResponse[];

  listaInsumoResponse: InsumoResponse[];
  displayedColumns: string[];
  dataSource: MatTableDataSource<InsumoResponse>;
  isLoading: boolean = false;

  formularioGrp: FormGroup;
  formErrors: any;
  columnsGrilla = [
    {
      columnDef: 'personal',
      header: 'Personal',
      cell: (m: InsumoResponse) => `${m.personal.persona.nombre ? m.personal.persona.nombre : ''} ${m.personal.persona.apePaterno ? m.personal.persona.apePaterno : ''} ${m.personal.persona.apeMaterno ? m.personal.persona.apeMaterno : ''}`
    }, {
      columnDef: 'tipoInsumo',
      header: 'Tipo insumo',
      cell: (m: InsumoResponse) => `${m.tipoInsumo.nombre ? m.tipoInsumo.nombre : ''}`
    }, {
      columnDef: 'cantidad',
      header: 'Cantidad',
      cell: (m: InsumoResponse) => `${m.cantidad ? m.cantidad : ''}`
    }, {
      columnDef: 'unidadMedida',
      header: 'Unidad medida',
      cell: (m: InsumoResponse) => `${m.tipoInsumo.nomTipoUnidadMedida ? m.tipoInsumo.nomTipoUnidadMedida : ''}`
    }, {
      columnDef: 'fecha',
      header: 'Fecha',
      cell: (m: InsumoResponse) => `${m.fecha ? this.datePipe.transform(m.fecha, 'dd/MM/yyyy') : ''}`
    }];

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor(private fb: FormBuilder,
    public dialog: MatDialog,
    private spinner: NgxSpinnerService,
    @Inject(PersonalService) private personalService: PersonalService,
    @Inject(InsumoService) private insumoService: InsumoService,
    @Inject(FormService) private formService: FormService,
    private _snackBar: MatSnackBar,
    private datePipe: DatePipe) { }

  ngOnInit() {
    this.formularioGrp = this.fb.group({
      tipoInsumo: ['', []],
      personal: ['', []],
      fecInicio: ['', []],
      fecFin: ['', []],
    });
    this.formErrors = this.formService.buildFormErrors(this.formularioGrp, this.formErrors);

    this.definirTabla();
    this.inicializarVariables();
  }

  inicializarVariables(): void {
    this.comboTipoInsumo();
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
    if (this.listaInsumoResponse.length > 0) {
      this.dataSource = new MatTableDataSource(this.listaInsumoResponse);
      this.dataSource.paginator = this.paginator;
      this.dataSource.sort = this.sort;
    }
  }

  comboTipoInsumo(): void {
    let req = new TipoInsumoBuscarRequest();

    this.insumoService.listarTipoInsumo(req).subscribe(
      (data: OutResponse<TipoInsumoResponse[]>) => {
        if (data.rCodigo == 0) {
          this.listaTipoInsumoResponse = data.rResult;
          this.listaTipoInsumoResponse.unshift(JSON.parse(JSON.stringify({ id: 0, nombre: 'TODOS' })));

          this.formularioGrp.get('tipoInsumo').setValue(this.listaTipoInsumoResponse[0]);
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

    let req = new InsumoBuscarRequest();
    req.idTipoInsumo = this.formularioGrp.get('tipoInsumo').value.id;
    req.idPersonal = this.formularioGrp.get('personal').value.id;
    req.fecInicio = this.formularioGrp.get('fecInicio').value;
    req.fecFin = this.formularioGrp.get('fecFin').value;

    this.insumoService.listarInsumo(req).subscribe(
      (data: OutResponse<InsumoResponse[]>) => {
        if (data.rCodigo == 0) {
          this.listaInsumoResponse = data.rResult;
        } else {
          this._snackBar.open(data.rMensaje, null, { duration: 5000, horizontalPosition: 'right', verticalPosition: 'top', panelClass: ['warning-snackbar'] });
          this.listaInsumoResponse = [];
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

  registrar(obj: InsumoResponse) {
    const dialogRef = this.dialog.open(RegInsumosPersonalComponent, {
      width: '600px',
      disableClose: false,
      data: {
        titulo: MENSAJES.INTRANET.BANDEJA_INSUMO.INSUMO.REGISTRAR.TITLE,
        objeto: null
      }
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.listaInsumoResponse.unshift(result);
        this.cargarDatosTabla();
      }
    });
  }

  modificar(obj: InsumoResponse) {
    let index = this.listaInsumoResponse.indexOf(obj);
    const dialogRef = this.dialog.open(RegInsumosPersonalComponent, {
      width: '600px',
      disableClose: false,
      data: {
        titulo: MENSAJES.INTRANET.BANDEJA_INSUMO.INSUMO.MODIFICAR.TITLE,
        objeto: obj
      }
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.listaInsumoResponse.splice(index, 1);
        this.listaInsumoResponse.unshift(result);
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

        let index = this.listaInsumoResponse.indexOf(obj);
        let req = new InsumoRequest();
        req.id = obj.id;

        this.insumoService.eliminarInsumo(req).subscribe(
          (data: OutResponse<InsumoResponse>) => {
            if (data.rCodigo == 0) {
              this.listaInsumoResponse.splice(index, 1);
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
