package com.besoft.panaderia.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.besoft.panaderia.dto.api.request.ApiPersonaBuscarRequest;
import com.besoft.panaderia.dto.api.response.ApiOutResponse;
import com.besoft.panaderia.dto.api.response.ApiPersonaResponse;
import com.besoft.panaderia.service.PersonaService;

@Service
public class PersonaServiceImpl implements PersonaService {

	@Autowired
	RestTemplate restTemplate;

	@Value("${api.urlApiReniec}")
	public String urlApiReniec;

	@Override
	public ApiOutResponse<ApiPersonaResponse> buscarPersona(ApiPersonaBuscarRequest req) {
		ApiOutResponse<ApiPersonaResponse> out = null;
		try {
			HttpHeaders headers = new HttpHeaders();
			// headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			HttpEntity<ApiPersonaBuscarRequest> entity = new HttpEntity<ApiPersonaBuscarRequest>(req, headers);
			out = restTemplate.exchange(urlApiReniec, HttpMethod.POST, entity, ApiOutResponse.class).getBody();
		} catch (Exception e) {
			out = new ApiOutResponse<>();
			out.setrCodigo(500);
			out.setrMensaje(e.getMessage());
		}
		return out;
	}

}
