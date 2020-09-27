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

import com.besoft.panaderia.dao.HonorarioDao;
import com.besoft.panaderia.dto.request.HonorarioBuscarRequest;
import com.besoft.panaderia.dto.request.HonorarioInsumoRequest;
import com.besoft.panaderia.dto.request.HonorarioRequest;
import com.besoft.panaderia.dto.request.array.HonorarioInsumoArray;
import com.besoft.panaderia.dto.response.OutResponse;
import com.besoft.panaderia.dto.response.mapper.HonorarioResponseMapper;
import com.besoft.panaderia.dto.response.HonorarioResponse;
import com.besoft.panaderia.util.ConstanteUtil;
import com.besoft.panaderia.util.DateUtil;

import oracle.sql.ARRAY;

@Repository
public class HonorarioDaoImpl implements HonorarioDao {

	@Autowired
	DataSource dataSource;

	@Autowired
	HonorarioInsumoArray detalleHonorarioArray;

	@Override
	public OutResponse<HonorarioResponse> registrarHonorario(HonorarioRequest c) {
		OutResponse<HonorarioResponse> outResponse = new OutResponse<>();
		HonorarioResponse resp = new HonorarioResponse();

		Integer rCodigo = 0;
		String rMensaje = "";

		Long id = 0L;
		try {
			SimpleJdbcCall jdbcCall = new SimpleJdbcCall(dataSource).withSchemaName(ConstanteUtil.BD_SCHEMA)
					.withCatalogName(ConstanteUtil.BD_PCK_PPOS_HONORARIO).withProcedureName("SP_I_HONORARIO");

			List<HonorarioInsumoRequest> lDetalle = c.getListaHonorarioInsumo();
			ARRAY array = detalleHonorarioArray.toArray(lDetalle);

			MapSqlParameterSource in = new MapSqlParameterSource();
			in.addValue("I_ID_PERSONAL", c.getPersonal().getId(), Types.NUMERIC);
			in.addValue("I_MONTO", c.getMonto(), Types.DECIMAL);
			in.addValue("I_FECHA_INICIO", DateUtil.formatSlashDDMMYYYY(c.getFechaInicio()), Types.VARCHAR);
			in.addValue("I_FECHA_FIN", DateUtil.formatSlashDDMMYYYY(c.getFechaFin()), Types.VARCHAR);
			in.addValue("I_FECHA", DateUtil.formatSlashDDMMYYYY(c.getFecha()), Types.VARCHAR);
			in.addValue("I_MES", c.getMes(), Types.NUMERIC);
			in.addValue("I_ANIO", c.getAnio(), Types.NUMERIC);
			in.addValue("I_FLG_ACTIVO", c.getFlgActivo(), Types.NUMERIC);
			in.addValue("I_ID_USUARIO_CREA", c.getIdUsuarioCrea(), Types.NUMERIC);
			in.addValue("I_FEC_USUARIO_CREA", DateUtil.formatSlashDDMMYYYY(c.getFecUsuarioCrea()), Types.VARCHAR);

			in.addValue("L_HONORARIO_INSUMO", array, Types.ARRAY);

			Map<String, Object> out = jdbcCall.execute(in);

			rCodigo = Integer.parseInt(out.get(ConstanteUtil.R_CODIGO).toString());
			rMensaje = out.get(ConstanteUtil.R_MENSAJE).toString();

			if (rCodigo == 0) {// CONSULTA CORRECTA
				id = Long.parseLong(out.get("R_ID").toString());

				resp.setId(id);
				resp.getPersonal().getPersona().setNombre(c.getPersonal().getPersona().getNombre());
				resp.getPersonal().getPersona().setApePaterno(c.getPersonal().getPersona().getApePaterno());
				resp.getPersonal().getPersona().setApePaterno(c.getPersonal().getPersona().getApeMaterno());
				resp.setMonto(c.getMonto());
				resp.setFechaInicio(c.getFechaInicio());
				resp.setFechaFin(c.getFechaFin());
				resp.setFecha(c.getFecha());
				resp.setMes(c.getMes());
				resp.setAnio(c.getAnio());
				resp.setFlgActivo(c.getFlgActivo());
				resp.setIdUsuarioCrea(c.getIdUsuarioCrea());
				resp.setFecUsuarioCrea(c.getFecUsuarioCrea());
				resp.setIdUsuarioMod(c.getIdUsuarioMod());
				resp.setFecUsuarioMod(c.getFecUsuarioMod());
			} else {
				resp = null;
			}
			outResponse.setrCodigo(rCodigo);
			outResponse.setrMensaje(rMensaje);
			outResponse.setrResult(resp);
		} catch (Exception e) {
			outResponse.setrCodigo(500);
			outResponse.setrMensaje(e.getMessage());
			outResponse.setrResult(null);
		}
		return outResponse;
	}

	@Override
	public OutResponse<List<HonorarioResponse>> listarHonorario(HonorarioBuscarRequest req) {
		OutResponse<List<HonorarioResponse>> outResponse = new OutResponse<>();

		List<HonorarioResponse> lista = new ArrayList<>();

		Integer rCodigo = 0;
		String rMensaje = "";

		try {
			SimpleJdbcCall jdbcCall = new SimpleJdbcCall(dataSource).withSchemaName(ConstanteUtil.BD_SCHEMA)
					.withCatalogName(ConstanteUtil.BD_PCK_PPOS_HONORARIO).withProcedureName("SP_L_HONORARIO")
					.returningResultSet(ConstanteUtil.R_LISTA, new HonorarioResponseMapper());

			MapSqlParameterSource in = new MapSqlParameterSource();
			in.addValue("I_ID_PERSONAL", req.getIdPersonal(), Types.NUMERIC);
			in.addValue("I_FEC_INICIO", DateUtil.formatSlashDDMMYYYY(req.getFecInicio()), Types.VARCHAR);
			in.addValue("I_FEC_FIN", DateUtil.formatSlashDDMMYYYY(req.getFecFin()), Types.VARCHAR);

			Map<String, Object> out = jdbcCall.execute(in);

			rCodigo = Integer.parseInt(out.get(ConstanteUtil.R_CODIGO).toString());
			rMensaje = out.get(ConstanteUtil.R_MENSAJE).toString();

			if (rCodigo == 0) {// CONSULTA CORRECTA
				lista = (List<HonorarioResponse>) out.get(ConstanteUtil.R_LISTA);
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
