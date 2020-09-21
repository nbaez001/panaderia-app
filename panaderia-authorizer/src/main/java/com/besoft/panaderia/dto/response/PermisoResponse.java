package com.besoft.panaderia.dto.response;

import java.util.List;

public class PermisoResponse {
	private Long id;
	private Long idPadre;
	private String nombre;
	private String ruta;
	private String icono;
	private Integer orden;
	private List<PermisoResponse> listaPermiso;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdPadre() {
		return idPadre;
	}

	public void setIdPadre(Long idPadre) {
		this.idPadre = idPadre;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getRuta() {
		return ruta;
	}

	public void setRuta(String ruta) {
		this.ruta = ruta;
	}

	public String getIcono() {
		return icono;
	}

	public void setIcono(String icono) {
		this.icono = icono;
	}

	public Integer getOrden() {
		return orden;
	}

	public void setOrden(Integer orden) {
		this.orden = orden;
	}

	public List<PermisoResponse> getListaPermiso() {
		return listaPermiso;
	}

	public void setListaPermiso(List<PermisoResponse> listaPermiso) {
		this.listaPermiso = listaPermiso;
	}

}
