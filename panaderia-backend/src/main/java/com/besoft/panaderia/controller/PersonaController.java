package com.besoft.panaderia.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.besoft.panaderia.dto.api.request.ApiPersonaBuscarRequest;
import com.besoft.panaderia.dto.api.response.ApiOutResponse;
import com.besoft.panaderia.dto.api.response.ApiPersonaResponse;
import com.besoft.panaderia.service.PersonaService;

@RestController
@RequestMapping("/persona")
public class PersonaController {
	Logger log = LoggerFactory.getLogger(ProductoController.class);

	@Autowired
	PersonaService personaService;

	@PostMapping("/buscarPersona")
	public ApiOutResponse<ApiPersonaResponse> buscarPersona(@RequestBody ApiPersonaBuscarRequest req) {
		return personaService.buscarPersona(req);
	}
}
