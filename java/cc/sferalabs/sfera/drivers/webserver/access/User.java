package cc.sferalabs.sfera.drivers.webserver.access;

import java.nio.file.Path;
import java.util.Base64;


public class User {

	final String username;
	final byte[] hashedPassword;
	final byte[] salt;

	public User(String username, byte[] hashedPassword, byte[] salt) {
		this.username = username;
		this.hashedPassword = hashedPassword;
		this.salt = salt;
	}
	
	public User(String username, String hashedPassword, String salt) {
		this(username, Base64.getDecoder().decode(hashedPassword), Base64.getDecoder().decode(salt));
	}

	public String getUsername() {
		return username;
	}

	public boolean isAuthorized(Path path) {
		// TODO Auto-generated method stub
		return true;
	}

}
