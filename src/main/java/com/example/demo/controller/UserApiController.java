package com.example.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserApiController {

	private static final Logger log = LoggerFactory.getLogger(UserApiController.class);

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<String> getUsers() {
		log.info("Context is {}", SecurityContextHolder.getContext());
		log.info("Authentication is {}", SecurityContextHolder.getContext().getAuthentication());
		log.info("Principal is {}", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		log.info("Details is {}", SecurityContextHolder.getContext().getAuthentication().getDetails());
		log.info("Returning USERS list.");
		return Arrays.asList("lhauspie", "ddumortier", "calexandre", "jpotteau");
	}
}