package com.besoft.panaderia.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.besoft.panaderia.dto.request.ComprobanteBuscarRequest;
import com.besoft.panaderia.dto.request.ComprobanteRequest;
import com.besoft.panaderia.dto.response.ComprobanteResponse;
import com.besoft.panaderia.dto.response.OutResponse;
import com.besoft.panaderia.service.ComprobanteService;

@RestController
@RequestMapping("/comprobante")
public class ComprobanteController {

	Logger log = LoggerFactory.getLogger(ComprobanteController.class);

	@Autowired
	ComprobanteService comprobanteService;

	@PostMapping("/listarComprobante")
	public OutResponse<List<ComprobanteResponse>> listarComprobante(@RequestBody ComprobanteBuscarRequest req) {
		OutResponse<List<ComprobanteResponse>> out = comprobanteService.listarComprobante(req);
		return out;
	}

	@PostMapping("/registrarComprobante")
	public OutResponse<ComprobanteResponse> registrarComprobante(@RequestBody ComprobanteRequest req) {
		return comprobanteService.registrarComprobante(req);
	}

	@PostMapping("/modificarComprobante")
	public OutResponse<ComprobanteResponse> modificarComprobante(@RequestBody ComprobanteRequest req) {
		return comprobanteService.modificarComprobante(req);
	}

	@PostMapping("/eliminarComprobante")
	public OutResponse<ComprobanteResponse> eliminarComprobante(@RequestBody ComprobanteRequest req) {
		return comprobanteService.eliminarComprobante(req);
	}

	@PostMapping("/establecerComprobanteActual")
	public OutResponse<ComprobanteResponse> establecerComprobanteActual(@RequestBody ComprobanteRequest req) {
		return comprobanteService.establecerComprobanteActual(req);
	}
}
