package com.besoft.panaderia.dto.response.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.besoft.panaderia.dto.response.RolResponse;

public class RolResponseMapper implements RowMapper<RolResponse> {

	@Override
	public RolResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
		RolResponse o = new RolResponse();
		o.setId(rs.getLong("ID"));
		o.setNombre(rs.getString("NOMBRE"));
		return o;
	}

}