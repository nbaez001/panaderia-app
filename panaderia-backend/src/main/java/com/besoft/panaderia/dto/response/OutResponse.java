package com.besoft.panaderia.dto.response;

public class OutResponse<T> {
	private Integer rCodigo;
	private String rMensaje;
	private T rResult;

	public Integer getrCodigo() {
		return rCodigo;
	}

	public void setrCodigo(Integer rCodigo) {
		this.rCodigo = rCodigo;
	}

	public String getrMensaje() {
		return rMensaje;
	}

	public void setrMensaje(String rMensaje) {
		this.rMensaje = rMensaje;
	}

	public T getrResult() {
		return rResult;
	}

	public void setrResult(T rResult) {
		this.rResult = rResult;
	}

}
