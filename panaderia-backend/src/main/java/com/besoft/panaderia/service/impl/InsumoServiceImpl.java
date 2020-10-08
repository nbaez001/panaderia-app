package com.besoft.panaderia.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.besoft.panaderia.dao.InsumoDao;
import com.besoft.panaderia.dto.request.InsumoBuscarRequest;
import com.besoft.panaderia.dto.request.InsumoRequest;
import com.besoft.panaderia.dto.request.TipoInsumoBuscarRequest;
import com.besoft.panaderia.dto.request.TipoInsumoRequest;
import com.besoft.panaderia.dto.response.FileResponse;
import com.besoft.panaderia.dto.response.InsumoResponse;
import com.besoft.panaderia.dto.response.OutResponse;
import com.besoft.panaderia.dto.response.TipoInsumoResponse;
import com.besoft.panaderia.service.InsumoService;
import com.besoft.panaderia.util.DateUtil;

@Service
public class InsumoServiceImpl implements InsumoService {

	Logger log = LoggerFactory.getLogger(InsumoServiceImpl.class);

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
	public OutResponse<FileResponse> reporteXlsxListarInsumo(@RequestBody InsumoBuscarRequest req) {
		OutResponse<List<InsumoResponse>> out = insumoDao.listarInsumo(req);

		OutResponse<FileResponse> outF = new OutResponse<>();
		if (out.getrCodigo() == 0) {
			try {
				String[] columns = { "Nro", "Personal", "Tipo insumo", "Cantidad", "Unidad Medida", "Fecha" };

				Workbook workbook = new HSSFWorkbook();
				ByteArrayOutputStream stream = new ByteArrayOutputStream();

				Sheet sheet = workbook.createSheet("Insumo personal");
				Row row = sheet.createRow(0);

				for (int i = 0; i < columns.length; i++) {
					Cell cell = row.createCell(i);
					cell.setCellValue(columns[i]);
				}

				int initRow = 1;
				for (InsumoResponse ir : out.getrResult()) {
					row = sheet.createRow(initRow);
					row.createCell(0).setCellValue(initRow);
					row.createCell(1)
							.setCellValue(ir.getPersonal().getPersona().getNombre() + " "
									+ ir.getPersonal().getPersona().getApePaterno() + " "
									+ ((ir.getPersonal().getPersona().getApeMaterno() != null)
											? ir.getPersonal().getPersona().getApeMaterno()
											: ""));
					row.createCell(2).setCellValue(ir.getTipoInsumo().getNombre());
					row.createCell(3).setCellValue(ir.getCantidad());
					row.createCell(4).setCellValue(ir.getTipoInsumo().getNomUnidadMedida());
					row.createCell(5).setCellValue(DateUtil.formatSlashDDMMYYYY(ir.getFecha()));

					initRow++;
				}

				workbook.write(stream);
				workbook.close();

				FileResponse resp = new FileResponse();
				resp.setNombre("Reporte Insumos.xls");
				resp.setType("application/vnd.ms-excel");
				resp.setData(stream.toByteArray());

				outF.setrCodigo(0);
				outF.setrMensaje("EXITO");
				outF.setrResult(resp);
			} catch (IOException e) {
				log.info(ExceptionUtils.getStackTrace(e));
				outF.setrCodigo(500);
				outF.setrMensaje(e.getMessage());
			}
		} else {
			outF.setrCodigo(out.getrCodigo());
			outF.setrMensaje(out.getrMensaje());
		}
		return outF;
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
