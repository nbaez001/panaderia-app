package com.besoft.panaderia.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestBody;

import com.besoft.panaderia.dto.request.HonorarioBuscarRequest;
import com.besoft.panaderia.dto.request.HonorarioPeriodoRequest;
import com.besoft.panaderia.dto.request.HonorarioRequest;
import com.besoft.panaderia.dto.response.FileResponse;
import com.besoft.panaderia.dto.response.HonorarioPeriodoResponse;
import com.besoft.panaderia.dto.response.HonorarioResponse;
import com.besoft.panaderia.dto.response.OutResponse;

public interface HonorarioService {

	public OutResponse<List<HonorarioResponse>> listarHonorario(HonorarioBuscarRequest req);

	public OutResponse<HonorarioResponse> registrarHonorario(HonorarioRequest c);

	public OutResponse<HonorarioPeriodoResponse> buscarPeriodoHonorario(HonorarioPeriodoRequest req);

	public OutResponse<FileResponse> reporteHonorario(@RequestBody HonorarioRequest req, HttpServletRequest request);

	public OutResponse<FileResponse> reporteXlsxListarHonorario(@RequestBody HonorarioBuscarRequest req);
}
