package com.besoft.panaderia.dto.response;

import java.io.Serializable;
import java.util.Date;

public class ComprobanteResponse implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private Long idtTipoComprobante;
	private String nomTipoComprobante;
	private String nombre;
	private Integer serie;
	private Integer numero;
	private Integer longSerie;
	private Integer longNumero;
	private Integer flgActual;
	private Integer flgActivo;
	private Integer flgUsado;
	private Long idUsuarioCrea;
	private Date fecUsuarioCrea;
	private Long idUsuarioMod;
	private Date fecUsuarioMod;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdtTipoComprobante() {
		return idtTipoComprobante;
	}

	public void setIdtTipoComprobante(Long idtTipoComprobante) {
		this.idtTipoComprobante = idtTipoComprobante;
	}

	public String getNomTipoComprobante() {
		return nomTipoComprobante;
	}

	public void setNomTipoComprobante(String nomTipoComprobante) {
		this.nomTipoComprobante = nomTipoComprobante;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Integer getSerie() {
		return serie;
	}

	public void setSerie(Integer serie) {
		this.serie = serie;
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public Integer getLongSerie() {
		return longSerie;
	}

	public void setLongSerie(Integer longSerie) {
		this.longSerie = longSerie;
	}

	public Integer getLongNumero() {
		return longNumero;
	}

	public void setLongNumero(Integer longNumero) {
		this.longNumero = longNumero;
	}

	public Integer getFlgActual() {
		return flgActual;
	}

	public void setFlgActual(Integer flgActual) {
		this.flgActual = flgActual;
	}

	public Integer getFlgActivo() {
		return flgActivo;
	}

	public void setFlgActivo(Integer flgActivo) {
		this.flgActivo = flgActivo;
	}

	public Integer getFlgUsado() {
		return flgUsado;
	}

	public void setFlgUsado(Integer flgUsado) {
		this.flgUsado = flgUsado;
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

}
