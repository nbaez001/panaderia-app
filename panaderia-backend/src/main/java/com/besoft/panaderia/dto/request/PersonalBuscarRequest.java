package com.besoft.panaderia.dto.request;

public class PersonalBuscarRequest {
	private Long idtTipoDocumento;
	private String nroDocumento;
	private String nombre;

	public Long getIdtTipoDocumento() {
		return idtTipoDocumento;
	}

	public void setIdtTipoDocumento(Long idtTipoDocumento) {
		this.idtTipoDocumento = idtTipoDocumento;
	}

	public String getNroDocumento() {
		return nroDocumento;
	}

	public void setNroDocumento(String nroDocumento) {
		this.nroDocumento = nroDocumento;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

}
