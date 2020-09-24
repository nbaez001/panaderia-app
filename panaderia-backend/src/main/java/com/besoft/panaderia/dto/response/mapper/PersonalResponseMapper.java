package com.besoft.panaderia.dto.response.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.besoft.panaderia.dto.response.PersonaResponse;
import com.besoft.panaderia.dto.response.PersonalResponse;

public class PersonalResponseMapper implements RowMapper<PersonalResponse> {

	@Override
	public PersonalResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
		PersonalResponse p = new PersonalResponse();
		p.setId(rs.getLong("ID"));
		p.setCargo(rs.getString("CARGO"));
		p.setFlgActivo(rs.getInt("FLG_ACTIVO"));
		p.setIdUsuarioCrea(rs.getLong("ID_USUARIO_CREA"));
		p.setFecUsuarioCrea(rs.getDate("FEC_USUARIO_CREA"));
		p.setIdUsuarioMod(rs.getLong("ID_USUARIO_MOD"));
		p.setFecUsuarioMod(rs.getDate("FEC_USUARIO_MOD"));

		PersonaResponse pe = new PersonaResponse();
		pe.setId(rs.getLong("ID_PERSONA"));
		pe.setIdtTipoDocumento(rs.getLong("IDT_TIPO_DOCUMENTO"));
		pe.setNomTipoDocumento(rs.getString("NOM_TIPO_DOCUMENTO"));
		pe.setIdtSexo(rs.getLong("IDT_SEXO"));
		pe.setNomSexo(rs.getString("NOM_SEXO"));
		pe.setIdPais(rs.getInt("ID_PAIS"));
		pe.setNomPais(rs.getString("NOM_PAIS"));
		pe.setIdDepartamento(rs.getInt("ID_DEPARTAMENTO"));
		pe.setNomDepartamento(rs.getString("NOM_DEPARTAMENTO"));
		pe.setIdProvincia(rs.getInt("ID_PROVINCIA"));
		pe.setNomProvincia(rs.getString("NOM_PROVINCIA"));
		pe.setIdDistrito(rs.getInt("ID_DISTRITO"));
		pe.setNomDistrito(rs.getString("NOM_DISTRITO"));
		pe.setNombre(rs.getString("NOMBRE"));
		pe.setApePaterno(rs.getString("APE_PATERNO"));
		pe.setApeMaterno(rs.getString("APE_MATERNO"));
		pe.setNroDocumento(rs.getString("NRO_DOCUMENTO"));
		pe.setDireccionDomicilio(rs.getString("DIRECCION_DOMICILIO"));
		pe.setFecNacimiento(rs.getDate("FEC_NACIMIENTO"));

		p.setPersona(pe);

		return p;
	}

}
