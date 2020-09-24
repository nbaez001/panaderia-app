package com.besoft.panaderia.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.besoft.panaderia.dao.PersonalDao;
import com.besoft.panaderia.dto.request.PersonalBuscarRequest;
import com.besoft.panaderia.dto.request.PersonalRequest;
import com.besoft.panaderia.dto.response.OutResponse;
import com.besoft.panaderia.dto.response.PersonalResponse;
import com.besoft.panaderia.service.PersonalService;

@Service
public class PersonalServiceImpl implements PersonalService {

	@Autowired
	PersonalDao personalDao;

	@Override
	public OutResponse<List<PersonalResponse>> listarPersonal(PersonalBuscarRequest req) {
		return personalDao.listarPersonal(req);
	}

	@Override
	public OutResponse<PersonalResponse> registrarPersonal(PersonalRequest req) {
		return personalDao.registrarPersonal(req);
	}

	@Override
	public OutResponse<PersonalResponse> modificarPersonal(PersonalRequest req) {
		return personalDao.modificarPersonal(req);
	}

	@Override
	public OutResponse<PersonalResponse> eliminarPersonal(PersonalRequest req) {
		return personalDao.eliminarPersonal(req);
	}
}
