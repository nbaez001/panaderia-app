package com.besoft.panaderia.dto.response.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.springframework.jdbc.core.RowMapper;

import com.besoft.panaderia.dto.response.PermisoResponse;

public class PermisorResponseMapper implements RowMapper<PermisoResponse> {

	@Override
	public PermisoResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
		PermisoResponse o = new PermisoResponse();
		o.setId(rs.getLong("ID"));
		o.setIdPadre(rs.getLong("ID_PADRE"));
		o.setNombre(rs.getString("NOMBRE"));
		o.setRuta(rs.getString("RUTA"));
		o.setOrden(rs.getInt("ORDEN"));
		o.setListaPermiso(new ArrayList<>());
		return o;
	}

}
