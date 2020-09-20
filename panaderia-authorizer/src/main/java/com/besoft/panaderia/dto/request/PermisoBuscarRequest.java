package com.besoft.panaderia.dto.request;

import java.io.Serializable;

public class PermisoBuscarRequest implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long idUsuario;

	public Long getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}

}
