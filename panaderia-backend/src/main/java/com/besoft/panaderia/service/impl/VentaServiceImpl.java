package com.besoft.panaderia.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.besoft.panaderia.dao.VentaDao;
import com.besoft.panaderia.dto.request.VentaRequest;
import com.besoft.panaderia.dto.response.ApiOutResponse;
import com.besoft.panaderia.dto.response.VentaResponse;
import com.besoft.panaderia.service.VentaService;

@Service
public class VentaServiceImpl implements VentaService {

	@Autowired
	VentaDao ventaDao;

	@Override
	public ApiOutResponse<VentaResponse> registrarVenta(VentaRequest c) {
		return ventaDao.registrarVenta(c);
	}

	@Override
	public ApiOutResponse<List<VentaResponse>> listarVenta() {
		return ventaDao.listarVenta();
	}

}
