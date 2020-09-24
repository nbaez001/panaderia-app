package com.besoft.panaderia.dto.request;

import java.util.Date;

public class InsumoBuscarRequest {
	private Long idTipoInsumo;
	private Long idPersonal;
	private Date fecInicio;
	private Date fecFin;

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

}
