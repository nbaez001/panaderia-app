package com.besoft.panaderia.service;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.besoft.panaderia.dao.UsuarioDao;
import com.besoft.panaderia.dto.CustomUser;
import com.besoft.panaderia.dto.response.OutResponse;
import com.besoft.panaderia.dto.response.UsuarioResponse;

@Service
public class UsuarioService implements UserDetailsService {

	Logger log = LoggerFactory.getLogger(UsuarioService.class);

	@Autowired
	UsuarioDao usuarioDao;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.info("[BUSCAR USUARIO][SERVICE][INICIO]");
		OutResponse<UsuarioResponse> out = null;
		try {
			out = usuarioDao.buscarUsuario(username);
			if (out.getrCodigo().equals(0)) {
				CustomUser customUser = new CustomUser(out.getrResult());
				log.info("[BUSCAR USUARIO][SERVICE][FIN]");
				return customUser;
			} else {
				log.info("[BUSCAR USUARIO][SERVICE][ERROR]");
				throw new UsernameNotFoundException("User " + username + " was not found in the database");
			}
		} catch (Exception e) {
			log.info("[BUSCAR USUARIO][SERVICE][EXCEPTION][" + ExceptionUtils.getStackTrace(e) + "]");
			throw new UsernameNotFoundException("User " + username + " was not found in the database");
		}
	}

}
