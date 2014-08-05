package com.homesystemsconsulting.drivers.webserver.access;

public class User {

	final String username;
	final byte[] hashedPassword;
	final byte[] salt;

	public User(String username, byte[] hashedPassword, byte[] salt) {
		this.username = username;
		this.hashedPassword = hashedPassword;
		this.salt = salt;
	}

}
