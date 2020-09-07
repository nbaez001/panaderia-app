import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class UsuarioService {
  id: number;
  usuario: string;
  contrasenia: string;

  constructor() { }

  set setId(id: number) { this.id = id; }
  set setUsuario(usuario: string) { this.usuario = usuario; }
  set setContrasenia(contrasenia: string) { this.contrasenia = contrasenia; }

  get getId() { return this.id; }
  get getUsuario() { return this.usuario; }
  get getContrasenia() { return this.contrasenia; }

  public limpiarRegistro(): void {
    this.id = null;
    this.usuario = null;
    this.contrasenia = null;
    return null;
  }
}
