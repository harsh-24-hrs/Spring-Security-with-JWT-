package com.security.helper;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
	private String jwtToken;
	private String username;
	private List<String> roles;
}
