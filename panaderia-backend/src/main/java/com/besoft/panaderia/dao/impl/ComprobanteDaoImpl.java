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

import com.besoft.panaderia.dao.ComprobanteDao;
import com.besoft.panaderia.dto.request.ComprobanteBuscarRequest;
import com.besoft.panaderia.dto.request.ComprobanteRequest;
import com.besoft.panaderia.dto.response.OutResponse;
import com.besoft.panaderia.dto.response.ComprobanteResponse;
import com.besoft.panaderia.dto.response.mapper.ComprobanteResponseMapper;
import com.besoft.panaderia.util.ConstanteUtil;
import com.besoft.panaderia.util.DateUtil;

@Repository
public class ComprobanteDaoImpl implements ComprobanteDao {

	@Autowired
	DataSource dataSource;

	@Override
	public OutResponse<List<ComprobanteResponse>> listarComprobante(ComprobanteBuscarRequest req) {
		OutResponse<List<ComprobanteResponse>> outResponse = new OutResponse<>();

		List<ComprobanteResponse> lista = new ArrayList<>();
		Integer rCodigo = 0;
		String rMensaje = "";
		try {
			SimpleJdbcCall jdbcCall = new SimpleJdbcCall(dataSource).withSchemaName(ConstanteUtil.BD_SCHEMA)
					.withCatalogName(ConstanteUtil.BD_PCK_ADMINISTRACION).withProcedureName("SP_L_COMPROBANTE")
					.returningResultSet(ConstanteUtil.R_LISTA, new ComprobanteResponseMapper());

			MapSqlParameterSource in = new MapSqlParameterSource();
			in.addValue("I_ID", req.getId(), Types.NUMERIC);
			in.addValue("I_FEC_INICIO", DateUtil.formatSlashDDMMYYYY(req.getFecInicio()), Types.VARCHAR);
			in.addValue("I_FEC_FIN", DateUtil.formatSlashDDMMYYYY(req.getFecFin()), Types.VARCHAR);
			in.addValue("I_IDT_TIPO_COMPROBANTE", req.getIdtTipoComprobante(), Types.NUMERIC);
			in.addValue("I_FLG_ACTUAL", req.getFlgActual(), Types.NUMERIC);

			Map<String, Object> out = jdbcCall.execute(in);

			rCodigo = Integer.parseInt(out.get(ConstanteUtil.R_CODIGO).toString());
			rMensaje = out.get(ConstanteUtil.R_MENSAJE).toString();

			if (rCodigo == ConstanteUtil.R_COD_EXITO) {// CONSULTA CORRECTA
				lista = (List<ComprobanteResponse>) out.get(ConstanteUtil.R_LISTA);
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
	public OutResponse<ComprobanteResponse> registrarComprobante(ComprobanteRequest req) {
		OutResponse<ComprobanteResponse> outResponse = new OutResponse<>();
		ComprobanteResponse res = new ComprobanteResponse();

		Integer rCodigo = 0;
		String rMensaje = "";

		Long id = 0L;
		Integer flgActual = 0;
		try {
			SimpleJdbcCall jdbcCall = new SimpleJdbcCall(dataSource).withSchemaName(ConstanteUtil.BD_SCHEMA)
					.withCatalogName(ConstanteUtil.BD_PCK_ADMINISTRACION).withProcedureName("SP_I_COMPROBANTE");

			MapSqlParameterSource in = new MapSqlParameterSource();
			in.addValue("I_IDT_TIPO_COMPROBANTE", req.getIdtTipoComprobante(), Types.NUMERIC);
			in.addValue("I_NOMBRE", req.getNombre(), Types.VARCHAR);
			in.addValue("I_SERIE", req.getSerie(), Types.NUMERIC);
			in.addValue("I_NUMERO", req.getNumero(), Types.NUMERIC);
			in.addValue("I_LONG_SERIE", req.getLongSerie(), Types.NUMERIC);
			in.addValue("I_LONG_NUMERO", req.getLongNumero(), Types.NUMERIC);
			in.addValue("I_FLG_ACTIVO", req.getFlgActivo(), Types.NUMERIC);
			in.addValue("I_FLG_ACTUAL", req.getFlgActual(), Types.NUMERIC);
			in.addValue("I_FLG_USADO", req.getFlgUsado(), Types.NUMERIC);
			in.addValue("I_ID_USUARIO_CREA", req.getIdUsuarioCrea(), Types.NUMERIC);
			in.addValue("I_FEC_USUARIO_CREA", DateUtil.formatSlashDDMMYYYY(req.getFecUsuarioCrea()), Types.VARCHAR);

			Map<String, Object> out = jdbcCall.execute(in);

			rCodigo = Integer.parseInt(out.get(ConstanteUtil.R_CODIGO).toString());
			rMensaje = out.get(ConstanteUtil.R_MENSAJE).toString();

			if (rCodigo == ConstanteUtil.R_COD_EXITO) {// CONSULTA CORRECTA
				id = Long.parseLong(out.get("R_ID").toString());
				flgActual = Integer.parseInt(out.get("I_FLG_ACTUAL").toString());

				res.setId(id);
				res.setIdtTipoComprobante(req.getIdtTipoComprobante());
				res.setNomTipoComprobante(req.getNomTipoComprobante());
				res.setNombre(req.getNombre());
				res.setSerie(req.getSerie());
				res.setNumero(req.getNumero());
				res.setLongSerie(req.getLongSerie());
				res.setLongNumero(req.getLongNumero());
				res.setFlgActual(flgActual);
				res.setFlgActivo(req.getFlgActivo());
				res.setFlgUsado(req.getFlgUsado());
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
	public OutResponse<ComprobanteResponse> modificarComprobante(ComprobanteRequest req) {
		OutResponse<ComprobanteResponse> outResponse = new OutResponse<>();
		ComprobanteResponse res = new ComprobanteResponse();

		Integer rCodigo = 0;
		String rMensaje = "";
		try {
			SimpleJdbcCall jdbcCall = new SimpleJdbcCall(dataSource).withSchemaName(ConstanteUtil.BD_SCHEMA)
					.withCatalogName(ConstanteUtil.BD_PCK_ADMINISTRACION).withProcedureName("SP_U_COMPROBANTE");

			MapSqlParameterSource in = new MapSqlParameterSource();
			in.addValue("I_ID", req.getId(), Types.NUMERIC);
			in.addValue("I_IDT_TIPO_COMPROBANTE", req.getIdtTipoComprobante(), Types.NUMERIC);
			in.addValue("I_NOMBRE", req.getNombre(), Types.VARCHAR);
			in.addValue("I_SERIE", req.getSerie(), Types.NUMERIC);
			in.addValue("I_NUMERO", req.getNumero(), Types.NUMERIC);
			in.addValue("I_LONG_SERIE", req.getLongSerie(), Types.NUMERIC);
			in.addValue("I_LONG_NUMERO", req.getLongNumero(), Types.NUMERIC);
			in.addValue("I_FLG_ACTIVO", req.getFlgActivo(), Types.NUMERIC);
			in.addValue("I_FLG_ACTUAL", req.getFlgActual(), Types.NUMERIC);
			in.addValue("I_FLG_USADO", req.getFlgUsado(), Types.NUMERIC);
			in.addValue("I_ID_USUARIO_MOD", req.getIdUsuarioMod(), Types.NUMERIC);
			in.addValue("I_FEC_USUARIO_MOD", DateUtil.formatSlashDDMMYYYY(req.getFecUsuarioMod()), Types.VARCHAR);

			Map<String, Object> out = jdbcCall.execute(in);

			rCodigo = Integer.parseInt(out.get(ConstanteUtil.R_CODIGO).toString());
			rMensaje = out.get(ConstanteUtil.R_MENSAJE).toString();

			if (rCodigo == ConstanteUtil.R_COD_EXITO) {// CONSULTA CORRECTA
				res.setId(req.getId());
				res.setIdtTipoComprobante(req.getIdtTipoComprobante());
				res.setNomTipoComprobante(req.getNomTipoComprobante());
				res.setNombre(req.getNombre());
				res.setSerie(req.getSerie());
				res.setNumero(req.getNumero());
				res.setLongSerie(req.getLongSerie());
				res.setLongNumero(req.getLongNumero());
				res.setFlgActual(req.getFlgActual());
				res.setFlgActivo(req.getFlgActivo());
				res.setFlgUsado(req.getFlgUsado());
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
	public OutResponse<ComprobanteResponse> eliminarComprobante(ComprobanteRequest req) {
		OutResponse<ComprobanteResponse> outResponse = new OutResponse<>();
		ComprobanteResponse res = new ComprobanteResponse();

		Integer rCodigo = 0;
		String rMensaje = "";
		try {
			SimpleJdbcCall jdbcCall = new SimpleJdbcCall(dataSource).withSchemaName(ConstanteUtil.BD_SCHEMA)
					.withCatalogName(ConstanteUtil.BD_PCK_ADMINISTRACION).withProcedureName("SP_D_COMPROBANTE");

			MapSqlParameterSource in = new MapSqlParameterSource();
			in.addValue("I_ID", req.getId(), Types.NUMERIC);

			Map<String, Object> out = jdbcCall.execute(in);

			rCodigo = Integer.parseInt(out.get(ConstanteUtil.R_CODIGO).toString());
			rMensaje = out.get(ConstanteUtil.R_MENSAJE).toString();

			if (rCodigo == ConstanteUtil.R_COD_EXITO) {// CONSULTA CORRECTA
				res.setId(req.getId());
				res.setIdtTipoComprobante(req.getIdtTipoComprobante());
				res.setNomTipoComprobante(req.getNomTipoComprobante());
				res.setNombre(req.getNombre());
				res.setSerie(req.getSerie());
				res.setNumero(req.getNumero());
				res.setLongSerie(req.getLongSerie());
				res.setLongNumero(req.getLongNumero());
				res.setFlgActual(req.getFlgActual());
				res.setFlgActivo(req.getFlgActivo());
				res.setFlgUsado(req.getFlgUsado());
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
	public OutResponse<ComprobanteResponse> establecerComprobanteActual(ComprobanteRequest req) {
		OutResponse<ComprobanteResponse> outResponse = new OutResponse<>();
		ComprobanteResponse res = new ComprobanteResponse();

		Integer rCodigo = 0;
		String rMensaje = "";
		try {
			SimpleJdbcCall jdbcCall = new SimpleJdbcCall(dataSource).withSchemaName(ConstanteUtil.BD_SCHEMA)
					.withCatalogName(ConstanteUtil.BD_PCK_ADMINISTRACION)
					.withProcedureName("SP_EST_COMPROBANTE_ACTUAL");

			MapSqlParameterSource in = new MapSqlParameterSource();
			in.addValue("I_ID", req.getId(), Types.NUMERIC);
			in.addValue("I_IDT_TIPO_COMPROBANTE", req.getIdtTipoComprobante(), Types.NUMERIC);

			Map<String, Object> out = jdbcCall.execute(in);

			rCodigo = Integer.parseInt(out.get(ConstanteUtil.R_CODIGO).toString());
			rMensaje = out.get(ConstanteUtil.R_MENSAJE).toString();

			if (rCodigo == ConstanteUtil.R_COD_EXITO) {// CONSULTA CORRECTA
				res.setId(req.getId());
				res.setIdtTipoComprobante(req.getIdtTipoComprobante());
				res.setNomTipoComprobante(req.getNomTipoComprobante());
				res.setNombre(req.getNombre());
				res.setSerie(req.getSerie());
				res.setNumero(req.getNumero());
				res.setLongSerie(req.getLongSerie());
				res.setLongNumero(req.getLongNumero());
				res.setFlgActual(req.getFlgActual());
				res.setFlgActivo(req.getFlgActivo());
				res.setFlgUsado(req.getFlgUsado());
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
