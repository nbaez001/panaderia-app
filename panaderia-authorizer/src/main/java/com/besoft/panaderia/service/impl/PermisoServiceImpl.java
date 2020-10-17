package com.besoft.panaderia.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.besoft.panaderia.dao.PermisoDao;
import com.besoft.panaderia.dto.request.PermisoBuscarRequest;
import com.besoft.panaderia.dto.response.OutResponse;
import com.besoft.panaderia.dto.response.PermisoResponse;
import com.besoft.panaderia.service.PermisoService;

@Service
public class PermisoServiceImpl implements PermisoService {

	Logger log = LoggerFactory.getLogger(PermisoServiceImpl.class);

	@Autowired
	PermisoDao permisoDao;

	@Override
	public OutResponse<List<PermisoResponse>> listarPermiso(PermisoBuscarRequest req) {
		log.info("[LISTAR PERMISO][SERVICE][INICIO]");
		OutResponse<List<PermisoResponse>> out = permisoDao.listarPermiso(req);

		List<PermisoResponse> lista = out.getrResult().stream()
				.filter(obj -> (obj.getIdPadre() == null || obj.getIdPadre() == 0L)).collect(Collectors.toList());

		for (PermisoResponse p : lista) {
			List<PermisoResponse> listaChild = out.getrResult().stream()
					.filter(obj -> obj.getIdPadre().equals(p.getId())).collect(Collectors.toList());
			p.setListaPermiso(listaChild);
		}
		out.setrResult(lista);
		log.info("[LISTAR PERMISO][SERVICE][FIN]");
		return out;
	}
}
