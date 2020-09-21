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

import com.besoft.panaderia.dao.MaestraDao;
import com.besoft.panaderia.dto.request.MaestraBuscarRequest;
import com.besoft.panaderia.dto.request.MaestraRequest;
import com.besoft.panaderia.dto.response.OutResponse;
import com.besoft.panaderia.dto.response.MaestraResponse;
import com.besoft.panaderia.dto.response.mapper.MaestraResponseMapper;
import com.besoft.panaderia.util.ConstanteUtil;
import com.besoft.panaderia.util.DateUtil;

@Repository
public class MaestraDaoImpl implements MaestraDao {

	@Autowired
	DataSource dataSource;

	@Override
	public OutResponse<List<MaestraResponse>> listarMaestra(MaestraBuscarRequest req) {
		OutResponse<List<MaestraResponse>> outResponse = new OutResponse<>();

		List<MaestraResponse> lista = new ArrayList<>();
		Integer rCodigo = 0;
		String rMensaje = "";
		try {
			SimpleJdbcCall jdbcCall = new SimpleJdbcCall(dataSource).withSchemaName(ConstanteUtil.BD_SCHEMA)
					.withCatalogName(ConstanteUtil.BD_PCK_ADMINISTRACION).withProcedureName("SP_L_MAESTRA")
					.returningResultSet(ConstanteUtil.R_LISTA, new MaestraResponseMapper());

			MapSqlParameterSource in = new MapSqlParameterSource();
			in.addValue("I_FEC_INICIO", DateUtil.formatSlashDDMMYYYY(req.getFecInicio()), Types.VARCHAR);
			in.addValue("I_FEC_FIN", DateUtil.formatSlashDDMMYYYY(req.getFecFin()), Types.VARCHAR);
			in.addValue("I_ID_MAESTRA", req.getIdMaestra(), Types.NUMERIC);
			in.addValue("I_ID_TABLA", req.getIdTabla(), Types.NUMERIC);

			Map<String, Object> out = jdbcCall.execute(in);

			rCodigo = Integer.parseInt(out.get(ConstanteUtil.R_CODIGO).toString());
			rMensaje = out.get(ConstanteUtil.R_MENSAJE).toString();

			if (rCodigo == ConstanteUtil.R_COD_EXITO) {// CONSULTA CORRECTA
				lista = (List<MaestraResponse>) out.get(ConstanteUtil.R_LISTA);
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
	public OutResponse<MaestraResponse> registrarMaestra(MaestraRequest req) {
		OutResponse<MaestraResponse> outResponse = new OutResponse<>();
		MaestraResponse res = new MaestraResponse();

		Integer rCodigo = 0;
		String rMensaje = "";

		Long id = 0L;
		try {
			SimpleJdbcCall jdbcCall = new SimpleJdbcCall(dataSource).withSchemaName(ConstanteUtil.BD_SCHEMA)
					.withCatalogName(ConstanteUtil.BD_PCK_ADMINISTRACION).withProcedureName("SP_I_MAESTRA");

			MapSqlParameterSource in = new MapSqlParameterSource();
			in.addValue("I_ID_MAESTRA", req.getIdMaestra(), Types.NUMERIC);
			in.addValue("I_ID_TABLA", req.getIdTabla(), Types.NUMERIC);
			in.addValue("I_ID_ITEM", req.getIdItem(), Types.NUMERIC);
			in.addValue("I_ORDEN", req.getOrden(), Types.NUMERIC);
			in.addValue("I_CODIGO", req.getCodigo(), Types.VARCHAR);
			in.addValue("I_NOMBRE", req.getNombre(), Types.VARCHAR);
			in.addValue("I_VALOR", req.getValor(), Types.VARCHAR);
			in.addValue("I_DESCRIPCION", req.getDescripcion(), Types.VARCHAR);
			in.addValue("I_FLAG_ACTIVO", req.getFlagActivo(), Types.NUMERIC);
			in.addValue("I_ID_USUARIO_CREA", req.getIdUsuarioCrea(), Types.NUMERIC);
			in.addValue("I_FEC_USUARIO_CREA", DateUtil.formatSlashDDMMYYYY(req.getFecUsuarioCrea()), Types.VARCHAR);

			Map<String, Object> out = jdbcCall.execute(in);

			rCodigo = Integer.parseInt(out.get(ConstanteUtil.R_CODIGO).toString());
			rMensaje = out.get(ConstanteUtil.R_MENSAJE).toString();

			if (rCodigo == ConstanteUtil.R_COD_EXITO) {// CONSULTA CORRECTA
				id = Long.parseLong(out.get("R_ID").toString());

				res.setId(id);
				res.setIdMaestra(req.getIdMaestra());
				res.setIdTabla(req.getIdTabla());
				res.setIdItem(req.getIdItem());
				res.setOrden(req.getOrden());
				res.setCodigo(req.getCodigo());
				res.setNombre(req.getNombre());
				res.setValor(req.getValor());
				res.setDescripcion(req.getDescripcion());
				res.setFlagActivo(req.getFlagActivo());
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
	public OutResponse<MaestraResponse> modificarMaestra(MaestraRequest req) {
		OutResponse<MaestraResponse> outResponse = new OutResponse<>();
		MaestraResponse res = new MaestraResponse();

		Integer rCodigo = 0;
		String rMensaje = "";
		try {
			SimpleJdbcCall jdbcCall = new SimpleJdbcCall(dataSource).withSchemaName(ConstanteUtil.BD_SCHEMA)
					.withCatalogName(ConstanteUtil.BD_PCK_ADMINISTRACION).withProcedureName("SP_U_MAESTRA");

			MapSqlParameterSource in = new MapSqlParameterSource();
			in.addValue("I_ID", req.getId(), Types.NUMERIC); // estoy inicializando los in de ariba
			in.addValue("I_ID_MAESTRA", req.getIdMaestra(), Types.NUMERIC);
			in.addValue("I_ID_TABLA", req.getIdTabla(), Types.NUMERIC);
			in.addValue("I_ID_ITEM", req.getIdItem(), Types.NUMERIC);
			in.addValue("I_ORDEN", req.getOrden(), Types.NUMERIC);
			in.addValue("I_CODIGO", req.getCodigo(), Types.VARCHAR);
			in.addValue("I_NOMBRE", req.getNombre(), Types.VARCHAR);
			in.addValue("I_VALOR", req.getValor(), Types.VARCHAR);
			in.addValue("I_DESCRIPCION", req.getDescripcion(), Types.VARCHAR);
			in.addValue("I_FLAG_ACTIVO", req.getFlagActivo(), Types.NUMERIC);
			in.addValue("I_ID_USUARIO_MOD", req.getIdUsuarioMod(), Types.NUMERIC);
			in.addValue("I_FEC_USUARIO_MOD", DateUtil.formatSlashDDMMYYYY(req.getFecUsuarioMod()), Types.VARCHAR);

			Map<String, Object> out = jdbcCall.execute(in);

			rCodigo = Integer.parseInt(out.get(ConstanteUtil.R_CODIGO).toString());
			rMensaje = out.get(ConstanteUtil.R_MENSAJE).toString();

			if (rCodigo == ConstanteUtil.R_COD_EXITO) {// CONSULTA CORRECTA
				res.setId(req.getId());
				res.setIdMaestra(req.getIdMaestra());
				res.setIdTabla(req.getIdTabla());
				res.setIdItem(req.getIdItem());
				res.setOrden(req.getOrden());
				res.setCodigo(req.getCodigo());
				res.setNombre(req.getNombre());
				res.setValor(req.getValor());
				res.setDescripcion(req.getDescripcion());
				res.setFlagActivo(req.getFlagActivo());
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
	public OutResponse<MaestraResponse> eliminarMaestra(MaestraRequest req) {
		OutResponse<MaestraResponse> outResponse = new OutResponse<>();
		MaestraResponse res = new MaestraResponse();

		Integer rCodigo = 0;
		String rMensaje = "";
		try {
			SimpleJdbcCall jdbcCall = new SimpleJdbcCall(dataSource).withSchemaName(ConstanteUtil.BD_SCHEMA)
					.withCatalogName(ConstanteUtil.BD_PCK_ADMINISTRACION).withProcedureName("SP_D_MAESTRA");

			MapSqlParameterSource in = new MapSqlParameterSource();
			in.addValue("I_ID", req.getId());

			Map<String, Object> out = jdbcCall.execute(in);

			rCodigo = Integer.parseInt(out.get(ConstanteUtil.R_CODIGO).toString());
			rMensaje = out.get(ConstanteUtil.R_MENSAJE).toString();

			if (rCodigo == ConstanteUtil.R_COD_EXITO) {// CONSULTA CORRECTA
				res.setId(req.getId());
				res.setIdMaestra(req.getIdMaestra());
				res.setIdTabla(req.getIdTabla());
				res.setIdItem(req.getIdItem());
				res.setOrden(req.getOrden());
				res.setCodigo(req.getCodigo());
				res.setNombre(req.getNombre());
				res.setValor(req.getValor());
				res.setDescripcion(req.getDescripcion());
				res.setFlagActivo(req.getFlagActivo());
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

}
