import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { FormService } from 'src/app/core/services/form.service';
import { UsuarioService } from 'src/app/core/services/usuario.service';
import { DataDialog } from 'src/app/modules/intranet/dto/data-dialog';
import { InsumoRequest } from 'src/app/modules/intranet/dto/request/insumo.request';
import { PersonalBuscarRequest } from 'src/app/modules/intranet/dto/request/personal-buscar.request';
import { TipoInsumoBuscarRequest } from 'src/app/modules/intranet/dto/request/tipo-insumo-buscar.request';
import { InsumoResponse } from 'src/app/modules/intranet/dto/response/insumo.response';
import { OutResponse } from 'src/app/modules/intranet/dto/response/out.response';
import { PersonalResponse } from 'src/app/modules/intranet/dto/response/personal.response';
import { TipoInsumoResponse } from 'src/app/modules/intranet/dto/response/tipo-insumo.response';
import { InsumoService } from 'src/app/modules/intranet/services/insumo.service';
import { PersonalService } from 'src/app/modules/intranet/services/personal.service';

@Component({
  selector: 'app-reg-insumos-personal',
  templateUrl: './reg-insumos-personal.component.html',
  styleUrls: ['./reg-insumos-personal.component.scss']
})
export class RegInsumosPersonalComponent implements OnInit {
  guardar: boolean = false;
  modif: boolean = false;

  listaPersonalResponse: PersonalResponse[];
  listaTipoInsumoResponse: TipoInsumoResponse[];

  formularioGrp: FormGroup;
  formErrors: any;

  constructor(private fb: FormBuilder,
    public dialogRef: MatDialogRef<RegInsumosPersonalComponent>,
    private _snackBar: MatSnackBar,
    @Inject(InsumoService) private insumoService: InsumoService,
    @Inject(PersonalService) private personalService: PersonalService,
    @Inject(UsuarioService) private user: UsuarioService,
    @Inject(FormService) private formService: FormService,
    @Inject(MAT_DIALOG_DATA) public data: DataDialog<InsumoResponse>) { }

  ngOnInit() {
    this.formularioGrp = this.fb.group({
      tipoInsumo: ['', [Validators.required]],
      personal: ['', [Validators.required]],
      cantidad: ['', [Validators.required]],
      fecha: ['', [Validators.required]],
    });
    this.formErrors = this.formService.buildFormErrors(this.formularioGrp, this.formErrors);
    this.formularioGrp.valueChanges.subscribe((val: any) => {
      this.formService.getValidationErrors(this.formularioGrp, this.formErrors, false);
    });

    this.inicializarVariables();
  }

  public inicializarVariables(): void {
    this.comboTipoInsumo();
    this.comboPersonal();

    if (this.data.objeto) {
      this.formularioGrp.get('cantidad').setValue(this.data.objeto.cantidad);
      this.formularioGrp.get('fecha').setValue(this.data.objeto.fecha ? new Date(this.data.objeto.fecha.toString().replace(/(\d{2})-(\d{2})-(\d{4})/, "$2/$1/$3")) : '');
    } else {
      this.formularioGrp.get('fecha').setValue(new Date());
    }
  }

  comboTipoInsumo(): void {
    let req = new TipoInsumoBuscarRequest();

    this.insumoService.listarTipoInsumo(req).subscribe(
      (data: OutResponse<TipoInsumoResponse[]>) => {
        if (data.rCodigo == 0) {
          this.listaTipoInsumoResponse = data.rResult;

          if (this.data.objeto) {
            this.formularioGrp.get('tipoInsumo').setValue(this.listaTipoInsumoResponse.filter(el => el.id == this.data.objeto.tipoInsumo.id)[0]);
          } else {
            this.formularioGrp.get('tipoInsumo').setValue(this.listaTipoInsumoResponse[0]);
          }
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

          if (this.data.objeto) {
            this.formularioGrp.get('personal').setValue(this.listaPersonalResponse.filter(el => el.id == this.data.objeto.personal.id)[0]);
          } else {
            this.formularioGrp.get('personal').setValue(this.listaPersonalResponse[0]);
          }
        } else {
          this._snackBar.open(data.rMensaje, null, { duration: 5000, horizontalPosition: 'right', verticalPosition: 'top', panelClass: ['error-snackbar'] });
        }
      }, error => {
        console.error(error);
        this._snackBar.open(error.statusText, null, { duration: 5000, horizontalPosition: 'right', verticalPosition: 'top', panelClass: ['error-snackbar'] });
      }
    )
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

      let mae = new InsumoRequest();
      mae.id = 0;
      mae.personal.id = this.formularioGrp.get('personal').value.id;
      mae.personal.persona.id = this.formularioGrp.get('personal').value.persona.id;
      mae.personal.persona.apePaterno = this.formularioGrp.get('personal').value.persona.apePaterno;
      mae.personal.persona.apeMaterno = this.formularioGrp.get('personal').value.persona.apeMaterno;
      mae.personal.persona.nombre = this.formularioGrp.get('personal').value.persona.nombre;
      mae.tipoInsumo.id = this.formularioGrp.get('tipoInsumo').value.id;
      mae.tipoInsumo.nombre = this.formularioGrp.get('tipoInsumo').value.nombre;
      mae.tipoInsumo.nomUnidadMedida = this.formularioGrp.get('tipoInsumo').value.nomUnidadMedida;
      mae.tipoInsumo.idtUnidadMedida = this.formularioGrp.get('tipoInsumo').value.idtUnidadMedida;
      mae.cantidad = this.formularioGrp.get('cantidad').value;
      mae.fecha = this.formularioGrp.get('fecha').value;
      mae.flgActivo = 1;
      mae.idUsuarioCrea = this.user.getId;
      mae.fecUsuarioCrea = new Date();

      console.log(mae);
      this.insumoService.registrarInsumo(mae).subscribe(
        (data: OutResponse<InsumoResponse>) => {
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

      let req = new InsumoRequest();
      req.id = this.data.objeto.id;
      req.personal.id = this.formularioGrp.get('personal').value.id;
      req.personal.persona.id = this.formularioGrp.get('personal').value.persona.id;
      req.personal.persona.apePaterno = this.formularioGrp.get('personal').value.persona.apePaterno;
      req.personal.persona.apeMaterno = this.formularioGrp.get('personal').value.persona.apeMaterno;
      req.personal.persona.nombre = this.formularioGrp.get('personal').value.persona.nombre;
      req.tipoInsumo.id = this.formularioGrp.get('tipoInsumo').value.id;
      req.tipoInsumo.nombre = this.formularioGrp.get('tipoInsumo').value.nombre;
      req.tipoInsumo.nomUnidadMedida = this.formularioGrp.get('tipoInsumo').value.nomUnidadMedida;
      req.tipoInsumo.idtUnidadMedida = this.formularioGrp.get('tipoInsumo').value.idtUnidadMedida;
      req.cantidad = this.formularioGrp.get('cantidad').value;
      req.fecha = this.formularioGrp.get('fecha').value;
      req.flgActivo = this.data.objeto.flgActivo;
      req.idUsuarioCrea = this.data.objeto.idUsuarioCrea;
      req.fecUsuarioCrea = this.data.objeto.fecUsuarioCrea;
      req.idUsuarioMod = this.user.getId;
      req.fecUsuarioMod = new Date();

      this.insumoService.modificarInsumo(req).subscribe(
        (data: OutResponse<InsumoResponse>) => {
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

}
