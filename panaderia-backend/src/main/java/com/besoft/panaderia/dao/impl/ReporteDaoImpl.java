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

import com.besoft.panaderia.dao.ReporteDao;
import com.besoft.panaderia.dto.request.ReporteInsumoBuscarRequest;
import com.besoft.panaderia.dto.request.ReporteVentaBuscarRequest;
import com.besoft.panaderia.dto.response.OutResponse;
import com.besoft.panaderia.dto.response.ReporteInsumoResponse;
import com.besoft.panaderia.dto.response.ReporteVentaResponse;
import com.besoft.panaderia.dto.response.mapper.InsumoResponseMapper;
import com.besoft.panaderia.dto.response.mapper.ReporteInsumoResponseMapper;
import com.besoft.panaderia.dto.response.mapper.ReporteVentaResponseMapper;
import com.besoft.panaderia.util.ConstanteUtil;
import com.besoft.panaderia.util.DateUtil;

@Repository
public class ReporteDaoImpl implements ReporteDao {

	@Autowired
	DataSource dataSource;

	@Override
	public OutResponse<List<ReporteInsumoResponse>> listarReporteInsumo(ReporteInsumoBuscarRequest req) {
		OutResponse<List<ReporteInsumoResponse>> outResponse = new OutResponse<>();

		List<ReporteInsumoResponse> lista = new ArrayList<>();
		Integer rCodigo = 0;
		String rMensaje = "";
		try {
			SimpleJdbcCall jdbcCall = new SimpleJdbcCall(dataSource).withSchemaName(ConstanteUtil.BD_SCHEMA)
					.withCatalogName(ConstanteUtil.BD_PCK_REPORTE).withProcedureName("SP_L_REP_INSUMO")
					.returningResultSet(ConstanteUtil.R_LISTA, new ReporteInsumoResponseMapper());

			MapSqlParameterSource in = new MapSqlParameterSource();
			in.addValue("I_ID_TIPO_REPORTE", req.getIdTipoReporte(), Types.NUMERIC);
			in.addValue("I_ID_TIPO_INSUMO", req.getIdTipoInsumo(), Types.NUMERIC);
			in.addValue("I_ID_PERSONAL", req.getIdPersonal(), Types.NUMERIC);
			in.addValue("I_FEC_INICIO", DateUtil.formatSlashDDMMYYYY(req.getFecInicio()), Types.VARCHAR);
			in.addValue("I_FEC_FIN", DateUtil.formatSlashDDMMYYYY(req.getFecFin()), Types.VARCHAR);

			Map<String, Object> out = jdbcCall.execute(in);

			rCodigo = Integer.parseInt(out.get(ConstanteUtil.R_CODIGO).toString());
			rMensaje = out.get(ConstanteUtil.R_MENSAJE).toString();

			if (rCodigo == ConstanteUtil.R_COD_EXITO) {// CONSULTA CORRECTA
				lista = (List<ReporteInsumoResponse>) out.get(ConstanteUtil.R_LISTA);
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
	public OutResponse<List<ReporteVentaResponse>> listarReporteVenta(ReporteVentaBuscarRequest req) {
		OutResponse<List<ReporteVentaResponse>> outResponse = new OutResponse<>();

		List<ReporteVentaResponse> lista = new ArrayList<>();
		Integer rCodigo = 0;
		String rMensaje = "";
		try {
			SimpleJdbcCall jdbcCall = new SimpleJdbcCall(dataSource).withSchemaName(ConstanteUtil.BD_SCHEMA)
					.withCatalogName(ConstanteUtil.BD_PCK_REPORTE).withProcedureName("SP_L_REP_VENTA")
					.returningResultSet(ConstanteUtil.R_LISTA, new ReporteVentaResponseMapper());

			MapSqlParameterSource in = new MapSqlParameterSource();
			in.addValue("I_ID_TIPO_REPORTE", req.getIdTipoReporte(), Types.NUMERIC);
			in.addValue("I_ID_PRODUCTO", req.getIdProducto(), Types.NUMERIC);
			in.addValue("I_FEC_INICIO", DateUtil.formatSlashDDMMYYYY(req.getFecInicio()), Types.VARCHAR);
			in.addValue("I_FEC_FIN", DateUtil.formatSlashDDMMYYYY(req.getFecFin()), Types.VARCHAR);

			Map<String, Object> out = jdbcCall.execute(in);

			rCodigo = Integer.parseInt(out.get(ConstanteUtil.R_CODIGO).toString());
			rMensaje = out.get(ConstanteUtil.R_MENSAJE).toString();

			if (rCodigo == ConstanteUtil.R_COD_EXITO) {// CONSULTA CORRECTA
				lista = (List<ReporteVentaResponse>) out.get(ConstanteUtil.R_LISTA);
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
