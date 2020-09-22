package com.besoft.panaderia.dto.request;

import java.util.Date;
import java.util.List;

public class VentaRequest {
	private Long id;
	private Long idComprobante;
	private Double total;
	private Integer flgActivo;
	private Long idUsuarioCrea;
	private Date fecUsuarioCrea;
	private Long idUsuarioMod;
	private Date fecUsuarioMod;

	private List<DetalleVentaRequest> listaDetalleVenta;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdComprobante() {
		return idComprobante;
	}

	public void setIdComprobante(Long idComprobante) {
		this.idComprobante = idComprobante;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
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

	public List<DetalleVentaRequest> getListaDetalleVenta() {
		return listaDetalleVenta;
	}

	public void setListaDetalleVenta(List<DetalleVentaRequest> listaDetalleVenta) {
		this.listaDetalleVenta = listaDetalleVenta;
	}

}
