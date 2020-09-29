package com.besoft.panaderia.service;

import java.util.List;

import com.besoft.panaderia.dto.request.VentaRequest;
import com.besoft.panaderia.dto.response.OutResponse;
import com.besoft.panaderia.dto.response.VentaResponse;

public interface VentaService {

	public OutResponse<VentaResponse> registrarVenta(VentaRequest c);
	
	public void imprimirVenta();

	public OutResponse<List<VentaResponse>> listarVenta();
}
