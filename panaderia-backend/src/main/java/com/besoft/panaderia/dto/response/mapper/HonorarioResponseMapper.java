package com.besoft.panaderia.dto.response.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.besoft.panaderia.dto.response.HonorarioResponse;

public class HonorarioResponseMapper implements RowMapper<HonorarioResponse> {

	@Override
	public HonorarioResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
		HonorarioResponse p = new HonorarioResponse();
		p.setId(rs.getLong("ID"));
		p.getPersonal().setId(rs.getLong("ID_PERSONAL"));
		p.getPersonal().getPersona().setNombre(rs.getString("NOMBRE"));
		p.getPersonal().getPersona().setApePaterno(rs.getString("APE_PATERNO"));
		p.getPersonal().getPersona().setApeMaterno(rs.getString("APE_MATERNO"));
		p.setMonto(rs.getDouble("MONTO"));
		p.setFechaInicio(rs.getDate("FECHA_INICIO"));
		p.setFechaFin(rs.getDate("FECHA_FIN"));
		p.setFecha(rs.getDate("FECHA"));
		p.setFlgActivo(rs.getInt("FLG_ACTIVO"));
		p.setIdUsuarioCrea(rs.getLong("ID_USUARIO_CREA"));
		p.setFecUsuarioCrea(rs.getDate("FEC_USUARIO_CREA"));
		p.setIdUsuarioMod(rs.getLong("ID_USUARIO_MOD"));
		p.setFecUsuarioMod(rs.getDate("FEC_USUARIO_MOD"));
		return p;
	}

}
