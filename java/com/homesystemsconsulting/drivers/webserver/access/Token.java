package com.homesystemsconsulting.drivers.webserver.access;

import java.util.UUID;

import com.homesystemsconsulting.drivers.webserver.HttpRequestHeader;

public class Token {
	
	private final String uuid;
	private final User user;
	private final String userAgent;

	public Token(User user, HttpRequestHeader httpRequestHeader) {
		this.uuid = UUID.randomUUID().toString();
		this.user = user;
		this.userAgent = httpRequestHeader.getUserAgent();
	}

	public String getUUID() {
		return uuid;
	}
	
	public User getUser() {
		return user;
	}
	
	public boolean match(HttpRequestHeader httpRequestHeader) {
		return this.userAgent == null || this.userAgent.equals(httpRequestHeader.getUserAgent());
	}
}
