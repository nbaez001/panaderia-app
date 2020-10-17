package com.besoft.panaderia.dao.impl;

import java.sql.Types;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import com.besoft.panaderia.dao.PermisoDao;
import com.besoft.panaderia.dto.request.PermisoBuscarRequest;
import com.besoft.panaderia.dto.response.OutResponse;
import com.besoft.panaderia.dto.response.PermisoResponse;
import com.besoft.panaderia.dto.response.mapper.PermisorResponseMapper;
import com.besoft.panaderia.util.ConstanteUtil;

@Repository
public class PermisoDaoImpl implements PermisoDao {

	Logger log = LoggerFactory.getLogger(PermisoDaoImpl.class);

	@Autowired
	DataSource dataSource;

	@Override
	public OutResponse<List<PermisoResponse>> listarPermiso(PermisoBuscarRequest req) {
		log.info("[LISTAR PERMISO][DAO][INICIO]");
		OutResponse<List<PermisoResponse>> outResponse = new OutResponse<>();
		List<PermisoResponse> listaPermiso = null;

		Integer rCodigo = -1;
		String rMensaje = "";
		try {
			SimpleJdbcCall jdbcCall = new SimpleJdbcCall(dataSource).withSchemaName(ConstanteUtil.BD_SCHEMA)
					.withCatalogName(ConstanteUtil.BD_PCK_PSEG_AUTENTICACION).withProcedureName("SP_L_PERMISO")
					.returningResultSet(ConstanteUtil.R_RESULT, new PermisorResponseMapper());

			MapSqlParameterSource in = new MapSqlParameterSource();
			in.addValue("I_ID_USUARIO", req.getIdUsuario(), Types.NUMERIC);
			log.info("[LISTAR PERMISO][DAO][INPUT][" + in.toString() + "]");

			Map<String, Object> out = jdbcCall.execute(in);
			log.info("[LISTAR PERMISO][DAO][OUTPUT][" + out.toString() + "]");

			rCodigo = Integer.parseInt(out.get(ConstanteUtil.R_CODIGO).toString());
			rMensaje = out.get(ConstanteUtil.R_MENSAJE).toString();

			if (rCodigo == 0) {// CONSULTA CORRECTA
				listaPermiso = (List<PermisoResponse>) out.get(ConstanteUtil.R_RESULT);

				outResponse.setrCodigo(rCodigo);
				outResponse.setrMensaje(rMensaje);
				outResponse.setrResult(listaPermiso);
			} else {
				outResponse.setrCodigo(rCodigo);
				outResponse.setrMensaje(rMensaje);
				outResponse.setrResult(null);
			}
		} catch (Exception e) {
			outResponse.setrCodigo(500);
			outResponse.setrMensaje(e.getMessage());
			outResponse.setrResult(null);
			log.info("[LISTAR PERMISO][DAO][EXCEPCION][" + e.getMessage() + "]");
		}
		log.info("[LISTAR PERMISO][DAO][FIN]");
		return outResponse;
	}
}
