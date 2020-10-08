package com.besoft.panaderia.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.besoft.panaderia.dto.request.PersonalBuscarRequest;
import com.besoft.panaderia.dto.request.PersonalRequest;
import com.besoft.panaderia.dto.response.FileResponse;
import com.besoft.panaderia.dto.response.OutResponse;
import com.besoft.panaderia.dto.response.PersonalResponse;
import com.besoft.panaderia.service.PersonalService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/personal")
@Api(value = "API Personal")
public class PersonalController {
	Logger log = LoggerFactory.getLogger(PersonalController.class);

	@Autowired
	PersonalService personalService;

	@PostMapping("/listarPersonal")
	public OutResponse<List<PersonalResponse>> listarPersonal(@RequestBody PersonalBuscarRequest req) {
		return personalService.listarPersonal(req);
	}

	@PostMapping("/registrarPersonal")
	public OutResponse<PersonalResponse> registrarPersonal(@RequestBody PersonalRequest req) {
		return personalService.registrarPersonal(req);
	}

	@PostMapping("/modificarPersonal")
	public OutResponse<PersonalResponse> modificarPersonal(@RequestBody PersonalRequest req) {
		return personalService.modificarPersonal(req);
	}

	@PostMapping("/eliminarPersonal")
	public OutResponse<PersonalResponse> eliminarPersonal(@RequestBody PersonalRequest req) {
		return personalService.eliminarPersonal(req);
	}
	
	@PostMapping("/reporteXlsxListarPersonal")
	public OutResponse<FileResponse> reporteXlsxListarPersonal(@RequestBody PersonalBuscarRequest req) {
		return personalService.reporteXlsxListarPersonal(req);
	}
}
