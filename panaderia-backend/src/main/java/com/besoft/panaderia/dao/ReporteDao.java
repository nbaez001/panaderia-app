package com.besoft.panaderia.dao;

import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;

import com.besoft.panaderia.dto.request.ReporteInsumoBuscarRequest;
import com.besoft.panaderia.dto.request.ReporteVentaBuscarRequest;
import com.besoft.panaderia.dto.response.OutResponse;
import com.besoft.panaderia.dto.response.ReporteInsumoResponse;
import com.besoft.panaderia.dto.response.ReporteVentaResponse;

public interface ReporteDao {

	public OutResponse<List<ReporteInsumoResponse>> listarReporteInsumo(@RequestBody ReporteInsumoBuscarRequest req);

	public OutResponse<List<ReporteVentaResponse>> listarReporteVenta(@RequestBody ReporteVentaBuscarRequest req);
}
