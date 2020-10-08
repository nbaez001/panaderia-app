package com.besoft.panaderia.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.besoft.panaderia.dto.request.ReporteInsumoBuscarRequest;
import com.besoft.panaderia.dto.request.ReporteVentaBuscarRequest;
import com.besoft.panaderia.dto.response.FileResponse;
import com.besoft.panaderia.dto.response.OutResponse;
import com.besoft.panaderia.dto.response.ReporteInsumoResponse;
import com.besoft.panaderia.dto.response.ReporteVentaResponse;
import com.besoft.panaderia.service.ReporteService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/reporte")
@Api(value = "API Reporte")
public class ReporteController {

	Logger log = LoggerFactory.getLogger(ReporteController.class);

	@Autowired
	ReporteService reporteService;
	
	@PostMapping("/validarReportes")
	public OutResponse<?> validarReportes() {
		return reporteService.validarReportes();
	}

	@PostMapping("/listarReporteInsumo")
	public OutResponse<List<ReporteInsumoResponse>> listarReporteInsumo(@RequestBody ReporteInsumoBuscarRequest req) {
		return reporteService.listarReporteInsumo(req);
	}

	@PostMapping("/listarReporteVenta")
	public OutResponse<List<ReporteVentaResponse>> listarReporteVenta(@RequestBody ReporteVentaBuscarRequest req) {
		return reporteService.listarReporteVenta(req);
	}

	@PostMapping("/generarReporteInsumoPDF")
	public OutResponse<FileResponse> generarReporteInsumoPDF(@RequestBody ReporteInsumoBuscarRequest req) {
		return reporteService.generarReporteInsumoPDF(req);
	}

	@PostMapping("/generarReporteVentaPDF")
	public OutResponse<FileResponse> generarReporteVentaPDF(@RequestBody ReporteVentaBuscarRequest req) {
		return reporteService.generarReporteVentaPDF(req);
	}

}
