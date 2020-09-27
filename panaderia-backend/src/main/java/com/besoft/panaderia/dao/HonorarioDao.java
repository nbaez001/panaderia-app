package com.besoft.panaderia.dao;

import java.util.List;

import com.besoft.panaderia.dto.request.HonorarioBuscarRequest;
import com.besoft.panaderia.dto.request.HonorarioRequest;
import com.besoft.panaderia.dto.response.OutResponse;
import com.besoft.panaderia.dto.response.HonorarioResponse;

public interface HonorarioDao {

	public OutResponse<HonorarioResponse> registrarHonorario(HonorarioRequest c);

	public OutResponse<List<HonorarioResponse>> listarHonorario(HonorarioBuscarRequest req);
}
