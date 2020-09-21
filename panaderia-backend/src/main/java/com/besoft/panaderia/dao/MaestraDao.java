package com.besoft.panaderia.dao;

import java.util.List;

import com.besoft.panaderia.dto.request.MaestraBuscarRequest;
import com.besoft.panaderia.dto.request.MaestraRequest;
import com.besoft.panaderia.dto.response.OutResponse;
import com.besoft.panaderia.dto.response.MaestraResponse;

public interface MaestraDao {

	public OutResponse<List<MaestraResponse>> listarMaestra(MaestraBuscarRequest req);

	public OutResponse<MaestraResponse> registrarMaestra(MaestraRequest req);

	public OutResponse<MaestraResponse> modificarMaestra(MaestraRequest req);

	public OutResponse<MaestraResponse> eliminarMaestra(MaestraRequest req);
}
