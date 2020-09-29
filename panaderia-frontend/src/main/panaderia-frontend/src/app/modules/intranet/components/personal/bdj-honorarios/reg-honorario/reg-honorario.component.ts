import { DecimalPipe } from '@angular/common';
import { Component, Inject, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { NgxSpinnerService } from 'ngx-spinner';
import { MENSAJES } from 'src/app/common';
import { FormService } from 'src/app/core/services/form.service';
import { UsuarioService } from 'src/app/core/services/usuario.service';
import { DataDialog } from 'src/app/modules/intranet/dto/data-dialog';
import { HonorarioDetalleRequest } from 'src/app/modules/intranet/dto/request/honorario-detalle.request';
import { HonorarioPeriodoRequest } from 'src/app/modules/intranet/dto/request/honorario-periodo.request';
import { HonorarioRequest } from 'src/app/modules/intranet/dto/request/honorario.request';
import { InsumoBuscarRequest } from 'src/app/modules/intranet/dto/request/insumo-buscar.request';
import { HonorarioPeriodoResponse } from 'src/app/modules/intranet/dto/response/honorario-periodo.response';
import { HonorarioResponse } from 'src/app/modules/intranet/dto/response/honorario.response';
import { InsumoResponse } from 'src/app/modules/intranet/dto/response/insumo.response';
import { OutResponse } from 'src/app/modules/intranet/dto/response/out.response';
import { PersonaResponse } from 'src/app/modules/intranet/dto/response/persona.response';
import { PersonalResponse } from 'src/app/modules/intranet/dto/response/personal.response';
import { HonorarioService } from 'src/app/modules/intranet/services/honorario.service';
import { InsumoService } from 'src/app/modules/intranet/services/insumo.service';
import { BuscarPersonalComponent } from './buscar-personal/buscar-personal.component';

@Component({
  selector: 'app-reg-honorario',
  templateUrl: './reg-honorario.component.html',
  styleUrls: ['./reg-honorario.component.scss']
})
export class RegHonorarioComponent implements OnInit {
  guardar: boolean = false;
  modif: boolean = false;

  personal: PersonalResponse;

  listaInsumo: InsumoResponse[];
  // listaTipoInsumoResponse: TipoInsumoResponse[];

  formularioGrp: FormGroup;
  formErrors: any;

  detFormularioGrp: FormGroup;
  detFormErrors: any;

  listaHonorarioDetalleRequest: HonorarioDetalleRequest[] = [];
  displayedColumns: string[];
  dataSource: MatTableDataSource<HonorarioDetalleRequest> = null;;
  isLoading: boolean = false;

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor(private fb: FormBuilder,
    public dialog: MatDialog,
    public dialogRef: MatDialogRef<RegHonorarioComponent>,
    private _snackBar: MatSnackBar,
    private spinner: NgxSpinnerService,
    private decimalPipe: DecimalPipe,
    @Inject(InsumoService) private insumoService: InsumoService,
    @Inject(HonorarioService) private honorarioService: HonorarioService,
    @Inject(UsuarioService) private user: UsuarioService,
    @Inject(FormService) private formService: FormService,
    @Inject(MAT_DIALOG_DATA) public data: DataDialog<HonorarioResponse>) { }

  ngOnInit() {
    this.formularioGrp = this.fb.group({
      nombre: [{ value: '', disabled: true }, [Validators.required]],
      fecInicio: ['', [Validators.required]],
      fecFin: ['', [Validators.required]],
      monto: ['', [Validators.required]],
    });
    this.formErrors = this.formService.buildFormErrors(this.formularioGrp, this.formErrors);
    this.formularioGrp.valueChanges.subscribe((val: any) => {
      this.formService.getValidationErrors(this.formularioGrp, this.formErrors, false);
    });

    this.detFormularioGrp = new FormGroup({});

    this.definirTabla();
    this.inicializarVariables();
  }

  public inicializarVariables(): void {
    // if (this.data.objeto) {
    //   this.formularioGrp.get('cantidad').setValue(this.data.objeto.cantidad);
    //   this.formularioGrp.get('fecha').setValue(this.data.objeto.fecha ? new Date(this.data.objeto.fecha.toString().replace(/(\d{2})-(\d{2})-(\d{4})/, "$2/$1/$3")) : '');
    // } else {
    //   this.formularioGrp.get('fecha').setValue(new Date());
    // }
  }

  definirTabla(): void {
    this.displayedColumns = ['tipoInsumo', 'unidadMedida', 'cantidad', 'tarifa', 'subtotal'];
    this.displayedColumns.unshift('id');
  }

  public cargarDatosTabla(): void {
    if (this.listaHonorarioDetalleRequest.length > 0) {
      this.dataSource = new MatTableDataSource(this.listaHonorarioDetalleRequest);
      this.dataSource.paginator = this.paginator;
      this.dataSource.sort = this.sort;
    }
  }

  ejecutar(): void {
    if (this.data.objeto) {
      this.modificar();
    } else {
      this.registrar();
    }
  }

  registrar(): void {
    if (this.formularioGrp.valid && this.detFormularioGrp.valid) {
      this.guardar = true;

      let mae = new HonorarioRequest();
      mae.id = 0;
      mae.personal.id = this.personal.id;
      mae.personal.persona.id = this.personal.persona.id;
      mae.personal.persona.apePaterno = this.personal.persona.apePaterno;
      mae.personal.persona.apeMaterno = this.personal.persona.apeMaterno;
      mae.personal.persona.nombre = this.personal.persona.nombre;
      mae.monto = this.formularioGrp.get('monto').value;
      mae.fechaInicio = this.formularioGrp.get('fecInicio').value;
      mae.fechaFin = this.formularioGrp.get('fecFin').value;
      mae.fecha = new Date();
      mae.flgActivo = 1;
      mae.idUsuarioCrea = this.user.getId;
      mae.fecUsuarioCrea = new Date();

      mae.listaInsumo = this.listaInsumo;
      mae.listaHonorarioDetalle = this.listaHonorarioDetalleRequest;

      console.log(mae);
      this.honorarioService.registrarHonorario(mae).subscribe(
        (data: OutResponse<HonorarioResponse>) => {
          if (data.rCodigo == 0) {
            this.dialogRef.close(data.rResult);
          } else {
            this._snackBar.open(data.rMensaje, null, { duration: 5000, horizontalPosition: 'right', verticalPosition: 'top', panelClass: ['warning-snackbar'] });
          }
          this.guardar = false;
        },
        error => {
          console.log(error);
          this.guardar = false;
          this._snackBar.open(error.statusText, null, { duration: 5000, horizontalPosition: 'right', verticalPosition: 'top', panelClass: ['error-snackbar'] });
        }
      );
    } else {
      this.formService.getValidationErrors(this.formularioGrp, this.formErrors, true);
      this.formService.getValidationErrors(this.detFormularioGrp, this.detFormErrors, true);
    }
  }

  modificar(): void {
    if (this.formularioGrp.valid) {
      this.modif = true;

      let req = new HonorarioRequest();
      req.id = this.data.objeto.id;
      req.personal.id = this.formularioGrp.get('personal').value.id;
      req.personal.persona.id = this.formularioGrp.get('personal').value.persona.id;
      req.personal.persona.apePaterno = this.formularioGrp.get('personal').value.persona.apePaterno;
      req.personal.persona.apeMaterno = this.formularioGrp.get('personal').value.persona.apeMaterno;
      req.personal.persona.nombre = this.formularioGrp.get('personal').value.persona.nombre;
      // req.tipoInsumo.id = this.formularioGrp.get('tipoInsumo').value.id;
      // req.tipoInsumo.nombre = this.formularioGrp.get('tipoInsumo').value.nombre;
      // req.tipoInsumo.nomUnidadMedida = this.formularioGrp.get('tipoInsumo').value.nomUnidadMedida;
      // req.tipoInsumo.idtUnidadMedida = this.formularioGrp.get('tipoInsumo').value.idtUnidadMedida;
      // req.cantidad = this.formularioGrp.get('cantidad').value;
      req.fecha = this.formularioGrp.get('fecha').value;
      req.flgActivo = this.data.objeto.flgActivo;
      req.idUsuarioCrea = this.data.objeto.idUsuarioCrea;
      req.fecUsuarioCrea = this.data.objeto.fecUsuarioCrea;
      req.idUsuarioMod = this.user.getId;
      req.fecUsuarioMod = new Date();

      this.honorarioService.modificarHonorario(req).subscribe(
        (data: OutResponse<HonorarioResponse>) => {
          if (data.rCodigo == 0) {
            this.dialogRef.close(req);
          } else {
            this._snackBar.open(data.rMensaje, null, { duration: 5000, horizontalPosition: 'right', verticalPosition: 'top', panelClass: ['warning-snackbar'] });
          }
          this.modif = false;
        }, error => {
          console.log(error);
          this.modif = false;
          this._snackBar.open(error.statusText, null, { duration: 5000, horizontalPosition: 'right', verticalPosition: 'top', panelClass: ['error-snackbar'] });
        }
      );
    } else {
      this.formService.getValidationErrors(this.formularioGrp, this.formErrors, true);
    }
  }

  buscarPersonal(): void {
    const dialogRef = this.dialog.open(BuscarPersonalComponent, {
      width: '600px',
      disableClose: false,
      data: {
        titulo: MENSAJES.INTRANET.BANDEJA_HONORARIO.BUSCAR_PERSONAL.TITLE,
        objeto: null
      }
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.personal = result;
        this.formularioGrp.get('nombre').setValue(this.personal.persona.nombre + ' ' + this.personal.persona.apePaterno + ' ' + this.personal.persona.apeMaterno)
        this.buscarPeriodoHonorario();
      }
    });
  }

  buscarPeriodoHonorario(): void {
    if (this.personal) {
      this.spinner.show();

      let req = new HonorarioPeriodoRequest();
      req.idPersonal = this.personal.id;

      this.honorarioService.buscarPeriodoHonorario(req).subscribe(
        (data: OutResponse<HonorarioPeriodoResponse>) => {
          if (data.rCodigo == 0) {
            this.formularioGrp.get('fecInicio').setValue(data.rResult.fecInicio ? new Date(data.rResult.fecInicio.toString().replace(/(\d{2})-(\d{2})-(\d{4})/, "$2/$1/$3")) : '');
            this.formularioGrp.get('fecFin').setValue(data.rResult.fecFin ? new Date(data.rResult.fecFin.toString().replace(/(\d{2})-(\d{2})-(\d{4})/, "$2/$1/$3")) : '');
          } else {
            this._snackBar.open(data.rMensaje, null, { duration: 5000, horizontalPosition: 'right', verticalPosition: 'top', panelClass: ['warning-snackbar'] });
          }
          this.spinner.hide();
        }, error => {
          console.log(error);
          this.spinner.hide();
          this._snackBar.open(error.statusText, null, { duration: 5000, horizontalPosition: 'right', verticalPosition: 'top', panelClass: ['error-snackbar'] });
        }
      );
    } else {
      this._snackBar.open("SELECCIONE PESONAL", null, { duration: 5000, horizontalPosition: 'right', verticalPosition: 'top', panelClass: ['error-snackbar'] });
    }
  }

  buscaInsumos(): void {
    if (this.formularioGrp.get('nombre').value) {
      if (this.formularioGrp.get('fecInicio').value && this.formularioGrp.get('fecFin').value) {
        this.dataSource = null;
        this.isLoading = true;

        let req = new InsumoBuscarRequest();
        req.idPersonal = this.personal.id;
        req.fecInicio = this.formularioGrp.get('fecInicio').value;
        req.fecFin = this.formularioGrp.get('fecFin').value;
        req.flgCalHonorario = 0;

        this.insumoService.listarInsumo(req).subscribe(
          (data: OutResponse<InsumoResponse[]>) => {
            console.log(data);
            if (data.rCodigo == 0) {
              this.listaInsumo = data.rResult;
              this.listaHonorarioDetalleRequest = this.resumirInsumos(this.listaInsumo);
              this.crearControles();
              this.cargarDatosTabla();
              this.isLoading = false;
            } else {
              this._snackBar.open(data.rMensaje, null, { duration: 5000, horizontalPosition: 'right', verticalPosition: 'top', panelClass: ['warning-snackbar'] });
              this.listaInsumo = [];
              this.cargarDatosTabla();
              this.isLoading = false;
            }
          },
          error => {
            console.log(error);
            this._snackBar.open(error.statusText, null, { duration: 5000, horizontalPosition: 'right', verticalPosition: 'top', panelClass: ['error-snackbar'] });
            this.isLoading = false;
          }
        );
      } else {
        this._snackBar.open("INGRESE FECHAS DE BUSQUEDA", null, { duration: 5000, horizontalPosition: 'right', verticalPosition: 'top', panelClass: ['error-snackbar'] });
      }
    } else {
      this._snackBar.open("SELECCIONE PESONAL", null, { duration: 5000, horizontalPosition: 'right', verticalPosition: 'top', panelClass: ['error-snackbar'] });
    }
  }

  resumirInsumos(lista: InsumoResponse[]): HonorarioDetalleRequest[] {
    let array: HonorarioDetalleRequest[] = [];

    lista.forEach(obj => {
      let selected = array.filter(el => el.tipoInsumo.id == obj.tipoInsumo.id)[0];
      if (selected) {
        let index = array.indexOf(selected);
        selected.cantidad += obj.cantidad;

        array.splice(index, 1);
        array.push(selected);
      } else {
        let req = new HonorarioDetalleRequest();
        req.idInsumo = obj.id;
        req.flgActivo = 1;
        req.tipoInsumo = obj.tipoInsumo;
        req.cantidad = obj.cantidad;
        req.subtotal = 0.0;

        array.push(req);
      }
    })
    return array;
  }

  crearControles(): void {
    const frmCtrl = {};

    this.listaHonorarioDetalleRequest.forEach((el, i) => {
      frmCtrl[`t${i}`] = new FormControl({ value: 0, disabled: false }, [Validators.required]);
    });

    this.detFormularioGrp = new FormGroup(frmCtrl);
    this.detFormErrors = this.formService.buildFormErrors(this.detFormularioGrp, this.detFormErrors);
    this.detFormularioGrp.valueChanges.subscribe((val: any) => {
      console.log('VALUE CHANGES');
      this.formService.getValidationErrors(this.detFormularioGrp, this.detFormErrors, false);
      this.calcularMonto();
    });
  }

  calcularMonto(): void {
    let monto = 0.0;
    this.listaHonorarioDetalleRequest.forEach((el, i) => {
      el.tarifa = this.detFormularioGrp.get(`t${i}`).value;
      el.subtotal = el.cantidad * el.tarifa;
      monto += el.subtotal;
    });

    this.formularioGrp.get('monto').setValue(monto);
  }

}
