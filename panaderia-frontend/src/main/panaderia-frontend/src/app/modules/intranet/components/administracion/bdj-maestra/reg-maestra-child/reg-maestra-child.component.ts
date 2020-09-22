import { Component, OnInit, ViewChild, Inject } from '@angular/core';
import { MaestraResponse } from 'src/app/modules/intranet/dto/response/maestra.response';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { UsuarioService } from 'src/app/core/services/usuario.service';
import { DatePipe } from '@angular/common';
import { MaestraRequest } from 'src/app/modules/intranet/dto/request/maestra.request';
import { MaestraBuscarRequest } from 'src/app/modules/intranet/dto/request/maestra-buscar.request';
import { OutResponse } from 'src/app/modules/intranet/dto/response/out.response';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MaestraService } from 'src/app/modules/intranet/services/maestra.service';
import { FormService } from 'src/app/core/services/form.service';
import { DataDialog } from 'src/app/modules/intranet/dto/data-dialog';

@Component({
  selector: 'app-reg-maestra-child',
  templateUrl: './reg-maestra-child.component.html',
  styleUrls: ['./reg-maestra-child.component.scss']
})
export class RegMaestraChildComponent implements OnInit {
  guardar: boolean = false;
  modificar: boolean = false;
  index: number;

  listaMaestraResponse: MaestraResponse[];
  displayedColumns: string[];
  dataSource: MatTableDataSource<MaestraResponse>;
  isLoading: boolean = false;

  eMaestra: MaestraRequest = null;

  formularioGrp: FormGroup;
  formErrors: any;

  columnsGrilla = [
    {
      columnDef: 'orden',
      header: 'Orden',
      cell: (m: MaestraResponse) => `${m.orden}`
    }, {
      columnDef: 'nombre',
      header: 'Nombre',
      cell: (m: MaestraResponse) => `${m.nombre}`
    }, {
      columnDef: 'codigo',
      header: 'Codigo',
      cell: (m: MaestraResponse) => `${m.codigo}`
    }, {
      columnDef: 'valor',
      header: 'Valor',
      cell: (m: MaestraResponse) => (m.valor != null) ? `${m.valor}` : ''
    }, {
      columnDef: 'descripcion',
      header: 'Descrpcion',
      cell: (m: MaestraResponse) => (m.descripcion != null) ? `${m.descripcion}` : ''
    }, {
      columnDef: 'idUsuarioCrea',
      header: 'Usuario creador',
      cell: (m: MaestraResponse) => `${m.idUsuarioCrea}`
    }, {
      columnDef: 'fecUsuarioCrea',
      header: 'Fecha creacion',
      cell: (m: MaestraResponse) => this.datePipe.transform(m.fecUsuarioCrea, 'dd/MM/yyyy')
    }, {
      columnDef: 'idUsuarioMod',
      header: 'Usuario modifica',
      cell: (m: MaestraResponse) => (m.idUsuarioMod != null) ? `${m.idUsuarioMod}` : ''
    }, {
      columnDef: 'fecUsuarioMod',
      header: 'Fecha modifica',
      cell: (m: MaestraResponse) => this.datePipe.transform(m.fecUsuarioMod, 'dd/MM/yyyy')
    }];
  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor(private fb: FormBuilder,
    public dialogRef: MatDialogRef<RegMaestraChildComponent>,
    private _snackBar: MatSnackBar,
    @Inject(MaestraService) private maestraService: MaestraService,
    @Inject(FormService) private formService: FormService,
    @Inject(UsuarioService) private user: UsuarioService,
    @Inject(MAT_DIALOG_DATA) public data: DataDialog<MaestraResponse>,
    private datePipe: DatePipe) { }

  ngOnInit() {
    console.log(this.data);
    this.formularioGrp = this.fb.group({
      nombre: ['', [Validators.required]],
      codigo: ['', [Validators.required]],
      orden: ['', [Validators.required]],
      valor: ['', []],
      descripcion: ['', []]
    });
    this.formErrors = this.formService.buildFormErrors(this.formularioGrp, this.formErrors);
    this.formularioGrp.valueChanges.subscribe((val: any) => {
      this.formService.getValidationErrors(this.formularioGrp, this.formErrors, false);
    });

    this.definirTabla();
    this.inicializarVariables();
  }

  inicializarVariables(): void {
    this.listarMaestra();
  }

  definirTabla(): void {
    this.displayedColumns = [];
    this.columnsGrilla.forEach(c => {
      this.displayedColumns.push(c.columnDef);
    });
    this.displayedColumns.unshift('id');
    this.displayedColumns.push('opt');
  }

  listarMaestra(): void {
    this.dataSource = null;
    this.isLoading = true;

    let req = new MaestraBuscarRequest();
    req.idMaestra = this.data.objeto.id;

    this.maestraService.listarMaestra(req).subscribe(
      (data: OutResponse<MaestraResponse[]>) => {
        if (data.rCodigo == 0) {
          this.listaMaestraResponse = data.rResult;
        } else {
          this.listaMaestraResponse = [];
        }
        this.cargarDatosTabla();
        this.isLoading = false;
      }, error => {
        this.isLoading = false;
      }
    );
  }

  cargarDatosTabla(): void {
    if (this.listaMaestraResponse.length > 0) {
      this.dataSource = new MatTableDataSource(this.listaMaestraResponse);
      this.dataSource.paginator = this.paginator;
      this.dataSource.sort = this.sort;
    }
    this.formularioGrp.get('orden').setValue(this.listaMaestraResponse.length + 1);
  }

  ejecutar(): void {
    if (this.data.objeto) {
      this.editMaestra();
    } else {
      this.regMaestra();
    }
  }

  regMaestra(): void {
    console.log(this.formularioGrp);
    if (this.formularioGrp.valid) {
      this.guardar = true;
      console.log('VALIDO');
      console.log(this.formularioGrp.value);

      let req = new MaestraRequest();
      req.id = 0;
      req.idMaestra = this.data.objeto.id;
      req.idTabla = this.data.objeto.idTabla;
      req.idItem = this.listaMaestraResponse.length + 1;
      req.orden = this.formularioGrp.get('orden').value;
      req.nombre = this.formularioGrp.get('nombre').value;
      req.codigo = this.formularioGrp.get('codigo').value;
      req.valor = this.formularioGrp.get('valor').value;
      req.descripcion = this.formularioGrp.get('descripcion').value;
      req.flagActivo = 1;
      req.idUsuarioCrea = this.user.getId;
      req.fecUsuarioCrea = new Date();

      console.log(req);
      this.maestraService.registrarMaestra(req).subscribe(
        (data: OutResponse<MaestraResponse>) => {
          if (data.rCodigo == 0) {
            // this.spinnerService.hide();
            this.limpiar();
            this.listaMaestraResponse.unshift(data.rResult);
            this.cargarDatosTabla();
          } else {
            this._snackBar.open(data.rMensaje, null, { duration: 5000, horizontalPosition: 'right', verticalPosition: 'top', panelClass: ['warning-snackbar'] });
          }
          this.guardar = false;
        },
        error => {
          console.error(error);
          this.guardar = false;
          this._snackBar.open(error.statusText, null, { duration: 5000, horizontalPosition: 'right', verticalPosition: 'top', panelClass: ['error-snackbar'] });
        }
      );
    } else {
      this.formService.getValidationErrors(this.formularioGrp, this.formErrors, true);
    }
  }

  mostrarMaestra(obj: MaestraResponse): void {
    this.index = this.listaMaestraResponse.indexOf(obj);

    console.log(obj);
    this.formularioGrp.get('nombre').setValue(obj.nombre);
    this.formularioGrp.get('codigo').setValue(obj.codigo);
    this.formularioGrp.get('orden').setValue(obj.orden);
    this.formularioGrp.get('valor').setValue(obj.valor);

    this.eMaestra = new MaestraRequest();
    this.eMaestra.id = obj.id;
    this.eMaestra.idMaestra = obj.idMaestra;
    this.eMaestra.idTabla = obj.idTabla;
    this.eMaestra.idItem = obj.idItem;
    this.eMaestra.orden = obj.orden;
    this.eMaestra.codigo = obj.codigo;
    this.eMaestra.nombre = obj.nombre;
    this.eMaestra.valor = obj.valor;
    this.eMaestra.descripcion = obj.descripcion;
    this.eMaestra.flagActivo = obj.flagActivo;
    this.eMaestra.idUsuarioCrea = obj.idUsuarioCrea;
    this.eMaestra.fecUsuarioCrea = obj.fecUsuarioCrea;
    console.log(this.eMaestra);
  }

  editMaestra(): void {
    if (this.formularioGrp.valid) {
      this.modificar = true;
      let mae = this.eMaestra;
      mae.nombre = this.formularioGrp.get('nombre').value;
      mae.codigo = this.formularioGrp.get('codigo').value;
      mae.orden = this.formularioGrp.get('orden').value;
      mae.valor = this.formularioGrp.get('valor').value;
      mae.descripcion = this.formularioGrp.get('descripcion').value;
      mae.idUsuarioMod = this.user.getId;
      mae.fecUsuarioMod = new Date();

      this.maestraService.modificarMaestra(mae).subscribe(
        (data: OutResponse<MaestraResponse>) => {
          if (data.rCodigo == 0) {
            this.limpiar();
            this.listaMaestraResponse.splice(this.index, 1);
            this.listaMaestraResponse.unshift(mae);
            this.cargarDatosTabla();
          } else {
            this._snackBar.open(data.rMensaje, null, { duration: 5000, horizontalPosition: 'right', verticalPosition: 'top', panelClass: ['warning-snackbar'] });
          }
          this.modificar = false;
        },
        error => {
          console.log(error);
          this.modificar = false;
          this._snackBar.open(error.statusText, null, { duration: 5000, horizontalPosition: 'right', verticalPosition: 'top', panelClass: ['error-snackbar'] });
        }
      );
    } else {
      this.formService.getValidationErrors(this.formularioGrp, this.formErrors, true);
    }
  }

  limpiar(): void {
    this.eMaestra = null;
    this.formService.setAsUntoched(this.formularioGrp, this.formErrors);
  }

  eliminarMaestra(obj): void {
    this.index = this.listaMaestraResponse.indexOf(obj);
    this.maestraService.eliminarMaestra(obj).subscribe(
      (data: OutResponse<MaestraResponse>) => {
        if (data.rCodigo == 0) {
          this.listaMaestraResponse.splice(this.index, 1);
          this.cargarDatosTabla();
        } else {
          this._snackBar.open(data.rMensaje, null, { duration: 5000, horizontalPosition: 'right', verticalPosition: 'top', panelClass: ['warning-snackbar'] });
        }
      },
      error => {
        console.log(error);
        this._snackBar.open(error.statusText, null, { duration: 5000, horizontalPosition: 'right', verticalPosition: 'top', panelClass: ['error-snackbar'] });
      }
    );
  }

}
