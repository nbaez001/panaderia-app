package com.besoft.panaderia.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/home")
@Api(value = "API home")
public class HomeController {

	@GetMapping
	public String index() {
		return "works";
	}
}
