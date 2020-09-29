package com.besoft.panaderia.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.besoft.panaderia.dto.request.HonorarioBuscarRequest;
import com.besoft.panaderia.dto.request.HonorarioPeriodoRequest;
import com.besoft.panaderia.dto.request.HonorarioRequest;
import com.besoft.panaderia.dto.response.FileResponse;
import com.besoft.panaderia.dto.response.HonorarioPeriodoResponse;
import com.besoft.panaderia.dto.response.HonorarioResponse;
import com.besoft.panaderia.dto.response.OutResponse;
import com.besoft.panaderia.service.HonorarioService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/honorario")
@Api(value = "API Honorario")
public class HonorarioController {
	Logger log = LoggerFactory.getLogger(HonorarioController.class);

	@Autowired
	HonorarioService honorarioService;

	@PostMapping("/listarHonorario")
	public OutResponse<List<HonorarioResponse>> listarHonorario(@RequestBody HonorarioBuscarRequest req) {
		return honorarioService.listarHonorario(req);
	}

	@PostMapping("/registrarHonorario")
	public OutResponse<HonorarioResponse> registrarHonorario(@RequestBody HonorarioRequest req) {
		return honorarioService.registrarHonorario(req);
	}

//	@PostMapping("/modificarHonorario")
//	public OutResponse<HonorarioResponse> modificarHonorario(@RequestBody HonorarioRequest req) {
//		return honorarioService.modificarHonorario(req);
//	}
//
//	@PostMapping("/eliminarHonorario")
//	public OutResponse<HonorarioResponse> eliminarHonorario(@RequestBody HonorarioRequest req) {
//		return honorarioService.eliminarHonorario(req);
//	}

	@PostMapping("/buscarPeriodoHonorario")
	public OutResponse<HonorarioPeriodoResponse> buscarPeriodoHonorario(@RequestBody HonorarioPeriodoRequest req) {
		return honorarioService.buscarPeriodoHonorario(req);
	}

	@PostMapping("/reporteHonorario")
	public OutResponse<FileResponse> reporteHonorario(@RequestBody HonorarioRequest req) {
		return honorarioService.reporteHonorario(req);
	}

}
