package com.besoft.panaderia.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.besoft.panaderia.dao.UbigeoDao;
import com.besoft.panaderia.dto.request.DepartamentoRequest;
import com.besoft.panaderia.dto.request.DistritoRequest;
import com.besoft.panaderia.dto.request.ProvinciaRequest;
import com.besoft.panaderia.dto.response.DepartamentoResponse;
import com.besoft.panaderia.dto.response.DistritoResponse;
import com.besoft.panaderia.dto.response.OutResponse;
import com.besoft.panaderia.dto.response.PaisResponse;
import com.besoft.panaderia.dto.response.ProvinciaResponse;
import com.besoft.panaderia.service.UbigeoService;

@Service
public class UbigeoServiceImpl implements UbigeoService {

	@Autowired
	UbigeoDao ubigeoDao;

	@Override
	public OutResponse<List<PaisResponse>> listarPais() {
		return ubigeoDao.listarPais();
	}

	@Override
	public OutResponse<List<DepartamentoResponse>> listarDepartamento(DepartamentoRequest req) {
		return ubigeoDao.listarDepartamento(req);
	}

	@Override
	public OutResponse<List<ProvinciaResponse>> listarProvincia(ProvinciaRequest req) {
		return ubigeoDao.listarProvincia(req);
	}

	@Override
	public OutResponse<List<DistritoResponse>> listarDistrito(DistritoRequest req) {
		return ubigeoDao.listarDistrito(req);
	}

}
