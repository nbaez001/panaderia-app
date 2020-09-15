package com.besoft.panaderia.dto.response.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.besoft.panaderia.dto.response.VentaResponse;

public class VentaResponseMapper implements RowMapper<VentaResponse> {

	public VentaResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
		VentaResponse c = new VentaResponse();
		c.setId(rs.getLong("ID"));
		c.setSerie(rs.getString("SERIE"));
		c.setNumero(rs.getString("NUMERO"));
		c.setTotal(rs.getDouble("TOTAL"));
		c.setFlgActivo(rs.getInt("FLG_ACTIVO"));
		c.setIdUsuarioCrea(rs.getLong("ID_USUARIO_CREA"));
		c.setFecUsuarioCrea(rs.getDate("FEC_USUARIO_CREA"));
		c.setIdUsuarioMod(rs.getLong("ID_USUARIO_MOD"));
		c.setFecUsuarioMod(rs.getDate("FEC_USUARIO_MOD"));

		return c;
	}

}
