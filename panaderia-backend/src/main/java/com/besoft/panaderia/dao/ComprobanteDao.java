package com.besoft.panaderia.dao;

import java.util.List;

import com.besoft.panaderia.dto.request.ComprobanteBuscarRequest;
import com.besoft.panaderia.dto.request.ComprobanteRequest;
import com.besoft.panaderia.dto.response.ApiOutResponse;
import com.besoft.panaderia.dto.response.ComprobanteResponse;

public interface ComprobanteDao {

	public ApiOutResponse<List<ComprobanteResponse>> listarComprobante(ComprobanteBuscarRequest req);

	public ApiOutResponse<ComprobanteResponse> registrarComprobante(ComprobanteRequest req);

	public ApiOutResponse<ComprobanteResponse> modificarComprobante(ComprobanteRequest req);

	public ApiOutResponse<ComprobanteResponse> eliminarComprobante(ComprobanteRequest req);
}
