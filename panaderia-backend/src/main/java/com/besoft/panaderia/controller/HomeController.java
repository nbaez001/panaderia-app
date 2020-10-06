package com.besoft.panaderia.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.besoft.panaderia.service.ReporteService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/home")
@Api(value = "API home")
public class HomeController {

	@Autowired
	ReporteService reporteService;
	
	@PostMapping("/validarArchivosReporte")
	public String index() {
		reporteService.copiarReportes();
		return "works";
	}
	
}
