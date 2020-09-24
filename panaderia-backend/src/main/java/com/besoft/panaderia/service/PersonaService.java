package com.besoft.panaderia.service;

import com.besoft.panaderia.dto.api.request.ApiPersonaBuscarRequest;
import com.besoft.panaderia.dto.api.response.ApiOutResponse;
import com.besoft.panaderia.dto.api.response.ApiPersonaResponse;

public interface PersonaService {
	
	public ApiOutResponse<ApiPersonaResponse> buscarPersona(ApiPersonaBuscarRequest req);
}
