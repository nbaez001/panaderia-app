package com.besoft.panaderia.dto.response.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.besoft.panaderia.dto.response.ReporteVentaResponse;
import com.besoft.panaderia.util.DateUtil;

public class ReporteVentaResponseMapper implements RowMapper<ReporteVentaResponse> {

	@Override
	public ReporteVentaResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
		ReporteVentaResponse p = new ReporteVentaResponse();
		if (rs.getInt("ID_TIPO_REPORTE") == 2) {
			p.setAnio(rs.getString("ANIO"));
			p.setMes(rs.getString("MES"));
			p.setFecha(rs.getString("FECHA"));
		} else {
			p.setFecha(DateUtil.formatSlashDDMMYYYY(rs.getDate("FECHA")));
		}
		p.setIdProducto(rs.getLong("ID_PRODUCTO"));
		p.setNomProducto(rs.getString("NOM_PRODUCTO"));
		p.setNomUnidadMedida(rs.getString("NOM_UNIDAD_MEDIDA"));
		p.setCantidad(rs.getDouble("CANTIDAD"));
		p.setSuma(rs.getDouble("SUMA"));
		return p;
	}

}
