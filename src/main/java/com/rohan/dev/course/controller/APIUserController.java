package com.rohan.dev.course.controller;

import static java.time.LocalDateTime.now;

import java.net.URI;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.rohan.dev.course.domain.APIResponse;
import com.rohan.dev.course.domain.User;
import com.rohan.dev.course.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class APIUserController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@PostMapping("/login")
	public ResponseEntity<APIResponse> login(String email, String password) {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
		return null;
	}
	
	@PostMapping("/users")
	public ResponseEntity<APIResponse> createUser(@RequestBody @Valid User userin) {
		var user =  userService.createUser(userin);
		return ResponseEntity.created(getUri()).body(
					new APIResponse().setTimeStamp(now().toString())
									 .setData(Map.of("user", user))
									 .setMessage("User created!")
									 .setStatus(HttpStatus.CREATED)
									 .setStatusCode(HttpStatus.CREATED.value())
				);
	}

	private URI getUri() {
		return URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/get/<userID>").toUriString());
	}
}
