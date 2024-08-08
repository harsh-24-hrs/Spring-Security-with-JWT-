package com.security.jwt;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;

@Slf4j
@Component
public class JwtUtils {

	@Value("${spring.security.jwt.maxtime}")
	private int maxExpiryTimeInMillisec;
	
	@Value("${spring.security.jwt.secretKey}")
	private String JwtSecret;
	
	public String getJwtToken(HttpServletRequest request) {
		String bearerToken= request.getHeader("Authorization");
		log.info("Getting Jwt Token From request Header:{}",bearerToken);
		
		if(bearerToken!=null && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}
	
	
	public String generateTokenFromUsername(UserDetails userDetails) {
		String username=userDetails.getUsername();
		log.info("Building Token from username and username={}",username);
		
		//putting extra informatin in the payload;
		Map<String,Object> claims=new HashMap();
		claims.put("username", username);
		claims.put("class", userDetails.getClass().getName());
		claims.put("role", userDetails.getAuthorities().toString());
		return Jwts.builder()
				.claims(claims)
				.subject(username)
				.issuedAt(new Date())
				.expiration(new Date(new Date().getTime()+ maxExpiryTimeInMillisec))
				.signWith(key()) 
				.compact();
	}
	
	public String getUsernameFromJwtToken(String token) {
		log.info("getting username from token where token={}",token);
		return Jwts.parser()
				.verifyWith((SecretKey)key())
				.build()
				.parseSignedClaims(token)
				.getPayload()
				.getSubject();
	}
	
	
	public boolean validateJwtToken(String authToken) {
		log.info("validating token");
		try{
			Jwts.parser()
				.verifyWith((SecretKey)key())
				.build()
				.parseSignedClaims(authToken);
			return true;
		}
		catch(MalformedJwtException e) {
			log.info("Invalid Jwt Token :{}",e.getMessage());
		}
		catch(UnsupportedJwtException e) {
			log.info("Jwt Token is unsupported: {}",e.getMessage());
		}
		catch(IllegalArgumentException e) {
			log.info("Jwt Claims are empty:{}",e.getMessage());
		}
		catch(ExpiredJwtException e) {
			log.info("JWT token is expired:{}",e.getMessage());
		}
		return false;
	}
	
	private Key key() {
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(JwtSecret));
	}
}
