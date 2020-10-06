package com.besoft.panaderia.dto.request;

import java.util.Date;

public class ReporteVentaBuscarRequest {
	private Integer idTipoReporte;
	private Long idProducto;
	private Date fecInicio;
	private Date fecFin;
	private String user;

	public Integer getIdTipoReporte() {
		return idTipoReporte;
	}

	public void setIdTipoReporte(Integer idTipoReporte) {
		this.idTipoReporte = idTipoReporte;
	}

	public Long getIdProducto() {
		return idProducto;
	}

	public void setIdProducto(Long idProducto) {
		this.idProducto = idProducto;
	}

	public Date getFecInicio() {
		return fecInicio;
	}

	public void setFecInicio(Date fecInicio) {
		this.fecInicio = fecInicio;
	}

	public Date getFecFin() {
		return fecFin;
	}

	public void setFecFin(Date fecFin) {
		this.fecFin = fecFin;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

}
