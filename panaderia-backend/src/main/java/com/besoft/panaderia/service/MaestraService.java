package com.besoft.panaderia.service;

import java.util.List;

import com.besoft.panaderia.dto.request.MaestraBuscarRequest;
import com.besoft.panaderia.dto.request.MaestraRequest;
import com.besoft.panaderia.dto.response.ApiOutResponse;
import com.besoft.panaderia.dto.response.MaestraResponse;

public interface MaestraService {

	public ApiOutResponse<List<MaestraResponse>> listarMaestra(MaestraBuscarRequest req);

	public ApiOutResponse<MaestraResponse> registrarMaestra(MaestraRequest req);

	public ApiOutResponse<MaestraResponse> modificarMaestra(MaestraRequest req);

	public ApiOutResponse<MaestraResponse> eliminarMaestra(MaestraRequest req);
}
