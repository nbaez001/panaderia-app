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

import com.besoft.panaderia.dao.UbigeoDao;
import com.besoft.panaderia.dto.request.DepartamentoRequest;
import com.besoft.panaderia.dto.request.DistritoRequest;
import com.besoft.panaderia.dto.request.ProvinciaRequest;
import com.besoft.panaderia.dto.response.DepartamentoResponse;
import com.besoft.panaderia.dto.response.DistritoResponse;
import com.besoft.panaderia.dto.response.OutResponse;
import com.besoft.panaderia.dto.response.PaisResponse;
import com.besoft.panaderia.dto.response.ProvinciaResponse;
import com.besoft.panaderia.dto.response.mapper.DepartamentoResponseMapper;
import com.besoft.panaderia.dto.response.mapper.DistritoResponseMapper;
import com.besoft.panaderia.dto.response.mapper.PaisResponseMapper;
import com.besoft.panaderia.dto.response.mapper.ProvinciaResponseMapper;
import com.besoft.panaderia.util.ConstanteUtil;

@Repository
public class UbigeoDaoImpl implements UbigeoDao {

	@Autowired
	DataSource dataSource;

	@Override
	public OutResponse<List<PaisResponse>> listarPais() {
		OutResponse<List<PaisResponse>> outResponse = new OutResponse<>();

		List<PaisResponse> lista = new ArrayList<>();
		Integer rCodigo = 0;
		String rMensaje = "";
		try {
			SimpleJdbcCall jdbcCall = new SimpleJdbcCall(dataSource).withSchemaName(ConstanteUtil.BD_SCHEMA)
					.withCatalogName(ConstanteUtil.BD_PCK_UBIGEO).withProcedureName("SP_L_PAIS")
					.returningResultSet(ConstanteUtil.R_LISTA, new PaisResponseMapper());

			MapSqlParameterSource in = new MapSqlParameterSource();

			Map<String, Object> out = jdbcCall.execute(in);

			rCodigo = Integer.parseInt(out.get(ConstanteUtil.R_CODIGO).toString());
			rMensaje = out.get(ConstanteUtil.R_MENSAJE).toString();

			if (rCodigo == ConstanteUtil.R_COD_EXITO) {// CONSULTA CORRECTA
				lista = (List<PaisResponse>) out.get(ConstanteUtil.R_LISTA);
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
	public OutResponse<List<DepartamentoResponse>> listarDepartamento(DepartamentoRequest req) {
		OutResponse<List<DepartamentoResponse>> outResponse = new OutResponse<>();

		List<DepartamentoResponse> lista = new ArrayList<>();
		Integer rCodigo = 0;
		String rMensaje = "";
		try {
			SimpleJdbcCall jdbcCall = new SimpleJdbcCall(dataSource).withSchemaName(ConstanteUtil.BD_SCHEMA)
					.withCatalogName(ConstanteUtil.BD_PCK_UBIGEO).withProcedureName("SP_L_DEPARTAMENTO")
					.returningResultSet(ConstanteUtil.R_LISTA, new DepartamentoResponseMapper());

			MapSqlParameterSource in = new MapSqlParameterSource();
			in.addValue("I_ID_PAIS", req.getIdPais(), Types.NUMERIC);

			Map<String, Object> out = jdbcCall.execute(in);

			rCodigo = Integer.parseInt(out.get(ConstanteUtil.R_CODIGO).toString());
			rMensaje = out.get(ConstanteUtil.R_MENSAJE).toString();

			if (rCodigo == ConstanteUtil.R_COD_EXITO) {// CONSULTA CORRECTA
				lista = (List<DepartamentoResponse>) out.get(ConstanteUtil.R_LISTA);
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
	public OutResponse<List<ProvinciaResponse>> listarProvincia(ProvinciaRequest req) {
		OutResponse<List<ProvinciaResponse>> outResponse = new OutResponse<>();

		List<ProvinciaResponse> lista = new ArrayList<>();
		Integer rCodigo = 0;
		String rMensaje = "";
		try {
			SimpleJdbcCall jdbcCall = new SimpleJdbcCall(dataSource).withSchemaName(ConstanteUtil.BD_SCHEMA)
					.withCatalogName(ConstanteUtil.BD_PCK_UBIGEO).withProcedureName("SP_L_PROVINCIA")
					.returningResultSet(ConstanteUtil.R_LISTA, new ProvinciaResponseMapper());

			MapSqlParameterSource in = new MapSqlParameterSource();
			in.addValue("I_ID_DEPARTAMENTO", req.getIdDepartamento(), Types.NUMERIC);

			Map<String, Object> out = jdbcCall.execute(in);

			rCodigo = Integer.parseInt(out.get(ConstanteUtil.R_CODIGO).toString());
			rMensaje = out.get(ConstanteUtil.R_MENSAJE).toString();

			if (rCodigo == ConstanteUtil.R_COD_EXITO) {// CONSULTA CORRECTA
				lista = (List<ProvinciaResponse>) out.get(ConstanteUtil.R_LISTA);
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
	public OutResponse<List<DistritoResponse>> listarDistrito(DistritoRequest req) {
		OutResponse<List<DistritoResponse>> outResponse = new OutResponse<>();

		List<DistritoResponse> lista = new ArrayList<>();
		Integer rCodigo = 0;
		String rMensaje = "";
		try {
			SimpleJdbcCall jdbcCall = new SimpleJdbcCall(dataSource).withSchemaName(ConstanteUtil.BD_SCHEMA)
					.withCatalogName(ConstanteUtil.BD_PCK_UBIGEO).withProcedureName("SP_L_DISTRITO")
					.returningResultSet(ConstanteUtil.R_LISTA, new DistritoResponseMapper());

			MapSqlParameterSource in = new MapSqlParameterSource();
			in.addValue("I_ID_PROVINCIA", req.getIdProvincia(), Types.NUMERIC);

			Map<String, Object> out = jdbcCall.execute(in);

			rCodigo = Integer.parseInt(out.get(ConstanteUtil.R_CODIGO).toString());
			rMensaje = out.get(ConstanteUtil.R_MENSAJE).toString();

			if (rCodigo == ConstanteUtil.R_COD_EXITO) {// CONSULTA CORRECTA
				lista = (List<DistritoResponse>) out.get(ConstanteUtil.R_LISTA);
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

}
