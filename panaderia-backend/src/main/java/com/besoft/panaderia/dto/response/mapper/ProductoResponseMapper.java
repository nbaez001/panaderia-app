package com.besoft.panaderia.dto.response.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.besoft.panaderia.dto.response.ProductoResponse;

public class ProductoResponseMapper implements RowMapper<ProductoResponse> {

	@Override
	public ProductoResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
		ProductoResponse p = new ProductoResponse();
		p.setId(rs.getLong("ID"));
		p.setIdtUnidadMedida(rs.getLong("IDT_UNIDAD_MEDIDA"));
		p.setNomUnidadMedida(rs.getString("NOM_UNIDAD_MEDIDA"));
		p.setNombre(rs.getString("NOMBRE"));
		p.setCodigo(rs.getString("CODIGO"));
		p.setPrecio(rs.getDouble("PRECIO"));
		p.setFlgActivo(rs.getInt("FLG_ACTIVO"));
		p.setIdUsuarioCrea(rs.getLong("ID_USUARIO_CREA"));
		p.setFecUsuarioCrea(rs.getDate("FEC_USUARIO_CREA"));
		p.setIdUsuarioMod(rs.getLong("ID_USUARIO_MOD"));
		p.setFecUsuarioMod(rs.getDate("FEC_USUARIO_MOD"));
		return p;
	}

}
