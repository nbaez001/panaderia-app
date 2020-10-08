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

import com.besoft.panaderia.dao.MaestraDao;
import com.besoft.panaderia.dto.request.MaestraBuscarRequest;
import com.besoft.panaderia.dto.request.MaestraRequest;
import com.besoft.panaderia.dto.response.FileResponse;
import com.besoft.panaderia.dto.response.MaestraResponse;
import com.besoft.panaderia.dto.response.OutResponse;
import com.besoft.panaderia.service.MaestraService;

@Service
public class MaestraServiceImpl implements MaestraService {

	Logger log = LoggerFactory.getLogger(MaestraServiceImpl.class);

	@Autowired
	MaestraDao maestraDao;

	@Override
	public OutResponse<List<MaestraResponse>> listarMaestra(MaestraBuscarRequest req) {
		return maestraDao.listarMaestra(req);
	}

	@Override
	public OutResponse<MaestraResponse> registrarMaestra(MaestraRequest req) {
		return maestraDao.registrarMaestra(req);
	}

	@Override
	public OutResponse<MaestraResponse> modificarMaestra(MaestraRequest req) {
		return maestraDao.modificarMaestra(req);
	}

	@Override
	public OutResponse<MaestraResponse> eliminarMaestra(MaestraRequest req) {
		return maestraDao.eliminarMaestra(req);
	}

	@Override
	public OutResponse<FileResponse> reporteXlsxListarMaestra(MaestraBuscarRequest req) {
		OutResponse<List<MaestraResponse>> out = maestraDao.listarMaestra(req);

		OutResponse<FileResponse> outF = new OutResponse<>();
		if (out.getrCodigo() == 0) {
			try {
				String[] columns = { "Nro", "Nombre", "Codigo", "Valor", "Descripcion", "ID Tabla" };

				Workbook workbook = new HSSFWorkbook();
				ByteArrayOutputStream stream = new ByteArrayOutputStream();

				Sheet sheet = workbook.createSheet("Parametros");
				Row row = sheet.createRow(0);

				for (int i = 0; i < columns.length; i++) {
					Cell cell = row.createCell(i);
					cell.setCellValue(columns[i]);
				}

				int initRow = 1;
				for (MaestraResponse ir : out.getrResult()) {
					row = sheet.createRow(initRow);
					row.createCell(0).setCellValue(initRow);
					row.createCell(1).setCellValue(ir.getNombre());
					row.createCell(2).setCellValue(ir.getCodigo());
					row.createCell(3).setCellValue(ir.getValor());
					row.createCell(4).setCellValue(ir.getDescripcion());
					row.createCell(5).setCellValue(ir.getIdTabla());

					initRow++;
				}

				workbook.write(stream);
				workbook.close();

				FileResponse resp = new FileResponse();
				resp.setNombre("Reporte Parametros.xls");
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

}
