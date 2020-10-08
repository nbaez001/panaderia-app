package com.besoft.panaderia.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.besoft.panaderia.dao.HonorarioDao;
import com.besoft.panaderia.dto.request.HonorarioBuscarRequest;
import com.besoft.panaderia.dto.request.HonorarioDetalleRequest;
import com.besoft.panaderia.dto.request.HonorarioPeriodoRequest;
import com.besoft.panaderia.dto.request.HonorarioRequest;
import com.besoft.panaderia.dto.request.InsumoBuscarRequest;
import com.besoft.panaderia.dto.response.FileResponse;
import com.besoft.panaderia.dto.response.HonorarioPeriodoResponse;
import com.besoft.panaderia.dto.response.HonorarioResponse;
import com.besoft.panaderia.dto.response.InsumoResponse;
import com.besoft.panaderia.dto.response.OutResponse;
import com.besoft.panaderia.service.HonorarioService;
import com.besoft.panaderia.util.ConexionUtil;
import com.besoft.panaderia.util.ConstanteUtil;
import com.besoft.panaderia.util.DateUtil;
import com.besoft.panaderia.util.NumberUtil;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;

@Service
public class HonorarioServiceImpl implements HonorarioService {

	Logger log = LoggerFactory.getLogger(HonorarioServiceImpl.class);

	@Autowired
	ConexionUtil conexionUtil;

	@Autowired
	HonorarioDao honorarioDao;

	@Autowired
	DataSource dataSource;

	@Value("${reportes.files.reporteHonorario}")
	String reporteHonorario;

	@Value("${reportes.files.reporteHonorarioDet}")
	String reporteHonorarioDet;

	@Override
	public OutResponse<List<HonorarioResponse>> listarHonorario(HonorarioBuscarRequest req) {
		return honorarioDao.listarHonorario(req);
	}

	@Override
	public OutResponse<HonorarioResponse> registrarHonorario(HonorarioRequest c) {
		convertirDetalleACadena(c);
		return honorarioDao.registrarHonorario(c);
	}

	@Override
	public OutResponse<HonorarioPeriodoResponse> buscarPeriodoHonorario(HonorarioPeriodoRequest req) {
		return honorarioDao.buscarPeriodoHonorario(req);
	}

	@Override
	public OutResponse<FileResponse> reporteHonorario(@RequestBody HonorarioRequest req, HttpServletRequest request) {
		log.info("[REPORTE HONORARIO][SERVICE][INICIO]");
		OutResponse<FileResponse> out = new OutResponse<>();

		JRPdfExporter exporter = new JRPdfExporter();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		try {
			InputStream fisResumenHonorario = this.getClass().getClassLoader().getResourceAsStream(reporteHonorario);
			InputStream fisResumenHonorarioDet = this.getClass().getClassLoader()
					.getResourceAsStream(reporteHonorarioDet);

			Map<String, Object> params = new HashMap<>();
			params.put("idHonorario", req.getId());
			params.put("SUBREPORT_STR", fisResumenHonorarioDet);
			log.info("[REPORTE HONORARIO][SERVICE][PARAMS-INPUT][" + params.toString() + "]");

			JasperPrint jp = JasperFillManager.fillReport(fisResumenHonorario, params, dataSource.getConnection());

			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jp);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outputStream);
			exporter.exportReport();

			FileResponse resp = new FileResponse();
			resp.setNombre("Reporte-honorario.pdf");
			resp.setType("application/pdf");
			resp.setData(outputStream.toByteArray());

			out.setrCodigo(0);
			out.setrMensaje("EXITO");
			out.setrResult(resp);
			log.info("[REPORTE HONORARIO][SERVICE][EXITO]");
		} catch (JRException | SQLException e) {
			out.setrCodigo(500);
			out.setrMensaje(e.getMessage());
			out.setrResult(null);
			log.info("[REPORTE HONORARIO][SERVICE][EXCEPTION]");
			log.info(ExceptionUtils.getStackTrace(e));
		}
		log.info("[REPORTE HONORARIO][SERVICE][FIN]");
		return out;
	}

	public void convertirDetalleACadena(HonorarioRequest v) {
		String honorarioInsumo = "";
		if (v.getListaInsumo().size() > 0) {
			for (InsumoResponse em : v.getListaInsumo()) {
				honorarioInsumo = honorarioInsumo + ((em.getId() != null) ? em.getId() : "0") + ","
						+ ((em.getFlgActivo() != null) ? em.getFlgActivo() : "0") + "|";

			}
			honorarioInsumo = honorarioInsumo.substring(0, honorarioInsumo.length() - 1);

			log.info("[REGISTRAR HONORARIO][SERVICE][CONVERSION HONORARIO INSUMO][" + honorarioInsumo + "]");
		}
		v.setHonorarioInsumo(honorarioInsumo);

		String honorarioDetalle = "";
		if (v.getListaHonorarioDetalle().size() > 0) {
			for (HonorarioDetalleRequest em : v.getListaHonorarioDetalle()) {
				honorarioDetalle = honorarioDetalle
						+ ((em.getTipoInsumo().getId() != null) ? em.getTipoInsumo().getId() : "0") + ","
						+ ((em.getCantidad() != null) ? NumberUtil.doubleToString(
								em.getCantidad(), ConstanteUtil.separadorPunto, ConstanteUtil.formato1Decimal) : "0.0")
						+ ","
						+ ((em.getTarifa() != null) ? NumberUtil.doubleToString(
								em.getTarifa(), ConstanteUtil.separadorPunto, ConstanteUtil.formato2Decimal) : "0.00")
						+ ","
						+ ((em.getSubtotal() != null) ? NumberUtil.doubleToString(em.getSubtotal(),
								ConstanteUtil.separadorPunto, ConstanteUtil.formato2Decimal) : "0.00")
						+ "," + ((em.getFlgActivo() != null) ? em.getFlgActivo() : "0") + "|";

			}
			honorarioDetalle = honorarioDetalle.substring(0, honorarioDetalle.length() - 1);

			log.info("[REGISTRAR HONORARIO][SERVICE][CONVERSION HONORARIO DETALLE][" + honorarioDetalle + "]");
		}
		v.setHonorarioDetalle(honorarioDetalle);
	}

	@Override
	public OutResponse<FileResponse> reporteXlsxListarHonorario(@RequestBody HonorarioBuscarRequest req) {
		OutResponse<List<HonorarioResponse>> out = honorarioDao.listarHonorario(req);

		OutResponse<FileResponse> outF = new OutResponse<>();
		if (out.getrCodigo() == 0) {
			try {
				String[] columns = { "Nro", "Personal", "Monto", "Fecha inicio calculo", "Fecha fin calculo", "Fecha" };

				Workbook workbook = new HSSFWorkbook();
				ByteArrayOutputStream stream = new ByteArrayOutputStream();

				Sheet sheet = workbook.createSheet("Honorario personal");
				Row row = sheet.createRow(0);

				for (int i = 0; i < columns.length; i++) {
					Cell cell = row.createCell(i);
					cell.setCellValue(columns[i]);
				}

				int initRow = 1;
				for (HonorarioResponse ir : out.getrResult()) {
					row = sheet.createRow(initRow);
					row.createCell(0).setCellValue(initRow);
					row.createCell(1)
							.setCellValue(ir.getPersonal().getPersona().getNombre() + " "
									+ ir.getPersonal().getPersona().getApePaterno() + " "
									+ ((ir.getPersonal().getPersona().getApeMaterno() != null)
											? ir.getPersonal().getPersona().getApeMaterno()
											: ""));
					row.createCell(2).setCellValue(ir.getMonto());
					row.createCell(3).setCellValue(DateUtil.formatSlashDDMMYYYY(ir.getFechaInicio()));
					row.createCell(4).setCellValue(DateUtil.formatSlashDDMMYYYY(ir.getFechaFin()));
					row.createCell(5).setCellValue(DateUtil.formatSlashDDMMYYYY(ir.getFecha()));

					initRow++;
				}

				workbook.write(stream);
				workbook.close();

				FileResponse resp = new FileResponse();
				resp.setNombre("Reporte Honorario.xls");
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
