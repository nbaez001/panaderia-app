package com.besoft.panaderia.dao;

import java.util.List;

import com.besoft.panaderia.dto.request.InsumoBuscarRequest;
import com.besoft.panaderia.dto.request.InsumoRequest;
import com.besoft.panaderia.dto.request.TipoInsumoBuscarRequest;
import com.besoft.panaderia.dto.request.TipoInsumoRequest;
import com.besoft.panaderia.dto.response.OutResponse;
import com.besoft.panaderia.dto.response.TipoInsumoResponse;
import com.besoft.panaderia.dto.response.InsumoResponse;

public interface InsumoDao {

	public OutResponse<List<InsumoResponse>> listarInsumo(InsumoBuscarRequest req);

	public OutResponse<InsumoResponse> registrarInsumo(InsumoRequest req);

	public OutResponse<InsumoResponse> modificarInsumo(InsumoRequest req);

	public OutResponse<InsumoResponse> eliminarInsumo(InsumoRequest req);

	public OutResponse<List<TipoInsumoResponse>> listarTipoInsumo(TipoInsumoBuscarRequest req);

	public OutResponse<TipoInsumoResponse> registrarTipoInsumo(TipoInsumoRequest req);

	public OutResponse<TipoInsumoResponse> modificarTipoInsumo(TipoInsumoRequest req);

	public OutResponse<TipoInsumoResponse> eliminarTipoInsumo(TipoInsumoRequest req);
}
