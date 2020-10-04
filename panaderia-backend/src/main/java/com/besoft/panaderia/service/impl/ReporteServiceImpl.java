package com.besoft.panaderia.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.besoft.panaderia.dao.ReporteDao;
import com.besoft.panaderia.dto.request.ReporteInsumoBuscarRequest;
import com.besoft.panaderia.dto.request.ReporteVentaBuscarRequest;
import com.besoft.panaderia.dto.response.OutResponse;
import com.besoft.panaderia.dto.response.ReporteInsumoResponse;
import com.besoft.panaderia.dto.response.ReporteVentaResponse;
import com.besoft.panaderia.service.ReporteService;

@Service
public class ReporteServiceImpl implements ReporteService {

	@Autowired
	ReporteDao reporteDao;

	@Override
	public OutResponse<List<ReporteInsumoResponse>> listarReporteInsumo(@RequestBody ReporteInsumoBuscarRequest req) {
		return reporteDao.listarReporteInsumo(req);
	}
	
	@Override
	public OutResponse<List<ReporteVentaResponse>> listarReporteVenta(@RequestBody ReporteVentaBuscarRequest req) {
		return reporteDao.listarReporteVenta(req);
	}
}
