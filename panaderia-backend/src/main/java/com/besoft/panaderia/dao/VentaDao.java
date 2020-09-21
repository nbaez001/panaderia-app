package com.besoft.panaderia.dao;

import java.util.List;

import com.besoft.panaderia.dto.request.VentaRequest;
import com.besoft.panaderia.dto.response.OutResponse;
import com.besoft.panaderia.dto.response.VentaResponse;

public interface VentaDao {

	public OutResponse<VentaResponse> registrarVenta(VentaRequest c);

	public OutResponse<List<VentaResponse>> listarVenta();

}
