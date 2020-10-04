import { Component, Inject, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { NgxSpinnerService } from 'ngx-spinner';
import { FormService } from 'src/app/core/services/form.service';
import { ReporteInsumoResponse } from '../../../dto/response/reporte-insumo.response';
import { PersonalResponse } from '../../../dto/response/personal.response';
import { TipoInsumoResponse } from '../../../dto/response/tipo-insumo.response';
import { PersonalService } from '../../../services/personal.service';
import { ReporteService } from '../../../services/reporte.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { DatePipe, DecimalPipe } from '@angular/common';
import { MomentDateAdapter, MAT_MOMENT_DATE_ADAPTER_OPTIONS } from '@angular/material-moment-adapter';
import { DateAdapter, MAT_DATE_FORMATS, MAT_DATE_LOCALE } from '@angular/material/core';
import { MatDatepicker } from '@angular/material/datepicker';
// Depending on whether rollup is used, moment needs to be imported differently.
// Since Moment.js doesn't have a default export, we normally need to import using the `* as`
// syntax. However, rollup creates a synthetic default module and we thus need to import it using
// the `default as` syntax.
import * as _moment from 'moment';
// tslint:disable-next-line:no-duplicate-imports
// import { default as _rollupMoment, Moment } from 'moment';
import { defaultFormat as _rollupMoment, Moment } from 'moment';
import { TipoInsumoBuscarRequest } from '../../../dto/request/tipo-insumo-buscar.request';
import { InsumoService } from '../../../services/insumo.service';
import { OutResponse } from '../../../dto/response/out.response';
import { PersonalBuscarRequest } from '../../../dto/request/personal-buscar.request';
import { ReporteInsumoBuscarRequest } from '../../../dto/request/reporte-insumo-buscar.request';

const moment = _rollupMoment || _moment;

// See the Moment.js docs for the meaning of these formats:
// https://momentjs.com/docs/#/displaying/format/
export const MY_FORMATS = {
  parse: {
    dateInput: 'DD/MM/YYYY',
  },
  display: {
    dateInput: 'DD/MM/YYYY',
    monthYearLabel: 'MMM YYYY',
    dateA11yLabel: 'LL',
    monthYearA11yLabel: 'MMMM YYYY',
  },
};

@Component({
  selector: 'app-bdj-rep-insumos',
  templateUrl: './bdj-rep-insumos.component.html',
  styleUrls: ['./bdj-rep-insumos.component.scss'],
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
export class BdjRepInsumosComponent implements OnInit {
  exportar = false;
  mesFinM: Date;

  listaTipoReporte: any[] = [{ id: 1, nombre: 'DIARIO' }, { id: 2, nombre: 'MENSUAL' }];

  listaPersonalResponse: PersonalResponse[];
  listaTipoInsumoResponse: TipoInsumoResponse[];

  listaReporteInsumoResponse: ReporteInsumoResponse[];
  displayedColumns: string[];
  dataSource: MatTableDataSource<ReporteInsumoResponse>;
  isLoading: boolean = false;

  formularioGrp: FormGroup;
  formErrors: any;
  columnsGrilla = [
    {
      columnDef: 'anio',
      header: 'Año',
      cell: (m: ReporteInsumoResponse) => `${m.anio ? m.anio : ''}`
    }, {
      columnDef: 'mes',
      header: 'Mes',
      cell: (m: ReporteInsumoResponse) => `${m.mes ? m.mes : ''}`
    }, {
      columnDef: 'fecha',
      header: 'Fecha',
      cell: (m: ReporteInsumoResponse) => `${m.fecha ? m.fecha : ''}`
    }, {
      columnDef: 'nomPersonal',
      header: 'Personal',
      cell: (m: ReporteInsumoResponse) => `${m.nomPersonal ? m.nomPersonal : ''}`
    }, {
      columnDef: 'nomTipoInsumo',
      header: 'Tipo insumo',
      cell: (m: ReporteInsumoResponse) => `${m.nomTipoInsumo ? m.nomTipoInsumo : ''}`
    }, {
      columnDef: 'suma',
      header: 'Cantidad total',
      cell: (m: ReporteInsumoResponse) => `${m.suma ? this.decimalPipe.transform(m.suma, '1.1-1') : ''}`
    }];

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor(private fb: FormBuilder,
    public dialog: MatDialog,
    private spinner: NgxSpinnerService,
    @Inject(PersonalService) private personalService: PersonalService,
    @Inject(InsumoService) private insumoService: InsumoService,
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
      personal: ['', []],
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
    this.comboTipoInsumo();
    this.comboPersonal();
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
    if (this.listaReporteInsumoResponse.length > 0) {
      this.dataSource = new MatTableDataSource(this.listaReporteInsumoResponse);
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

  buscar(): void {
    if (this.formularioGrp.valid) {
      this.dataSource = null;
      this.isLoading = true;

      let req = new ReporteInsumoBuscarRequest();
      req.idTipoReporte = this.formularioGrp.get('tipoReporte').value.id;
      req.idTipoInsumo = this.formularioGrp.get('tipoInsumo').value.id;
      req.idPersonal = this.formularioGrp.get('personal').value.id;
      if (req.idTipoReporte == 1) {
        req.fecInicio = this.formularioGrp.get('fecInicio').value;
        req.fecFin = this.formularioGrp.get('fecFin').value;
      } else {
        req.fecInicio = this.formularioGrp.get('mesInicio').value;
        req.fecFin = this.formularioGrp.get('mesFin').value;
      }
      console.log(req);

      this.reporteService.listarReporteInsumo(req).subscribe(
        (data: OutResponse<ReporteInsumoResponse[]>) => {
          console.log(data);
          if (data.rCodigo == 0) {
            this.listaReporteInsumoResponse = data.rResult;
          } else {
            this._snackBar.open(data.rMensaje, null, { duration: 5000, horizontalPosition: 'right', verticalPosition: 'top', panelClass: ['warning-snackbar'] });
            this.listaReporteInsumoResponse = [];
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
    this.exportar = true;

    setTimeout(() => {
      this.exportar = false;
    }, 2000);
    // const dialogRef = this.dialog.open(RegInsumosPersonalComponent, {
    //   width: '600px',
    //   disableClose: false,
    //   data: {
    //     titulo: MENSAJES.INTRANET.BANDEJA_INSUMO.INSUMO.REGISTRAR.TITLE,
    //     objeto: null
    //   }
    // });

    // dialogRef.afterClosed().subscribe((result) => {
    //   if (result) {
    //     this.listaReporteInsumoResponse.unshift(result);
    //     this.cargarDatosTabla();
    //   }
    // });
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
