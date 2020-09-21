import { DatePipe } from '@angular/common';
import { Component, Inject, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { MENSAJES } from 'src/app/common';
import { FormService } from 'src/app/core/services/form.service';
import { ComprobanteBuscarRequest } from '../../../dto/request/comprobante-buscar.request';
import { ComprobanteResponse } from '../../../dto/response/comprobante.response';
import { MaestraResponse } from '../../../dto/response/maestra.response';
import { OutResponse } from '../../../dto/response/out.response';
import { ComprobanteService } from '../../../services/comprobante.service';
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
      cell: (m: ComprobanteResponse) => `${m.nombre}`
    }, {
      columnDef: 'serie',
      header: 'Serie',
      cell: (m: ComprobanteResponse) => `${m.serie}`
    }, {
      columnDef: 'numero',
      header: 'Numero',
      cell: (m: ComprobanteResponse) => (m.numero != null) ? `${m.numero}` : ''
    }, {
      columnDef: 'nomTipoComprobante',
      header: 'Tipo comprobante',
      cell: (m: ComprobanteResponse) => (m.nomTipoComprobante != null) ? `${m.nomTipoComprobante}` : ''
    }, {
      columnDef: 'flgActual',
      header: 'Actual',
      cell: (m: ComprobanteResponse) => `${m.flgActual}`
    }, {
      columnDef: 'flgUsado',
      header: '¿Se generaron numeros?',
      cell: (m: ComprobanteResponse) => (m.flgUsado != null) ? `${m.flgUsado}` : ''
    }, {
      columnDef: 'idUsuarioCrea',
      header: 'Usuario creador',
      cell: (m: ComprobanteResponse) => `${m.idUsuarioCrea}`
    }, {
      columnDef: 'fecUsuarioCrea',
      header: 'Fecha creacion',
      cell: (m: ComprobanteResponse) => this.datePipe.transform(m.fecUsuarioCrea, 'dd/MM/yyyy')
    }];

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor(private fb: FormBuilder,
    public dialog: MatDialog,
    @Inject(ComprobanteService) private comprobanteService: ComprobanteService,
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

  buscar(): void {
    this.dataSource = null;
    this.isLoading = true;

    let req = new ComprobanteBuscarRequest();
    req.idtTipoComprobante = this.formularioGrp.get('tipoComprobante').value.id;
    req.fecInicio = this.formularioGrp.get('fecInicio').value;
    req.fecFin = this.formularioGrp.get('fecFin').value;

    this.comprobanteService.listarComprobante(req).subscribe(
      (data: OutResponse<ComprobanteResponse[]>) => {
        console.log(data);
        if (data.rCodigo == 0) {
          this.listaComprobanteResponse = data.rResult;
        } else {
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
    this.index = this.listaComprobanteResponse.indexOf(obj);

    this.comprobanteService.eliminarComprobante(obj).subscribe(
      (data: OutResponse<ComprobanteResponse>) => {
        if (data.rCodigo == 0) {
          this.listaComprobanteResponse.splice(this.index, 1);
          this.cargarDatosTabla();
        } else {
          this._snackBar.open(data.rMensaje, null, { duration: 5000, horizontalPosition: 'right', verticalPosition: 'top', panelClass: ['error-snackbar'] });
        }
      }, error => {
        console.error(error);
        this._snackBar.open(error.statusText, null, { duration: 5000, horizontalPosition: 'right', verticalPosition: 'top', panelClass: ['error-snackbar'] });
      }
    )
  }

}
