package com.security.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.security.Entity.User;
import com.security.helper.LoginRequest;
import com.security.helper.LoginResponse;
import com.security.jwt.JwtUtils;
import com.security.service.UserService;

@RestController
public class MainController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private JwtUtils jwtUtils;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	//@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/admin/save")
	public void saveUser(@RequestBody User user) {
		userService.savaUser(user);
	}
	
	//@PreAuthorize("hasRole('NORMAL')")
	@GetMapping("/user/getAllUsers")
	public List<User> getAllUsers(){
		System.out.println("Main Controller");
		return userService.getAllUsers();
	}
	
	@GetMapping("/user/getById/{id}")
	public ResponseEntity<?> getById(@PathVariable Long id) {
		User user=userService.getById(id);
		if(user==null) {
			return ResponseEntity.status(404).body("User not found");
		}
		return ResponseEntity.of(Optional.of(user));
	}
	
	@PostMapping("/jwt/signin")
	public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest){
		Authentication authentication;
		try {
			authentication=authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),loginRequest.getPassword()));
		}catch(AuthenticationException e) {
			Map<String,Object> map=new HashMap<>();
			map.put("message", "BAD CREDENTIALS");
			map.put("status", false);
			return new ResponseEntity<Object>(map,HttpStatus.NOT_FOUND);
		}

		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		UserDetails userDetails=(UserDetails) authentication.getPrincipal();
		String jwtToken=jwtUtils.generateTokenFromUsername(userDetails);
		List<String> roles=userDetails.getAuthorities().stream()
				.map(item->item.getAuthority())
				.collect(Collectors.toList());
		
		LoginResponse response=new LoginResponse(jwtToken,userDetails.getUsername(),roles);
		return ResponseEntity.ok(response);
	}
}
