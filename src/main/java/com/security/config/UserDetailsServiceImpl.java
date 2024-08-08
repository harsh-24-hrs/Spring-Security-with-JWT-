package com.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.security.Entity.User;
import com.security.Repository.UserRepo;

public class UserDetailsServiceImpl implements UserDetailsService{

	@Autowired
	UserRepo userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user=userRepo.findByUsername(username);
		if(user==null) {
			throw new UsernameNotFoundException("UserNotFound");
		}
//		System.out.println("loadUserByUserName method");
//		System.out.println("loaded User is :"+user);
		return new CustomUserDetails(user);
		
	}
	
}
