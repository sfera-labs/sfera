package com.homesystemsconsulting.drivers.webserver.access;

import java.nio.file.Path;


public class User {

	final String username;
	final byte[] hashedPassword;
	final byte[] salt;

	public User(String username, byte[] hashedPassword, byte[] salt) {
		this.username = username;
		this.hashedPassword = hashedPassword;
		this.salt = salt;
	}
	
	public String getUsername() {
		return username;
	}

	public boolean isAuthorized(Path path) {
		// TODO Auto-generated method stub
		return true;
	}

}
