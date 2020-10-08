package com.besoft.panaderia.dto.response;

public class OutResponse<T> {
	private Integer rCodigo;
	private String rMensaje;
	private T rResult;

	public OutResponse() {
	}

	public OutResponse(Integer rCodigo, String rMensaje) {
		this.rCodigo = rCodigo;
		this.rMensaje = rMensaje;
	}

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

	@Override
	public String toString() {
		return "OutResponse [rCodigo=" + rCodigo + ", rMensaje=" + rMensaje + ", rResult=" + rResult + "]";
	}

}
