package com.besoft.panaderia.dto.request;

import java.util.Date;

public class ReporteInsumoBuscarRequest {
	private Integer idTipoReporte;
	private Long idTipoInsumo;
	private Long idPersonal;
	private Date fecInicio;
	private Date fecFin;
	private String user;

	public Integer getIdTipoReporte() {
		return idTipoReporte;
	}

	public void setIdTipoReporte(Integer idTipoReporte) {
		this.idTipoReporte = idTipoReporte;
	}

	public Long getIdTipoInsumo() {
		return idTipoInsumo;
	}

	public void setIdTipoInsumo(Long idTipoInsumo) {
		this.idTipoInsumo = idTipoInsumo;
	}

	public Long getIdPersonal() {
		return idPersonal;
	}

	public void setIdPersonal(Long idPersonal) {
		this.idPersonal = idPersonal;
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
