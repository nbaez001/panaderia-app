package com.besoft.panaderia.dto.response.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.besoft.panaderia.dto.response.DetalleVentaResponse;

public class DetalleVentaResponseMapper implements RowMapper<DetalleVentaResponse> {

	public DetalleVentaResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
		DetalleVentaResponse c = new DetalleVentaResponse();
		c.setId(rs.getLong("ID"));
		c.setIdProducto(rs.getLong("ID_PRODUCTO"));
		c.setNomProducto(rs.getString("NOM_PRODUCTO"));
		c.setIdVenta(rs.getLong("ID_VENTA"));
		c.setCantidad(rs.getDouble("CANTIDAD"));
		c.setPrecio(rs.getDouble("PRECIO"));
		c.setSubtotal(rs.getDouble("SUBTOTAL"));
		c.setFlagActivo(rs.getInt("FLG_ACTIVO"));
		
		return c;
	}

}
