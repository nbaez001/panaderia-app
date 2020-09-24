import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { TABLAS_MAESTRA } from 'src/app/common';
import { FormService } from 'src/app/core/services/form.service';
import { UsuarioService } from 'src/app/core/services/usuario.service';
import { DataDialog } from 'src/app/modules/intranet/dto/data-dialog';
import { ApiPersonaBuscarRequest } from 'src/app/modules/intranet/dto/request/api-persona-buscar.request';
import { DepartamentoRequest } from 'src/app/modules/intranet/dto/request/departamento.request';
import { DistritoRequest } from 'src/app/modules/intranet/dto/request/distrito.request';
import { MaestraBuscarRequest } from 'src/app/modules/intranet/dto/request/maestra-buscar.request';
import { PersonalRequest } from 'src/app/modules/intranet/dto/request/personal.request';
import { ProvinciaRequest } from 'src/app/modules/intranet/dto/request/provincia.request';
import { ApiOutResponse } from 'src/app/modules/intranet/dto/response/api-out.response';
import { ApiPersonaResponse } from 'src/app/modules/intranet/dto/response/api-persona.response';
import { DepartamentoResponse } from 'src/app/modules/intranet/dto/response/departamento.response';
import { DistritoResponse } from 'src/app/modules/intranet/dto/response/distrito.response';
import { MaestraResponse } from 'src/app/modules/intranet/dto/response/maestra.response';
import { OutResponse } from 'src/app/modules/intranet/dto/response/out.response';
import { PaisResponse } from 'src/app/modules/intranet/dto/response/pais.response';
import { PersonalResponse } from 'src/app/modules/intranet/dto/response/personal.response';
import { ProvinciaResponse } from 'src/app/modules/intranet/dto/response/provincia.response';
import { MaestraService } from 'src/app/modules/intranet/services/maestra.service';
import { PersonaService } from 'src/app/modules/intranet/services/persona.service';
import { PersonalService } from 'src/app/modules/intranet/services/personal.service';
import { UbigeoService } from 'src/app/modules/intranet/services/ubigeo.service';
import { fileURLToPath } from 'url';

@Component({
  selector: 'app-reg-personal',
  templateUrl: './reg-personal.component.html',
  styleUrls: ['./reg-personal.component.scss']
})
export class RegPersonalComponent implements OnInit {
  guardar: boolean = false;
  modif: boolean = false;
  buscar: boolean = false;

  listaTipoDocumento: MaestraResponse[] = [];
  listaSexo: MaestraResponse[] = [];
  listaPais: PaisResponse[] = [];
  listaDepartamento: DepartamentoResponse[] = [];
  listaProvincia: ProvinciaResponse[] = [];
  listaDistrito: DistritoResponse[] = [];

  formularioGrp: FormGroup;
  formErrors: any;

  constructor(private fb: FormBuilder,
    public dialogRef: MatDialogRef<RegPersonalComponent>,
    private _snackBar: MatSnackBar,
    // private spinnerService: Ng4LoadingSpinnerService,
    @Inject(UsuarioService) private user: UsuarioService,
    @Inject(FormService) private formService: FormService,
    @Inject(MaestraService) private maestraService: MaestraService,
    @Inject(PersonalService) private personalService: PersonalService,
    @Inject(PersonaService) private personaService: PersonaService,
    @Inject(UbigeoService) private ubigeoService: UbigeoService,
    @Inject(MAT_DIALOG_DATA) public data: DataDialog<PersonalResponse>) { }

  ngOnInit() {
    this.formularioGrp = this.fb.group({
      cargo: ['', [Validators.required]],
      tipoDocumento: ['', [Validators.required]],
      nroDocumento: ['', []],
      nombre: ['', [Validators.required]],
      apePaterno: ['', [Validators.required]],
      apeMaterno: ['', []],
      direccionDomicilio: ['', []],
      pais: [{ value: '', disabled: true }, []],
      departamento: ['', []],
      provincia: ['', []],
      distrito: ['', []],
      fecNacimiento: ['', []],
      sexo: ['', []],
    });
    this.formErrors = this.formService.buildFormErrors(this.formularioGrp, this.formErrors);
    this.formularioGrp.valueChanges.subscribe((val: any) => {
      this.formService.getValidationErrors(this.formularioGrp, this.formErrors, false);
    });

    this.inicializarVariables();
  }

  public inicializarVariables(): void {
    this.comboTipoDocumento();
    this.comboSexo();
    this.comboPais();

    if (this.data.objeto) {
      this.formularioGrp.get('cargo').setValue(this.data.objeto.cargo ? this.data.objeto.cargo : '');
      this.formularioGrp.get('tipoDocumento').setValue(this.data.objeto.persona.idtTipoDocumento ? this.listaTipoDocumento.filter(el => el.id == this.data.objeto.persona.idtTipoDocumento)[0] : this.listaTipoDocumento[0]);
      this.formularioGrp.get('sexo').setValue(this.data.objeto.persona.idtSexo ? this.listaSexo.filter(el => el.id == this.data.objeto.persona.idtSexo)[0] : this.listaSexo[0]);

      this.formularioGrp.get('nombre').setValue(this.data.objeto.persona.nombre ? this.data.objeto.persona.nombre : '');
      this.formularioGrp.get('apePaterno').setValue(this.data.objeto.persona.apePaterno ? this.data.objeto.persona.apePaterno : '');
      this.formularioGrp.get('apeMaterno').setValue(this.data.objeto.persona.apeMaterno ? this.data.objeto.persona.apeMaterno : '');
      this.formularioGrp.get('nroDocumento').setValue(this.data.objeto.persona.nroDocumento ? this.data.objeto.persona.nroDocumento : '');
      this.formularioGrp.get('direccionDomicilio').setValue(this.data.objeto.persona.direccionDomicilio ? this.data.objeto.persona.direccionDomicilio : '');
      this.formularioGrp.get('fecNacimiento').setValue(this.data.objeto.persona.fecNacimiento ? new Date(this.data.objeto.persona.fecNacimiento.toString().replace(/(\d{2})-(\d{2})-(\d{4})/, "$2/$1/$3")) : '');
    }
  }

  comboTipoDocumento(): void {
    let maestra = new MaestraBuscarRequest();
    maestra.idTabla = TABLAS_MAESTRA.TIPO_DOCUMENTO.ID;
    this.maestraService.listarMaestra(maestra).subscribe(
      (data: OutResponse<MaestraResponse[]>) => {
        if (data.rCodigo == 0) {
          this.listaTipoDocumento = data.rResult;

          if (this.data.objeto) {
            this.formularioGrp.get('tipoDocumento').setValue(this.listaTipoDocumento.filter(el => el.id == this.data.objeto.persona.idtTipoDocumento)[0]);
          } else {
            this.formularioGrp.get('tipoDocumento').setValue(this.listaTipoDocumento[0]);
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

  comboSexo(): void {
    let maestra = new MaestraBuscarRequest();
    maestra.idTabla = TABLAS_MAESTRA.SEXO.ID;
    this.maestraService.listarMaestra(maestra).subscribe(
      (data: OutResponse<MaestraResponse[]>) => {
        if (data.rCodigo == 0) {
          this.listaSexo = data.rResult;

          if (this.data.objeto) {
            this.formularioGrp.get('sexo').setValue(this.listaSexo.filter(el => el.id == this.data.objeto.persona.idtSexo)[0]);
          } else {
            this.formularioGrp.get('sexo').setValue(this.listaSexo[0]);
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

  comboPais(): void {
    this.ubigeoService.listarPais().subscribe(
      (data: OutResponse<PaisResponse[]>) => {
        if (data.rCodigo == 0) {
          this.listaPais = data.rResult;

          if (this.data.objeto) {
            this.formularioGrp.get('pais').setValue(this.data.objeto.persona.idPais ? this.listaPais.filter(el => el.id == this.data.objeto.persona.idPais)[0] : this.listaPais[0]);
          } else {
            this.formularioGrp.get('pais').setValue(this.listaPais[0]);
          }
          this.comboDepartamento();
        } else {
          this._snackBar.open(data.rMensaje, null, { duration: 5000, horizontalPosition: 'right', verticalPosition: 'top', panelClass: ['warning-snackbar'] });
        }
      }, error => {
        console.log(error);
        this._snackBar.open(error, null, { duration: 5000, horizontalPosition: 'right', verticalPosition: 'top', panelClass: ['error-snackbar'] });
      }
    );
  }

  comboDepartamento(): void {
    let req = new DepartamentoRequest();
    req.idPais = this.formularioGrp.get('pais').value.id;

    this.ubigeoService.listarDepartamento(req).subscribe(
      (data: OutResponse<DepartamentoResponse[]>) => {
        if (data.rCodigo == 0) {
          this.listaDepartamento = data.rResult;

          if (this.data.objeto) {
            this.formularioGrp.get('departamento').setValue(this.data.objeto.persona.idDepartamento ? this.listaDepartamento.filter(el => el.id == this.data.objeto.persona.idDepartamento)[0] : this.listaDepartamento[0]);
          } else {
            this.formularioGrp.get('departamento').setValue(this.listaDepartamento[0]);
          }
          this.comboProvincia();
        } else {
          this._snackBar.open(data.rMensaje, null, { duration: 5000, horizontalPosition: 'right', verticalPosition: 'top', panelClass: ['warning-snackbar'] });
        }
      }, error => {
        console.log(error);
        this._snackBar.open(error, null, { duration: 5000, horizontalPosition: 'right', verticalPosition: 'top', panelClass: ['error-snackbar'] });
      }
    );
  }

  comboProvincia(): void {
    let req = new ProvinciaRequest();
    req.idDepartamento = this.formularioGrp.get('departamento').value.id;

    this.ubigeoService.listarProvincia(req).subscribe(
      (data: OutResponse<ProvinciaResponse[]>) => {
        if (data.rCodigo == 0) {
          this.listaProvincia = data.rResult;

          if (this.data.objeto) {
            this.formularioGrp.get('provincia').setValue(this.data.objeto.persona.idProvincia ? this.listaProvincia.filter(el => el.id == this.data.objeto.persona.idProvincia)[0] : this.listaProvincia[0]);
          } else {
            this.formularioGrp.get('provincia').setValue(this.listaProvincia[0]);
          }
          this.comboDistrito();
        } else {
          this._snackBar.open(data.rMensaje, null, { duration: 5000, horizontalPosition: 'right', verticalPosition: 'top', panelClass: ['warning-snackbar'] });
        }
      }, error => {
        console.log(error);
        this._snackBar.open(error, null, { duration: 5000, horizontalPosition: 'right', verticalPosition: 'top', panelClass: ['error-snackbar'] });
      }
    );
  }

  comboDistrito(): void {
    let req = new DistritoRequest();
    req.idProvincia = this.formularioGrp.get('provincia').value.id;

    this.ubigeoService.listarDistrito(req).subscribe(
      (data: OutResponse<DistritoResponse[]>) => {
        if (data.rCodigo == 0) {
          this.listaDistrito = data.rResult;

          if (this.data.objeto) {
            this.formularioGrp.get('distrito').setValue(this.data.objeto.persona.idDistrito ? this.listaDistrito.filter(el => el.id == this.data.objeto.persona.idDistrito)[0] : this.listaDistrito[0]);
          } else {
            this.formularioGrp.get('distrito').setValue(this.listaDistrito[0]);
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

  buscarPersona(): void {
    let dni = this.formularioGrp.get('nroDocumento').value;
    if (dni && dni.toString().length == 8) {
      this.buscar = true;
      let req = new ApiPersonaBuscarRequest();
      req.dni = dni;

      this.personaService.buscarPersona(req).subscribe(
        (data: ApiOutResponse<ApiPersonaResponse>) => {
          if (data.rCodigo == 0) {
            let persona = data.result;
            this.formularioGrp.get('sexo').setValue(this.listaSexo.filter(el => el.valor == persona.Sexo)[0]);
            this.formularioGrp.get('departamento').setValue(this.listaDepartamento.filter(el => el.ubigeoReniec == persona.UbigeoDptoDomicilio)[0]);
            this.formularioGrp.get('nombre').setValue(persona.Nombres);
            this.formularioGrp.get('apePaterno').setValue(persona.ApellidoPaterno);
            this.formularioGrp.get('apeMaterno').setValue(persona.ApellidoMaterno);
            this.formularioGrp.get('direccionDomicilio').setValue(persona.DireccionDomicilio);
            this.formularioGrp.get('fecNacimiento').setValue(new Date(persona.FechaNacimiento.replace(/(\d{4})(\d{2})(\d{2})/, "$2/$3/$1")));
            this.comboProvincia2(persona);
          } else {
            console.log(data.rMensaje);
            this._snackBar.open(data.rMensaje, null, { duration: 5000, horizontalPosition: 'right', verticalPosition: 'top', panelClass: ['error-snackbar'] });
          }
          this.buscar = false;
        }, error => {
          console.log(error);
          this._snackBar.open(error, null, { duration: 5000, horizontalPosition: 'right', verticalPosition: 'top', panelClass: ['error-snackbar'] });
          this.buscar = false;
        }
      )
    } else {
      this._snackBar.open('INGRESE LOS 8 DIGITOS DEL DOCUMENTO', null, { duration: 5000, horizontalPosition: 'right', verticalPosition: 'top', panelClass: ['warning-snackbar'] });
    }
  }

  comboProvincia2(persona: ApiPersonaResponse): void {
    let req = new ProvinciaRequest();
    req.idDepartamento = this.formularioGrp.get('departamento').value.id;

    this.ubigeoService.listarProvincia(req).subscribe(
      (data: OutResponse<ProvinciaResponse[]>) => {
        if (data.rCodigo == 0) {
          this.listaProvincia = data.rResult;
          let filtered = this.listaProvincia.filter(el => el.ubigeoReniec == persona.UbigeoDptoDomicilio + persona.UbigeoProvDomicilio)[0];
          this.formularioGrp.get('provincia').setValue(filtered);
          this.comboDistrito2(persona);
        } else {
          this._snackBar.open(data.rMensaje, null, { duration: 5000, horizontalPosition: 'right', verticalPosition: 'top', panelClass: ['warning-snackbar'] });
        }
      }, error => {
        console.log(error);
        this._snackBar.open(error, null, { duration: 5000, horizontalPosition: 'right', verticalPosition: 'top', panelClass: ['error-snackbar'] });
      }
    );
  }

  comboDistrito2(persona: ApiPersonaResponse): void {
    let req = new DistritoRequest();
    req.idProvincia = this.formularioGrp.get('provincia').value.id;

    this.ubigeoService.listarDistrito(req).subscribe(
      (data: OutResponse<DistritoResponse[]>) => {
        if (data.rCodigo == 0) {
          this.listaDistrito = data.rResult;
          this.formularioGrp.get('distrito').setValue(this.listaDistrito.filter(el => el.ubigeoReniec == persona.UbigeoDptoDomicilio + persona.UbigeoProvDomicilio + persona.UbigeoDistDomicilio)[0]);
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

      let obj = new PersonalRequest();

      obj.id = 0;
      obj.cargo = this.formularioGrp.get('cargo').value;

      obj.persona.idtTipoDocumento = this.formularioGrp.get('tipoDocumento').value ? this.formularioGrp.get('tipoDocumento').value.id : null;
      obj.persona.nomTipoDocumento = this.formularioGrp.get('tipoDocumento').value ? this.formularioGrp.get('tipoDocumento').value.nombre : null;
      obj.persona.idtSexo = this.formularioGrp.get('sexo').value ? this.formularioGrp.get('sexo').value.id : null;
      obj.persona.nomSexo = this.formularioGrp.get('sexo').value ? this.formularioGrp.get('sexo').value.nombre : null;
      obj.persona.idPais = this.formularioGrp.get('pais').value ? this.formularioGrp.get('pais').value.id : null;
      obj.persona.nomPais = this.formularioGrp.get('pais').value ? this.formularioGrp.get('pais').value.nombre : null;
      obj.persona.idDepartamento = this.formularioGrp.get('departamento').value ? this.formularioGrp.get('departamento').value.id : null;
      obj.persona.nomDepartamento = this.formularioGrp.get('departamento').value ? this.formularioGrp.get('departamento').value.nombre : null;
      obj.persona.idProvincia = this.formularioGrp.get('provincia').value ? this.formularioGrp.get('provincia').value.id : null;
      obj.persona.nomProvincia = this.formularioGrp.get('provincia').value ? this.formularioGrp.get('provincia').value.nombre : null;
      obj.persona.idDistrito = this.formularioGrp.get('distrito').value ? this.formularioGrp.get('distrito').value.id : null;
      obj.persona.nomDistrito = this.formularioGrp.get('distrito').value ? this.formularioGrp.get('distrito').value.nombre : null;

      obj.persona.nombre = this.formularioGrp.get('nombre').value;
      obj.persona.apePaterno = this.formularioGrp.get('apePaterno').value;
      obj.persona.apeMaterno = this.formularioGrp.get('apeMaterno').value;
      obj.persona.nroDocumento = this.formularioGrp.get('nroDocumento').value;
      obj.persona.direccionDomicilio = this.formularioGrp.get('direccionDomicilio').value;
      obj.persona.fecNacimiento = this.formularioGrp.get('fecNacimiento').value;

      obj.flgActivo = 1;
      obj.idUsuarioCrea = this.user.getId;
      obj.fecUsuarioCrea = new Date();

      this.personalService.registrarPersonal(obj).subscribe(
        (data: OutResponse<PersonalResponse>) => {
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

      let obj = new PersonalRequest();
      obj.id = this.data.objeto.id;
      obj.cargo = this.formularioGrp.get('cargo').value;

      obj.persona.id = this.data.objeto.persona.id;
      obj.persona.idtTipoDocumento = this.formularioGrp.get('tipoDocumento').value ? this.formularioGrp.get('tipoDocumento').value.id : null;
      obj.persona.nomTipoDocumento = this.formularioGrp.get('tipoDocumento').value ? this.formularioGrp.get('tipoDocumento').value.nombre : null;
      obj.persona.idtSexo = this.formularioGrp.get('sexo').value ? this.formularioGrp.get('sexo').value.id : null;
      obj.persona.nomSexo = this.formularioGrp.get('sexo').value ? this.formularioGrp.get('sexo').value.nombre : null;
      obj.persona.idPais = this.formularioGrp.get('pais').value ? this.formularioGrp.get('pais').value.id : null;
      obj.persona.nomPais = this.formularioGrp.get('pais').value ? this.formularioGrp.get('pais').value.nombre : null;
      obj.persona.idDepartamento = this.formularioGrp.get('departamento').value ? this.formularioGrp.get('departamento').value.id : null;
      obj.persona.nomDepartamento = this.formularioGrp.get('departamento').value ? this.formularioGrp.get('departamento').value.nombre : null;
      obj.persona.idProvincia = this.formularioGrp.get('provincia').value ? this.formularioGrp.get('provincia').value.id : null;
      obj.persona.nomProvincia = this.formularioGrp.get('provincia').value ? this.formularioGrp.get('provincia').value.nombre : null;
      obj.persona.idDistrito = this.formularioGrp.get('distrito').value ? this.formularioGrp.get('distrito').value.id : null;
      obj.persona.nomDistrito = this.formularioGrp.get('distrito').value ? this.formularioGrp.get('distrito').value.nombre : null;

      obj.persona.nombre = this.formularioGrp.get('nombre').value;
      obj.persona.apePaterno = this.formularioGrp.get('apePaterno').value;
      obj.persona.apeMaterno = this.formularioGrp.get('apeMaterno').value;
      obj.persona.nroDocumento = this.formularioGrp.get('nroDocumento').value;
      obj.persona.direccionDomicilio = this.formularioGrp.get('direccionDomicilio').value;
      obj.persona.fecNacimiento = this.formularioGrp.get('fecNacimiento').value;

      obj.flgActivo = 1;
      obj.idUsuarioCrea = this.data.objeto.idUsuarioCrea;
      obj.fecUsuarioCrea = this.data.objeto.fecUsuarioCrea;
      obj.idUsuarioMod = this.user.getId;
      obj.fecUsuarioMod = new Date();

      this.personalService.modificarPersonal(obj).subscribe(
        (data: OutResponse<PersonalRequest>) => {
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
