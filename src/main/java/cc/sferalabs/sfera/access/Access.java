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

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListMap;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.sferalabs.sfera.console.Console;
import cc.sferalabs.sfera.events.Bus;
import cc.sferalabs.sfera.events.Node;

/**
 * Utility class for users authorization and authentication
 *
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 * 
 */
public class Access extends Node {

	/** Users credentials configuration file */
	private static final String USERS_FILE_PATH = "data/access/passwd";

	/** Map of existing users indexed by username */
	private static final Map<String, User> users = new ConcurrentSkipListMap<String, User>(
			String.CASE_INSENSITIVE_ORDER);

	private static final Logger logger = LoggerFactory.getLogger(Access.class);

	private static final Access INSTANCE = new Access();

	/**
	 * 
	 */
	private Access() {
		super("access");
	}

	/**
	 * Initialize user credentials reading from the configuration file
	 * 
	 * @throws IOException
	 *             if an I/O error occurs loading the saved data
	 */
	public synchronized static void init() throws IOException {
		Console.addHandler(AccessConsoleCommandHandler.INSTANCE);
		try {
			List<String> lines = Files.readAllLines(Paths.get(USERS_FILE_PATH), StandardCharsets.UTF_8);
			int lineNum = 0;
			for (String line : lines) {
				line = line.trim();
				if (line.length() > 0) {
					try {
						String[] splitted = line.split(":");
						String username = splitted[0];
						String hashedPassword = splitted[1];
						String salt = splitted[2];
						String[] roles;
						if (splitted.length > 3) {
							roles = splitted[3].split(",");
						} else {
							roles = new String[0];
						}
						User u = new User(username, hashedPassword, salt, roles);
						users.put(u.getUsername(), u);
						logger.debug("User '{}' loaded. Roles: {}", u.getUsername(), u.getRoles());
					} catch (Exception e) {
						logger.error("Error reading file '" + USERS_FILE_PATH + "' on line " + lineNum, e);
					}
				}
				lineNum++;
			}
		} catch (NoSuchFileException e) {
			logger.debug("File '{}' not found", USERS_FILE_PATH);
		}
	}

	/**
	 * Creates a new user and adds it to the list
	 * 
	 * @param username
	 *            username for the new user
	 * @param plainPassword
	 *            plain-text password
	 * @param roles
	 *            list of roles the user belongs to
	 * 
	 * @throws UsernameAlreadyUsedException
	 *             if a user with the specified username already exists
	 * @throws IOException
	 *             if an I/O error occurs saving the access data
	 */
	public synchronized static void addUser(String username, String plainPassword, String[] roles)
			throws UsernameAlreadyUsedException, IOException {
		if (users.containsKey(username)) {
			throw new UsernameAlreadyUsedException();
		}

		byte[] salt = generateSalt();
		byte[] hashedPassword = getEncryptedPassword(plainPassword, salt);

		User u = new User(username, hashedPassword, salt, roles);
		users.put(username, u);
		writeUsers();

		triggerChangeEvent(username);
		logger.info("User '{}' added. Roles: {}", username, roles);
	}

	/**
	 * 
	 */
	private static void triggerChangeEvent(String username) {
		Bus.post(new AccessChangeEvent(INSTANCE, username));
	}

	/**
	 * Updates the credentials of the specified user.
	 * 
	 * @param username
	 *            the username of the user to update
	 * @param plainPassword
	 *            the new password or {@code null} if unchanged
	 * @param roles
	 *            the new list of roles or {@code null} if unchanged
	 * @throws IOException
	 *             if an I/O error occurs saving the access data
	 * @throws UserNotFoundException
	 *             if no user with the specified username exists
	 */
	public synchronized static void updateUser(String username, String plainPassword, String[] roles)
			throws IOException, UserNotFoundException {
		User user = users.get(username);
		if (user == null) {
			throw new UserNotFoundException();
		}
		byte[] salt;
		byte[] hashedPassword;
		if (plainPassword == null) {
			salt = user.getSalt();
			hashedPassword = user.getHashedPassword();
		} else {
			salt = generateSalt();
			hashedPassword = getEncryptedPassword(plainPassword, salt);
		}

		if (roles == null) {
			roles = user.getRoles();
		}

		User newUser = new User(username, hashedPassword, salt, roles);
		users.put(username, newUser);
		writeUsers();

		triggerChangeEvent(username);
		logger.info("User '{}' updated. Roles: {}", username, roles);
	}

	/**
	 * 
	 * @throws IOException
	 */
	private static void writeUsers() throws IOException {
		Path tmp = Files.createTempFile(Access.class.getName(), null);
		try {
			try (BufferedWriter writer = Files.newBufferedWriter(tmp, StandardCharsets.UTF_8)) {
				for (User u : users.values()) {
					writer.write(getUserLine(u));
				}
			}
			Path path = Paths.get(USERS_FILE_PATH);
			Files.createDirectories(path.getParent());
			Files.move(tmp, path, StandardCopyOption.REPLACE_EXISTING);
		} finally {
			try {
				Files.delete(tmp);
			} catch (Exception e) {
			}
		}
	}

	/**
	 * @param user
	 * @return
	 */
	private static String getUserLine(User user) {
		StringBuilder line = new StringBuilder(user.getUsername());
		line.append(':');
		line.append(Base64.getEncoder().encodeToString(user.getHashedPassword()));
		line.append(':');
		line.append(Base64.getEncoder().encodeToString(user.getSalt()));
		line.append(':');
		String[] roles = user.getRoles();
		for (int i = 0; i < roles.length; i++) {
			if (i != 0) {
				line.append(',');
			}
			line.append(roles[i]);
		}
		line.append('\n');
		return line.toString();
	}

	/**
	 * Removes the specified user
	 * 
	 * @param username
	 *            username of the user to be removed
	 * 
	 * @throws IOException
	 *             if an I/O error occurs saving the access data
	 * @throws UserNotFoundException
	 *             if no user with the specified username exists
	 */
	public synchronized static void removeUser(String username) throws IOException, UserNotFoundException {
		User removed = users.remove(username);
		if (removed == null) {
			throw new UserNotFoundException();
		}
		writeUsers();

		triggerChangeEvent(username);
		logger.info("User '{}' removed", username);
	}

	/**
	 * Returns the user with the specified username, or {@code null} if no user with
	 * the specified username exists.
	 * 
	 * @param username
	 *            the username
	 * @return the user with the specified username, or {@code null} if no user with
	 *         the specified username exists
	 * 
	 */
	public synchronized static User getUser(String username) {
		return users.get(username);
	}

	/**
	 * Returns a set containing all the existing users.
	 * 
	 * @return a set containing all the existing users
	 */
	public synchronized static Set<User> getUsers() {
		return new HashSet<User>(users.values());
	}

	/**
	 * If authentication succeeds, returns the {@code User} associated with the
	 * specified username. Otherwise it returns {@code null}.
	 * 
	 * @param username
	 *            the username
	 * @param attemptedPassword
	 *            the attempted password
	 * @return the {@code User} associated with the specified username if the
	 *         authentication is successful; {@code null} otherwise
	 */
	public synchronized static User authenticate(String username, String attemptedPassword) {
		if (attemptedPassword == null) {
			return null;
		}
		User u = users.get(username);
		if (u == null) {
			return null;
		}
		byte[] hashedPassword = getEncryptedPassword(attemptedPassword, u.getSalt());
		if (Arrays.equals(hashedPassword, u.getHashedPassword())) {
			return u;
		}
		return null;
	}

	/**
	 * 
	 * @return random bytes sequence to be used as salt
	 */
	private static byte[] generateSalt() {
		try {
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			byte[] salt = new byte[8];
			random.nextBytes(salt);

			return salt;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 
	 * @param password
	 *            plain-text password
	 * @param salt
	 *            the salt
	 * @return bytes sequence representing the encrypted password
	 */
	private static byte[] getEncryptedPassword(String password, byte[] salt) {
		try {
			KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 20000, 20 * 8);
			SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			return f.generateSecret(spec).getEncoded();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
