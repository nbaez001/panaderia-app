import { Injectable } from '@angular/core';
import { Oauth2Response } from 'src/app/modules/sesion/dto/request/oauth2-response';
import { PermisoResponse } from 'src/app/modules/sesion/dto/response/permiso.response';

@Injectable({
  providedIn: 'root'
})
export class UsuarioService {
  id: number;
  nombre: string;
  apePaterno: string;
  apeMaterno: string;
  email: string;
  telefono: string;
  // usuario: string;
  // contrasenia: string;
  access_token: string;
  refresh_token: string;
  listaPermiso: PermisoResponse[];

  constructor() { }

  set setId(id: number) { this.id = id; }
  set setNombre(nombre: string) { this.nombre = nombre; }
  set setApePaterno(apePaterno: string) { this.apePaterno = apePaterno; }
  set setApeMaterno(apeMaterno: string) { this.apeMaterno = apeMaterno; }
  set setEmail(email: string) { this.email = email; }
  set setTelefono(telefono: string) { this.telefono = telefono; }
  // set setUsuario(usuario: string) { this.usuario = usuario; }
  // set setContrasenia(contrasenia: string) { this.contrasenia = contrasenia; }
  set setAccessToken(access_token: string) { this.access_token = access_token; }
  set setRefreshToken(refresh_token: string) { this.refresh_token = refresh_token; }
  set setListaPermiso(listaPermiso: PermisoResponse[]) { this.listaPermiso = listaPermiso; }

  get getId() { return this.id; }
  get getNombre() { return this.nombre }
  get getApePaterno() { return this.apePaterno }
  get getApeMaterno() { return this.apeMaterno }
  get getEmail() { return this.email }
  get getTelefono() { return this.telefono }
  // get getUsuario() { return this.usuario; }
  // get getContrasenia() { return this.contrasenia; }
  get getAccessToken() { return this.access_token }
  get getRefreshToken() { return this.refresh_token }
  get getListaPermiso() { return this.listaPermiso }

  limpiarRegistro(): void {
    this.id = null;
    this.nombre = null;
    this.apePaterno = null;
    this.apeMaterno = null;
    this.email = null;
    this.telefono = null;
    // this.usuario = null;
    // this.contrasenia = null;
    this.access_token = null;
    this.refresh_token = null;
    this.listaPermiso = null;
    return null;
  }

  setValues(auth: Oauth2Response, lista: PermisoResponse[]): void {
    this.id = auth.id;
    this.nombre = auth.nombre;
    this.apePaterno = auth.apePaterno;
    this.apeMaterno = auth.apeMaterno;
    this.email = auth.email;
    this.telefono = auth.telefono;
    this.access_token = auth.access_token;
    this.refresh_token = auth.refresh_token;
    this.listaPermiso = lista;
  }
}
