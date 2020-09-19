import { Component, OnInit, Inject } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MaestraResponse } from 'src/app/modules/intranet/dto/response/maestra.response';
import { UsuarioService } from 'src/app/core/services/usuario.service';
import { MaestraRequest } from 'src/app/modules/intranet/dto/request/maestra.request';
import { OutResponse } from 'src/app/modules/intranet/dto/response/out.response';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MaestraService } from 'src/app/modules/intranet/services/maestra.service';
import { FormService } from 'src/app/core/services/form.service';
import { DataDialog } from 'src/app/modules/intranet/dto/data-dialog';

@Component({
  selector: 'app-reg-maestra',
  templateUrl: './reg-maestra.component.html',
  styleUrls: ['./reg-maestra.component.scss']
})
export class RegMaestraComponent implements OnInit {
  guardar: boolean = false;
  modificar: boolean = false;

  maestraEdit: MaestraResponse;

  formularioGrp: FormGroup;
  formErrors: any;

  constructor(private fb: FormBuilder,
    public dialogRef: MatDialogRef<RegMaestraComponent>,
    private _snackBar: MatSnackBar,
    @Inject(MaestraService) private maestraService: MaestraService,
    @Inject(UsuarioService) private user: UsuarioService,
    @Inject(FormService) private formService: FormService,
    @Inject(MAT_DIALOG_DATA) public data: DataDialog) { }

  ngOnInit() {
    this.formularioGrp = this.fb.group({
      nombre: ['', [Validators.required]],
      codigo: ['', [Validators.required]],
      valor: ['', []],
      idTabla: ['', [Validators.required]],
      descripcion: ['', []]
    });
    this.formErrors = this.formService.buildFormErrors(this.formularioGrp, this.formErrors);
    this.formularioGrp.valueChanges.subscribe((val: any) => {
      this.formService.getValidationErrors(this.formularioGrp, this.formErrors, false);
    });

    this.inicializarVariables();
  }

  public inicializarVariables(): void {
    if (this.data.objeto) {
      this.maestraEdit = JSON.parse(JSON.stringify(this.data.objeto));
      this.formularioGrp.get('nombre').setValue(this.maestraEdit.nombre);
      this.formularioGrp.get('codigo').setValue(this.maestraEdit.codigo);
      this.formularioGrp.get('valor').setValue(this.maestraEdit.valor);
      this.formularioGrp.get('descripcion').setValue(this.maestraEdit.descripcion);
      this.formularioGrp.get('idTabla').setValue(this.maestraEdit.idTabla);
    }
  }

  ejecutar(): void {
    if (this.data.objeto) {
      this.editMaestra();
    } else {
      this.regMaestra();
    }
  }

  regMaestra(): void {
    if (this.formularioGrp.valid) {
      this.guardar = true;

      let mae = new MaestraRequest();
      mae.id = 0;
      mae.idMaestra = 0;
      mae.idItem = 0;
      mae.orden = 0;
      mae.idTabla = this.formularioGrp.get('idTabla').value;
      mae.nombre = this.formularioGrp.get('nombre').value;
      mae.codigo = this.formularioGrp.get('codigo').value;
      mae.valor = this.formularioGrp.get('valor').value;
      mae.descripcion = this.formularioGrp.get('descripcion').value;
      mae.flagActivo = 1;
      mae.idUsuarioCrea = this.user.getId;
      mae.fecUsuarioCrea = new Date();

      console.log(mae);
      this.maestraService.registrarMaestra(mae).subscribe(
        (data: OutResponse<MaestraResponse>) => {
          if (data.rCodigo == 0) {
            this.dialogRef.close(data.result);
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

  editMaestra(): void {
    if (this.formularioGrp.valid) {
      this.modificar = true;

      let obj: MaestraResponse = JSON.parse(JSON.stringify(this.data.objeto));
      let req = new MaestraRequest();
      req.id = obj.id;
      req.idMaestra = obj.idMaestra;
      req.idTabla = obj.idTabla;
      req.idItem = obj.idItem;
      req.orden = obj.orden;
      req.codigo = this.formularioGrp.get('codigo').value;
      req.nombre = this.formularioGrp.get('nombre').value;
      req.valor = this.formularioGrp.get('valor').value;
      req.descripcion = this.formularioGrp.get('descripcion').value;
      req.flagActivo = obj.flagActivo;
      req.idUsuarioCrea = obj.idUsuarioCrea;
      req.fecUsuarioCrea = obj.fecUsuarioCrea;
      req.idUsuarioMod = this.user.getId;
      req.fecUsuarioMod = new Date();

      this.maestraService.modificarMaestra(req).subscribe(
        (data: OutResponse<MaestraResponse>) => {
          if (data.rCodigo == 0) {
            this.dialogRef.close(req);
          } else {
            this._snackBar.open(data.rMensaje, null, { duration: 5000, horizontalPosition: 'right', verticalPosition: 'top', panelClass: ['warning-snackbar'] });
          }
          this.modificar = false;
        }, error => {
          console.log(error);
          this.modificar = false;
          this._snackBar.open(error.statusText, null, { duration: 5000, horizontalPosition: 'right', verticalPosition: 'top', panelClass: ['error-snackbar'] });
        }
      );
    } else {
      this.formService.getValidationErrors(this.formularioGrp, this.formErrors, true);
    }
  }

  generarCodigo(): void {
    let nom = this.formularioGrp.get('nombre').value;
    if (nom) {
      let array = nom.split(' ');
      nom = '';
      array.forEach((el: string) => {
        nom += el.charAt(0);
      });

      if (!this.formularioGrp.get('codigo').value) {
        this.formularioGrp.get('codigo').setValue(nom);
      }
    }
  }

}
