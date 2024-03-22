package com.cooksys.socialmediaassignment.team2.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.socialmediaassignment.team2.services.ValidateService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/validate")
public class ValidateController {
	
	private final ValidateService validateService;
	
	
	@GetMapping("/tag/exists/{label}")
	@ResponseStatus(HttpStatus.OK)
	public boolean doesHashtagExist(@PathVariable("label") String label) {
		return validateService.doesHashtagExist(label);
		// Checks whether or not a given hashtag exists.
		// Postman Example Input: localhost:8080/validate/tag/exists/%23mario
	}
	
	
	@GetMapping("/username/exists/@{username}")
	@ResponseStatus(HttpStatus.OK)
	public boolean doesUsernameExist(@PathVariable("username") String username) {
		return validateService.doesUsernameExist(username);
		// Checks whether or not a given username exists.
		// Postman Example Input: localhost:8080/validate/username/exists/@therealmc
	}
	
	@GetMapping("/username/available/@{username}")
	@ResponseStatus(HttpStatus.OK)
	public boolean isUsernameAvailable(@PathVariable("username") String username) {
		return validateService.isUsernameAvailable(username);
		// Checks whether or not a given username is available.
		// Postman Example Input: localhost:8080/validate/username/available/@therealmc
	}

}
