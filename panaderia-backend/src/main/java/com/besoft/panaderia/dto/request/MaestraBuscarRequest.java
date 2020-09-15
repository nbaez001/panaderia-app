package com.besoft.panaderia.dto.request;

import java.io.Serializable;
import java.util.Date;

public class MaestraBuscarRequest implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Date fecInicio;
	private Date fecFin;
	private Long idMaestra;
	private Integer idTabla;

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

	public Long getIdMaestra() {
		return idMaestra;
	}

	public void setIdMaestra(Long idMaestra) {
		this.idMaestra = idMaestra;
	}

	public Integer getIdTabla() {
		return idTabla;
	}

	public void setIdTabla(Integer idTabla) {
		this.idTabla = idTabla;
	}

}
