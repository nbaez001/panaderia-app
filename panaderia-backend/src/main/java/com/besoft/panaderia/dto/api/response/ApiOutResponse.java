package com.besoft.panaderia.dto.api.response;

public class ApiOutResponse<T> {
	private Integer rCodigo;
	private String rMensaje;
	private T result;

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

	public T getResult() {
		return result;
	}

	public void setResult(T result) {
		this.result = result;
	}

}
