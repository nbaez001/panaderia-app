package com.besoft.panaderia.dto.response.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.besoft.panaderia.dto.response.ComprobanteResponse;

public class ComprobanteResponseMapper implements RowMapper<ComprobanteResponse> {

	@Override
	public ComprobanteResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
		ComprobanteResponse p = new ComprobanteResponse();
		p.setId(rs.getLong("ID"));
		p.setIdtTipoComprobante(rs.getLong("IDT_TIPO_COMPROBANTE"));
		p.setNomTipoComprobante(rs.getString("NOM_TIPO_COMPROBANTE"));
		p.setNombre(rs.getString("NOMBRE"));
		p.setSerie(rs.getInt("SERIE"));
		p.setNumero(rs.getInt("NUMERO"));
		p.setLongSerie(rs.getInt("LONG_SERIE"));
		p.setLongNumero(rs.getInt("LONG_NUMERO"));
		p.setFlgActual(rs.getInt("FLG_ACTUAL"));
		p.setFlgActivo(rs.getInt("FLG_ACTIVO"));
		p.setFlgUsado(rs.getInt("FLG_USADO"));
		p.setIdUsuarioCrea(rs.getLong("ID_USUARIO_CREA"));
		p.setFecUsuarioCrea(rs.getDate("FEC_USUARIO_CREA"));
		p.setIdUsuarioMod(rs.getLong("ID_USUARIO_MOD"));
		p.setFecUsuarioMod(rs.getDate("FEC_USUARIO_MOD"));
		return p;
	}

}
