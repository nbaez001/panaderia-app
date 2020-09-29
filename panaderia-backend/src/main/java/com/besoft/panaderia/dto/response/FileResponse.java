package com.besoft.panaderia.dto.response;

import java.io.Serializable;

public class FileResponse implements Serializable {
	private static final long serialVersionUID = 1L;

	private String nombre;
	private String type;
	private byte[] data;

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

}
