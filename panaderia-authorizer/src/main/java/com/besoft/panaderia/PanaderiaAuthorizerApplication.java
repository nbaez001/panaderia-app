package com.besoft.panaderia;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

@SpringBootApplication
@EnableAuthorizationServer
public class PanaderiaAuthorizerApplication {
	private static final Logger log = LoggerFactory.getLogger(PanaderiaAuthorizerApplication.class);

	public static void main(String[] args) {
		log.info("---------Start class Application---------");
		SpringApplication.run(PanaderiaAuthorizerApplication.class, args);
	}
}
