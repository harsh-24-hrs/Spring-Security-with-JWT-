package com.security.service;

import java.util.*;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.security.Entity.User;
import com.security.Repository.UserRepo;

@Service
public class UserService {

	@Autowired
	private UserRepo repo;
	
	@Autowired
	private PasswordEncoder bCryptPasswordEncoder;
	
	public void savaUser(User user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		System.out.println("Original password is "+user.getPassword());
		repo.save(user);
	}
	
	public List<User> getAllUsers(){
		System.out.println("Get All Users in UserService class");
		return repo.findAll();
	}
	
	public User getById(Long id) {
		Optional<User> user=repo.findById(id);
		if(user.isPresent()) {
			return user.get();
		}
		return null;
	}
	
}
