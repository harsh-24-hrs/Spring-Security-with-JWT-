package com.security.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.security.jwt.AuthEntryPointJwt;
import com.security.jwt.AuthFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class MySecurityConfig {
	
	@Autowired
	AuthEntryPointJwt authenticationEntryPointJwt;
	
	@Bean
	public AuthFilter authenticationFilter() {
		return new AuthFilter();
	}
	
	@Bean
	public SecurityFilterChain filter(HttpSecurity http)throws Exception {
		System.out.println("Security Filter Chain");
		http.authorizeHttpRequests((request)->
				request.requestMatchers("jwt/signin").permitAll()
				.anyRequest().authenticated()
				);
		
		http.sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				);
		
		http.exceptionHandling(exception->exception.authenticationEntryPoint(authenticationEntryPointJwt));
		
		http.headers(headers->headers
				.frameOptions(frameOption->frameOption
						.sameOrigin()));
		
		http.csrf().disable();
		http.addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}
	
	
//	@Bean
//	public UserDetailsService userDetailsService() {
//		
//		UserDetails user=User.withUsername("user")
//				.password("user")
//				.roles("NORMAL")
//				.build();
//		
//		UserDetails admin=User.withUsername("admin")
//				.password("{noop}admin")
//				.roles("ADMIN")
//				.build(); 
//		return new InMemoryUserDetailsManager(admin,user);
//	}
	
	@Bean
	public UserDetailsService userDetailsService() {
		System.out.println("UserDetailsService");
		return new UserDetailsServiceImpl();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		System.out.println("Password Encoder");
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception {
		return builder.getAuthenticationManager();
	}
}
