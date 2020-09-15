package com.besoft.panaderia.dao;

import java.util.List;

import com.besoft.panaderia.dto.request.ProductoBuscarRequest;
import com.besoft.panaderia.dto.request.ProductoRequest;
import com.besoft.panaderia.dto.response.ApiOutResponse;
import com.besoft.panaderia.dto.response.ProductoResponse;

public interface ProductoDao {

	public ApiOutResponse<List<ProductoResponse>> listarProducto(ProductoBuscarRequest req);

	public ApiOutResponse<ProductoResponse> registrarProducto(ProductoRequest req);

	public ApiOutResponse<ProductoResponse> modificarProducto(ProductoRequest req);

	public ApiOutResponse<ProductoResponse> eliminarProducto(ProductoRequest req);
}
