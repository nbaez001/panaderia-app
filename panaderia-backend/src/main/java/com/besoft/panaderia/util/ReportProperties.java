package com.besoft.panaderia.util;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "reportes")
public class ReportProperties {

	private Map<String, String> pdf;

	public Map<String, String> getPdf() {
		return pdf;
	}

	public void setPdf(Map<String, String> pdf) {
		this.pdf = pdf;
	}

}
