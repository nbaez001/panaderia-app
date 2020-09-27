package com.besoft.panaderia.service;

import java.util.List;

import com.besoft.panaderia.dto.request.HonorarioBuscarRequest;
import com.besoft.panaderia.dto.request.HonorarioRequest;
import com.besoft.panaderia.dto.response.HonorarioResponse;
import com.besoft.panaderia.dto.response.OutResponse;

public interface HonorarioService {

	public OutResponse<List<HonorarioResponse>> listarHonorario(HonorarioBuscarRequest req);

	public OutResponse<HonorarioResponse> registrarHonorario(HonorarioRequest c);

}
