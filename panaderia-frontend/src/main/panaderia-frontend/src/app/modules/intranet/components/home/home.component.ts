import { Component, Inject, OnInit } from '@angular/core';
import { UsuarioService } from 'src/app/core/services/usuario.service';
import { ReporteService } from '../../services/reporte.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  constructor(@Inject(UsuarioService) private user: UsuarioService,) { }

  ngOnInit(): void {
  }

}
