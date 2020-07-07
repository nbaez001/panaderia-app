package com.besoft.panaderia.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.annotations.Api;

@Controller
@RequestMapping("/home")
@CrossOrigin(origins = "http://localhost:4200")
@Api(value = "API home")
public class HomeController {

	@GetMapping
	public String index() {
		return "works";
	}
}
