package com.besoft.panaderia.service;

import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;

import com.besoft.panaderia.dto.request.DepartamentoRequest;
import com.besoft.panaderia.dto.request.DistritoRequest;
import com.besoft.panaderia.dto.request.ProvinciaRequest;
import com.besoft.panaderia.dto.response.DepartamentoResponse;
import com.besoft.panaderia.dto.response.DistritoResponse;
import com.besoft.panaderia.dto.response.OutResponse;
import com.besoft.panaderia.dto.response.PaisResponse;
import com.besoft.panaderia.dto.response.ProvinciaResponse;

public interface UbigeoService {

	public OutResponse<List<PaisResponse>> listarPais();

	public OutResponse<List<DepartamentoResponse>> listarDepartamento(@RequestBody DepartamentoRequest req);

	public OutResponse<List<ProvinciaResponse>> listarProvincia(@RequestBody ProvinciaRequest req);

	public OutResponse<List<DistritoResponse>> listarDistrito(@RequestBody DistritoRequest req);
}
