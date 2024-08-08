package com.spring.security.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "UserTable")
public class User {
	
	@Id
	private Long id;
	@Column(name="name" , nullable = false , unique = true)
	private String username;
	@Column(length = 10 , nullable=false)
	private String password;

}
