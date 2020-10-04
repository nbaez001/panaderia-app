package com.besoft.panaderia.dto.response;

public class ReporteInsumoResponse {
	private String anio;
	private String mes;
	private String fecha;
	private Long idPersonal;
	private String nomPersonal;
	private Long idTipoInsumo;
	private String nomTipoInsumo;
	private String nomUnidadMedida;
	private Double suma;

	public String getAnio() {
		return anio;
	}

	public void setAnio(String anio) {
		this.anio = anio;
	}

	public String getMes() {
		return mes;
	}

	public void setMes(String mes) {
		this.mes = mes;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public Long getIdPersonal() {
		return idPersonal;
	}

	public void setIdPersonal(Long idPersonal) {
		this.idPersonal = idPersonal;
	}

	public String getNomPersonal() {
		return nomPersonal;
	}

	public void setNomPersonal(String nomPersonal) {
		this.nomPersonal = nomPersonal;
	}

	public Long getIdTipoInsumo() {
		return idTipoInsumo;
	}

	public void setIdTipoInsumo(Long idTipoInsumo) {
		this.idTipoInsumo = idTipoInsumo;
	}

	public String getNomTipoInsumo() {
		return nomTipoInsumo;
	}

	public void setNomTipoInsumo(String nomTipoInsumo) {
		this.nomTipoInsumo = nomTipoInsumo;
	}

	public String getNomUnidadMedida() {
		return nomUnidadMedida;
	}

	public void setNomUnidadMedida(String nomUnidadMedida) {
		this.nomUnidadMedida = nomUnidadMedida;
	}

	public Double getSuma() {
		return suma;
	}

	public void setSuma(Double suma) {
		this.suma = suma;
	}

}
