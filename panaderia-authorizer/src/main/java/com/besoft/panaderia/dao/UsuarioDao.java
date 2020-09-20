package com.besoft.panaderia.dao;

import com.besoft.panaderia.dto.response.OutResponse;
import com.besoft.panaderia.dto.response.UsuarioResponse;

public interface UsuarioDao {

	public OutResponse<UsuarioResponse> buscarUsuario(String username);

}
