package com.besoft.panaderia.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestBody;

import com.besoft.panaderia.dao.ReporteDao;
import com.besoft.panaderia.dto.request.ReporteInsumoBuscarRequest;
import com.besoft.panaderia.dto.request.ReporteVentaBuscarRequest;
import com.besoft.panaderia.dto.response.FileResponse;
import com.besoft.panaderia.dto.response.OutResponse;
import com.besoft.panaderia.dto.response.ReporteInsumoResponse;
import com.besoft.panaderia.dto.response.ReporteVentaResponse;
import com.besoft.panaderia.service.ReporteService;

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

	@Value("${reportes.pdf.pathReportes}")
	String pathReportes;

	@Value("${reportes.pdf.resumenHonorario}")
	String resumenHonorario;

	@Value("${reportes.pdf.resumenHonorarioDet}")
	String resumenHonorarioDet;

	@Value("${reportes.pdf.reporteInsumosMensual}")
	String reporteInsumosMensual;

	@Value("${reportes.pdf.reporteInsumosMensualDetalle}")
	String reporteInsumosMensualDetalle;

	@Value("${reportes.pdf.reporteInsumosDiario}")
	String reporteInsumosDiario;

	@Value("${reportes.pdf.reporteInsumosDiarioDetalle}")
	String reporteInsumosDiarioDetalle;

	@Value("${reportes.pdf.reporteVentasMensual}")
	String reporteVentasMensual;

	@Value("${reportes.pdf.reporteVentasMensualDetalle}")
	String reporteVentasMensualDetalle;

	@Value("${reportes.pdf.reporteVentasDiario}")
	String reporteVentasDiario;

	@Value("${reportes.pdf.reporteVentasDiarioDetalle}")
	String reporteVentasDiarioDetalle;

	public void copiarReportes() {
		log.info("[REPORTE][COPIAR REPORTES][INICIO]");
		File file = new File("D:/app-panaderia");
		if (!file.exists()) {
			if (file.mkdir()) {
				crearReportes();
			} else {
				log.info("[REPORTE][ERROR AL COPIAR ARCHIVOS]");
			}
		} else {
			crearReportes();
		}
		log.info("[REPORTE][COPIAR REPORTES][FIN]");
	}

	public void crearReportes() {
		File resources;
		try {
			resources = ResourceUtils.getFile("classpath:reports");
			if (resources.exists()) {
				File files[] = resources.listFiles();
				for (File original : files) {
					String name = original.getName();
					File copied = new File("D:/app-panaderia/" + name);
					if (!copied.exists()) {
						InputStream targetStream = new FileInputStream(original);

						FileUtils.copyInputStreamToFile(targetStream, copied);
						log.info("[REPORTE][EXITO AL COPIAR ARCHIVO]");
					}
				}
			} else {
				log.info("[REPORTE][ERROR RESOURCES NO EXISTE]");
			}
		} catch (IOException e) {
			log.info("[REPORTE][" + e.getMessage() + "]");
			log.info(ExceptionUtils.getStackTrace(e));
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
			String pathSubReporte = "";

			if (req.getIdTipoReporte() == 1L) {
				pathReporte = pathReportes + reporteInsumosDiario;
				pathSubReporte = pathReportes + reporteInsumosDiarioDetalle;
			} else {
				pathReporte = pathReportes + reporteInsumosMensual;
				pathSubReporte = pathReportes + reporteInsumosMensualDetalle;
			}
			Map<String, Object> params = new HashMap<>();
			params.put("idPersonal", req.getIdPersonal());
			params.put("idTipoInsumo", req.getIdTipoInsumo());
			params.put("fecInicio", req.getFecInicio());
			params.put("fecFin", req.getFecFin());
			params.put("user", req.getUser());
			params.put("SUBREPORT_DIR", pathSubReporte);

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
			String pathSubReporte = "";

			if (req.getIdTipoReporte() == 1L) {
				pathReporte = pathReportes + reporteVentasDiario;
				pathSubReporte = pathReportes + reporteVentasDiarioDetalle;
			} else {
				pathReporte = pathReportes + reporteVentasMensual;
				pathSubReporte = pathReportes + reporteVentasMensualDetalle;
			}
			Map<String, Object> params = new HashMap<>();
			params.put("idProducto", req.getIdProducto());
			params.put("fecInicio", req.getFecInicio());
			params.put("fecFin", req.getFecFin());
			params.put("user", req.getUser());
			params.put("SUBREPORT_DIR", pathSubReporte);

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
