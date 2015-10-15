package cc.sferalabs.sfera.access;

import java.util.Base64;

/**
 * Class to hold user information
 *
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class User {

	private final String username;
	private final byte[] hashedPassword;
	private final byte[] salt;
	private final String[] roles;

	/**
	 * User constructor
	 * 
	 * @param username
	 *            the username
	 * @param hashedPassword
	 *            the hashed password
	 * @param salt
	 *            the salt
	 * @param roles
	 *            list of roles
	 */
	User(String username, byte[] hashedPassword, byte[] salt, String[] roles) {
		this.username = username;
		this.hashedPassword = hashedPassword;
		this.salt = salt;
		this.roles = roles;
	}

	/**
	 * User constructor
	 * 
	 * @param username
	 *            the username
	 * @param hashedPassword
	 *            the hashed password base64-encoded
	 * @param salt
	 *            the salt
	 * @param roles
	 *            list of roles
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
	 * @param role
	 *            the role identifier
	 * @return {@code true} if the user has the specified role, {@code false}
	 *         otherwise
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
