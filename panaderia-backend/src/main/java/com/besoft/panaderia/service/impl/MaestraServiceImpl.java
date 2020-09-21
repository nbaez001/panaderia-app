package com.besoft.panaderia.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.besoft.panaderia.dao.MaestraDao;
import com.besoft.panaderia.dto.request.MaestraBuscarRequest;
import com.besoft.panaderia.dto.request.MaestraRequest;
import com.besoft.panaderia.dto.response.OutResponse;
import com.besoft.panaderia.dto.response.MaestraResponse;
import com.besoft.panaderia.service.MaestraService;

@Service
public class MaestraServiceImpl implements MaestraService {

	@Autowired
	MaestraDao maestraDao;

	@Override
	public OutResponse<List<MaestraResponse>> listarMaestra(MaestraBuscarRequest req) {
		return maestraDao.listarMaestra(req);
	}

	@Override
	public OutResponse<MaestraResponse> registrarMaestra(MaestraRequest req) {
		return maestraDao.registrarMaestra(req);
	}

	@Override
	public OutResponse<MaestraResponse> modificarMaestra(MaestraRequest req) {
		return maestraDao.modificarMaestra(req);
	}

	@Override
	public OutResponse<MaestraResponse> eliminarMaestra(MaestraRequest req) {
		return maestraDao.eliminarMaestra(req);
	}

}
