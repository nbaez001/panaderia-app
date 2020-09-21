import { Component, OnInit, Inject } from '@angular/core';
import { UsuarioRequest } from '../../dto/request/usuario-request';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { UsuarioService } from 'src/app/core/services/usuario.service';
import { FormService } from 'src/app/core/services/form.service';
import { AutenticacionService } from '../../services/autenticacion.service';
import { Oauth2Response } from '../../dto/request/oauth2-response';
import { PermisoBuscarRequest } from '../../dto/request/permiso-buscar.request';
import { OutResponse } from 'src/app/modules/intranet/dto/response/out.response';
import { PermisoResponse } from '../../dto/response/permiso.response';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  ingresar: boolean = false;

  usuario: UsuarioRequest = new UsuarioRequest();
  errorMessage: string = 'Usuario o contraseña son incorrectos';
  showMessage: boolean = false;

  formularioGrp: FormGroup;
  formErrors: any;

  constructor(private fb: FormBuilder, private router: Router,
    @Inject(UsuarioService) private user: UsuarioService,
    @Inject(FormService) private formService: FormService,
    @Inject(AutenticacionService) private autenticacionService: AutenticacionService,) { }

  ngOnInit() {
    this.formularioGrp = this.fb.group({
      usuario: ['', [Validators.required]],
      contrasenia: ['', [Validators.required]],
    });
    this.formErrors = this.formService.buildFormErrors(this.formularioGrp, this.formErrors);
    this.formularioGrp.valueChanges.subscribe((val: any) => {
      this.formService.getValidationErrors(this.formularioGrp, this.formErrors, false);
    });

    this.formularioGrp.get('usuario').setValue('admin');
    this.formularioGrp.get('contrasenia').setValue('123456');
  }

  autenticar() {
    if (this.formularioGrp.valid) {
      this.ingresar = true;

      this.usuario.usuario = this.formularioGrp.get('usuario').value;
      this.usuario.contrasenia = this.formularioGrp.get('contrasenia').value;

      this.autenticacionService.oauth2Token(this.usuario).subscribe(
        (data: Oauth2Response) => {
          this.listarPermiso(data);
          this.autenticacionService.saveToken(data);
        }, error => {
          console.log(error);

          this.ingresar = false;
          this.errorMessage = 'Usuario o contraseña son incorrectos';
          this.showMessage = true;
          setTimeout(() => {
            this.showMessage = false;
          }, 7000);
        }
      );
    } else {
      this.formService.getValidationErrors(this.formularioGrp, this.formErrors, true);
    }
  }

  listarPermiso(oauth: Oauth2Response): void {
    let req = new PermisoBuscarRequest();
    req.idUsuario = oauth.id;

    this.autenticacionService.listarPermiso(req, oauth.access_token).subscribe(
      (data: OutResponse<PermisoResponse[]>) => {
        console.log(data);
        if (data.rCodigo == 0) {
          this.user.setValues(oauth, data.rResult);
          this.autenticacionService.saveToken(oauth);
          this.ingresar = false;
          this.router.navigate(['/intranet/home']);
        } else {
          this.ingresar = false;
          this.errorMessage = 'Error al cargar datos de usuario';
          this.showMessage = true;
          setTimeout(() => {
            this.showMessage = false;
          }, 7000);
        }
      }, error => {
        console.log(error);

        this.ingresar = false;
        this.errorMessage = 'Error al cargar datos de usuario';
        this.showMessage = true;
        setTimeout(() => {
          this.showMessage = false;
        }, 7000);
      }
    );
  }

}
