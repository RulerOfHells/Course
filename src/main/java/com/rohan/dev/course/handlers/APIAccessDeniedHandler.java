package com.rohan.dev.course.handlers;

import static java.time.LocalDateTime.now;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rohan.dev.course.domain.APIResponse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class APIAccessDeniedHandler implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		
		APIResponse res = new APIResponse().setTimeStamp(now().toString())
				 .setMessage("You don't have sufficient perms")
				 .setStatus(HttpStatus.FORBIDDEN)
				 .setStatusCode(HttpStatus.FORBIDDEN.value());
		
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(res.getStatusCode());
		
		var outputStream = response.getOutputStream();
		var mapper = new ObjectMapper();
		mapper.writeValue(outputStream, res);
		
		outputStream.flush();
		
	}

}
