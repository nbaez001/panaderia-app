package com.besoft.panaderia.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.besoft.panaderia.dao.ReporteDao;
import com.besoft.panaderia.dto.request.ReporteInsumoBuscarRequest;
import com.besoft.panaderia.dto.request.ReporteVentaBuscarRequest;
import com.besoft.panaderia.dto.response.FileResponse;
import com.besoft.panaderia.dto.response.OutResponse;
import com.besoft.panaderia.dto.response.ReporteInsumoResponse;
import com.besoft.panaderia.dto.response.ReporteVentaResponse;
import com.besoft.panaderia.service.ReporteService;
import com.besoft.panaderia.util.ReportProperties;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;

@Service
public class ReporteServiceImpl implements ReporteService {

	Logger log = LoggerFactory.getLogger(ReporteServiceImpl.class);

	@Autowired
	DataSource dataSource;

	@Autowired
	ReporteDao reporteDao;

	@Autowired
	ReportProperties reportProperties;

	@Override
	public OutResponse<?> validarReportes() {
		OutResponse<?> out = new OutResponse<>();
		log.info("[REPORTE SERVICE][VALIDAR REPORTES][INICIO]");
		File file = new File(reportProperties.getRuta());
		if (!file.exists()) {
			if (file.mkdir()) {
				out = crearReportes();
			} else {
				log.info("[REPORTE SERVICE][VALIDAR REPORTES][ERROR AL COPIAR ARCHIVOS]");
				out.setrCodigo(-1);
				out.setrMensaje("Error al crear carpeta en: " + reportProperties.getRuta());
			}
		} else {
			out = crearReportes();
		}
		log.info("[REPORTE SERVICE][VALIDAR REPORTES][FIN]");
		return out;
	}

	public OutResponse<?> crearReportes() {
		Map<String, String> map = reportProperties.getFiles();
		try {
			for (Map.Entry<String, String> entry : map.entrySet()) {
				File copied = new File(reportProperties.getRuta() + reportProperties.getFiles().get(entry.getKey()));
				if (!copied.exists()) {
					InputStream targetStream = this.getClass().getClassLoader()
							.getResourceAsStream(reportProperties.getFiles().get(entry.getKey()));
					if (targetStream != null) {
						FileUtils.copyInputStreamToFile(targetStream, copied);
						log.info("[REPORTE SERVICE][VALIDAR REPORTES][GENERADO]["
								+ reportProperties.getFiles().get(entry.getKey()) + "]");
					}
				}
			}
			return new OutResponse<>(0, "ARCHIVOS DE REPORTES SE GENERARON CORRECTAMENTE");
		} catch (IOException e) {
			log.info("[REPORTE SERVICE][VALIDAR REPORTES][" + e.getMessage() + "]");
			log.info(ExceptionUtils.getStackTrace(e));
			return new OutResponse<>(500, e.getMessage());
		}
	}

	@Override
	public OutResponse<List<ReporteInsumoResponse>> listarReporteInsumo(@RequestBody ReporteInsumoBuscarRequest req) {
		return reporteDao.listarReporteInsumo(req);
	}

	@Override
	public OutResponse<List<ReporteVentaResponse>> listarReporteVenta(@RequestBody ReporteVentaBuscarRequest req) {
		return reporteDao.listarReporteVenta(req);
	}

	@Override
	public OutResponse<FileResponse> generarReporteInsumoPDF(ReporteInsumoBuscarRequest req) {
		log.info("[REPORTE HONORARIO][SERVICE][INICIO]");
		OutResponse<FileResponse> out = new OutResponse<>();

		JRPdfExporter exporter = new JRPdfExporter();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		try {
			String pathReporte = "";
			String pathReporteDet = "";

			if (req.getIdTipoReporte() == 1L) {
				pathReporte = reportProperties.getRuta() + reportProperties.getFiles().get("reporteInsumosDiario");
				pathReporteDet = reportProperties.getRuta()
						+ reportProperties.getFiles().get("reporteInsumosDiarioDetalle");
			} else {
				pathReporte = reportProperties.getRuta() + reportProperties.getFiles().get("reporteInsumosMensual");
				pathReporteDet = reportProperties.getRuta()
						+ reportProperties.getFiles().get("reporteInsumosMensualDetalle");
			}
//			JasperReport jr = JasperCompileManager.compileReport(fisReporteInsumoDet);

			Map<String, Object> params = new HashMap<>();
			params.put("idPersonal", req.getIdPersonal());
			params.put("idTipoInsumo", req.getIdTipoInsumo());
			params.put("fecInicio", req.getFecInicio());
			params.put("fecFin", req.getFecFin());
			params.put("user", req.getUser());
			params.put("SUBREPORT_DIR", pathReporteDet);

			log.info("[REPORTE HONORARIO][SERVICE][PARAMS-INPUT][" + params.toString() + "]");

			JasperPrint jp = JasperFillManager.fillReport(pathReporte, params, dataSource.getConnection());

			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jp);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outputStream);
			exporter.exportReport();

			FileResponse resp = new FileResponse();
			resp.setNombre("Reporte-insumos.pdf");
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

	@Override
	public OutResponse<FileResponse> generarReporteVentaPDF(ReporteVentaBuscarRequest req) {
		log.info("[REPORTE VENTAS][SERVICE][INICIO]");
		OutResponse<FileResponse> out = new OutResponse<>();

		JRPdfExporter exporter = new JRPdfExporter();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		try {
			String pathReporte = "";
			String pathReporteDet = "";

			if (req.getIdTipoReporte() == 1L) {
				pathReporte = reportProperties.getRuta() + reportProperties.getFiles().get("reporteVentasDiario");
				pathReporteDet = reportProperties.getRuta()
						+ reportProperties.getFiles().get("reporteVentasDiarioDetalle");
			} else {
				pathReporte = reportProperties.getRuta() + reportProperties.getFiles().get("reporteVentasMensual");
				pathReporteDet = reportProperties.getRuta()
						+ reportProperties.getFiles().get("reporteVentasMensualDetalle");
			}
			Map<String, Object> params = new HashMap<>();
			params.put("idProducto", req.getIdProducto());
			params.put("fecInicio", req.getFecInicio());
			params.put("fecFin", req.getFecFin());
			params.put("user", req.getUser());
			params.put("SUBREPORT_DIR", pathReporteDet);

			log.info("[REPORTE VENTAS][SERVICE][PARAMS-INPUT][" + params.toString() + "]");

			JasperPrint jp = JasperFillManager.fillReport(pathReporte, params, dataSource.getConnection());

			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jp);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outputStream);
			exporter.exportReport();

			FileResponse resp = new FileResponse();
			resp.setNombre("Reporte-insumos.pdf");
			resp.setType("application/pdf");
			resp.setData(outputStream.toByteArray());

			out.setrCodigo(0);
			out.setrMensaje("EXITO");
			out.setrResult(resp);
			log.info("[REPORTE VENTAS][SERVICE][EXITO]");
		} catch (JRException | SQLException e) {
			out.setrCodigo(500);
			out.setrMensaje(e.getMessage());
			out.setrResult(null);
			log.info("[REPORTE VENTAS][SERVICE][EXCEPTION]");
			log.info(ExceptionUtils.getStackTrace(e));
		}
		log.info("[REPORTE VENTAS][SERVICE][FIN]");
		return out;
	}
}
