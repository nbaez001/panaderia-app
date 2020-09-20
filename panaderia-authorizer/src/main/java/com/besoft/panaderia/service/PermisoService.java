package com.besoft.panaderia.service;

import java.util.List;

import com.besoft.panaderia.dto.request.PermisoBuscarRequest;
import com.besoft.panaderia.dto.response.OutResponse;
import com.besoft.panaderia.dto.response.PermisoResponse;

public interface PermisoService {

	public OutResponse<List<PermisoResponse>> listarPermiso(PermisoBuscarRequest req);
}
