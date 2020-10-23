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
import com.besoft.panaderia.dto.request.DetalleVentaRequest;
import com.besoft.panaderia.dto.request.VentaBuscarRequest;
import com.besoft.panaderia.dto.request.VentaRequest;
import com.besoft.panaderia.dto.response.DetalleVentaResponse;
import com.besoft.panaderia.dto.response.OutResponse;
import com.besoft.panaderia.dto.response.VentaResponse;
import com.besoft.panaderia.service.VentaService;
import com.besoft.panaderia.util.BillPrintable;
import com.besoft.panaderia.util.BillUtil;
import com.besoft.panaderia.util.ConstanteUtil;
import com.besoft.panaderia.util.NumberUtil;

@Service
public class VentaServiceImpl implements VentaService {

	Logger log = LoggerFactory.getLogger(VentaServiceImpl.class);

	@Autowired
	VentaDao ventaDao;

	@Override
	public OutResponse<VentaResponse> registrarVenta(VentaRequest c) {
		log.info("[REGISTRAR VENTA][SERVICE][INICIO]");
		c.setDetalleVenta(convertirDetalleACadena(c));
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
	public OutResponse<?> imprimirTicketVenta(VentaRequest c) {
		log.info("[IMPRIMIR TICKET VENTA][SERVICE][INICIO]");
		
		OutResponse<List<DetalleVentaResponse>> out = ventaDao.imprimirTicketVenta(c);
		if (out.getrCodigo() == 0) {
			for (DetalleVentaResponse dv : out.getrResult()) {
				PrinterJob pj = PrinterJob.getPrinterJob();
				BillPrintable printable = new BillPrintable(dv);
				pj.setPrintable(printable, new BillUtil().getPageFormat(pj));
				try {
					pj.print();
				} catch (PrinterException ex) {
					log.info("[IMPRIMIR TICKET VENTA][SERVICE][EXCEPTION]");
					log.info(ExceptionUtils.getStackTrace(ex));
				}
			}
		}
		
		log.info("[IMPRIMIR TICKET VENTA][SERVICE][FIN]");
		return out;
	}

	@Override
	public OutResponse<List<VentaResponse>> listarVenta(VentaBuscarRequest req) {
		return ventaDao.listarVenta(req);
	}

	public String convertirDetalleACadena(VentaRequest v) {
		String detalleVenta = "";
		if (v.getListaDetalleVenta().size() > 0) {
			for (DetalleVentaRequest em : v.getListaDetalleVenta()) {
				detalleVenta = detalleVenta 
						+ ((em.getIdProducto() != null) ? em.getIdProducto() : "0") + ","
						+ ((em.getCantidad() != null) ? NumberUtil.doubleToString(em.getCantidad(), ConstanteUtil.separadorPunto, ConstanteUtil.formato1Decimal) : "0.0") + "," 
						+ ((em.getPrecio() != null) ? NumberUtil.doubleToString(em.getPrecio(), ConstanteUtil.separadorPunto, ConstanteUtil.formato2Decimal) : "0.00") + ","
						+ ((em.getSubtotal() != null) ? NumberUtil.doubleToString(em.getSubtotal(), ConstanteUtil.separadorPunto, ConstanteUtil.formato2Decimal)  : "0.00") + ","
						+ ((em.getFlagActivo() != null) ? em.getFlagActivo() : "0") + "|";

			}
			detalleVenta = detalleVenta.substring(0, detalleVenta.length() - 1);

			log.info("[REGISTRAR VENTA][SERVICE][CONVERSION DETALLE VENTA][" + detalleVenta + "]");
		}
		return detalleVenta;
	}

}
