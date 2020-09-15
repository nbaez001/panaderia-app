package com.besoft.panaderia.dto.response.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.besoft.panaderia.dto.response.UsuarioResponse;

public class UsuarioMapper implements RowMapper<UsuarioResponse> {

	@Override
	public UsuarioResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
		UsuarioResponse o = new UsuarioResponse();
		o.setId(rs.getLong("ID"));
		o.setNombre(rs.getString("NOMBRE"));
		o.setApePaterno(rs.getString("APE_PATERNO"));
		o.setApeMaterno(rs.getString("APE_MATERNO"));
		o.setUsername(rs.getString("USERNAME"));
		o.setPassword(rs.getString("PASSWORD"));
		o.setEmail(rs.getString("EMAIL"));
		o.setTelefono(rs.getString("TELEFONO"));
		return o;
	}
}
