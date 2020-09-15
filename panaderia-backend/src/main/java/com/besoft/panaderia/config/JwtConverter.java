package com.besoft.panaderia.config;

//@Component
public class JwtConverter {//extends DefaultAccessTokenConverter implements JwtAccessTokenConverterConfigurer {
//
//	@Override
//	public void configure(JwtAccessTokenConverter converter) {
//		converter.setAccessTokenConverter(this);
//	}
//
//	@Override
//	public OAuth2Authentication extractAuthentication(Map<String, ?> map) {
//		OAuth2Authentication auth = super.extractAuthentication(map);
//		TokenMapper details = new TokenMapper();
//		if (map.get("id") != null)
//			details.setId(Long.parseLong(map.get("id").toString()));
//		if (map.get("nombre") != null)
//			details.setNombre(map.get("nombre").toString());
//		if (map.get("apePaterno") != null)
//			details.setApePaterno(map.get("apePaterno").toString());
//		if (map.get("apeMaterno") != null)
//			details.setApeMaterno(map.get("apeMaterno").toString());
//		if (map.get("email") != null)
//			details.setEmail(map.get("email").toString());
//		if (map.get("telefono") != null)
//			details.setTelefono(map.get("telefono").toString());
//		if (auth.getAuthorities() != null && !auth.getAuthorities().isEmpty()) {
//			List<String> authorities = new ArrayList<>();
//			for (GrantedAuthority gn : auth.getAuthorities()) {
//				authorities.add(gn.getAuthority());
//			}
//		}
//		auth.setDetails(details);
//		return auth;
//	}

}