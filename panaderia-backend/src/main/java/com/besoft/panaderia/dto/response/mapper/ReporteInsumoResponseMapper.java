package com.besoft.panaderia.dto.response.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.besoft.panaderia.dto.response.ReporteInsumoResponse;
import com.besoft.panaderia.util.DateUtil;

public class ReporteInsumoResponseMapper implements RowMapper<ReporteInsumoResponse> {

	@Override
	public ReporteInsumoResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
		ReporteInsumoResponse p = new ReporteInsumoResponse();
		if (rs.getInt("ID_TIPO_REPORTE") == 2) {
			p.setAnio(rs.getString("ANIO"));
			p.setMes(rs.getString("MES"));
			p.setFecha(rs.getString("FECHA"));
		}else {
			p.setFecha(DateUtil.formatSlashDDMMYYYY(rs.getDate("FECHA")));
		}
		p.setIdPersonal(rs.getLong("ID_PERSONAL"));
		p.setNomPersonal(rs.getString("NOM_PERSONAL"));
		p.setIdTipoInsumo(rs.getLong("ID_TIPO_INSUMO"));
		p.setNomTipoInsumo(rs.getString("NOM_TIPO_INSUMO"));
		p.setNomUnidadMedida(rs.getString("NOM_UNIDAD_MEDIDA"));
		p.setSuma(rs.getDouble("SUMA"));
		return p;
	}

}
