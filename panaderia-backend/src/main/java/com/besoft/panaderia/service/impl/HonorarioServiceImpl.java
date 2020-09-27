package com.besoft.panaderia.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.besoft.panaderia.dao.HonorarioDao;
import com.besoft.panaderia.dto.request.HonorarioBuscarRequest;
import com.besoft.panaderia.dto.request.HonorarioRequest;
import com.besoft.panaderia.dto.response.HonorarioResponse;
import com.besoft.panaderia.dto.response.OutResponse;
import com.besoft.panaderia.service.HonorarioService;

@Service
public class HonorarioServiceImpl implements HonorarioService {

	@Autowired
	HonorarioDao honorarioDao;

	@Override
	public OutResponse<List<HonorarioResponse>> listarHonorario(HonorarioBuscarRequest req) {
		return honorarioDao.listarHonorario(req);
	}

	@Override
	public OutResponse<HonorarioResponse> registrarHonorario(HonorarioRequest c) {
		return honorarioDao.registrarHonorario(c);
	}

}
