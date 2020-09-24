import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { TABLAS_MAESTRA } from 'src/app/common';
import { FormService } from 'src/app/core/services/form.service';
import { UsuarioService } from 'src/app/core/services/usuario.service';
import { DataDialog } from 'src/app/modules/intranet/dto/data-dialog';
import { MaestraBuscarRequest } from 'src/app/modules/intranet/dto/request/maestra-buscar.request';
import { TipoInsumoRequest } from 'src/app/modules/intranet/dto/request/tipo-insumo.request';
import { MaestraResponse } from 'src/app/modules/intranet/dto/response/maestra.response';
import { OutResponse } from 'src/app/modules/intranet/dto/response/out.response';
import { TipoInsumoResponse } from 'src/app/modules/intranet/dto/response/tipo-insumo.response';
import { InsumoService } from 'src/app/modules/intranet/services/insumo.service';
import { MaestraService } from 'src/app/modules/intranet/services/maestra.service';

@Component({
  selector: 'app-reg-tipo-insumo',
  templateUrl: './reg-tipo-insumo.component.html',
  styleUrls: ['./reg-tipo-insumo.component.scss']
})
export class RegTipoInsumoComponent implements OnInit {
  guardar: boolean = false;
  modif: boolean = false;

  listaUnidadMedida: MaestraResponse[] = [];

  formularioGrp: FormGroup;
  formErrors: any;

  constructor(private fb: FormBuilder,
    public dialogRef: MatDialogRef<RegTipoInsumoComponent>,
    private _snackBar: MatSnackBar,
    @Inject(UsuarioService) private user: UsuarioService,
    @Inject(FormService) private formService: FormService,
    @Inject(MaestraService) private maestraService: MaestraService,
    @Inject(InsumoService) private insumoService: InsumoService,
    @Inject(MAT_DIALOG_DATA) public data: DataDialog<TipoInsumoResponse>) { }

  ngOnInit() {
    this.formularioGrp = this.fb.group({
      unidadMedida: ['', [Validators.required]],
      nombre: ['', [Validators.required, Validators.maxLength(50)]],
      observacion: ['', []],
    });
    this.formErrors = this.formService.buildFormErrors(this.formularioGrp, this.formErrors);
    this.formularioGrp.valueChanges.subscribe((val: any) => {
      this.formService.getValidationErrors(this.formularioGrp, this.formErrors, false);
    });

    this.inicializarVariables();
  }

  public inicializarVariables(): void {
    this.comboUnidadesMedida();

    if (this.data.objeto) {
      this.formularioGrp.get('nombre').setValue(this.data.objeto.nombre);
      this.formularioGrp.get('observacion').setValue(this.data.objeto.observacion);
    }
  }

  completarCombo(controlName: string, lista: any[], field: string, value: any): void {
    if (this.data.objeto) {
      this.formularioGrp.get(controlName).setValue(lista.filter(el => el[field] == value));
    } else {
      this.formularioGrp.get(controlName).setValue(lista[0]);
    }
  }

  comboUnidadesMedida(): void {
    let maestra = new MaestraBuscarRequest();
    maestra.idTabla = TABLAS_MAESTRA.UNIDAD_MEDIDA.ID;
    this.maestraService.listarMaestra(maestra).subscribe(
      (data: OutResponse<MaestraResponse[]>) => {
        if (data.rCodigo == 0) {
          this.listaUnidadMedida = data.rResult;

          if (this.data.objeto) {
            this.formularioGrp.get('unidadMedida').setValue(this.listaUnidadMedida.filter(el => el.id == this.data.objeto.idtUnidadMedida)[0]);
          } else {
            this.formularioGrp.get('unidadMedida').setValue(this.listaUnidadMedida[0]);
          }
        } else {
          this._snackBar.open(data.rMensaje, null, { duration: 5000, horizontalPosition: 'right', verticalPosition: 'top', panelClass: ['warning-snackbar'] });
        }
      }, error => {
        console.log(error);
        this._snackBar.open(error, null, { duration: 5000, horizontalPosition: 'right', verticalPosition: 'top', panelClass: ['error-snackbar'] });
      }
    );
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

      let obj = new TipoInsumoRequest();
      obj.id = 0;
      obj.idtUnidadMedida = this.formularioGrp.get('unidadMedida').value.id;
      obj.nomUnidadMedida = this.formularioGrp.get('unidadMedida').value.nombre;
      obj.nombre = this.formularioGrp.get('nombre').value;
      obj.observacion = this.formularioGrp.get('observacion').value;
      obj.flgActivo = 1;
      obj.idUsuarioCrea = this.user.getId;
      obj.fecUsuarioCrea = new Date();

      this.insumoService.registrarTipoInsumo(obj).subscribe(
        (data: OutResponse<TipoInsumoResponse>) => {
          if (data.rCodigo == 0) {
            this.dialogRef.close(data.rResult);
          } else {
            this._snackBar.open(data.rMensaje, null, { duration: 5000, horizontalPosition: 'right', verticalPosition: 'top', panelClass: ['warning-snackbar'] });
          }
          this.guardar = false;
        }, error => {
          this.guardar = false;
          this._snackBar.open(error, null, { duration: 5000, horizontalPosition: 'right', verticalPosition: 'top', panelClass: ['error-snackbar'] });
        }
      );
    } else {
      this.formService.getValidationErrors(this.formularioGrp, this.formErrors, true);
      console.log(this.formErrors);
    }
  }

  modificar(): void {
    if (this.formularioGrp.valid) {
      this.modif = true;

      let obj = new TipoInsumoRequest();
      obj.id = this.data.objeto.id;
      obj.idtUnidadMedida = this.formularioGrp.get('unidadMedida').value.id;
      obj.nomUnidadMedida = this.formularioGrp.get('unidadMedida').value.nombre;
      obj.nombre = this.formularioGrp.get('nombre').value;
      obj.observacion = this.formularioGrp.get('observacion').value;
      obj.codigo = this.data.objeto.codigo;
      obj.flgActivo = this.data.objeto.flgActivo;
      obj.idUsuarioMod = this.user.getId;
      obj.fecUsuarioMod = new Date();

      this.insumoService.modificarTipoInsumo(obj).subscribe(
        (data: OutResponse<TipoInsumoResponse>) => {
          if (data.rCodigo == 0) {
            this.dialogRef.close(data.rResult);
          } else {
            this._snackBar.open(data.rMensaje, null, { duration: 5000, horizontalPosition: 'right', verticalPosition: 'top', panelClass: ['warning-snackbar'] });
          }
          this.modif = false;
        }, error => {
          this.modif = false;
          this._snackBar.open(error, null, { duration: 5000, horizontalPosition: 'right', verticalPosition: 'top', panelClass: ['error-snackbar'] });
        }
      );
    } else {
      this.formService.getValidationErrors(this.formularioGrp, this.formErrors, true);
      console.log(this.formErrors);
    }
  }

}
