package com.besoft.panaderia.dto.request;

import com.besoft.panaderia.dto.response.TipoInsumoResponse;

public class HonorarioDetalleRequest {
	private Long id;
	private Long idHonorario;
	private Long idInsumo;
	private TipoInsumoResponse tipoInsumo;
	private Double cantidad;
	private Double tarifa;
	private Double subtotal;
	private Integer flgActivo;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdHonorario() {
		return idHonorario;
	}

	public void setIdHonorario(Long idHonorario) {
		this.idHonorario = idHonorario;
	}

	public Long getIdInsumo() {
		return idInsumo;
	}

	public void setIdInsumo(Long idInsumo) {
		this.idInsumo = idInsumo;
	}

	public TipoInsumoResponse getTipoInsumo() {
		return tipoInsumo;
	}

	public void setTipoInsumo(TipoInsumoResponse tipoInsumo) {
		this.tipoInsumo = tipoInsumo;
	}

	public Double getCantidad() {
		return cantidad;
	}

	public void setCantidad(Double cantidad) {
		this.cantidad = cantidad;
	}

	public Double getTarifa() {
		return tarifa;
	}

	public void setTarifa(Double tarifa) {
		this.tarifa = tarifa;
	}

	public Double getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(Double subtotal) {
		this.subtotal = subtotal;
	}

	public Integer getFlgActivo() {
		return flgActivo;
	}

	public void setFlgActivo(Integer flgActivo) {
		this.flgActivo = flgActivo;
	}

}
