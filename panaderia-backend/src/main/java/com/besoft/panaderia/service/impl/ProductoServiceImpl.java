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

import com.besoft.panaderia.dao.ProductoDao;
import com.besoft.panaderia.dto.request.ProductoBuscarRequest;
import com.besoft.panaderia.dto.request.ProductoRequest;
import com.besoft.panaderia.dto.response.FileResponse;
import com.besoft.panaderia.dto.response.OutResponse;
import com.besoft.panaderia.dto.response.ProductoResponse;
import com.besoft.panaderia.service.ProductoService;

@Service
public class ProductoServiceImpl implements ProductoService {

	Logger log = LoggerFactory.getLogger(ProductoServiceImpl.class);

	@Autowired
	ProductoDao productoDao;

	@Override
	public OutResponse<List<ProductoResponse>> listarProducto(ProductoBuscarRequest req) {
		return productoDao.listarProducto(req);
	}

	@Override
	public OutResponse<ProductoResponse> registrarProducto(ProductoRequest req) {
		return productoDao.registrarProducto(req);
	}

	@Override
	public OutResponse<ProductoResponse> modificarProducto(ProductoRequest req) {
		return productoDao.modificarProducto(req);
	}

	@Override
	public OutResponse<ProductoResponse> eliminarProducto(ProductoRequest req) {
		return productoDao.eliminarProducto(req);
	}

	@Override
	public OutResponse<FileResponse> reporteXlsxListarProducto(ProductoBuscarRequest req) {
		OutResponse<List<ProductoResponse>> out = productoDao.listarProducto(req);

		OutResponse<FileResponse> outF = new OutResponse<>();
		if (out.getrCodigo() == 0) {
			try {
				String[] columns = { "Nro", "Codigo", "Nombre", "Unidad medida", "Precio" };

				Workbook workbook = new HSSFWorkbook();
				ByteArrayOutputStream stream = new ByteArrayOutputStream();

				Sheet sheet = workbook.createSheet("Producto");
				Row row = sheet.createRow(0);

				for (int i = 0; i < columns.length; i++) {
					Cell cell = row.createCell(i);
					cell.setCellValue(columns[i]);
				}

				int initRow = 1;
				for (ProductoResponse ir : out.getrResult()) {
					row = sheet.createRow(initRow);
					row.createCell(0).setCellValue(initRow);
					row.createCell(1).setCellValue(ir.getCodigo());
					row.createCell(2).setCellValue(ir.getNombre());
					row.createCell(3).setCellValue(ir.getNomUnidadMedida());
					row.createCell(4).setCellValue(ir.getPrecio());

					initRow++;
				}

				workbook.write(stream);
				workbook.close();

				FileResponse resp = new FileResponse();
				resp.setNombre("Reporte Producto.xls");
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
