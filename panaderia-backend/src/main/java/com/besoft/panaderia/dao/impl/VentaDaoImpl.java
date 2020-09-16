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

import com.besoft.panaderia.dao.VentaDao;
import com.besoft.panaderia.dto.request.DetalleVentaRequest;
import com.besoft.panaderia.dto.request.VentaRequest;
import com.besoft.panaderia.dto.request.array.DetalleVentaArray;
import com.besoft.panaderia.dto.response.ApiOutResponse;
import com.besoft.panaderia.dto.response.DetalleVentaResponse;
import com.besoft.panaderia.dto.response.VentaResponse;
import com.besoft.panaderia.dto.response.mapper.VentaResponseMapper;
import com.besoft.panaderia.util.ConstanteUtil;
import com.besoft.panaderia.util.DateUtil;

import oracle.sql.ARRAY;

@Repository
public class VentaDaoImpl implements VentaDao {
	@Autowired
	DataSource dataSource;

	@Autowired
	DetalleVentaArray detalleVentaArray;

	@Override
	public ApiOutResponse<VentaResponse> registrarVenta(VentaRequest c) {
		ApiOutResponse<VentaResponse> outResponse = new ApiOutResponse<>();
		VentaResponse resp = new VentaResponse();

		Integer rCodigo = 0;
		String rMensaje = "";

		Long id = 0L;
		try {
			SimpleJdbcCall jdbcCall = new SimpleJdbcCall(dataSource).withSchemaName(ConstanteUtil.BD_SCHEMA)
					.withCatalogName(ConstanteUtil.BD_PCK_VENTA).withProcedureName("SP_I_VENTA");

			List<DetalleVentaRequest> lDetalle = c.getListaDetalleVenta();
			ARRAY array = detalleVentaArray.toArray(lDetalle);

			MapSqlParameterSource in = new MapSqlParameterSource();
			in.addValue("I_NUMERO", c.getNumero(), Types.VARCHAR);
			in.addValue("I_SERIE", c.getSerie(), Types.VARCHAR);
			in.addValue("I_TOTAL", c.getTotal(), Types.DECIMAL);
			in.addValue("I_FLG_ACTIVO", c.getFlgActivo(), Types.NUMERIC);
			in.addValue("I_ID_USUARIO_CREA", c.getIdUsuarioCrea(), Types.NUMERIC);
			in.addValue("I_FEC_USUARIO_CREA", DateUtil.formatSlashDDMMYYYY(c.getFecUsuarioCrea()), Types.VARCHAR);

			in.addValue("L_DETALLE_VENTA", array, Types.ARRAY);

			Map<String, Object> out = jdbcCall.execute(in);

			rCodigo = Integer.parseInt(out.get(ConstanteUtil.R_CODIGO).toString());
			rMensaje = out.get(ConstanteUtil.R_MENSAJE).toString();

			if (rCodigo == 0) {// CONSULTA CORRECTA
				id = Long.parseLong(out.get("R_ID").toString());

				resp.setId(id);
				resp.setSerie(c.getSerie());
				resp.setNumero(c.getNumero());
				resp.setTotal(c.getTotal());
				resp.setFlgActivo(c.getFlgActivo());
				resp.setIdUsuarioCrea(c.getIdUsuarioCrea());
				resp.setFecUsuarioCrea(c.getFecUsuarioCrea());
				resp.setIdUsuarioMod(c.getIdUsuarioMod());
				resp.setFecUsuarioMod(c.getFecUsuarioMod());

				List<DetalleVentaResponse> ldv = new ArrayList<>();
				for (DetalleVentaRequest d : c.getListaDetalleVenta()) {
					DetalleVentaResponse dv = new DetalleVentaResponse();
					dv.setId(d.getId());
					dv.setIdProducto(d.getIdProducto());
					dv.setNomProducto(d.getNomProducto());
					dv.setIdVenta(id);
					dv.setCantidad(d.getCantidad());
					dv.setPrecio(d.getPrecio());
					dv.setSubtotal(d.getSubtotal());
					dv.setFlagActivo(d.getFlagActivo());
					ldv.add(dv);
				}

				resp.setListaDetalleVenta(ldv);
			} else {
				resp = null;
			}
			outResponse.setrCodigo(rCodigo);
			outResponse.setrMensaje(rMensaje);
			outResponse.setResult(resp);
		} catch (Exception e) {
			outResponse.setrCodigo(500);
			outResponse.setrMensaje(e.getMessage());
			outResponse.setResult(null);
		}
		return outResponse;
	}

	@Override
	public ApiOutResponse<List<VentaResponse>> listarVenta() {
		ApiOutResponse<List<VentaResponse>> outResponse = new ApiOutResponse<>();

		List<VentaResponse> lista = new ArrayList<>();

		Integer rCodigo = 0;
		String rMensaje = "";

		try {
			SimpleJdbcCall jdbcCall = new SimpleJdbcCall(dataSource).withSchemaName(ConstanteUtil.BD_SCHEMA)
					.withCatalogName(ConstanteUtil.BD_PCK_VENTA).withProcedureName("SP_L_VENTA")
					.returningResultSet(ConstanteUtil.R_LISTA, new VentaResponseMapper());

			MapSqlParameterSource in = new MapSqlParameterSource();

			Map<String, Object> out = jdbcCall.execute(in);

			rCodigo = Integer.parseInt(out.get(ConstanteUtil.R_CODIGO).toString());
			rMensaje = out.get(ConstanteUtil.R_MENSAJE).toString();

			if (rCodigo == 0) {// CONSULTA CORRECTA
				lista = (List<VentaResponse>) out.get(ConstanteUtil.R_LISTA);
			} else {
				lista = null;
			}
			outResponse.setrCodigo(rCodigo);
			outResponse.setrMensaje(rMensaje);
			outResponse.setResult(lista);
		} catch (Exception e) {
			outResponse.setrCodigo(500);
			outResponse.setrMensaje(e.getMessage());
			outResponse.setResult(null);
		}
		return outResponse;
	}
}
