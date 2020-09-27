package com.besoft.panaderia.dto.request;

public class HonorarioInsumoRequest {
	private Long id;
	private Long idHonorario;
	private Long idInsumo;
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

	public Integer getFlgActivo() {
		return flgActivo;
	}

	public void setFlgActivo(Integer flgActivo) {
		this.flgActivo = flgActivo;
	}

}
