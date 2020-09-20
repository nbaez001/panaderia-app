package com.besoft.panaderia.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.besoft.panaderia.dao.ComprobanteDao;
import com.besoft.panaderia.dto.request.ComprobanteBuscarRequest;
import com.besoft.panaderia.dto.request.ComprobanteRequest;
import com.besoft.panaderia.dto.response.ApiOutResponse;
import com.besoft.panaderia.dto.response.ComprobanteResponse;
import com.besoft.panaderia.service.ComprobanteService;

@Service
public class ComprobanteServiceImpl implements ComprobanteService {

	@Autowired
	ComprobanteDao comprobanteDao;

	@Override
	public ApiOutResponse<List<ComprobanteResponse>> listarComprobante(ComprobanteBuscarRequest req) {
		return comprobanteDao.listarComprobante(req);
	}

	@Override
	public ApiOutResponse<ComprobanteResponse> registrarComprobante(ComprobanteRequest req) {
		return comprobanteDao.registrarComprobante(req);
	}

	@Override
	public ApiOutResponse<ComprobanteResponse> modificarComprobante(ComprobanteRequest req) {
		return comprobanteDao.modificarComprobante(req);
	}

	@Override
	public ApiOutResponse<ComprobanteResponse> eliminarComprobante(ComprobanteRequest req) {
		return comprobanteDao.eliminarComprobante(req);
	}

}
