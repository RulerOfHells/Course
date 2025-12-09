package com.rohan.dev.course.handlers;

import com.rohan.dev.course.exceptions.MfaRequiredException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.support.SessionFlashMapManager;

import java.io.IOException;

@Component
public class LoginFailureHandler implements AuthenticationFailureHandler {

    private final SessionFlashMapManager flashMapManager = new SessionFlashMapManager();

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        System.out.println(request.getRequestURI());
        if(exception instanceof BadCredentialsException) {
            response.sendRedirect("/login?error=bad_credentials");
        }
        else if(exception instanceof MfaRequiredException) {
            FlashMap flashMap = new FlashMap();
            flashMap.put("mfaRequired", Boolean.TRUE);
            flashMap.put("email", exception.getMessage());
            flashMapManager.saveOutputFlashMap(flashMap, request, response);
            response.sendRedirect("/mfa-login");
        }
    }
}
