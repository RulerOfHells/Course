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
import com.rohan.dev.course.dto.LoginForm;
import com.rohan.dev.course.dto.UserDTO;
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
	public ResponseEntity<APIResponse> login(@Valid LoginForm login) {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login.getEmail(), login.getPassword()));
		UserDTO userDTO = userService.getUserFromEmail(login.getEmail());
		
		return (!userDTO.isUsingMFA())? sendResponse(userDTO) : sendVerificationCode(userDTO);
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
	
	private ResponseEntity<APIResponse> sendVerificationCode(UserDTO userDTO) {
		userService.sendVerificationCode(userDTO);
		return ResponseEntity.ok().body(
				new APIResponse().setTimeStamp(now().toString())
				 .setData(Map.of("user", userDTO))
				 .setMessage("Verification Code sent!")
				 .setStatus(HttpStatus.OK)
				 .setStatusCode(HttpStatus.OK.value()));
	}
	
	private ResponseEntity<APIResponse> sendResponse(UserDTO userDTO) {
		return ResponseEntity.ok().body(
				new APIResponse().setTimeStamp(now().toString())
				 .setData(Map.of("user", userDTO))
				 .setMessage("Login successful!")
				 .setStatus(HttpStatus.OK)
				 .setStatusCode(HttpStatus.OK.value()));
	}
}
