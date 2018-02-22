package com.example.demo.controller;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customers")
public class CustomerApiController {

	private static final Logger log = LoggerFactory.getLogger(CustomerApiController.class);

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<String> getCustomers() {
		log.info("Context is {}", SecurityContextHolder.getContext());
		log.info("Authentication is {}", SecurityContextHolder.getContext().getAuthentication());
		log.info("UserAuthentication is {}", ((OAuth2Authentication)SecurityContextHolder.getContext().getAuthentication()).getUserAuthentication());
		log.info("Principale is {}", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		log.info("Details is {}", SecurityContextHolder.getContext().getAuthentication().getDetails());
		log.info("Returning CUSTOMERS list.");
		return Arrays.asList("Scott Rossillo", "Kyung Lee", "Keith Leggins", "Ben Loy");
	}
}