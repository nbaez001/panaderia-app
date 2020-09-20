package com.besoft.panaderia.dto.request;

import java.io.Serializable;
import java.util.Date;

public class ComprobanteBuscarRequest implements Serializable {
	private static final long serialVersionUID = 1L;
	private Date fecInicio;
	private Date fecFin;
	private Long idtTipoComprobante;

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

	public Long getIdtTipoComprobante() {
		return idtTipoComprobante;
	}

	public void setIdtTipoComprobante(Long idtTipoComprobante) {
		this.idtTipoComprobante = idtTipoComprobante;
	}

}
