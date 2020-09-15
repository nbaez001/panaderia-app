package com.besoft.panaderia.dto.response.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.besoft.panaderia.dto.response.MaestraResponse;

public class MaestraResponseMapper implements RowMapper<MaestraResponse> {

	@Override
	public MaestraResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
		MaestraResponse p = new MaestraResponse();
		p.setId(rs.getLong("ID"));
		p.setIdMaestra(rs.getLong("ID_MAESTRA"));
		p.setIdTabla(rs.getInt("ID_TABLA"));
		p.setIdItem(rs.getInt("ID_ITEM"));
		p.setOrden(rs.getInt("ORDEN"));
		p.setCodigo(rs.getString("CODIGO"));
		p.setNombre(rs.getString("NOMBRE"));
		p.setValor(rs.getString("VALOR"));
		p.setDescripcion(rs.getString("DESCRIPCION"));
		p.setFlagActivo(rs.getInt("FLAG_ACTIVO"));
		p.setIdUsuarioCrea(rs.getLong("ID_USUARIO_CREA"));
		p.setFecUsuarioCrea(rs.getDate("FEC_USUARIO_CREA"));
		p.setIdUsuarioMod(rs.getLong("ID_USUARIO_MOD"));
		p.setFecUsuarioMod(rs.getDate("FEC_USUARIO_MOD"));
		return p;
	}

}
