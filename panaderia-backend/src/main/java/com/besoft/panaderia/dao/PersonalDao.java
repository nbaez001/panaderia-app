package com.besoft.panaderia.dao;

import java.util.List;

import com.besoft.panaderia.dto.request.PersonalBuscarRequest;
import com.besoft.panaderia.dto.request.PersonalRequest;
import com.besoft.panaderia.dto.response.OutResponse;
import com.besoft.panaderia.dto.response.PersonalResponse;

public interface PersonalDao {

	public OutResponse<List<PersonalResponse>> listarPersonal(PersonalBuscarRequest req);

	public OutResponse<PersonalResponse> registrarPersonal(PersonalRequest req);

	public OutResponse<PersonalResponse> modificarPersonal(PersonalRequest req);

	public OutResponse<PersonalResponse> eliminarPersonal(PersonalRequest req);
}
