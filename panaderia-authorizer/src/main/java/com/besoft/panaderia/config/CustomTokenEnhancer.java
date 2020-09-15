package com.besoft.panaderia.config;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import com.besoft.panaderia.dto.CustomUser;

public class CustomTokenEnhancer extends JwtAccessTokenConverter {

	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		CustomUser user = (CustomUser) authentication.getPrincipal();
		Map<String, Object> info = new LinkedHashMap<>(accessToken.getAdditionalInformation());
		info.put("id", user.getId() != null ? user.getId() : 0);
		info.put("nombre", user.getNombre() != null ? user.getNombre() : "");
		info.put("apePaterno", user.getApePaterno() != null ? user.getApePaterno() : "");
		info.put("apeMaterno", user.getApeMaterno() != null ? user.getApeMaterno() : "");
		info.put("email", user.getEmail() != null ? user.getEmail() : "");
		info.put("telefono", user.getTelefono() != null ? user.getTelefono() : "");
		
		DefaultOAuth2AccessToken customAccessToken = new DefaultOAuth2AccessToken(accessToken);
		customAccessToken.setAdditionalInformation(info);
		return super.enhance(customAccessToken, authentication);
	}
}
