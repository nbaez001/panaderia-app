import { Component, Input, Inject, OnInit } from '@angular/core';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { Observable } from 'rxjs';
import { map, shareReplay } from 'rxjs/operators';
import { Router } from '@angular/router';
import { UsuarioService } from 'src/app/core/services/usuario.service';
import { AutenticacionService } from 'src/app/modules/sesion/services/autenticacion.service';
import { Oauth2Response } from 'src/app/modules/sesion/dto/request/oauth2-response';

@Component({
  selector: 'navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit {
  @Input() showSubmenu1: boolean;
  @Input() showSubmenu2: boolean;
  @Input() showSubmenu3: boolean;

  isHandset$: Observable<boolean> = this.breakpointObserver.observe(Breakpoints.Handset)
    .pipe(
      map(result => result.matches),
      shareReplay()
    );

  constructor(private breakpointObserver: BreakpointObserver,
    @Inject(UsuarioService) public user: UsuarioService,
    @Inject(AutenticacionService) public autenticacionService: AutenticacionService,
    private router: Router) { }

  ngOnInit() {
    if (!this.user.getId) {
      let token = this.autenticacionService.existeToken();
      if (token != null && typeof (token) !== 'undefined' && token != '') {
        let oauth = new Oauth2Response();
        oauth.refresh_token = token;
        this.autenticacionService.refreshOauth2Token(oauth).subscribe(
          (data: Oauth2Response) => {
            this.user.setValues(data);
            this.autenticacionService.saveToken(data);
          }, error => {
            console.log(error);
            this.autenticacionService.salir();
          });
      } else {
        this.autenticacionService.salir();
      }
    }
  }

  salir(): void {
    this.autenticacionService.salir();
  }
}
