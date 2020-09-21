package com.besoft.panaderia.service;

import java.util.List;

import com.besoft.panaderia.dto.request.ProductoBuscarRequest;
import com.besoft.panaderia.dto.request.ProductoRequest;
import com.besoft.panaderia.dto.response.OutResponse;
import com.besoft.panaderia.dto.response.ProductoResponse;

public interface ProductoService {

	public OutResponse<List<ProductoResponse>> listarProducto(ProductoBuscarRequest req);

	public OutResponse<ProductoResponse> registrarProducto(ProductoRequest req);

	public OutResponse<ProductoResponse> modificarProducto(ProductoRequest req);

	public OutResponse<ProductoResponse> eliminarProducto(ProductoRequest req);
}
