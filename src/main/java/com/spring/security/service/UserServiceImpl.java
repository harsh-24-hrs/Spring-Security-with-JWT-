package com.spring.security.service;

import java.util.List;

import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import com.spring.security.repository.UserRepository;

@Service
public class UserServiceImpl{

	
	private UserRepository userRepo;
	
	public UserServiceImpl(UserRepository userRepo) {
		super();
		this.userRepo = userRepo;
	}



	public List<User> getAllUsers(){
		return userRepo.findAll();
	}

}
