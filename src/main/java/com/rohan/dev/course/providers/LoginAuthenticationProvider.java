package com.rohan.dev.course.providers;

import com.rohan.dev.course.exceptions.MfaRequiredException;
import com.rohan.dev.course.repository.impl.UserRepositoryImpl;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;

public class LoginAuthenticationProvider extends DaoAuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Authentication auth = super.authenticate(authentication);
        UserDetailsService userDetailsService = getUserDetailsService();
        if(userDetailsService instanceof UserRepositoryImpl && auth != null) {
            String email = authentication.getPrincipal().toString();
            boolean isMfaRequired = ((UserRepositoryImpl) userDetailsService).getUserByEmail(email).isUsingMFA();
            if(isMfaRequired)
                throw new MfaRequiredException(email);
        }
        return auth;
    }
}
