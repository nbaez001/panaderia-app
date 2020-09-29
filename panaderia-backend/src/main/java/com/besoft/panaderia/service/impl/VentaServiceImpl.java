package com.besoft.panaderia.service.impl;

import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.besoft.panaderia.dao.VentaDao;
import com.besoft.panaderia.dto.request.VentaRequest;
import com.besoft.panaderia.dto.response.OutResponse;
import com.besoft.panaderia.dto.response.DetalleVentaResponse;
import com.besoft.panaderia.dto.response.VentaResponse;
import com.besoft.panaderia.service.VentaService;
import com.besoft.panaderia.util.BillPrintable;
import com.besoft.panaderia.util.BillUtil;

@Service
public class VentaServiceImpl implements VentaService {

	Logger log = LoggerFactory.getLogger(VentaServiceImpl.class);

	@Autowired
	VentaDao ventaDao;

	@Override
	public OutResponse<VentaResponse> registrarVenta(VentaRequest c) {
		log.info("[REGISTRAR VENTA][SERVICE][INICIO]");
		OutResponse<VentaResponse> out = ventaDao.registrarVenta(c);
		if (out.getrCodigo() == 0) {
			for (DetalleVentaResponse dv : out.getrResult().getListaDetalleVenta()) {
				PrinterJob pj = PrinterJob.getPrinterJob();
				BillPrintable printable = new BillPrintable(dv);
				pj.setPrintable(printable, new BillUtil().getPageFormat(pj));
				try {
					pj.print();
				} catch (PrinterException ex) {
					log.info("[REGISTRAR VENTA][SERVICE][EXCEPTION]");
					log.info(ExceptionUtils.getStackTrace(ex));
				}
			}
		}
		log.info("[REGISTRAR VENTA][SERVICE][FIN]");

		return out;
	}

	@Override
	public void imprimirVenta() {
		log.info("[REGISTRAR VENTA][SERVICE][INICIO]");
		DetalleVentaResponse dv = new DetalleVentaResponse();
		dv.setCantidad(14.0);
		dv.setFlagActivo(1);
		dv.setId(1L);
		dv.setIdProducto(1L);
		dv.setNomProducto("CHAPLA");
		dv.setPrecio(0.2);
		dv.setSubtotal(7.0);
		
		PrinterJob pj = PrinterJob.getPrinterJob();
		BillPrintable printable = new BillPrintable(dv);
		pj.setPrintable(printable, new BillUtil().getPageFormat(pj));
		try {
			pj.print();
		} catch (PrinterException ex) {
			log.info("[REGISTRAR VENTA][SERVICE][EXCEPTION]");
			log.info(ExceptionUtils.getStackTrace(ex));
		}
		log.info("[REGISTRAR VENTA][SERVICE][FIN]");
	}

	@Override
	public OutResponse<List<VentaResponse>> listarVenta() {
		return ventaDao.listarVenta();
	}

}
