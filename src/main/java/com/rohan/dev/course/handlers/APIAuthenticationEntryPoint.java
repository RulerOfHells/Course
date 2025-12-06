package com.rohan.dev.course.handlers;

import static java.time.LocalDateTime.now;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rohan.dev.course.domain.APIResponse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class APIAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		APIResponse res = new APIResponse().setTimeStamp(now().toString())
				 .setMessage("Please log in to access")
				 .setStatus(HttpStatus.UNAUTHORIZED)
				 .setStatusCode(HttpStatus.UNAUTHORIZED.value());
		
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(res.getStatusCode());
		
		var outputStream = response.getOutputStream();
		var mapper = new ObjectMapper();
		mapper.writeValue(outputStream, res);
		
		outputStream.flush();
	}
	
}
