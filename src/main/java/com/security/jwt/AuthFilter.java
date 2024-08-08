package com.security.jwt;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.security.config.CustomUserDetails;
import com.security.config.UserDetailsServiceImpl;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AuthFilter extends OncePerRequestFilter{
	
	@Autowired
	private JwtUtils jwtUtils;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		log.info("inside our custom filter");
		try {
			
			String jwt=jwtUtils.getJwtToken(request);
			log.info("token got from request in filter");
			
			if(jwt!=null && jwtUtils.validateJwtToken(jwt)) {
				String username=jwtUtils.getUsernameFromJwtToken(jwt);
				
				UserDetails userDetails=userDetailsService.loadUserByUsername(username);
				
				UsernamePasswordAuthenticationToken authentication=new UsernamePasswordAuthenticationToken(userDetails,
						null,
						userDetails.getAuthorities());
				
				log.info("Authorities from Jwt:{} ",userDetails.getAuthorities());
				
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
			
		}catch(Exception e) {
			log.info("cannot set authentication {}:",e);
		}
		
		filterChain.doFilter(request, response);
		
	}

}
