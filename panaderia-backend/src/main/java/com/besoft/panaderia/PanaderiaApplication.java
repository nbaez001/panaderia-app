package com.besoft.panaderia;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
//@EnableResourceServer
public class PanaderiaApplication {

	private static final Logger log = LoggerFactory.getLogger(PanaderiaApplication.class);

	public static void main(String[] args) {
		log.info("---------Start class Application---------");
		SpringApplication.run(PanaderiaApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

}
