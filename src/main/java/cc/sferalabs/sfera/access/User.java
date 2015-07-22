package cc.sferalabs.sfera.access;

import java.util.Base64;

public class User {

	private final String username;
	private final byte[] hashedPassword;
	private final byte[] salt;
	private final String[] roles;

	/**
	 * 
	 * @param username
	 * @param hashedPassword
	 * @param salt
	 * @param roles
	 */
	User(String username, byte[] hashedPassword, byte[] salt, String[] roles) {
		this.username = username;
		this.hashedPassword = hashedPassword;
		this.salt = salt;
		this.roles = roles;
	}

	/**
	 * 
	 * @param username
	 * @param hashedPassword
	 * @param salt
	 * @param roles
	 */
	User(String username, String hashedPassword, String salt, String[] roles) {
		this(username, Base64.getDecoder().decode(hashedPassword), Base64.getDecoder().decode(salt),
				roles);
	}

	/**
	 * 
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * 
	 * @return the hash of the user's password
	 */
	byte[] getHashedPassword() {
		return hashedPassword;
	}

	/**
	 * 
	 * @return the salt used to generate the hash of the password
	 */
	byte[] getSalt() {
		return salt;
	}

	/**
	 * 
	 * @return true if the user has the specified role, false otherwise
	 */
	public boolean isInRole(String role) {
		for (String r : roles) {
			if (r.equals(role)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 
	 * @return the user's roles
	 */
	public String[] getRoles() {
		return roles;
	}

}
