import { Component, Input, Inject, OnInit } from '@angular/core';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { Observable } from 'rxjs';
import { map, shareReplay } from 'rxjs/operators';
import { Router } from '@angular/router';
import { UsuarioService } from 'src/app/core/services/usuario.service';

@Component({
  selector: 'navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit {
  @Input() showSubmenu1: boolean;
  @Input() showSubmenu2: boolean;

  isHandset$: Observable<boolean> = this.breakpointObserver.observe(Breakpoints.Handset)
    .pipe(
      map(result => result.matches),
      shareReplay()
    );

  constructor(private breakpointObserver: BreakpointObserver,
    @Inject(UsuarioService) private user: UsuarioService,
    private router: Router) { }

  ngOnInit() {
    if (this.user.getId == null) {
      this.router.navigate(['/sesion/login']);
    }
  }

  salir(): void {
    this.router.navigate(['/sesion/login']);
  }
}
