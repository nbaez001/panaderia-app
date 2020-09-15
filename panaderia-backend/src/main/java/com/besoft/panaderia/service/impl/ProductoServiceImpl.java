package com.besoft.panaderia.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.besoft.panaderia.dao.ProductoDao;
import com.besoft.panaderia.dto.request.ProductoBuscarRequest;
import com.besoft.panaderia.dto.request.ProductoRequest;
import com.besoft.panaderia.dto.response.ApiOutResponse;
import com.besoft.panaderia.dto.response.ProductoResponse;
import com.besoft.panaderia.service.ProductoService;

@Service
public class ProductoServiceImpl implements ProductoService {

	@Autowired
	ProductoDao productoDao;

	@Override
	public ApiOutResponse<List<ProductoResponse>> listarProducto(ProductoBuscarRequest req) {
		return productoDao.listarProducto(req);
	}

	@Override
	public ApiOutResponse<ProductoResponse> registrarProducto(ProductoRequest req) {
		return productoDao.registrarProducto(req);
	}

	@Override
	public ApiOutResponse<ProductoResponse> modificarProducto(ProductoRequest req) {
		return productoDao.modificarProducto(req);
	}

	@Override
	public ApiOutResponse<ProductoResponse> eliminarProducto(ProductoRequest req) {
		return productoDao.eliminarProducto(req);
	}

}
