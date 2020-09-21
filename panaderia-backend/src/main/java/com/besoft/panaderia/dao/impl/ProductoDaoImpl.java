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

import com.besoft.panaderia.dao.ProductoDao;
import com.besoft.panaderia.dto.request.ProductoBuscarRequest;
import com.besoft.panaderia.dto.request.ProductoRequest;
import com.besoft.panaderia.dto.response.OutResponse;
import com.besoft.panaderia.dto.response.ProductoResponse;
import com.besoft.panaderia.dto.response.mapper.ProductoResponseMapper;
import com.besoft.panaderia.util.ConstanteUtil;
import com.besoft.panaderia.util.DateUtil;

@Repository
public class ProductoDaoImpl implements ProductoDao {

	@Autowired
	DataSource dataSource;

	@Override
	public OutResponse<List<ProductoResponse>> listarProducto(ProductoBuscarRequest req) {
		OutResponse<List<ProductoResponse>> outResponse = new OutResponse<>();

		List<ProductoResponse> lista = new ArrayList<>();
		Integer rCodigo = 0;
		String rMensaje = "";
		try {
			SimpleJdbcCall jdbcCall = new SimpleJdbcCall(dataSource).withSchemaName(ConstanteUtil.BD_SCHEMA)
					.withCatalogName(ConstanteUtil.BD_PCK_ADMINISTRACION).withProcedureName("SP_L_PRODUCTO")
					.returningResultSet(ConstanteUtil.R_LISTA, new ProductoResponseMapper());

			MapSqlParameterSource in = new MapSqlParameterSource();
			in.addValue("I_NOMBRE", req.getNombre(), Types.VARCHAR);
			in.addValue("I_FEC_INICIO", DateUtil.formatSlashDDMMYYYY(req.getFecInicio()), Types.VARCHAR);
			in.addValue("I_FEC_FIN", DateUtil.formatSlashDDMMYYYY(req.getFecFin()), Types.VARCHAR);

			Map<String, Object> out = jdbcCall.execute(in);

			rCodigo = Integer.parseInt(out.get(ConstanteUtil.R_CODIGO).toString());
			rMensaje = out.get(ConstanteUtil.R_MENSAJE).toString();

			if (rCodigo == ConstanteUtil.R_COD_EXITO) {// CONSULTA CORRECTA
				lista = (List<ProductoResponse>) out.get(ConstanteUtil.R_LISTA);
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
	public OutResponse<ProductoResponse> registrarProducto(ProductoRequest req) {
		OutResponse<ProductoResponse> outResponse = new OutResponse<>();
		ProductoResponse res = new ProductoResponse();

		Integer rCodigo = 0;
		String rMensaje = "";

		Long id = 0L;
		String codProducto = "";
		try {
			SimpleJdbcCall jdbcCall = new SimpleJdbcCall(dataSource).withSchemaName(ConstanteUtil.BD_SCHEMA)
					.withCatalogName(ConstanteUtil.BD_PCK_ADMINISTRACION).withProcedureName("SP_I_PRODUCTO");

			MapSqlParameterSource in = new MapSqlParameterSource();
			in.addValue("I_IDT_UNIDAD_MEDIDA", req.getIdtUnidadMedida(), Types.NUMERIC);
			in.addValue("I_NOMBRE", req.getNombre(), Types.VARCHAR);
			in.addValue("I_CODIGO", req.getCodigo(), Types.VARCHAR);
			in.addValue("I_PRECIO", req.getPrecio(), Types.NUMERIC);
			in.addValue("I_FLG_ACTIVO", req.getFlgActivo(), Types.NUMERIC);
			in.addValue("I_ID_USUARIO_CREA", req.getIdUsuarioCrea(), Types.NUMERIC);
			in.addValue("I_FEC_USUARIO_CREA", DateUtil.formatSlashDDMMYYYY(req.getFecUsuarioCrea()), Types.VARCHAR);

			Map<String, Object> out = jdbcCall.execute(in);

			rCodigo = Integer.parseInt(out.get(ConstanteUtil.R_CODIGO).toString());
			rMensaje = out.get(ConstanteUtil.R_MENSAJE).toString();

			if (rCodigo == ConstanteUtil.R_COD_EXITO) {// CONSULTA CORRECTA
				id = Long.parseLong(out.get("R_ID").toString());
				codProducto = out.get("I_CODIGO").toString();

				res.setId(id);
				res.setIdtUnidadMedida(req.getIdtUnidadMedida());
				res.setNomUnidadMedida(req.getNomUnidadMedida());
				res.setNombre(req.getNombre());
				res.setCodigo(codProducto);
				res.setPrecio(req.getPrecio());
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
	public OutResponse<ProductoResponse> modificarProducto(ProductoRequest req) {
		OutResponse<ProductoResponse> outResponse = new OutResponse<>();
		ProductoResponse res = new ProductoResponse();

		Integer rCodigo = 0;
		String rMensaje = "";
		try {
			SimpleJdbcCall jdbcCall = new SimpleJdbcCall(dataSource).withSchemaName(ConstanteUtil.BD_SCHEMA)
					.withCatalogName(ConstanteUtil.BD_PCK_ADMINISTRACION).withProcedureName("SP_U_PRODUCTO");

			MapSqlParameterSource in = new MapSqlParameterSource();
			in.addValue("I_ID", req.getId(), Types.NUMERIC);
			in.addValue("I_IDT_UNIDAD_MEDIDA", req.getIdtUnidadMedida(), Types.NUMERIC);
			in.addValue("I_NOMBRE", req.getNombre(), Types.VARCHAR);
			in.addValue("I_CODIGO", req.getCodigo(), Types.VARCHAR);
			in.addValue("I_PRECIO", req.getPrecio(), Types.NUMERIC);
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
				res.setPrecio(req.getPrecio());
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
	public OutResponse<ProductoResponse> eliminarProducto(ProductoRequest req) {
		OutResponse<ProductoResponse> outResponse = new OutResponse<>();
		ProductoResponse res = new ProductoResponse();

		Integer rCodigo = 0;
		String rMensaje = "";
		try {
			SimpleJdbcCall jdbcCall = new SimpleJdbcCall(dataSource).withSchemaName(ConstanteUtil.BD_SCHEMA)
					.withCatalogName(ConstanteUtil.BD_PCK_ADMINISTRACION).withProcedureName("SP_D_PRODUCTO");

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
