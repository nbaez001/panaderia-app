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

import com.besoft.panaderia.dao.InsumoDao;
import com.besoft.panaderia.dto.request.InsumoBuscarRequest;
import com.besoft.panaderia.dto.request.InsumoRequest;
import com.besoft.panaderia.dto.request.TipoInsumoBuscarRequest;
import com.besoft.panaderia.dto.request.TipoInsumoRequest;
import com.besoft.panaderia.dto.response.InsumoResponse;
import com.besoft.panaderia.dto.response.OutResponse;
import com.besoft.panaderia.dto.response.PersonaResponse;
import com.besoft.panaderia.dto.response.PersonalResponse;
import com.besoft.panaderia.dto.response.TipoInsumoResponse;
import com.besoft.panaderia.dto.response.mapper.InsumoResponseMapper;
import com.besoft.panaderia.dto.response.mapper.TipoInsumoResponseMapper;
import com.besoft.panaderia.util.ConstanteUtil;
import com.besoft.panaderia.util.DateUtil;

@Repository
public class InsumoDaoImpl implements InsumoDao {

	@Autowired
	DataSource dataSource;

	@Override
	public OutResponse<List<InsumoResponse>> listarInsumo(InsumoBuscarRequest req) {
		OutResponse<List<InsumoResponse>> outResponse = new OutResponse<>();

		List<InsumoResponse> lista = new ArrayList<>();
		Integer rCodigo = 0;
		String rMensaje = "";
		try {
			SimpleJdbcCall jdbcCall = new SimpleJdbcCall(dataSource).withSchemaName(ConstanteUtil.BD_SCHEMA)
					.withCatalogName(ConstanteUtil.BD_PCK_INSUMO).withProcedureName("SP_L_INSUMO")
					.returningResultSet(ConstanteUtil.R_LISTA, new InsumoResponseMapper());

			MapSqlParameterSource in = new MapSqlParameterSource();
			in.addValue("I_ID_TIPO_INSUMO", req.getIdTipoInsumo(), Types.NUMERIC);
			in.addValue("I_ID_PERSONAL", req.getIdPersonal(), Types.NUMERIC);
			in.addValue("I_FEC_INICIO", DateUtil.formatSlashDDMMYYYY(req.getFecInicio()), Types.VARCHAR);
			in.addValue("I_FEC_FIN", DateUtil.formatSlashDDMMYYYY(req.getFecFin()), Types.VARCHAR);
			in.addValue("I_FLG_CAL_HONORARIO", req.getFlgCalHonorario(), Types.NUMERIC);

			Map<String, Object> out = jdbcCall.execute(in);

			rCodigo = Integer.parseInt(out.get(ConstanteUtil.R_CODIGO).toString());
			rMensaje = out.get(ConstanteUtil.R_MENSAJE).toString();

			if (rCodigo == ConstanteUtil.R_COD_EXITO) {// CONSULTA CORRECTA
				lista = (List<InsumoResponse>) out.get(ConstanteUtil.R_LISTA);
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
	public OutResponse<InsumoResponse> registrarInsumo(InsumoRequest req) {
		OutResponse<InsumoResponse> outResponse = new OutResponse<>();
		InsumoResponse res = new InsumoResponse();

		Integer rCodigo = 0;
		String rMensaje = "";

		Long id = 0L;
		try {
			SimpleJdbcCall jdbcCall = new SimpleJdbcCall(dataSource).withSchemaName(ConstanteUtil.BD_SCHEMA)
					.withCatalogName(ConstanteUtil.BD_PCK_INSUMO).withProcedureName("SP_I_INSUMO");

			MapSqlParameterSource in = new MapSqlParameterSource();
			in.addValue("I_ID_PERSONAL", req.getPersonal().getId(), Types.NUMERIC);
			in.addValue("I_ID_TIPO_INSUMO", req.getTipoInsumo().getId(), Types.NUMERIC);
			in.addValue("I_CANTIDAD", req.getCantidad(), Types.DECIMAL);
			in.addValue("I_FECHA", DateUtil.formatSlashDDMMYYYY(req.getFecha()), Types.VARCHAR);
			in.addValue("I_FLG_ACTIVO", req.getFlgActivo(), Types.NUMERIC);
			in.addValue("I_FLG_CAL_HONORARIO", req.getFlgCalHonorario(), Types.NUMERIC);
			in.addValue("I_ID_USUARIO_CREA", req.getIdUsuarioCrea(), Types.NUMERIC);
			in.addValue("I_FEC_USUARIO_CREA", DateUtil.formatSlashDDMMYYYY(req.getFecUsuarioCrea()), Types.VARCHAR);

			Map<String, Object> out = jdbcCall.execute(in);

			rCodigo = Integer.parseInt(out.get(ConstanteUtil.R_CODIGO).toString());
			rMensaje = out.get(ConstanteUtil.R_MENSAJE).toString();

			if (rCodigo == ConstanteUtil.R_COD_EXITO) {// CONSULTA CORRECTA
				id = Long.parseLong(out.get("R_ID").toString());

				PersonaResponse per = new PersonaResponse();
				per.setNombre(req.getPersonal().getPersona().getNombre());
				per.setApePaterno(req.getPersonal().getPersona().getApePaterno());
				per.setApeMaterno(req.getPersonal().getPersona().getApeMaterno());

				PersonalResponse p = new PersonalResponse();
				p.setId(req.getPersonal().getId());
				p.setPersona(per);

				TipoInsumoResponse ti = new TipoInsumoResponse();
				ti.setId(req.getTipoInsumo().getId());
				ti.setNombre(req.getTipoInsumo().getNombre());
				ti.setIdtUnidadMedida(req.getTipoInsumo().getIdtUnidadMedida());
				ti.setNomUnidadMedida(req.getTipoInsumo().getNomUnidadMedida());

				res.setId(id);
				res.setPersonal(p);
				res.setTipoInsumo(ti);
				res.setCantidad(req.getCantidad());
				res.setFecha(req.getFecha());
				res.setFlgActivo(req.getFlgActivo());
				res.setFlgCalHonorario(req.getFlgCalHonorario());
				res.setIdUsuarioCrea(req.getIdUsuarioCrea());
				res.setFecUsuarioCrea(req.getFecUsuarioCrea());
				res.setIdUsuarioMod(req.getIdUsuarioMod());
				res.setFecUsuarioMod(req.getFecUsuarioMod());
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
	public OutResponse<InsumoResponse> modificarInsumo(InsumoRequest req) {
		OutResponse<InsumoResponse> outResponse = new OutResponse<>();
		InsumoResponse res = new InsumoResponse();

		Integer rCodigo = 0;
		String rMensaje = "";
		try {
			SimpleJdbcCall jdbcCall = new SimpleJdbcCall(dataSource).withSchemaName(ConstanteUtil.BD_SCHEMA)
					.withCatalogName(ConstanteUtil.BD_PCK_INSUMO).withProcedureName("SP_U_INSUMO");

			MapSqlParameterSource in = new MapSqlParameterSource();
			in.addValue("I_ID", req.getId(), Types.NUMERIC);
			in.addValue("I_ID_PERSONAL", req.getPersonal().getId(), Types.NUMERIC);
			in.addValue("I_ID_TIPO_INSUMO", req.getTipoInsumo().getId(), Types.NUMERIC);
			in.addValue("I_CANTIDAD", req.getCantidad(), Types.DECIMAL);
			in.addValue("I_FECHA", DateUtil.formatSlashDDMMYYYY(req.getFecha()), Types.VARCHAR);
			in.addValue("I_FLG_ACTIVO", req.getFlgActivo(), Types.NUMERIC);
			in.addValue("I_FLG_CAL_HONORARIO", req.getFlgCalHonorario(), Types.NUMERIC);
			in.addValue("I_ID_USUARIO_MOD", req.getIdUsuarioMod(), Types.NUMERIC);
			in.addValue("I_FEC_USUARIO_MOD", DateUtil.formatSlashDDMMYYYY(req.getFecUsuarioMod()), Types.VARCHAR);

			Map<String, Object> out = jdbcCall.execute(in);

			rCodigo = Integer.parseInt(out.get(ConstanteUtil.R_CODIGO).toString());
			rMensaje = out.get(ConstanteUtil.R_MENSAJE).toString();

			if (rCodigo == ConstanteUtil.R_COD_EXITO) {// CONSULTA CORRECTA
				PersonaResponse per = new PersonaResponse();
				per.setNombre(req.getPersonal().getPersona().getNombre());
				per.setApePaterno(req.getPersonal().getPersona().getApePaterno());
				per.setApeMaterno(req.getPersonal().getPersona().getApeMaterno());

				PersonalResponse p = new PersonalResponse();
				p.setId(req.getPersonal().getId());
				p.setPersona(per);

				TipoInsumoResponse ti = new TipoInsumoResponse();
				ti.setId(req.getTipoInsumo().getId());
				ti.setNombre(req.getTipoInsumo().getNombre());
				ti.setIdtUnidadMedida(req.getTipoInsumo().getIdtUnidadMedida());
				ti.setNomUnidadMedida(req.getTipoInsumo().getNomUnidadMedida());

				res.setId(req.getId());
				res.setPersonal(p);
				res.setTipoInsumo(ti);
				res.setCantidad(req.getCantidad());
				res.setFecha(req.getFecha());
				res.setFlgActivo(req.getFlgActivo());
				res.setFlgCalHonorario(req.getFlgCalHonorario());
				res.setIdUsuarioCrea(req.getIdUsuarioCrea());
				res.setFecUsuarioCrea(req.getFecUsuarioCrea());
				res.setIdUsuarioMod(req.getIdUsuarioMod());
				res.setFecUsuarioMod(req.getFecUsuarioMod());
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
	public OutResponse<InsumoResponse> eliminarInsumo(InsumoRequest req) {
		OutResponse<InsumoResponse> outResponse = new OutResponse<>();
		InsumoResponse res = new InsumoResponse();

		Integer rCodigo = 0;
		String rMensaje = "";
		try {
			SimpleJdbcCall jdbcCall = new SimpleJdbcCall(dataSource).withSchemaName(ConstanteUtil.BD_SCHEMA)
					.withCatalogName(ConstanteUtil.BD_PCK_INSUMO).withProcedureName("SP_D_INSUMO");

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

	@Override
	public OutResponse<List<TipoInsumoResponse>> listarTipoInsumo(TipoInsumoBuscarRequest req) {
		OutResponse<List<TipoInsumoResponse>> outResponse = new OutResponse<>();

		List<TipoInsumoResponse> lista = new ArrayList<>();
		Integer rCodigo = 0;
		String rMensaje = "";
		try {
			SimpleJdbcCall jdbcCall = new SimpleJdbcCall(dataSource).withSchemaName(ConstanteUtil.BD_SCHEMA)
					.withCatalogName(ConstanteUtil.BD_PCK_INSUMO).withProcedureName("SP_L_TIPO_INSUMO")
					.returningResultSet(ConstanteUtil.R_LISTA, new TipoInsumoResponseMapper());

			MapSqlParameterSource in = new MapSqlParameterSource();
			in.addValue("I_NOMBRE", req.getNombre(), Types.VARCHAR);

			Map<String, Object> out = jdbcCall.execute(in);

			rCodigo = Integer.parseInt(out.get(ConstanteUtil.R_CODIGO).toString());
			rMensaje = out.get(ConstanteUtil.R_MENSAJE).toString();

			if (rCodigo == ConstanteUtil.R_COD_EXITO) {// CONSULTA CORRECTA
				lista = (List<TipoInsumoResponse>) out.get(ConstanteUtil.R_LISTA);
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
	public OutResponse<TipoInsumoResponse> registrarTipoInsumo(TipoInsumoRequest req) {
		OutResponse<TipoInsumoResponse> outResponse = new OutResponse<>();
		TipoInsumoResponse res = new TipoInsumoResponse();

		Integer rCodigo = 0;
		String rMensaje = "";

		Long id = 0L;
		String codTipoInsumo = "";
		try {
			SimpleJdbcCall jdbcCall = new SimpleJdbcCall(dataSource).withSchemaName(ConstanteUtil.BD_SCHEMA)
					.withCatalogName(ConstanteUtil.BD_PCK_INSUMO).withProcedureName("SP_I_TIPO_INSUMO");

			MapSqlParameterSource in = new MapSqlParameterSource();
			in.addValue("I_IDT_UNIDAD_MEDIDA", req.getIdtUnidadMedida(), Types.NUMERIC);
			in.addValue("I_NOMBRE", req.getNombre(), Types.VARCHAR);
			in.addValue("I_CODIGO", req.getCodigo(), Types.VARCHAR);
			in.addValue("I_OBSERVACION", req.getObservacion(), Types.VARCHAR);
			in.addValue("I_FLG_ACTIVO", req.getFlgActivo(), Types.NUMERIC);
			in.addValue("I_ID_USUARIO_CREA", req.getIdUsuarioCrea(), Types.NUMERIC);
			in.addValue("I_FEC_USUARIO_CREA", DateUtil.formatSlashDDMMYYYY(req.getFecUsuarioCrea()), Types.VARCHAR);

			Map<String, Object> out = jdbcCall.execute(in);

			rCodigo = Integer.parseInt(out.get(ConstanteUtil.R_CODIGO).toString());
			rMensaje = out.get(ConstanteUtil.R_MENSAJE).toString();

			if (rCodigo == ConstanteUtil.R_COD_EXITO) {// CONSULTA CORRECTA
				id = Long.parseLong(out.get("R_ID").toString());
				codTipoInsumo = out.get("I_CODIGO").toString();

				res.setId(id);
				res.setIdtUnidadMedida(req.getIdtUnidadMedida());
				res.setNomUnidadMedida(req.getNomUnidadMedida());
				res.setNombre(req.getNombre());
				res.setCodigo(codTipoInsumo);
				res.setObservacion(req.getObservacion());
				res.setFlgActivo(req.getFlgActivo());
				res.setIdUsuarioCrea(req.getIdUsuarioCrea());
				res.setFecUsuarioCrea(req.getFecUsuarioCrea());
				res.setIdUsuarioMod(req.getIdUsuarioMod());
				res.setFecUsuarioMod(req.getFecUsuarioMod());
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
	public OutResponse<TipoInsumoResponse> modificarTipoInsumo(TipoInsumoRequest req) {
		OutResponse<TipoInsumoResponse> outResponse = new OutResponse<>();
		TipoInsumoResponse res = new TipoInsumoResponse();

		Integer rCodigo = 0;
		String rMensaje = "";
		try {
			SimpleJdbcCall jdbcCall = new SimpleJdbcCall(dataSource).withSchemaName(ConstanteUtil.BD_SCHEMA)
					.withCatalogName(ConstanteUtil.BD_PCK_INSUMO).withProcedureName("SP_U_TIPO_INSUMO");

			MapSqlParameterSource in = new MapSqlParameterSource();
			in.addValue("I_ID", req.getId(), Types.NUMERIC);
			in.addValue("I_IDT_UNIDAD_MEDIDA", req.getIdtUnidadMedida(), Types.NUMERIC);
			in.addValue("I_NOMBRE", req.getNombre(), Types.VARCHAR);
			in.addValue("I_CODIGO", req.getCodigo(), Types.VARCHAR);
			in.addValue("I_OBSERVACION", req.getObservacion(), Types.VARCHAR);
			in.addValue("I_FLG_ACTIVO", req.getFlgActivo(), Types.NUMERIC);
			in.addValue("I_ID_USUARIO_MOD", req.getIdUsuarioMod(), Types.NUMERIC);
			in.addValue("I_FEC_USUARIO_MOD", DateUtil.formatSlashDDMMYYYY(req.getFecUsuarioMod()), Types.VARCHAR);

			Map<String, Object> out = jdbcCall.execute(in);

			rCodigo = Integer.parseInt(out.get(ConstanteUtil.R_CODIGO).toString());
			rMensaje = out.get(ConstanteUtil.R_MENSAJE).toString();

			if (rCodigo == ConstanteUtil.R_COD_EXITO) {// CONSULTA CORRECTA
				res.setId(req.getId());
				res.setIdtUnidadMedida(req.getIdtUnidadMedida());
				res.setNomUnidadMedida(req.getNomUnidadMedida());
				res.setNombre(req.getNombre());
				res.setCodigo(req.getCodigo());
				res.setObservacion(req.getObservacion());
				res.setFlgActivo(req.getFlgActivo());
				res.setIdUsuarioCrea(req.getIdUsuarioCrea());
				res.setFecUsuarioCrea(req.getFecUsuarioCrea());
				res.setIdUsuarioMod(req.getIdUsuarioMod());
				res.setFecUsuarioMod(req.getFecUsuarioMod());
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
	public OutResponse<TipoInsumoResponse> eliminarTipoInsumo(TipoInsumoRequest req) {
		OutResponse<TipoInsumoResponse> outResponse = new OutResponse<>();
		TipoInsumoResponse res = new TipoInsumoResponse();

		Integer rCodigo = 0;
		String rMensaje = "";
		try {
			SimpleJdbcCall jdbcCall = new SimpleJdbcCall(dataSource).withSchemaName(ConstanteUtil.BD_SCHEMA)
					.withCatalogName(ConstanteUtil.BD_PCK_INSUMO).withProcedureName("SP_D_TIPO_INSUMO");

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
