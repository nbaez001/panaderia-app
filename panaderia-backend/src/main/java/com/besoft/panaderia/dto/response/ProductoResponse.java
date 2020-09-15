package com.besoft.panaderia.dto.response;

import java.util.Date;

public class ProductoResponse {
	private Long id;
	private Long idtUnidadMedida;
	private String nomUnidadMedida;
	private String nombre;
	private String codigo;
	private Double precio;
	private Integer flgActivo;
	private Long idUsuarioCrea;
	private Date fecUsuarioCrea;
	private Long idUsuarioMod;
	private Date fecUsuarioMod;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdtUnidadMedida() {
		return idtUnidadMedida;
	}

	public void setIdtUnidadMedida(Long idtUnidadMedida) {
		this.idtUnidadMedida = idtUnidadMedida;
	}

	public String getNomUnidadMedida() {
		return nomUnidadMedida;
	}

	public void setNomUnidadMedida(String nomUnidadMedida) {
		this.nomUnidadMedida = nomUnidadMedida;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public Double getPrecio() {
		return precio;
	}

	public void setPrecio(Double precio) {
		this.precio = precio;
	}

	public Integer getFlgActivo() {
		return flgActivo;
	}

	public void setFlgActivo(Integer flgActivo) {
		this.flgActivo = flgActivo;
	}

	public Long getIdUsuarioCrea() {
		return idUsuarioCrea;
	}

	public void setIdUsuarioCrea(Long idUsuarioCrea) {
		this.idUsuarioCrea = idUsuarioCrea;
	}

	public Date getFecUsuarioCrea() {
		return fecUsuarioCrea;
	}

	public void setFecUsuarioCrea(Date fecUsuarioCrea) {
		this.fecUsuarioCrea = fecUsuarioCrea;
	}

	public Long getIdUsuarioMod() {
		return idUsuarioMod;
	}

	public void setIdUsuarioMod(Long idUsuarioMod) {
		this.idUsuarioMod = idUsuarioMod;
	}

	public Date getFecUsuarioMod() {
		return fecUsuarioMod;
	}

	public void setFecUsuarioMod(Date fecUsuarioMod) {
		this.fecUsuarioMod = fecUsuarioMod;
	}

}
