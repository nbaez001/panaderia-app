package com.besoft.panaderia.dto.response.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.besoft.panaderia.dto.response.PaisResponse;

public class PaisResponseMapper implements RowMapper<PaisResponse> {

	@Override
	public PaisResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
		PaisResponse p = new PaisResponse();
		p.setId(rs.getInt("ID"));
		p.setNombre(rs.getString("NOMBRE"));
		p.setCodigo(rs.getString("CODIGO"));
		p.setFlgActivo(rs.getInt("FLG_ACTIVO"));
		p.setIdUsuarioCrea(rs.getLong("ID_USUARIO_CREA"));
		p.setFecUsuarioCrea(rs.getDate("FEC_USUARIO_CREA"));
		p.setIdUsuarioMod(rs.getLong("ID_USUARIO_MOD"));
		p.setFecUsuarioMod(rs.getDate("FEC_USUARIO_MOD"));
		return p;
	}

}
