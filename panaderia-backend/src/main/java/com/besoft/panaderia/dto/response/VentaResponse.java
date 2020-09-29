package com.besoft.panaderia.dto.response;

import java.util.Date;
import java.util.List;

public class VentaResponse {
	private Long id;
	private Long idComprobante;
	private String serie;
	private String numero;
	private Double total;
	private Integer flgActivo;
	private Long idUsuarioCrea;
	private Date fecUsuarioCrea;
	private Long idUsuarioMod;
	private Date fecUsuarioMod;

	private List<DetalleVentaResponse> listaDetalleVenta;

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

	public String getSerie() {
		return serie;
	}

	public void setSerie(String serie) {
		this.serie = serie;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
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

	public List<DetalleVentaResponse> getListaDetalleVenta() {
		return listaDetalleVenta;
	}

	public void setListaDetalleVenta(List<DetalleVentaResponse> listaDetalleVenta) {
		this.listaDetalleVenta = listaDetalleVenta;
	}

	@Override
	public String toString() {
		return "VentaResponse [id=" + id + ", idComprobante=" + idComprobante + ", serie=" + serie + ", numero="
				+ numero + ", total=" + total + ", flgActivo=" + flgActivo + ", idUsuarioCrea=" + idUsuarioCrea
				+ ", fecUsuarioCrea=" + fecUsuarioCrea + ", idUsuarioMod=" + idUsuarioMod + ", fecUsuarioMod="
				+ fecUsuarioMod + ", listaDetalleVenta=" + listaDetalleVenta + "]";
	}

}
