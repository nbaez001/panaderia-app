package com.besoft.panaderia.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.besoft.panaderia.dto.request.ProductoBuscarRequest;
import com.besoft.panaderia.dto.request.ProductoRequest;
import com.besoft.panaderia.dto.response.OutResponse;
import com.besoft.panaderia.dto.response.ProductoResponse;
import com.besoft.panaderia.service.ProductoService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/producto")
@Api(value = "API Producto")
public class ProductoController {

	Logger log = LoggerFactory.getLogger(ProductoController.class);

	@Autowired
	ProductoService productoService;

	@PostMapping("/listarProducto")
	public OutResponse<List<ProductoResponse>> listarProducto(@RequestBody ProductoBuscarRequest req) {
		return productoService.listarProducto(req);
	}

	@PostMapping("/registrarProducto")
	public OutResponse<ProductoResponse> registrarProducto(@RequestBody ProductoRequest req) {
		return productoService.registrarProducto(req);
	}

	@PostMapping("/modificarProducto")
	public OutResponse<ProductoResponse> modificarProducto(@RequestBody ProductoRequest req) {
		return productoService.modificarProducto(req);
	}

	@PostMapping("/eliminarProducto")
	public OutResponse<ProductoResponse> eliminarProducto(@RequestBody ProductoRequest req) {
		return productoService.eliminarProducto(req);
	}
}
