package com.besoft.panaderia.dto.response.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.besoft.panaderia.dto.response.DepartamentoResponse;

public class DepartamentoResponseMapper implements RowMapper<DepartamentoResponse> {

	@Override
	public DepartamentoResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
		DepartamentoResponse p = new DepartamentoResponse();
		p.setId(rs.getInt("ID"));
		p.setIdPais(rs.getInt("ID_PAIS"));
		p.setNombre(rs.getString("NOMBRE"));
		p.setUbigeo(rs.getString("UBIGEO"));
		p.setUbigeoReniec(rs.getString("UBIGEO_RENIEC"));
		p.setFlgActivo(rs.getInt("FLG_ACTIVO"));
		p.setIdUsuarioCrea(rs.getLong("ID_USUARIO_CREA"));
		p.setFecUsuarioCrea(rs.getDate("FEC_USUARIO_CREA"));
		p.setIdUsuarioMod(rs.getLong("ID_USUARIO_MOD"));
		p.setFecUsuarioMod(rs.getDate("FEC_USUARIO_MOD"));
		return p;
	}

}
