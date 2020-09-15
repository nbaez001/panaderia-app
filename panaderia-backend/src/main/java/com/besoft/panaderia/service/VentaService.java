package com.besoft.panaderia.service;

import java.util.List;

import com.besoft.panaderia.dto.request.VentaRequest;
import com.besoft.panaderia.dto.response.ApiOutResponse;
import com.besoft.panaderia.dto.response.VentaResponse;

public interface VentaService {

	public ApiOutResponse<VentaResponse> registrarVenta(VentaRequest c);

	public ApiOutResponse<List<VentaResponse>> listarVenta();
}
