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

import com.besoft.panaderia.dao.PersonalDao;
import com.besoft.panaderia.dto.request.PersonalBuscarRequest;
import com.besoft.panaderia.dto.request.PersonalRequest;
import com.besoft.panaderia.dto.response.FileResponse;
import com.besoft.panaderia.dto.response.OutResponse;
import com.besoft.panaderia.dto.response.PersonalResponse;
import com.besoft.panaderia.service.PersonalService;

@Service
public class PersonalServiceImpl implements PersonalService {

	Logger log = LoggerFactory.getLogger(PersonalServiceImpl.class);

	@Autowired
	PersonalDao personalDao;

	@Override
	public OutResponse<List<PersonalResponse>> listarPersonal(PersonalBuscarRequest req) {
		return personalDao.listarPersonal(req);
	}

	@Override
	public OutResponse<PersonalResponse> registrarPersonal(PersonalRequest req) {
		return personalDao.registrarPersonal(req);
	}

	@Override
	public OutResponse<PersonalResponse> modificarPersonal(PersonalRequest req) {
		return personalDao.modificarPersonal(req);
	}

	@Override
	public OutResponse<PersonalResponse> eliminarPersonal(PersonalRequest req) {
		return personalDao.eliminarPersonal(req);
	}

	@Override
	public OutResponse<FileResponse> reporteXlsxListarPersonal(PersonalBuscarRequest req) {
		OutResponse<List<PersonalResponse>> out = personalDao.listarPersonal(req);

		OutResponse<FileResponse> outF = new OutResponse<>();
		if (out.getrCodigo() == 0) {
			try {
				String[] columns = { "Nro", "Cargo", "Nombre", "Apellidos", "Tipo documento", "Nro documento",
						"Dir. Domicilio", "Distrito", "Provincia", "Departamento", "Pais" };

				Workbook workbook = new HSSFWorkbook();
				ByteArrayOutputStream stream = new ByteArrayOutputStream();

				Sheet sheet = workbook.createSheet("Personal");
				Row row = sheet.createRow(0);

				for (int i = 0; i < columns.length; i++) {
					Cell cell = row.createCell(i);
					cell.setCellValue(columns[i]);
				}

				int initRow = 1;
				for (PersonalResponse ir : out.getrResult()) {
					row = sheet.createRow(initRow);
					row.createCell(0).setCellValue(initRow);
					row.createCell(1).setCellValue(ir.getCargo());
					row.createCell(2).setCellValue(ir.getPersona().getNombre());
					row.createCell(3).setCellValue(ir.getPersona().getApePaterno() + " "
							+ (ir.getPersona().getApeMaterno() != null ? ir.getPersona().getApeMaterno() : ""));
					row.createCell(4).setCellValue(ir.getPersona().getNomTipoDocumento());
					row.createCell(5).setCellValue(ir.getPersona().getNroDocumento());
					row.createCell(6).setCellValue(ir.getPersona().getDireccionDomicilio());
					row.createCell(7).setCellValue(
							ir.getPersona().getNomDistrito() != null ? ir.getPersona().getNomDistrito() : "");
					row.createCell(8).setCellValue(
							ir.getPersona().getNomProvincia() != null ? ir.getPersona().getNomProvincia() : "");
					row.createCell(9).setCellValue(
							ir.getPersona().getNomDepartamento() != null ? ir.getPersona().getNomDepartamento() : "");
					row.createCell(10)
							.setCellValue(ir.getPersona().getNomPais() != null ? ir.getPersona().getNomPais() : "");

					initRow++;
				}

				workbook.write(stream);
				workbook.close();

				FileResponse resp = new FileResponse();
				resp.setNombre("Reporte Personal.xls");
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
