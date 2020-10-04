package com.besoft.panaderia.dto.response;

public class ReporteVentaResponse {
	private String anio;
	private String mes;
	private String fecha;
	private Long idProducto;
	private String nomProducto;
	private String nomUnidadMedida;
	private Double cantidad;
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

	public Long getIdProducto() {
		return idProducto;
	}

	public void setIdProducto(Long idProducto) {
		this.idProducto = idProducto;
	}

	public String getNomProducto() {
		return nomProducto;
	}

	public void setNomProducto(String nomProducto) {
		this.nomProducto = nomProducto;
	}

	public String getNomUnidadMedida() {
		return nomUnidadMedida;
	}

	public void setNomUnidadMedida(String nomUnidadMedida) {
		this.nomUnidadMedida = nomUnidadMedida;
	}

	public Double getCantidad() {
		return cantidad;
	}

	public void setCantidad(Double cantidad) {
		this.cantidad = cantidad;
	}

	public Double getSuma() {
		return suma;
	}

	public void setSuma(Double suma) {
		this.suma = suma;
	}

}
