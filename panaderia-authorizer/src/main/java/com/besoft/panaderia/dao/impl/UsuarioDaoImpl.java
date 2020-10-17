package com.besoft.panaderia.dao.impl;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Repository;

import com.besoft.panaderia.dao.UsuarioDao;
import com.besoft.panaderia.dto.response.OutResponse;
import com.besoft.panaderia.dto.response.RolResponse;
import com.besoft.panaderia.dto.response.UsuarioResponse;
import com.besoft.panaderia.dto.response.mapper.RolResponseMapper;
import com.besoft.panaderia.dto.response.mapper.UsuarioMapper;
import com.besoft.panaderia.util.ConstanteUtil;

@Repository
public class UsuarioDaoImpl implements UsuarioDao {

	Logger log = LoggerFactory.getLogger(UsuarioDaoImpl.class);

	@Autowired
	DataSource dataSource;

	@SuppressWarnings("unchecked")
	@Override
	public OutResponse<UsuarioResponse> buscarUsuario(String username) {
		log.info("[BUSCAR USUARIO][DAO][INICIO]");
		OutResponse<UsuarioResponse> outResponse = new OutResponse<>();
		UsuarioResponse user = null;
		List<RolResponse> listaRol = null;

		Integer rCodigo = -1;
		String rMensaje = "";
		try {
			SimpleJdbcCall jdbcCall = new SimpleJdbcCall(dataSource).withSchemaName(ConstanteUtil.BD_SCHEMA)
					.withCatalogName(ConstanteUtil.BD_PCK_PSEG_AUTENTICACION).withProcedureName("SP_BUSCAR_USUARIO")
					.returningResultSet(ConstanteUtil.R_RESULT, new UsuarioMapper())
					.returningResultSet(ConstanteUtil.R_RESULT_DET, new RolResponseMapper());

			MapSqlParameterSource in = new MapSqlParameterSource();
			in.addValue("I_USERNAME", username, Types.VARCHAR);
			log.info("[BUSCAR USUARIO][DAO][INPUT][" + in.toString() + "]");

			Map<String, Object> out = jdbcCall.execute(in);
			log.info("[BUSCAR USUARIO][DAO][OUTPUT][" + out.toString() + "]");

			rCodigo = Integer.parseInt(out.get(ConstanteUtil.R_CODIGO).toString());
			rMensaje = out.get(ConstanteUtil.R_MENSAJE).toString();

			if (rCodigo == 0) {// CONSULTA CORRECTA
				user = ((List<UsuarioResponse>) out.get(ConstanteUtil.R_RESULT)).get(0);
				listaRol = (List<RolResponse>) out.get(ConstanteUtil.R_RESULT_DET);

				Collection<GrantedAuthority> list = new ArrayList<>();
				for (RolResponse p : listaRol) {
					GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(p.getNombre());
					list.add(grantedAuthority);
				}

				user.setGrantedAuthoritiesList(list);

				outResponse.setrCodigo(rCodigo);
				outResponse.setrMensaje(rMensaje);
				outResponse.setrResult(user);
			} else {
				outResponse.setrCodigo(rCodigo);
				outResponse.setrMensaje(rMensaje);
				outResponse.setrResult(null);
			}
		} catch (Exception e) {
			outResponse.setrCodigo(500);
			outResponse.setrMensaje(e.getMessage());
			outResponse.setrResult(null);
			log.info("[BUSCAR USUARIO][DAO][EXCEPCION][" + ExceptionUtils.getStackTrace(e) + "]");
		}
		log.info("[BUSCAR USUARIO][DAO][FIN]");
		return outResponse;
	}

}
