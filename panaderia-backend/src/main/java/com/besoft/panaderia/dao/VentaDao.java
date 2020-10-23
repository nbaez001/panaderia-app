package com.besoft.panaderia.dao;

import java.util.List;

import com.besoft.panaderia.dto.request.VentaBuscarRequest;
import com.besoft.panaderia.dto.request.VentaRequest;
import com.besoft.panaderia.dto.response.DetalleVentaResponse;
import com.besoft.panaderia.dto.response.OutResponse;
import com.besoft.panaderia.dto.response.VentaResponse;

public interface VentaDao {

	public OutResponse<VentaResponse> registrarVenta(VentaRequest c);

	public OutResponse<List<VentaResponse>> listarVenta(VentaBuscarRequest req);

	public OutResponse<List<DetalleVentaResponse>> imprimirTicketVenta(VentaRequest c);

}
