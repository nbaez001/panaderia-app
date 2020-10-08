package com.besoft.panaderia.service;

import java.util.List;

import com.besoft.panaderia.dto.request.MaestraBuscarRequest;
import com.besoft.panaderia.dto.request.MaestraRequest;
import com.besoft.panaderia.dto.response.FileResponse;
import com.besoft.panaderia.dto.response.MaestraResponse;
import com.besoft.panaderia.dto.response.OutResponse;

public interface MaestraService {

	public OutResponse<List<MaestraResponse>> listarMaestra(MaestraBuscarRequest req);

	public OutResponse<MaestraResponse> registrarMaestra(MaestraRequest req);

	public OutResponse<MaestraResponse> modificarMaestra(MaestraRequest req);

	public OutResponse<MaestraResponse> eliminarMaestra(MaestraRequest req);
	
	public OutResponse<FileResponse> reporteXlsxListarMaestra(MaestraBuscarRequest req);
}
