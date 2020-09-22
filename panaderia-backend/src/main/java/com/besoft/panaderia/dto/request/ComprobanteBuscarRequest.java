package com.besoft.panaderia.dto.request;

import java.io.Serializable;
import java.util.Date;

public class ComprobanteBuscarRequest implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private Date fecInicio;
	private Date fecFin;
	private Long idtTipoComprobante;
	private Integer flgActual;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Long getIdtTipoComprobante() {
		return idtTipoComprobante;
	}

	public void setIdtTipoComprobante(Long idtTipoComprobante) {
		this.idtTipoComprobante = idtTipoComprobante;
	}

	public Integer getFlgActual() {
		return flgActual;
	}

	public void setFlgActual(Integer flgActual) {
		this.flgActual = flgActual;
	}

}
