package com.besoft.panaderia.controller;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.besoft.panaderia.dto.request.VentaRequest;
import com.besoft.panaderia.dto.response.ApiOutResponse;
import com.besoft.panaderia.dto.response.VentaResponse;
import com.besoft.panaderia.service.VentaService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/venta")
@CrossOrigin(origins = { "http://localhost:4200", "http://localhost:8080", "http://localhost" })
@Api(value = "API Venta")
public class VentaController {

	Logger log = LoggerFactory.getLogger(VentaController.class);

	@Autowired
	VentaService ventaService;

	@PostMapping("/registrarVenta")
	public ApiOutResponse<VentaResponse> registrarVenta(@RequestBody VentaRequest c) {
		return ventaService.registrarVenta(c);
	}

	@PostMapping("/listarVenta")
	public ApiOutResponse<List<VentaResponse>> listarVenta() {
		return ventaService.listarVenta();
	}

//	@PostMapping("/decargarComprobante")
//	public ApiOutResponse<FileResponse> decargarComprobante(@RequestBody DescargarComprobanteRequest req) {
//		ApiOutResponse<FileResponse> out = new ApiOutResponse<>();
//		try {
//			File file = new File(ConstanteUtil.ubicacionSfs + "sunat_archivos/sfs/REPO/" + req.getNombreArchivo());
//			if (file.exists()) {
////				HttpHeaders respHeaders = new HttpHeaders();
////				MediaType mediaType = MediaType.parseMediaType("application/pdf");
////				respHeaders.setContentType(mediaType);
////				respHeaders.setContentLength(file.length());
////				respHeaders.setContentDispositionFormData("attachment", file.getName());
//				InputStreamResource isr = new InputStreamResource(new FileInputStream(file));
//
//				FileResponse fresp = new FileResponse();
//				fresp.setNombre(req.getNombreArchivo());
//				fresp.setType("application/pdf");
//				fresp.setData(IOUtils.toByteArray(isr.getInputStream()));
////				return new ResponseEntity<InputStreamResource>(, respHeaders, HttpStatus.OK);
//				out.setrCodigo(0);
//				out.setrMensaje("Exito");
//				out.setObjeto(fresp);
//			} else {
//				out.setrCodigo(1);
//				out.setrMensaje("Archivo no existe");
//				out.setObjeto(null);
//			}
//		} catch (Exception e) {
//			log.info("Error al descargar archivo - " + e.getMessage());
//			out.setrCodigo(1);
//			out.setrMensaje(e.getMessage());
//			out.setObjeto(null);
//		}
//		return out;
//	}
}