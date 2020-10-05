package com.besoft.panaderia.service.impl;

import java.io.ByteArrayOutputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.besoft.panaderia.dao.ReporteDao;
import com.besoft.panaderia.dto.request.HonorarioRequest;
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

	@Value("${reportes.reporteInsumosMensual}")
	String reporteInsumosMensual;

	@Value("${reportes.reporteInsumosMensualDetalle}")
	String reporteInsumosMensualDetalle;

	@Value("${reportes.reporteInsumosDiario}")
	String reporteInsumosDiario;

	@Value("${reportes.reporteInsumosDiarioDetalle}")
	String reporteInsumosDiarioDetalle;

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
				pathReporte = reporteInsumosDiario;
				pathSubReporte = reporteInsumosDiarioDetalle;
			} else {
				pathReporte = reporteInsumosMensual;
				pathSubReporte = reporteInsumosMensualDetalle;
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
}
