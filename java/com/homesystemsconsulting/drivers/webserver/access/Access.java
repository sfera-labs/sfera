package com.homesystemsconsulting.drivers.webserver.access;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public abstract class Access {
	
	private static final  Map<String, User> users = new TreeMap<String, User>(String.CASE_INSENSITIVE_ORDER);

	/**
	 * 
	 * @param username
	 * @param password
	 * @throws UsernameAlreadyUsedException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	synchronized public static void addUser(String username, String password) throws UsernameAlreadyUsedException, NoSuchAlgorithmException, InvalidKeySpecException {
		if (existsUser(username)) {
			throw new UsernameAlreadyUsedException();
		}
		
		byte[] salt = generateSalt();
		byte[] hashedPassword = getEncryptedPassword(password, salt);	
		
		users.put(username, new User(username, hashedPassword, salt));
	}
	
	/**
	 * 
	 * @param username
	 * @param attemptedPassword
	 * @return
	 * @throws Exception
	 */
	synchronized public static boolean authenticate(String username, String attemptedPassword) throws Exception {
		User u = users.get(username);
		if (u == null) {
			return false;
		}
		
		byte[] hashedPassword = getEncryptedPassword(attemptedPassword, u.salt); 
		return Arrays.equals(hashedPassword, u.hashedPassword);
	}
	
	/**
	 * 
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	private static byte[] generateSalt() throws NoSuchAlgorithmException {
		SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
		byte[] salt = new byte[8];
		random.nextBytes(salt);

		return salt;
	}
	
	/**
	 * 
	 * @param password
	 * @param salt
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	private static byte[] getEncryptedPassword(String password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
		KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 20000, 20 * 8);
		SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

		return f.generateSecret(spec).getEncoded();
	}

	/**
	 * 
	 * @param username
	 * @return
	 */
	private static boolean existsUser(String username) {
		return users.containsKey(username);
	}
}
