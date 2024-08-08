package com.spring.security.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.security.repository.UserRepository;
import com.spring.security.service.UserServiceImpl;

@RestController
public class MainController {
	
	@Autowired
	private UserRepository userRepository;
	
	private UserServiceImpl userService=new UserServiceImpl(userRepository);
	
	@GetMapping("/user/getAll")
	public List<User> getAllUsers() {
		return userService.getAllUsers();
	}

}
