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
import com.besoft.panaderia.dto.response.FileResponse;
import com.besoft.panaderia.dto.response.MaestraResponse;
import com.besoft.panaderia.dto.response.OutResponse;
import com.besoft.panaderia.service.MaestraService;

@RestController
@RequestMapping("/maestra")
public class MaestraController {

	Logger log = LoggerFactory.getLogger(MaestraController.class);

	@Autowired
	MaestraService maestraService;

//	@PreAuthorize("hasAnyRole('ADMIN_ROLE','VENTAS')")
	@PostMapping("/listarMaestra")
	public OutResponse<List<MaestraResponse>> listarMaestra(@RequestBody MaestraBuscarRequest req) {
//		TokenMapper tokenMapper = (TokenMapper) ((OAuth2AuthenticationDetails) SecurityContextHolder.getContext()
//				.getAuthentication().getDetails()).getDecodedDetails();
//		log.info("Token nombre: " + tokenMapper.getNombre());

		OutResponse<List<MaestraResponse>> out = maestraService.listarMaestra(req);
		return out;
	}

	@PostMapping("/registrarMaestra")
	public OutResponse<MaestraResponse> registrarMaestra(@RequestBody MaestraRequest req) {
		return maestraService.registrarMaestra(req);
	}

	@PostMapping("/modificarMaestra")
	public OutResponse<MaestraResponse> modificarMaestra(@RequestBody MaestraRequest req) {
		return maestraService.modificarMaestra(req);
	}

	@PostMapping("/eliminarMaestra")
	public OutResponse<MaestraResponse> eliminarMaestra(@RequestBody MaestraRequest req) {
		return maestraService.eliminarMaestra(req);
	}
	
	@PostMapping("/reporteXlsxListarMaestra")
	public OutResponse<FileResponse> reporteXlsxListarMaestra(@RequestBody MaestraBuscarRequest req) {
		return maestraService.reporteXlsxListarMaestra(req);
	}
}
