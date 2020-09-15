package com.besoft.panaderia.service;

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

	@Autowired
	UsuarioDao usuarioDao;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		OutResponse<UsuarioResponse> out = null;
		try {
			out = usuarioDao.buscarUsuario(username);
			if (out.getrCodigo().equals(0)) {
				CustomUser customUser = new CustomUser(out.getrResult());
				return customUser;
			} else {
				throw new UsernameNotFoundException("User " + username + " was not found in the database");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new UsernameNotFoundException("User " + username + " was not found in the database");
		}
	}

}
