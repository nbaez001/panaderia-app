import { Component, OnInit, Inject } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { DataDialog } from 'src/app/modules/intranet/dto/data-dialog';
import { ProductoRequest } from 'src/app/modules/intranet/dto/request/producto.request';
import { MaestraResponse } from 'src/app/modules/intranet/dto/response/maestra.response';
import { MaestraRequest } from 'src/app/modules/intranet/dto/request/maestra.request';
import { TABLAS_MAESTRA } from 'src/app/common';
import { OutResponse } from 'src/app/modules/intranet/dto/response/out.response';
import { MaestraService } from 'src/app/modules/intranet/services/maestra.service';
import { FormService } from 'src/app/core/services/form.service';
import { MaestraBuscarRequest } from 'src/app/modules/intranet/dto/request/maestra-buscar.request';
import { MatSnackBar } from '@angular/material/snack-bar';
import { UsuarioService } from 'src/app/core/services/usuario.service';
import { ProductoService } from 'src/app/modules/intranet/services/producto.service';
import { ProductoResponse } from 'src/app/modules/intranet/dto/response/producto.response';

@Component({
  selector: 'app-reg-producto',
  templateUrl: './reg-producto.component.html',
  styleUrls: ['./reg-producto.component.scss']
})
export class RegProductoComponent implements OnInit {
  guardar: boolean = false;
  modif: boolean = false;

  listaUnidadMedida: MaestraResponse[] = [];

  formularioGrp: FormGroup;
  formErrors: any;

  constructor(private fb: FormBuilder,
    public dialogRef: MatDialogRef<RegProductoComponent>,
    private _snackBar: MatSnackBar,
    // private spinnerService: Ng4LoadingSpinnerService,
    @Inject(UsuarioService) private user: UsuarioService,
    @Inject(FormService) private formService: FormService,
    @Inject(MaestraService) private maestraService: MaestraService,
    @Inject(ProductoService) private productoService: ProductoService,
    @Inject(MAT_DIALOG_DATA) public data: DataDialog) { }

  ngOnInit() {
    this.formularioGrp = this.fb.group({
      unidadMedida: ['', [Validators.required]],
      nombre: ['', [Validators.required, Validators.maxLength(10)]],
      precio: ['', [Validators.required]],
    });
    this.formErrors = this.formService.buildFormErrors(this.formularioGrp, this.formErrors);
    this.formularioGrp.valueChanges.subscribe((val: any) => {
      this.formService.getValidationErrors(this.formularioGrp, this.formErrors, false);
    });

    this.inicializarVariables();
  }

  public inicializarVariables(): void {
    this.comboUnidadesMedida();
  }

  comboUnidadesMedida(): void {
    let maestra = new MaestraBuscarRequest();
    maestra.idTabla = TABLAS_MAESTRA.UNIDAD_MEDIDA.ID;
    this.maestraService.listarMaestra(maestra).subscribe(
      (data: OutResponse<MaestraResponse[]>) => {
        if (data.rCodigo == 0) {
          this.listaUnidadMedida = data.result;
          this.formularioGrp.get('unidadMedida').setValue(this.listaUnidadMedida[0]);
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

      let obj = new ProductoRequest();
      obj.id = 0;
      obj.idtUnidadMedida = this.formularioGrp.get('unidadMedida').value.id;
      obj.nomUnidadMedida = this.formularioGrp.get('unidadMedida').value.nombre;
      obj.nombre = this.formularioGrp.get('nombre').value;
      obj.precio = this.formularioGrp.get('precio').value;
      obj.flgActivo = 1;
      obj.idUsuarioCrea = this.user.getId;
      obj.fecUsuarioCrea = new Date();

      this.productoService.registrarProducto(obj).subscribe(
        (data: OutResponse<ProductoResponse>) => {
          if (data.rCodigo == 0) {
            this.dialogRef.close(data.result);
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
    //   if (this.egresoGrp.valid) {
    //     let obj: Egreso = JSON.parse(JSON.stringify(this.data.objeto));
    //     obj.idTipoEgreso = this.egresoGrp.get('tipoEgreso').value.id;
    //     obj.nomTipoEgreso = this.egresoGrp.get('tipoEgreso').value.nombre;
    //     obj.idUnidadMedida = this.egresoGrp.get('unidadMedida').value.id;
    //     obj.nomUnidadMedida = this.egresoGrp.get('unidadMedida').value.nombre;
    //     obj.nombre = this.egresoGrp.get('nombre').value;
    //     obj.cantidad = this.egresoGrp.get('cantidad').value;
    //     obj.precio = this.egresoGrp.get('precio').value;
    //     obj.total = this.egresoGrp.get('total').value;
    //     obj.descripcion = this.egresoGrp.get('descripcion').value;
    //     obj.ubicacion = this.egresoGrp.get('ubicacion').value;
    //     obj.fecha = this.egresoGrp.get('fecha').value;
    //     obj.dia = this.dias[(obj.fecha.getDay() == 0 ? 7 : obj.fecha.getDay()) - 1];
    //     obj.idUsuarioMod = this.user.getIdUsuario;
    //     obj.fecUsuarioMod = new Date();

    //     console.log(obj)

    //     this.spinnerService.show();
    //     this.egresoService.editEgreso(obj).subscribe(
    //       (data: ApiResponse[]) => {
    //         if (typeof data[0] != undefined && data[0].rcodigo == 0) {
    //           console.log('Exito al modificar');
    //           this.dialogRef.close(obj);
    //           this.spinnerService.hide();
    //         } else {
    //           console.error('Ocurrio un error al modificar egreso');
    //         }
    //       }, error => {
    //         console.error('Error al modificar egreso');
    //       }
    //     );
    //   } else {
    //     this.validationService.getValidationErrors(this.egresoGrp, this.messages, this.formErrors, true);
    //   }
  }

}
