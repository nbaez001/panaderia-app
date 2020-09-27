import { Component, Inject, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { NgxSpinnerService } from 'ngx-spinner';
import { FormService } from 'src/app/core/services/form.service';
import { DataDialog } from 'src/app/modules/intranet/dto/data-dialog';
import { PersonalBuscarRequest } from 'src/app/modules/intranet/dto/request/personal-buscar.request';
import { OutResponse } from 'src/app/modules/intranet/dto/response/out.response';
import { PersonalResponse } from 'src/app/modules/intranet/dto/response/personal.response';
import { PersonalService } from 'src/app/modules/intranet/services/personal.service';

@Component({
  selector: 'app-buscar-personal',
  templateUrl: './buscar-personal.component.html',
  styleUrls: ['./buscar-personal.component.scss']
})
export class BuscarPersonalComponent implements OnInit {
  listaPersonalResponse: PersonalResponse[];
  displayedColumns: string[];
  dataSource: MatTableDataSource<PersonalResponse>;
  isLoading: boolean = false;

  formularioGrp: FormGroup;
  formErrors: any;
  columnsGrilla = [
    {
      columnDef: 'nombre',
      header: 'Nombre',
      cell: (m: PersonalResponse) => `${m.persona.nombre ? m.persona.nombre : ''}`
    }, {
      columnDef: 'apePaterno',
      header: 'Apellido paterno',
      cell: (m: PersonalResponse) => `${m.persona.apePaterno ? m.persona.apePaterno : ''}`
    }, {
      columnDef: 'apeMaterno',
      header: 'Apellido materno',
      cell: (m: PersonalResponse) => `${m.persona.apeMaterno ? m.persona.apeMaterno : ''}`
    }];

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor(private fb: FormBuilder,
    public dialog: MatDialog,
    public dialogRef: MatDialogRef<BuscarPersonalComponent>,
    private spinner: NgxSpinnerService,
    @Inject(PersonalService) private personalService: PersonalService,
    @Inject(FormService) private formService: FormService,
    private _snackBar: MatSnackBar,
    @Inject(MAT_DIALOG_DATA) public data: DataDialog<any>
  ) { }

  ngOnInit() {
    this.formularioGrp = this.fb.group({
      nombre: ['', []],
      apePaterno: ['', []],
      apeMaterno: ['', []],
    });
    this.formErrors = this.formService.buildFormErrors(this.formularioGrp, this.formErrors);

    this.definirTabla();
    this.inicializarVariables();
  }

  inicializarVariables(): void {
  }

  definirTabla(): void {
    this.displayedColumns = [];
    this.columnsGrilla.forEach(c => {
      this.displayedColumns.push(c.columnDef);
    });
    this.displayedColumns.unshift('id');
    // this.displayedColumns.push('opt');
  }

  public cargarDatosTabla(): void {
    if (this.listaPersonalResponse.length > 0) {
      this.dataSource = new MatTableDataSource(this.listaPersonalResponse);
      this.dataSource.paginator = this.paginator;
      this.dataSource.sort = this.sort;
    }
  }

  buscar(): void {
    this.dataSource = null;
    this.isLoading = true;

    let req = new PersonalBuscarRequest();
    req.nombre = this.formularioGrp.get('nombre').value;
    req.apePaterno = this.formularioGrp.get('apePaterno').value;
    req.apeMaterno = this.formularioGrp.get('apeMaterno').value;

    this.personalService.listarPersonal(req).subscribe(
      (data: OutResponse<PersonalResponse[]>) => {
        if (data.rCodigo == 0) {
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

  seleccionar(el): void {
    this.dialogRef.close(el);
  }

}
