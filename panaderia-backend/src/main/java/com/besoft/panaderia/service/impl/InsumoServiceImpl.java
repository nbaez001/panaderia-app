package com.besoft.panaderia.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.besoft.panaderia.dao.InsumoDao;
import com.besoft.panaderia.dto.request.InsumoBuscarRequest;
import com.besoft.panaderia.dto.request.InsumoRequest;
import com.besoft.panaderia.dto.request.TipoInsumoBuscarRequest;
import com.besoft.panaderia.dto.request.TipoInsumoRequest;
import com.besoft.panaderia.dto.response.OutResponse;
import com.besoft.panaderia.dto.response.TipoInsumoResponse;
import com.besoft.panaderia.dto.response.InsumoResponse;
import com.besoft.panaderia.service.InsumoService;

@Service
public class InsumoServiceImpl implements InsumoService {

	@Autowired
	InsumoDao insumoDao;

	@Override
	public OutResponse<List<InsumoResponse>> listarInsumo(InsumoBuscarRequest req) {
		return insumoDao.listarInsumo(req);
	}

	@Override
	public OutResponse<InsumoResponse> registrarInsumo(InsumoRequest req) {
		return insumoDao.registrarInsumo(req);
	}

	@Override
	public OutResponse<InsumoResponse> modificarInsumo(InsumoRequest req) {
		return insumoDao.modificarInsumo(req);
	}

	@Override
	public OutResponse<InsumoResponse> eliminarInsumo(InsumoRequest req) {
		return insumoDao.eliminarInsumo(req);
	}

	@Override
	public OutResponse<List<TipoInsumoResponse>> listarTipoInsumo(TipoInsumoBuscarRequest req) {
		return insumoDao.listarTipoInsumo(req);
	}

	@Override
	public OutResponse<TipoInsumoResponse> registrarTipoInsumo(TipoInsumoRequest req) {
		return insumoDao.registrarTipoInsumo(req);
	}

	@Override
	public OutResponse<TipoInsumoResponse> modificarTipoInsumo(TipoInsumoRequest req) {
		return insumoDao.modificarTipoInsumo(req);
	}

	@Override
	public OutResponse<TipoInsumoResponse> eliminarTipoInsumo(TipoInsumoRequest req) {
		return insumoDao.eliminarTipoInsumo(req);
	}
}
