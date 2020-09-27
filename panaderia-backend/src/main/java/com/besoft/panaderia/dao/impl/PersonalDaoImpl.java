package com.besoft.panaderia.dao.impl;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import com.besoft.panaderia.dao.PersonalDao;
import com.besoft.panaderia.dto.request.PersonalBuscarRequest;
import com.besoft.panaderia.dto.request.PersonalRequest;
import com.besoft.panaderia.dto.response.OutResponse;
import com.besoft.panaderia.dto.response.PersonaResponse;
import com.besoft.panaderia.dto.response.PersonalResponse;
import com.besoft.panaderia.dto.response.mapper.PersonalResponseMapper;
import com.besoft.panaderia.util.ConstanteUtil;
import com.besoft.panaderia.util.DateUtil;

@Repository
public class PersonalDaoImpl implements PersonalDao {

	@Autowired
	DataSource dataSource;

	@Override
	public OutResponse<List<PersonalResponse>> listarPersonal(PersonalBuscarRequest req) {
		OutResponse<List<PersonalResponse>> outResponse = new OutResponse<>();

		List<PersonalResponse> lista = new ArrayList<>();
		Integer rCodigo = 0;
		String rMensaje = "";
		try {
			SimpleJdbcCall jdbcCall = new SimpleJdbcCall(dataSource).withSchemaName(ConstanteUtil.BD_SCHEMA)
					.withCatalogName(ConstanteUtil.BD_PCK_PERSONAL).withProcedureName("SP_L_PERSONAL")
					.returningResultSet(ConstanteUtil.R_LISTA, new PersonalResponseMapper());

			MapSqlParameterSource in = new MapSqlParameterSource();
			in.addValue("I_IDT_TIPO_DOCUMENTO", req.getIdtTipoDocumento(), Types.NUMERIC);
			in.addValue("I_NRO_DOCUMENTO", req.getNroDocumento(), Types.VARCHAR);
			in.addValue("I_NOMBRE", req.getNombre(), Types.VARCHAR);
			in.addValue("I_APE_PATERNO", req.getApePaterno(), Types.VARCHAR);
			in.addValue("I_APE_MATERNO", req.getApeMaterno(), Types.VARCHAR);

			Map<String, Object> out = jdbcCall.execute(in);

			rCodigo = Integer.parseInt(out.get(ConstanteUtil.R_CODIGO).toString());
			rMensaje = out.get(ConstanteUtil.R_MENSAJE).toString();

			if (rCodigo == ConstanteUtil.R_COD_EXITO) {// CONSULTA CORRECTA
				lista = (List<PersonalResponse>) out.get(ConstanteUtil.R_LISTA);
			} else {
				lista = null;
			}
			outResponse.setrCodigo(rCodigo);
			outResponse.setrMensaje(rMensaje);
			outResponse.setrResult(lista);
		} catch (Exception e) {
			outResponse.setrCodigo(500);
			outResponse.setrMensaje(e.getMessage());
			outResponse.setrResult(null);
		}
		return outResponse;
	}

	@Override
	public OutResponse<PersonalResponse> registrarPersonal(PersonalRequest req) {
		OutResponse<PersonalResponse> outResponse = new OutResponse<>();
		PersonalResponse res = new PersonalResponse();

		Integer rCodigo = 0;
		String rMensaje = "";

		Long id = 0L;
		Long idPersona = 0L;
		try {
			SimpleJdbcCall jdbcCall = new SimpleJdbcCall(dataSource).withSchemaName(ConstanteUtil.BD_SCHEMA)
					.withCatalogName(ConstanteUtil.BD_PCK_PERSONAL).withProcedureName("SP_I_PERSONAL");

			MapSqlParameterSource in = new MapSqlParameterSource();
			in.addValue("I_CARGO", req.getCargo(), Types.VARCHAR);
			in.addValue("I_IDT_TIPO_DOCUMENTO", req.getPersona().getIdtTipoDocumento(), Types.NUMERIC);
			in.addValue("I_IDT_SEXO", req.getPersona().getIdtSexo(), Types.NUMERIC);
			in.addValue("I_ID_PAIS", req.getPersona().getIdPais(), Types.NUMERIC);
			in.addValue("I_ID_DEPARTAMENTO", req.getPersona().getIdDepartamento(), Types.NUMERIC);
			in.addValue("I_ID_PROVINCIA", req.getPersona().getIdProvincia(), Types.NUMERIC);
			in.addValue("I_ID_DISTRITO", req.getPersona().getIdDistrito(), Types.NUMERIC);
			in.addValue("I_NOMBRE", req.getPersona().getNombre(), Types.VARCHAR);
			in.addValue("I_APE_PATERNO", req.getPersona().getApePaterno(), Types.VARCHAR);
			in.addValue("I_APE_MATERNO", req.getPersona().getApeMaterno(), Types.VARCHAR);
			in.addValue("I_NRO_DOCUMENTO", req.getPersona().getNroDocumento(), Types.VARCHAR);
			in.addValue("I_DIRECCION_DOMICILIO", req.getPersona().getDireccionDomicilio(), Types.VARCHAR);
			in.addValue("I_FEC_NACIMIENTO", DateUtil.formatSlashDDMMYYYY(req.getPersona().getFecNacimiento()),
					Types.VARCHAR);
			in.addValue("I_FLG_ACTIVO", req.getFlgActivo(), Types.NUMERIC);
			in.addValue("I_ID_USUARIO_CREA", req.getIdUsuarioCrea(), Types.NUMERIC);
			in.addValue("I_FEC_USUARIO_CREA", DateUtil.formatSlashDDMMYYYY(req.getFecUsuarioCrea()), Types.VARCHAR);

			Map<String, Object> out = jdbcCall.execute(in);

			rCodigo = Integer.parseInt(out.get(ConstanteUtil.R_CODIGO).toString());
			rMensaje = out.get(ConstanteUtil.R_MENSAJE).toString();

			if (rCodigo == ConstanteUtil.R_COD_EXITO) {// CONSULTA CORRECTA
				id = Long.parseLong(out.get("R_ID").toString());
				idPersona = Long.parseLong(out.get("R_ID_PERSONA").toString());

				res.setId(id);
				res.setCargo(req.getCargo());
				res.setFlgActivo(req.getFlgActivo());
				res.setIdUsuarioCrea(req.getIdUsuarioCrea());
				res.setFecUsuarioCrea(req.getFecUsuarioCrea());
				res.setIdUsuarioMod(req.getIdUsuarioMod());
				res.setFecUsuarioMod(req.getFecUsuarioMod());

				PersonaResponse pe = new PersonaResponse();
				pe.setId(idPersona);
				pe.setIdtTipoDocumento(req.getPersona().getIdtTipoDocumento());
				pe.setNomTipoDocumento(req.getPersona().getNomTipoDocumento());
				pe.setIdtSexo(req.getPersona().getIdtSexo());
				pe.setNomSexo(req.getPersona().getNomSexo());
				pe.setIdPais(req.getPersona().getIdPais());
				pe.setNomPais(req.getPersona().getNomPais());
				pe.setIdDepartamento(req.getPersona().getIdDepartamento());
				pe.setNomDepartamento(req.getPersona().getNomDepartamento());
				pe.setIdProvincia(req.getPersona().getIdProvincia());
				pe.setNomProvincia(req.getPersona().getNomProvincia());
				pe.setIdDistrito(req.getPersona().getIdDistrito());
				pe.setNomDistrito(req.getPersona().getNomDistrito());
				pe.setNombre(req.getPersona().getNombre());
				pe.setApePaterno(req.getPersona().getApePaterno());
				pe.setApeMaterno(req.getPersona().getApeMaterno());
				pe.setNroDocumento(req.getPersona().getNroDocumento());
				pe.setDireccionDomicilio(req.getPersona().getDireccionDomicilio());
				pe.setFecNacimiento(req.getPersona().getFecNacimiento());

				res.setPersona(pe);
			} else {
				res = null;
			}
			outResponse.setrCodigo(rCodigo);
			outResponse.setrMensaje(rMensaje);
			outResponse.setrResult(res);
		} catch (Exception e) {
			outResponse.setrCodigo(500);
			outResponse.setrMensaje(e.getMessage());
			outResponse.setrResult(null);
		}
		return outResponse;
	}

	@Override
	public OutResponse<PersonalResponse> modificarPersonal(PersonalRequest req) {
		OutResponse<PersonalResponse> outResponse = new OutResponse<>();
		PersonalResponse res = new PersonalResponse();

		Integer rCodigo = 0;
		String rMensaje = "";
		try {
			SimpleJdbcCall jdbcCall = new SimpleJdbcCall(dataSource).withSchemaName(ConstanteUtil.BD_SCHEMA)
					.withCatalogName(ConstanteUtil.BD_PCK_PERSONAL).withProcedureName("SP_U_PERSONAL");

			MapSqlParameterSource in = new MapSqlParameterSource();
			in.addValue("I_ID", req.getId(), Types.NUMERIC);
			in.addValue("I_ID_PERSONA", req.getPersona().getId(), Types.NUMERIC);
			in.addValue("I_CARGO", req.getCargo(), Types.VARCHAR);
			in.addValue("I_IDT_TIPO_DOCUMENTO", req.getPersona().getIdtTipoDocumento(), Types.NUMERIC);
			in.addValue("I_IDT_SEXO", req.getPersona().getIdtSexo(), Types.NUMERIC);
			in.addValue("I_ID_PAIS", req.getPersona().getIdPais(), Types.NUMERIC);
			in.addValue("I_ID_DEPARTAMENTO", req.getPersona().getIdDepartamento(), Types.NUMERIC);
			in.addValue("I_ID_PROVINCIA", req.getPersona().getIdProvincia(), Types.NUMERIC);
			in.addValue("I_ID_DISTRITO", req.getPersona().getIdDistrito(), Types.NUMERIC);
			in.addValue("I_NOMBRE", req.getPersona().getNombre(), Types.VARCHAR);
			in.addValue("I_APE_PATERNO", req.getPersona().getApePaterno(), Types.VARCHAR);
			in.addValue("I_APE_MATERNO", req.getPersona().getApeMaterno(), Types.VARCHAR);
			in.addValue("I_NRO_DOCUMENTO", req.getPersona().getNroDocumento(), Types.VARCHAR);
			in.addValue("I_DIRECCION_DOMICILIO", req.getPersona().getDireccionDomicilio(), Types.VARCHAR);
			in.addValue("I_FEC_NACIMIENTO", DateUtil.formatSlashDDMMYYYY(req.getPersona().getFecNacimiento()),
					Types.VARCHAR);
			in.addValue("I_FLG_ACTIVO", req.getFlgActivo(), Types.NUMERIC);
			in.addValue("I_ID_USUARIO_MOD", req.getIdUsuarioMod(), Types.NUMERIC);
			in.addValue("I_FEC_USUARIO_MOD", DateUtil.formatSlashDDMMYYYY(req.getFecUsuarioMod()), Types.VARCHAR);

			Map<String, Object> out = jdbcCall.execute(in);

			rCodigo = Integer.parseInt(out.get(ConstanteUtil.R_CODIGO).toString());
			rMensaje = out.get(ConstanteUtil.R_MENSAJE).toString();

			if (rCodigo == ConstanteUtil.R_COD_EXITO) {// CONSULTA CORRECTA
				res.setId(req.getId());
				res.setCargo(req.getCargo());
				res.setFlgActivo(req.getFlgActivo());
				res.setIdUsuarioCrea(req.getIdUsuarioCrea());
				res.setFecUsuarioCrea(req.getFecUsuarioCrea());
				res.setIdUsuarioMod(req.getIdUsuarioMod());
				res.setFecUsuarioMod(req.getFecUsuarioMod());

				PersonaResponse pe = new PersonaResponse();
				pe.setId(req.getPersona().getId());
				pe.setIdtTipoDocumento(req.getPersona().getIdtTipoDocumento());
				pe.setNomTipoDocumento(req.getPersona().getNomTipoDocumento());
				pe.setIdtSexo(req.getPersona().getIdtSexo());
				pe.setNomSexo(req.getPersona().getNomSexo());
				pe.setIdPais(req.getPersona().getIdPais());
				pe.setNomPais(req.getPersona().getNomPais());
				pe.setIdDepartamento(req.getPersona().getIdDepartamento());
				pe.setNomDepartamento(req.getPersona().getNomDepartamento());
				pe.setIdProvincia(req.getPersona().getIdProvincia());
				pe.setNomProvincia(req.getPersona().getNomProvincia());
				pe.setIdDistrito(req.getPersona().getIdDistrito());
				pe.setNomDistrito(req.getPersona().getNomDistrito());
				pe.setNombre(req.getPersona().getNombre());
				pe.setApePaterno(req.getPersona().getApePaterno());
				pe.setApeMaterno(req.getPersona().getApeMaterno());
				pe.setNroDocumento(req.getPersona().getNroDocumento());
				pe.setDireccionDomicilio(req.getPersona().getDireccionDomicilio());
				pe.setFecNacimiento(req.getPersona().getFecNacimiento());

				res.setPersona(pe);
			} else {
				res = null;
			}
			outResponse.setrCodigo(rCodigo);
			outResponse.setrMensaje(rMensaje);
			outResponse.setrResult(res);
		} catch (Exception e) {
			outResponse.setrCodigo(500);
			outResponse.setrMensaje(e.getMessage());
			outResponse.setrResult(null);
		}
		return outResponse;
	}

	@Override
	public OutResponse<PersonalResponse> eliminarPersonal(PersonalRequest req) {
		OutResponse<PersonalResponse> outResponse = new OutResponse<>();
		PersonalResponse res = new PersonalResponse();

		Integer rCodigo = 0;
		String rMensaje = "";
		try {
			SimpleJdbcCall jdbcCall = new SimpleJdbcCall(dataSource).withSchemaName(ConstanteUtil.BD_SCHEMA)
					.withCatalogName(ConstanteUtil.BD_PCK_PERSONAL).withProcedureName("SP_D_PERSONAL");

			MapSqlParameterSource in = new MapSqlParameterSource();
			in.addValue("I_ID", req.getId());

			Map<String, Object> out = jdbcCall.execute(in);

			rCodigo = Integer.parseInt(out.get(ConstanteUtil.R_CODIGO).toString());
			rMensaje = out.get(ConstanteUtil.R_MENSAJE).toString();

			if (rCodigo == ConstanteUtil.R_COD_EXITO) {// CONSULTA CORRECTA
				res.setId(req.getId());
			} else {
				res = null;
			}
			outResponse.setrCodigo(rCodigo);
			outResponse.setrMensaje(rMensaje);
			outResponse.setrResult(res);
		} catch (Exception e) {
			outResponse.setrCodigo(500);
			outResponse.setrMensaje(e.getMessage());
			outResponse.setrResult(null);
		}
		return outResponse;
	}
}
