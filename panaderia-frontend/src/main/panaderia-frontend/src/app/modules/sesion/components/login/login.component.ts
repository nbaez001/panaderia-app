import { Component, OnInit, Inject } from '@angular/core';
import { UsuarioRequest } from '../../dto/request/usuario-request';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { UsuarioService } from 'src/app/core/services/usuario.service';
import { FormService } from 'src/app/core/services/form.service';
import { AutenticacionService } from '../../services/autenticacion.service';
import { Oauth2Response } from '../../dto/request/oauth2-response';

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
          this.user.setValues(data);
          this.autenticacionService.saveToken(data);
          this.ingresar = false;
          this.router.navigate(['/intranet/home']);
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

}
