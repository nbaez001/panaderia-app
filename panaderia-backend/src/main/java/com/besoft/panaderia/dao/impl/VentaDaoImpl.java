package com.besoft.panaderia.dao.impl;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import com.besoft.panaderia.dao.VentaDao;
import com.besoft.panaderia.dto.request.DetalleVentaRequest;
import com.besoft.panaderia.dto.request.VentaBuscarRequest;
import com.besoft.panaderia.dto.request.VentaRequest;
import com.besoft.panaderia.dto.response.DetalleVentaResponse;
import com.besoft.panaderia.dto.response.OutResponse;
import com.besoft.panaderia.dto.response.VentaResponse;
import com.besoft.panaderia.dto.response.mapper.DetalleVentaResponseMapper;
import com.besoft.panaderia.dto.response.mapper.VentaResponseMapper;
import com.besoft.panaderia.util.ConstanteUtil;
import com.besoft.panaderia.util.DateUtil;

@Repository
public class VentaDaoImpl implements VentaDao {

	Logger log = LoggerFactory.getLogger(VentaDaoImpl.class);

	@Autowired
	DataSource dataSource;

	@Override
	public OutResponse<VentaResponse> registrarVenta(VentaRequest c) {
		log.info("[REGISTRAR VENTA][DAO][INICIO]");
		OutResponse<VentaResponse> outResponse = new OutResponse<>();
		VentaResponse resp = new VentaResponse();

		Integer rCodigo = 0;
		String rMensaje = "";

		Long id = 0L;
		String serie = "";
		String numero = "";
		try {
			SimpleJdbcCall jdbcCall = new SimpleJdbcCall(dataSource).withSchemaName(ConstanteUtil.BD_SCHEMA)
					.withCatalogName(ConstanteUtil.BD_PCK_VENTA).withProcedureName("SP_I_VENTA");

			MapSqlParameterSource in = new MapSqlParameterSource();
			in.addValue("I_ID_COMPROBANTE", c.getIdComprobante(), Types.NUMERIC);
			in.addValue("I_TOTAL", c.getTotal(), Types.DECIMAL);
			in.addValue("I_FLG_ACTIVO", c.getFlgActivo(), Types.NUMERIC);
			in.addValue("I_ID_USUARIO_CREA", c.getIdUsuarioCrea(), Types.NUMERIC);
			in.addValue("I_FEC_USUARIO_CREA", DateUtil.formatSlashDDMMYYYY(c.getFecUsuarioCrea()), Types.VARCHAR);
			in.addValue("I_DETALLE_VENTA", c.getDetalleVenta(), Types.VARCHAR);
			log.info("[REGISTRAR VENTA][DAO][INPUT][" + in.toString() + "]");

			Map<String, Object> out = jdbcCall.execute(in);
			log.info("[REGISTRAR VENTA][DAO][OUTPUT][" + out.toString() + "]");

			rCodigo = Integer.parseInt(out.get(ConstanteUtil.R_CODIGO).toString());
			rMensaje = out.get(ConstanteUtil.R_MENSAJE).toString();

			if (rCodigo == 0) {// CONSULTA CORRECTA
				id = Long.parseLong(out.get("R_ID").toString());
				serie = out.get("R_SERIE").toString();
				numero = out.get("R_NUMERO").toString();

				resp.setId(id);
				resp.setIdComprobante(c.getIdComprobante());
				resp.setSerie(serie);
				resp.setNumero(numero);
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
			outResponse.setrResult(resp);
			log.info("[REGISTRAR VENTA][DAO][EXITO][" + outResponse.toString() + "]");
		} catch (Exception e) {
			outResponse.setrCodigo(500);
			outResponse.setrMensaje(e.getMessage());
			outResponse.setrResult(null);
			log.info("[REGISTRAR VENTA][DAO][EXCEPCION][" + outResponse.toString() + "]");
		}
		log.info("[REGISTRAR VENTA][DAO][FIN]");
		return outResponse;
	}

	@Override
	public OutResponse<List<VentaResponse>> listarVenta(VentaBuscarRequest req) {
		OutResponse<List<VentaResponse>> outResponse = new OutResponse<>();

		List<VentaResponse> lista = new ArrayList<>();

		Integer rCodigo = 0;
		String rMensaje = "";

		try {
			SimpleJdbcCall jdbcCall = new SimpleJdbcCall(dataSource).withSchemaName(ConstanteUtil.BD_SCHEMA)
					.withCatalogName(ConstanteUtil.BD_PCK_VENTA).withProcedureName("SP_L_VENTA")
					.returningResultSet(ConstanteUtil.R_LISTA, new VentaResponseMapper());

			MapSqlParameterSource in = new MapSqlParameterSource();
			in.addValue("I_FEC_INICIO", DateUtil.formatSlashDDMMYYYY(req.getFecInicio()), Types.VARCHAR);
			in.addValue("I_FEC_FIN", DateUtil.formatSlashDDMMYYYY(req.getFecFin()), Types.VARCHAR);

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
			outResponse.setrResult(lista);
		} catch (Exception e) {
			outResponse.setrCodigo(500);
			outResponse.setrMensaje(e.getMessage());
			outResponse.setrResult(null);
		}
		return outResponse;
	}

	@Override
	public OutResponse<List<DetalleVentaResponse>> imprimirTicketVenta(VentaRequest c) {
		log.info("[IMPRIMIR TICKET VENTA][DAO][INICIO]");
		OutResponse<List<DetalleVentaResponse>> outResponse = new OutResponse<>();
		List<DetalleVentaResponse> ldvr = new ArrayList<>();

		Integer rCodigo = 0;
		String rMensaje = "";

		Long id = 0L;
		try {
			SimpleJdbcCall jdbcCall = new SimpleJdbcCall(dataSource).withSchemaName(ConstanteUtil.BD_SCHEMA)
					.withCatalogName(ConstanteUtil.BD_PCK_VENTA).withProcedureName("SP_L_DETALLE_VENTA")
					.returningResultSet(ConstanteUtil.R_LISTA, new DetalleVentaResponseMapper());

			MapSqlParameterSource in = new MapSqlParameterSource();
			in.addValue("I_ID_VENTA", c.getId(), Types.NUMERIC);

			log.info("[IMPRIMIR TICKET VENTA][DAO][INPUT][" + in.toString() + "]");

			Map<String, Object> out = jdbcCall.execute(in);
			log.info("[IMPRIMIR TICKET VENTA][DAO][OUTPUT][" + out.toString() + "]");

			rCodigo = Integer.parseInt(out.get(ConstanteUtil.R_CODIGO).toString());
			rMensaje = out.get(ConstanteUtil.R_MENSAJE).toString();

			if (rCodigo == 0) {// CONSULTA CORRECTA
				ldvr = (List<DetalleVentaResponse>) out.get(ConstanteUtil.R_LISTA);
			} else {
				ldvr = null;
			}
			outResponse.setrCodigo(rCodigo);
			outResponse.setrMensaje(rMensaje);
			outResponse.setrResult(ldvr);
			log.info("[IMPRIMIR TICKET VENTA][DAO][EXITO][" + outResponse.toString() + "]");
		} catch (Exception e) {
			outResponse.setrCodigo(500);
			outResponse.setrMensaje(e.getMessage());
			outResponse.setrResult(null);
			log.info("[IMPRIMIR TICKET VENTA][DAO][EXCEPCION][" + outResponse.toString() + "]");
		}
		log.info("[IMPRIMIR TICKET VENTA][DAO][FIN]");

		return outResponse;
	}
}
