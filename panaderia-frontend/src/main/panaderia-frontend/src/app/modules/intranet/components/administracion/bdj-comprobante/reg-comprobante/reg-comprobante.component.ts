import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { TABLAS_MAESTRA } from 'src/app/common';
import { FormService } from 'src/app/core/services/form.service';
import { UsuarioService } from 'src/app/core/services/usuario.service';
import { DataDialog } from 'src/app/modules/intranet/dto/data-dialog';
import { ComprobanteRequest } from 'src/app/modules/intranet/dto/request/comprobante.request';
import { MaestraBuscarRequest } from 'src/app/modules/intranet/dto/request/maestra-buscar.request';
import { ComprobanteResponse } from 'src/app/modules/intranet/dto/response/comprobante.response';
import { MaestraResponse } from 'src/app/modules/intranet/dto/response/maestra.response';
import { OutResponse } from 'src/app/modules/intranet/dto/response/out.response';
import { ComprobanteService } from 'src/app/modules/intranet/services/comprobante.service';
import { MaestraService } from 'src/app/modules/intranet/services/maestra.service';

@Component({
  selector: 'app-reg-comprobante',
  templateUrl: './reg-comprobante.component.html',
  styleUrls: ['./reg-comprobante.component.scss']
})
export class RegComprobanteComponent implements OnInit {
  guardar: boolean = false;
  modif: boolean = false;

  listaTipoComprobante: MaestraResponse[] = [];

  formularioGrp: FormGroup;
  formErrors: any;

  constructor(private fb: FormBuilder,
    public dialogRef: MatDialogRef<RegComprobanteComponent>,
    private _snackBar: MatSnackBar,
    @Inject(UsuarioService) private user: UsuarioService,
    @Inject(FormService) private formService: FormService,
    @Inject(MaestraService) private maestraService: MaestraService,
    @Inject(ComprobanteService) private comprobanteService: ComprobanteService,
    @Inject(MAT_DIALOG_DATA) public data: DataDialog<ComprobanteResponse>) { }

  ngOnInit() {
    this.formularioGrp = this.fb.group({
      tipoComprobante: ['', [Validators.required]],
      nombre: ['', [Validators.required]],
      serie: ['', [Validators.required]],
      numero: ['', [Validators.required]],
      longSerie: [{ value: 3, disabled: false }, [Validators.required, Validators.min(1), Validators.max(4)]],
      longNumero: [{ value: 8, disabled: false }, [Validators.required, Validators.min(1), Validators.max(8)]],
    });
    this.formErrors = this.formService.buildFormErrors(this.formularioGrp, this.formErrors);
    this.formularioGrp.valueChanges.subscribe((val: any) => {
      this.formService.getValidationErrors(this.formularioGrp, this.formErrors, false);
    });

    this.inicializarVariables();
  }

  public inicializarVariables(): void {
    this.comboTipoComprobante();
    if (this.data.objeto) {
      this.formularioGrp.get('nombre').setValue(this.data.objeto.nombre);
      this.formularioGrp.get('serie').setValue(this.data.objeto.serie);
      this.formularioGrp.get('numero').setValue(this.data.objeto.numero);
      this.formularioGrp.get('longSerie').setValue(this.data.objeto.longSerie);
      this.formularioGrp.get('longNumero').setValue(this.data.objeto.longNumero);
    }
  }

  comboTipoComprobante(): void {
    let maestra = new MaestraBuscarRequest();
    maestra.idTabla = TABLAS_MAESTRA.TIPO_COMPROBANTE.ID;
    this.maestraService.listarMaestra(maestra).subscribe(
      (data: OutResponse<MaestraResponse[]>) => {
        if (data.rCodigo == 0) {
          this.listaTipoComprobante = data.rResult;

          if (this.data.objeto) {
            this.formularioGrp.get('tipoComprobante').setValue(this.listaTipoComprobante.filter(el => el.id == this.data.objeto.idtTipoComprobante)[0]);
          } else {
            this.formularioGrp.get('tipoComprobante').setValue(this.listaTipoComprobante[0]);
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

      let obj = new ComprobanteRequest();
      obj.id = 0;
      obj.idtTipoComprobante = this.formularioGrp.get('tipoComprobante').value.id;
      obj.nomTipoComprobante = this.formularioGrp.get('tipoComprobante').value.nombre;
      obj.nombre = this.formularioGrp.get('nombre').value;
      obj.serie = this.formularioGrp.get('serie').value;
      obj.numero = this.formularioGrp.get('numero').value;
      obj.longSerie = this.formularioGrp.get('longSerie').value;
      obj.longNumero = this.formularioGrp.get('longNumero').value;
      obj.flgActivo = 1;
      obj.flgActual = 0;
      obj.flgUsado = 0;
      obj.idUsuarioCrea = this.user.getId;
      obj.fecUsuarioCrea = new Date();

      this.comprobanteService.registrarComprobante(obj).subscribe(
        (data: OutResponse<ComprobanteResponse>) => {
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

      let obj = new ComprobanteRequest();
      obj.id = this.data.objeto.id;
      obj.idtTipoComprobante = this.formularioGrp.get('tipoComprobante').value.id;
      obj.nomTipoComprobante = this.formularioGrp.get('tipoComprobante').value.nombre;
      obj.nombre = this.formularioGrp.get('nombre').value;
      obj.serie = this.formularioGrp.get('serie').value;
      obj.numero = this.formularioGrp.get('numero').value;
      obj.longSerie = this.formularioGrp.get('longSerie').value;
      obj.longNumero = this.formularioGrp.get('longNumero').value;
      obj.flgActivo = this.data.objeto.flgActivo;
      obj.flgActual = this.data.objeto.flgActual;
      obj.flgUsado = this.data.objeto.flgUsado;
      obj.idUsuarioCrea = this.data.objeto.idUsuarioCrea;
      obj.fecUsuarioCrea = this.data.objeto.fecUsuarioCrea
      obj.idUsuarioMod = this.user.getId;
      obj.fecUsuarioMod = new Date();

      this.comprobanteService.modificarComprobante(obj).subscribe(
        (data: OutResponse<ComprobanteResponse>) => {
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
