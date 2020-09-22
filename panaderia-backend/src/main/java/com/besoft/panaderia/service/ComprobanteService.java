package com.besoft.panaderia.service;

import java.util.List;

import com.besoft.panaderia.dto.request.ComprobanteBuscarRequest;
import com.besoft.panaderia.dto.request.ComprobanteRequest;
import com.besoft.panaderia.dto.response.OutResponse;
import com.besoft.panaderia.dto.response.ComprobanteResponse;

public interface ComprobanteService {

	public OutResponse<List<ComprobanteResponse>> listarComprobante(ComprobanteBuscarRequest req);

	public OutResponse<ComprobanteResponse> registrarComprobante(ComprobanteRequest req);

	public OutResponse<ComprobanteResponse> modificarComprobante(ComprobanteRequest req);

	public OutResponse<ComprobanteResponse> eliminarComprobante(ComprobanteRequest req);
	
	public OutResponse<ComprobanteResponse> establecerComprobanteActual(ComprobanteRequest req);
}
