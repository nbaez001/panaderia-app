package com.besoft.panaderia.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.besoft.panaderia.dto.request.DepartamentoRequest;
import com.besoft.panaderia.dto.request.DistritoRequest;
import com.besoft.panaderia.dto.request.ProvinciaRequest;
import com.besoft.panaderia.dto.response.DepartamentoResponse;
import com.besoft.panaderia.dto.response.DistritoResponse;
import com.besoft.panaderia.dto.response.OutResponse;
import com.besoft.panaderia.dto.response.PaisResponse;
import com.besoft.panaderia.dto.response.ProvinciaResponse;
import com.besoft.panaderia.service.UbigeoService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/ubigeo")
@Api(value = "API Ubigeo")
public class UbigeoController {
	Logger log = LoggerFactory.getLogger(UbigeoController.class);

	@Autowired
	UbigeoService ubigeoService;

	@PostMapping("/listarPais")
	public OutResponse<List<PaisResponse>> listarPais() {
		return ubigeoService.listarPais();
	}

	@PostMapping("/listarDepartamento")
	public OutResponse<List<DepartamentoResponse>> listarDepartamento(@RequestBody DepartamentoRequest req) {
		return ubigeoService.listarDepartamento(req);
	}

	@PostMapping("/listarProvincia")
	public OutResponse<List<ProvinciaResponse>> listarProvincia(@RequestBody ProvinciaRequest req) {
		return ubigeoService.listarProvincia(req);
	}

	@PostMapping("/listarDistrito")
	public OutResponse<List<DistritoResponse>> listarDistrito(@RequestBody DistritoRequest req) {
		return ubigeoService.listarDistrito(req);
	}
}
