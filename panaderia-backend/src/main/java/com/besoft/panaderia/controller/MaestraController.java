package com.besoft.panaderia.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.besoft.panaderia.dto.request.MaestraBuscarRequest;
import com.besoft.panaderia.dto.request.MaestraRequest;
import com.besoft.panaderia.dto.response.ApiOutResponse;
import com.besoft.panaderia.dto.response.MaestraResponse;
import com.besoft.panaderia.service.MaestraService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/maestra")
@Api(value = "API Maestra")
public class MaestraController {

	Logger log = LoggerFactory.getLogger(MaestraController.class);

	@Autowired
	MaestraService maestraService;

//	@PreAuthorize("hasAnyRole('ADMIN_ROLE','VENTAS')")
	@PostMapping("/listarMaestra")
	public ApiOutResponse<List<MaestraResponse>> listarMaestra(@RequestBody MaestraBuscarRequest req) {
//		TokenMapper tokenMapper = (TokenMapper) ((OAuth2AuthenticationDetails) SecurityContextHolder.getContext()
//				.getAuthentication().getDetails()).getDecodedDetails();
//		log.info("Token nombre: " + tokenMapper.getNombre());

		ApiOutResponse<List<MaestraResponse>> out = maestraService.listarMaestra(req);
		return out;
	}

	@PostMapping("/registrarMaestra")
	public ApiOutResponse<MaestraResponse> registrarMaestra(@RequestBody MaestraRequest req) {
		return maestraService.registrarMaestra(req);
	}

	@PostMapping("/modificarMaestra")
	public ApiOutResponse<MaestraResponse> modificarMaestra(@RequestBody MaestraRequest req) {
		return maestraService.modificarMaestra(req);
	}

	@PostMapping("/eliminarMaestra")
	public ApiOutResponse<MaestraResponse> eliminarMaestra(@RequestBody MaestraRequest req) {
		return maestraService.eliminarMaestra(req);
	}
}
