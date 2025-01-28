package com.UserManagment.JWT.userManagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.UserManagment.JWT.userManagement")
public class JwtUserManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(JwtUserManagementApplication.class, args);
	}

}
