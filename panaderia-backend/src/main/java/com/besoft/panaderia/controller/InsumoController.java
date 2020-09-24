package com.besoft.panaderia.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.besoft.panaderia.dto.request.InsumoBuscarRequest;
import com.besoft.panaderia.dto.request.InsumoRequest;
import com.besoft.panaderia.dto.request.TipoInsumoBuscarRequest;
import com.besoft.panaderia.dto.request.TipoInsumoRequest;
import com.besoft.panaderia.dto.response.OutResponse;
import com.besoft.panaderia.dto.response.TipoInsumoResponse;
import com.besoft.panaderia.dto.response.InsumoResponse;
import com.besoft.panaderia.service.InsumoService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/insumo")
@Api(value = "API Insumo")
public class InsumoController {
	Logger log = LoggerFactory.getLogger(InsumoController.class);

	@Autowired
	InsumoService insumoService;

	@PostMapping("/listarInsumo")
	public OutResponse<List<InsumoResponse>> listarInsumo(@RequestBody InsumoBuscarRequest req) {
		return insumoService.listarInsumo(req);
	}

	@PostMapping("/registrarInsumo")
	public OutResponse<InsumoResponse> registrarInsumo(@RequestBody InsumoRequest req) {
		return insumoService.registrarInsumo(req);
	}

	@PostMapping("/modificarInsumo")
	public OutResponse<InsumoResponse> modificarInsumo(@RequestBody InsumoRequest req) {
		return insumoService.modificarInsumo(req);
	}

	@PostMapping("/eliminarInsumo")
	public OutResponse<InsumoResponse> eliminarInsumo(@RequestBody InsumoRequest req) {
		return insumoService.eliminarInsumo(req);
	}

	@PostMapping("/listarTipoInsumo")
	public OutResponse<List<TipoInsumoResponse>> listarTipoInsumo(@RequestBody TipoInsumoBuscarRequest req) {
		return insumoService.listarTipoInsumo(req);
	}

	@PostMapping("/registrarTipoInsumo")
	public OutResponse<TipoInsumoResponse> registrarTipoInsumo(@RequestBody TipoInsumoRequest req) {
		return insumoService.registrarTipoInsumo(req);
	}

	@PostMapping("/modificarTipoInsumo")
	public OutResponse<TipoInsumoResponse> modificarTipoInsumo(@RequestBody TipoInsumoRequest req) {
		return insumoService.modificarTipoInsumo(req);
	}

	@PostMapping("/eliminarTipoInsumo")
	public OutResponse<TipoInsumoResponse> eliminarTipoInsumo(@RequestBody TipoInsumoRequest req) {
		return insumoService.eliminarTipoInsumo(req);
	}
}
