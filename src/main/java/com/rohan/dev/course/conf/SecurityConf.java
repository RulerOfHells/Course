package com.rohan.dev.course.conf;

import com.rohan.dev.course.handlers.APIAccessDeniedHandler;
import com.rohan.dev.course.handlers.APIAuthenticationEntryPoint;
import com.rohan.dev.course.handlers.LoginFailureHandler;
import com.rohan.dev.course.providers.LoginAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConf {

	private static final String[] PUBLIC_URLS = {"/", "/login", "/process-login", "/logout", "/mfa-login"};
	private static final String[] DELETE_URLS = {"/users/remove/**", "/courses/remove/**", "/students/remove/**"};
	private static final String[] API_PUBLIC_URLS = {"/api/login/**", "/mfa-login"};
	private static final String[] API_DELETE_URLS = {"/api/users/remove/**", "/api/courses/remove/**", "/api/students/remove/**"};

	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@Autowired
	private APIAccessDeniedHandler apiADHandler;
	
	@Autowired
	private APIAuthenticationEntryPoint apiAuthEntryPoint;

    @Autowired
    private LoginFailureHandler loginFailureHandler;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Bean
	@Order(1)
	public SecurityFilterChain apiSecurityFilterChain(HttpSecurity https) throws Exception {
		
		https
			.securityMatcher("/api/**")
			.cors(AbstractHttpConfigurer::disable)
			.csrf(AbstractHttpConfigurer::disable)
			.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		
			.authorizeHttpRequests(ahr -> {
				ahr.requestMatchers(API_PUBLIC_URLS).permitAll();
				ahr.requestMatchers(HttpMethod.DELETE, API_DELETE_URLS[0]).hasAnyAuthority("DELETE:USER");
				ahr.requestMatchers(HttpMethod.DELETE, API_DELETE_URLS[1]).hasAnyAuthority("DELETE:COURSE");
				ahr.requestMatchers(HttpMethod.DELETE, API_DELETE_URLS[2]).hasAnyAuthority("DELETE:STUDENT");
				ahr.anyRequest().authenticated();
			})
			
			.exceptionHandling(eh -> eh.accessDeniedHandler(apiADHandler).authenticationEntryPoint(apiAuthEntryPoint));

		return https.build();
	}
	
	@Bean
	public SecurityFilterChain webSecurityFilterChain(HttpSecurity https) throws Exception {
		
		https
            .authenticationManager(authenticationManager())
			.formLogin(flc -> {
                flc.loginPage("/login");
                flc.usernameParameter("email");
                flc.loginProcessingUrl("/process-login");
                flc.defaultSuccessUrl("/", false);
                flc.failureHandler(loginFailureHandler);
            })
            .logout(loc -> {
                loc.logoutUrl("/logout");
                loc.logoutSuccessUrl("/login");
                loc.clearAuthentication(true);
            })
			.cors(AbstractHttpConfigurer::disable)
			.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
		
			.authorizeHttpRequests(ahr -> {
				ahr.requestMatchers(PUBLIC_URLS).permitAll();
				ahr.requestMatchers(HttpMethod.GET, DELETE_URLS[0]).hasAnyAuthority("DELETE:USER");
				ahr.requestMatchers(HttpMethod.GET, DELETE_URLS[1]).hasAnyAuthority("DELETE:COURSE");
				ahr.requestMatchers(HttpMethod.GET, DELETE_URLS[2]).hasAnyAuthority("DELETE:STUDENT");
				ahr.anyRequest().authenticated();
			})
			
			.exceptionHandling(eh -> eh.accessDeniedHandler(null).authenticationEntryPoint(null));
			
		
		return https.build();
	}
	
	@Bean
	public AuthenticationManager authenticationManager() {
		var authProvider = new LoginAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(encoder);
		return new ProviderManager(authProvider);
	}
	
}
