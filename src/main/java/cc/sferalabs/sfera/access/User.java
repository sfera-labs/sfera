/*-
 * +======================================================================+
 * Sfera
 * ---
 * Copyright (C) 2015 - 2016 Sfera Labs S.r.l.
 * ---
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * -======================================================================-
 */

package cc.sferalabs.sfera.access;

import java.util.Base64;
import java.util.Objects;

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
		this.username = Objects.requireNonNull(username, "username must not be null");
		this.hashedPassword = Objects.requireNonNull(hashedPassword,
				"hashedPassword must not be null");
		this.salt = Objects.requireNonNull(salt, "salt must not be null");
		this.roles = Objects.requireNonNull(roles, "roles must not be null");
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
