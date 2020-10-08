package com.besoft.panaderia.service;

import java.util.List;

import com.besoft.panaderia.dto.request.InsumoBuscarRequest;
import com.besoft.panaderia.dto.request.InsumoRequest;
import com.besoft.panaderia.dto.request.TipoInsumoBuscarRequest;
import com.besoft.panaderia.dto.request.TipoInsumoRequest;
import com.besoft.panaderia.dto.response.FileResponse;
import com.besoft.panaderia.dto.response.InsumoResponse;
import com.besoft.panaderia.dto.response.OutResponse;
import com.besoft.panaderia.dto.response.TipoInsumoResponse;

public interface InsumoService {

	public OutResponse<List<InsumoResponse>> listarInsumo(InsumoBuscarRequest req);

	public OutResponse<InsumoResponse> registrarInsumo(InsumoRequest req);

	public OutResponse<InsumoResponse> modificarInsumo(InsumoRequest req);

	public OutResponse<InsumoResponse> eliminarInsumo(InsumoRequest req);

	public OutResponse<FileResponse> reporteXlsxListarInsumo(InsumoBuscarRequest req);

	
	public OutResponse<List<TipoInsumoResponse>> listarTipoInsumo(TipoInsumoBuscarRequest req);

	public OutResponse<TipoInsumoResponse> registrarTipoInsumo(TipoInsumoRequest req);

	public OutResponse<TipoInsumoResponse> modificarTipoInsumo(TipoInsumoRequest req);

	public OutResponse<TipoInsumoResponse> eliminarTipoInsumo(TipoInsumoRequest req);
	
	public OutResponse<FileResponse> reporteXlsxListarTipoInsumo(TipoInsumoBuscarRequest req);
}
