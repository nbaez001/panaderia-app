package com.besoft.panaderia.service;

import java.util.List;

import com.besoft.panaderia.dto.request.VentaBuscarRequest;
import com.besoft.panaderia.dto.request.VentaRequest;
import com.besoft.panaderia.dto.response.OutResponse;
import com.besoft.panaderia.dto.response.VentaResponse;

public interface VentaService {

	public OutResponse<VentaResponse> registrarVenta(VentaRequest c);
	
	public OutResponse<?> imprimirTicketVenta(VentaRequest c);

	public OutResponse<List<VentaResponse>> listarVenta(VentaBuscarRequest req);
}
