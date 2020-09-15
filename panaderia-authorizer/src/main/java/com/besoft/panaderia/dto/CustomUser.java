package com.besoft.panaderia.dto;

import org.springframework.security.core.userdetails.User;

import com.besoft.panaderia.dto.response.UsuarioResponse;

public class CustomUser extends User {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String nombre;
	private String apePaterno;
	private String apeMaterno;
	private String email;
	private String telefono;

	public CustomUser(UsuarioResponse user) {
		super(user.getUsername(), user.getPassword(), user.getGrantedAuthoritiesList());
		this.id = user.getId();
		this.nombre = user.getNombre();
		this.apePaterno = user.getApePaterno();
		this.apeMaterno = user.getApeMaterno();
		this.email = user.getEmail();
		this.telefono = user.getTelefono();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApePaterno() {
		return apePaterno;
	}

	public void setApePaterno(String apePaterno) {
		this.apePaterno = apePaterno;
	}

	public String getApeMaterno() {
		return apeMaterno;
	}

	public void setApeMaterno(String apeMaterno) {
		this.apeMaterno = apeMaterno;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

}
