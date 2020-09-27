import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MENSAJES } from 'src/app/common';
import { FormService } from 'src/app/core/services/form.service';
import { UsuarioService } from 'src/app/core/services/usuario.service';
import { DataDialog } from 'src/app/modules/intranet/dto/data-dialog';
import { HonorarioRequest } from 'src/app/modules/intranet/dto/request/honorario.request';
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

  constructor(private fb: FormBuilder,
    public dialog: MatDialog,
    public dialogRef: MatDialogRef<RegHonorarioComponent>,
    private _snackBar: MatSnackBar,
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

  ejecutar(): void {
    if (this.data.objeto) {
      this.modificar();
    } else {
      this.registrar();
    }
  }

  registrar(): void {
    if (this.formularioGrp.valid) {
      this.guardar = true;

      let mae = new HonorarioRequest();
      mae.id = 0;
      mae.personal.id = this.formularioGrp.get('personal').value.id;
      mae.personal.persona.id = this.formularioGrp.get('personal').value.persona.id;
      mae.personal.persona.apePaterno = this.formularioGrp.get('personal').value.persona.apePaterno;
      mae.personal.persona.apeMaterno = this.formularioGrp.get('personal').value.persona.apeMaterno;
      mae.personal.persona.nombre = this.formularioGrp.get('personal').value.persona.nombre;
      // mae.tipoInsumo.id = this.formularioGrp.get('tipoInsumo').value.id;
      // mae.tipoInsumo.nombre = this.formularioGrp.get('tipoInsumo').value.nombre;
      // mae.tipoInsumo.nomUnidadMedida = this.formularioGrp.get('tipoInsumo').value.nomUnidadMedida;
      // mae.tipoInsumo.idtUnidadMedida = this.formularioGrp.get('tipoInsumo').value.idtUnidadMedida;
      // mae.cantidad = this.formularioGrp.get('cantidad').value;
      mae.fecha = this.formularioGrp.get('fecha').value;
      mae.flgActivo = 1;
      // mae.flgCalHonorario = 0;
      mae.idUsuarioCrea = this.user.getId;
      mae.fecUsuarioCrea = new Date();

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
      }
    });
  }

}
