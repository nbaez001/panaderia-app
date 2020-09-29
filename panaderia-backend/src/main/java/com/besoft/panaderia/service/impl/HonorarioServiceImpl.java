package com.besoft.panaderia.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestBody;

import com.besoft.panaderia.dao.HonorarioDao;
import com.besoft.panaderia.dto.request.HonorarioBuscarRequest;
import com.besoft.panaderia.dto.request.HonorarioPeriodoRequest;
import com.besoft.panaderia.dto.request.HonorarioRequest;
import com.besoft.panaderia.dto.response.FileResponse;
import com.besoft.panaderia.dto.response.HonorarioPeriodoResponse;
import com.besoft.panaderia.dto.response.HonorarioResponse;
import com.besoft.panaderia.dto.response.OutResponse;
import com.besoft.panaderia.service.HonorarioService;
import com.besoft.panaderia.util.ConexionUtil;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;

@Service
public class HonorarioServiceImpl implements HonorarioService {

	@Autowired
	ConexionUtil conexionUtil;

	@Autowired
	HonorarioDao honorarioDao;

	@Override
	public OutResponse<List<HonorarioResponse>> listarHonorario(HonorarioBuscarRequest req) {
		return honorarioDao.listarHonorario(req);
	}

	@Override
	public OutResponse<HonorarioResponse> registrarHonorario(HonorarioRequest c) {
		return honorarioDao.registrarHonorario(c);
	}

	@Override
	public OutResponse<HonorarioPeriodoResponse> buscarPeriodoHonorario(HonorarioPeriodoRequest req) {
		return honorarioDao.buscarPeriodoHonorario(req);
	}

	@Override
	public OutResponse<FileResponse> reporteHonorario(@RequestBody HonorarioRequest req) {
		OutResponse<FileResponse> out = new OutResponse<>();

		JRPdfExporter exporter = new JRPdfExporter();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		try {
			File file = ResourceUtils.getFile("classpath:reports/resumenHonorario.jasper");
			File fileDet = ResourceUtils.getFile("classpath:reports/resumenHonorario_detalle.jasper");
//			String applicationPath = request.getServletContext().getRealPath("");

			Map<String, Object> params = new HashMap<>();
			params.put("idHonorario", req.getId());
			params.put("SUBREPORT_DIR", fileDet.getAbsolutePath());

			JasperPrint jp = JasperFillManager.fillReport(file.getAbsolutePath(), params, conexionUtil.getConexion());
			conexionUtil.cerrarConexion();

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
		} catch (JRException | FileNotFoundException e) {
			System.out.println("Unable to process download");
			out.setrCodigo(500);
			out.setrMensaje(e.getMessage());
			out.setrResult(null);
		}
		return out;
	}
//
//	public byte[] generateJasperReportPDF(HttpServletRequest httpServletRequest, String jasperReportName,
//			ByteArrayOutputStream outputStream, Map parametros) {
//		JRPdfExporter exporter = new JRPdfExporter();
//		try {
//			String reportLocation = httpServletRequest.getRealPath("/") + "resources\\jasper\\" + jasperReportName
//					+ ".jrxml";
//
//			InputStream jrxmlInput = new FileInputStream(new File(reportLocation));
//			// this.getClass().getClassLoader().getResource("data.jrxml").openStream();
//			JasperDesign design = JRXmlLoader.load(jrxmlInput);
//			JasperReport jasperReport = JasperCompileManager.compileReport(design);
//			// System.out.println("Report compiled");
//
//			// JasperReport jasperReport =
//			// JasperCompileManager.compileReport(reportLocation);
//			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros,
//					HibernateUtils.currentSession().connection()); // datasource Service
//
//			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
//			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outputStream);
//			exporter.exportReport();
//		} catch (Exception e) {
//			e.printStackTrace();
//			System.out.println("Error in generate Report..." + e);
//		} finally {
//		}
//		return outputStream.toByteArray();
//	}

}
