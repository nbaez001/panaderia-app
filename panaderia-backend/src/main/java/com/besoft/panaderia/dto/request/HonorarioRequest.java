package com.besoft.panaderia.dto.request;

import java.util.Date;
import java.util.List;

import com.besoft.panaderia.dto.response.InsumoResponse;

public class HonorarioRequest {
	private Long id;
	private PersonalRequest personal;
	private Double monto;
	private Date fechaInicio;
	private Date fechaFin;
	private Date fecha;
	private Integer flgActivo;
	private Long idUsuarioCrea;
	private Date fecUsuarioCrea;
	private Long idUsuarioMod;
	private Date fecUsuarioMod;

	private List<InsumoResponse> listaInsumo;
	private List<HonorarioDetalleRequest> listaHonorarioDetalle;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public PersonalRequest getPersonal() {
		return personal;
	}

	public void setPersonal(PersonalRequest personal) {
		this.personal = personal;
	}

	public Double getMonto() {
		return monto;
	}

	public void setMonto(Double monto) {
		this.monto = monto;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
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

	public List<InsumoResponse> getListaInsumo() {
		return listaInsumo;
	}

	public void setListaInsumo(List<InsumoResponse> listaInsumo) {
		this.listaInsumo = listaInsumo;
	}

	public List<HonorarioDetalleRequest> getListaHonorarioDetalle() {
		return listaHonorarioDetalle;
	}

	public void setListaHonorarioDetalle(List<HonorarioDetalleRequest> listaHonorarioDetalle) {
		this.listaHonorarioDetalle = listaHonorarioDetalle;
	}

}
