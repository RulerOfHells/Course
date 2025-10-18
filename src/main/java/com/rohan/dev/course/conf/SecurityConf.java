package com.rohan.dev.course.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConf {

	private static final String[] PUBLIC_URLS = {"/", "/login", "/home"};
	private static final String[] DELETE_URLS = {"/users/delete/**", "/courses/delete/**", "/students/delete/**"};

	@Autowired
	private BCryptPasswordEncoder encoder;
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity https) throws Exception {
		
		https.cors(cors -> cors.disable());
		https.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		https.authorizeHttpRequests(ahr -> ahr.requestMatchers(PUBLIC_URLS).permitAll());
		https.authorizeHttpRequests(ahr -> ahr.requestMatchers(HttpMethod.DELETE, DELETE_URLS[0]).hasAnyAuthority("DELETE:USER"));
		https.authorizeHttpRequests(ahr -> ahr.requestMatchers(HttpMethod.DELETE, DELETE_URLS[1]).hasAnyAuthority("DELETE:COURSE"));
		https.authorizeHttpRequests(ahr -> ahr.requestMatchers(HttpMethod.DELETE, DELETE_URLS[2]).hasAnyAuthority("DELETE:STUDENT"));
		https.exceptionHandling(eh -> eh.accessDeniedHandler(null).authenticationEntryPoint(null));
		
		https.authorizeHttpRequests(ahr -> ahr.anyRequest().authenticated());
		return https.build();
	}
	
	@Bean
	public AuthenticationManager authenticationManager() {
		var authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(null);
		authProvider.setPasswordEncoder(encoder);
		
		return new ProviderManager(authProvider);
	}
	
}
