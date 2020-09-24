package com.besoft.panaderia.dto.response.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.besoft.panaderia.dto.response.InsumoResponse;
import com.besoft.panaderia.dto.response.PersonaResponse;
import com.besoft.panaderia.dto.response.PersonalResponse;
import com.besoft.panaderia.dto.response.TipoInsumoResponse;

public class InsumoResponseMapper implements RowMapper<InsumoResponse> {

	@Override
	public InsumoResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
		InsumoResponse p = new InsumoResponse();
		p.setId(rs.getLong("ID"));
		p.setCantidad(rs.getDouble("CANTIDAD"));
		p.setFecha(rs.getDate("FECHA"));
		p.setFlgActivo(rs.getInt("FLG_ACTIVO"));
		p.setIdUsuarioCrea(rs.getLong("ID_USUARIO_CREA"));
		p.setFecUsuarioCrea(rs.getDate("FEC_USUARIO_CREA"));
		p.setIdUsuarioMod(rs.getLong("ID_USUARIO_MOD"));
		p.setFecUsuarioMod(rs.getDate("FEC_USUARIO_MOD"));
		
		TipoInsumoResponse ti = new  TipoInsumoResponse();
		ti.setId(rs.getLong("ID_TIPO_INSUMO"));
		ti.setNombre(rs.getString("NOMBRE_TI"));
		ti.setIdtUnidadMedida(rs.getLong("IDT_UNIDAD_MEDIDA_TI"));
		ti.setNomUnidadMedida(rs.getString("NOM_UNIDAD_MEDIDA"));
		
		PersonaResponse per = new PersonaResponse();
		per.setNombre(rs.getString("NOMBRE_PER"));
		per.setApePaterno(rs.getString("APE_PATERNO_PER"));
		per.setApeMaterno(rs.getString("APE_MATERNO_PER"));
		
		PersonalResponse pe= new PersonalResponse();
		pe.setId(rs.getLong("ID_PERSONAL"));
		pe.setPersona(per);
		
		p.setTipoInsumo(ti);
		p.setPersonal(pe);
		
		return p;
	}

}
