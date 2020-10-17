package com.besoft.panaderia.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.besoft.panaderia.dto.request.PermisoBuscarRequest;
import com.besoft.panaderia.dto.response.OutResponse;
import com.besoft.panaderia.dto.response.PermisoResponse;
import com.besoft.panaderia.service.PermisoService;

@RestController
@RequestMapping("/permiso")
public class PermisoController {
	Logger log = LoggerFactory.getLogger(PermisoController.class);

	@Autowired
	PermisoService permisoService;

	@PostMapping("/listarPermiso")
	public OutResponse<List<PermisoResponse>> listarPermiso(@RequestBody PermisoBuscarRequest req) {
		log.info("[LISTAR PERMISO][CONTROLLER][INICIO]");
		OutResponse<List<PermisoResponse>> out = permisoService.listarPermiso(req);
		log.info("[LISTAR PERMISO][CONTROLLER][FIN]");
		return out;
	}
}
