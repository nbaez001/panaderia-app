package com.besoft.panaderia.service.impl;

import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.besoft.panaderia.dao.VentaDao;
import com.besoft.panaderia.dto.request.VentaRequest;
import com.besoft.panaderia.dto.response.ApiOutResponse;
import com.besoft.panaderia.dto.response.DetalleVentaResponse;
import com.besoft.panaderia.dto.response.VentaResponse;
import com.besoft.panaderia.service.VentaService;
import com.besoft.panaderia.util.BillPrintable;
import com.besoft.panaderia.util.BillUtil;

@Service
public class VentaServiceImpl implements VentaService {

	@Autowired
	VentaDao ventaDao;

	@Override
	public ApiOutResponse<VentaResponse> registrarVenta(VentaRequest c) {
		ApiOutResponse<VentaResponse> out = ventaDao.registrarVenta(c);
		if (out.getrCodigo() == 0) {
			for (DetalleVentaResponse dv : out.getResult().getListaDetalleVenta()) {
				PrinterJob pj = PrinterJob.getPrinterJob();
				BillPrintable printable = new BillPrintable(dv);
				pj.setPrintable(printable, new BillUtil().getPageFormat(pj));
				try {
					pj.print();
				} catch (PrinterException ex) {
					ex.printStackTrace();
				}
			}
		}

		return out;
	}

	@Override
	public ApiOutResponse<List<VentaResponse>> listarVenta() {
		return ventaDao.listarVenta();
	}

}
