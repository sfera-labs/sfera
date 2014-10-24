package com.homesystemsconsulting.drivers.webserver.access;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import com.homesystemsconsulting.core.Sfera;
import com.homesystemsconsulting.drivers.webserver.HttpRequestHeader;
import com.homesystemsconsulting.drivers.webserver.WebServer;

public abstract class Access {
	
	private static final Map<String, User> users = new ConcurrentSkipListMap<String, User>(String.CASE_INSENSITIVE_ORDER);
	private static final Map<String, Token> tokens = new ConcurrentHashMap<String, Token>();
	
	private static final Path USERS_FILE_PATH = Paths.get("data/webserver/passwd");
	
	/**
	 * 
	 * @throws Exception
	 */
	public static void init(WebServer webServer) throws Exception {
		List<String> lines = Files.readAllLines(USERS_FILE_PATH, Sfera.CHARSET);
		int lineNum = 0;
		for (String line : lines) {
	    	line = line.trim();
	    	if (line.length() > 0) {
		    	try {
			    	String[] splitted = line.split(":");
			    	User u = new User(splitted[0], splitted[1], splitted[2]);
			    	users.put(u.getUsername(), u);
		    	} catch (Exception e) {
		    		webServer.getLogger().error("error reading file " + USERS_FILE_PATH + " on line " + lineNum);
		    	}
	    	}
	    	lineNum++;
	    }
	}
	
	/**
	 * 
	 * @param username
	 * @param plainPassword
	 * @throws UsernameAlreadyUsedException
	 * @throws Exception
	 */
	public static void addUser(String username, String plainPassword) throws UsernameAlreadyUsedException, Exception {
		if (existsUser(username)) {
			throw new UsernameAlreadyUsedException();
		}
		
		byte[] salt = generateSalt();
		byte[] hashedPassword = getEncryptedPassword(plainPassword, salt);	
	
		users.put(username, new User(username, hashedPassword, salt));
		
		String userLine = username + ":";
		userLine += Base64.getEncoder().encodeToString(hashedPassword) + ":";
		userLine += Base64.getEncoder().encodeToString(salt) + "\n";
		
		Files.write(USERS_FILE_PATH, userLine.getBytes(Sfera.CHARSET), StandardOpenOption.APPEND);
	}
	
	/**
	 * 
	 * @param username
	 * @return
	 */
	private static boolean existsUser(String username) {
		return users.containsKey(username);
	}
	
	/**
	 * 
	 * @param username
	 * @param attemptedPassword
	 * @return
	 * @throws Exception
	 */
	public static User authenticate(String username, String attemptedPassword) throws Exception {
		User u = users.get(username);
		if (u == null) {
			return null;
		}
		
		byte[] hashedPassword = getEncryptedPassword(attemptedPassword, u.salt); 
		if (Arrays.equals(hashedPassword, u.hashedPassword)) {
			return u;
		}
		
		return null;
	}
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	private static byte[] generateSalt() throws Exception {
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
	 * @throws Exception
	 */
	private static byte[] getEncryptedPassword(String password, byte[] salt) throws Exception {
		KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 20000, 20 * 8);
		SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

		return f.generateSecret(spec).getEncoded();
	}

	/**
	 * 
	 * @param user
	 * @param httpRequestHeader 
	 * @return
	 */
	public static String assignToken(User user, HttpRequestHeader httpRequestHeader) {
		Token token = new Token(user, httpRequestHeader);
		tokens.put(token.getUUID(), token);
        return token.getUUID();
	}

	/**
	 * 
	 * @param tokenUUID
	 * @param httpRequestHeader
	 * @return
	 */
	public static Token getToken(String tokenUUID, HttpRequestHeader httpRequestHeader) {
		Token token = tokens.get(tokenUUID);
		if (token == null) {
			return null;
		}
		if (token.isExpired()) {
			tokens.remove(tokenUUID);
			return null;
		}
		if (token.match(httpRequestHeader)) {
			return token;
		}
		
		return null;
	}

	/**
	 * 
	 * @param token
	 */
	public static void removeToken(String tokenUUID) {
		tokens.remove(tokenUUID);
	}
}
