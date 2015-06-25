package cc.sferalabs.sfera.access;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Access {

	private static final Map<String, User> users = new ConcurrentSkipListMap<String, User>(
			String.CASE_INSENSITIVE_ORDER);

	private static final String USERS_FILE_PATH = "data/access/passwd";

	private static final Logger logger = LogManager.getLogger();

	/**
	 * 
	 * @throws Exception
	 */
	public synchronized static void init() throws Exception {
		List<String> lines = Files.readAllLines(Paths.get(USERS_FILE_PATH),
				StandardCharsets.UTF_8);
		int lineNum = 0;
		for (String line : lines) {
			line = line.trim();
			if (line.length() > 0) {
				try {
					String[] splitted = line.split(":");
					User u = new User(splitted[0], splitted[1], splitted[2],
							splitted[3].split(","));
					users.put(u.getUsername(), u);
					logger.debug("User '{}' created", u.getUsername());
				} catch (Exception e) {
					logger.error("Error reading file '" + USERS_FILE_PATH
							+ "' on line " + lineNum, e);
				}
			}
			lineNum++;
		}
	}

	/**
	 * 
	 * @param username
	 * @param plainPassword
	 * @param roles
	 * @throws UsernameAlreadyUsedException
	 * @throws IOException
	 */
	public synchronized static void addUser(String username,
			String plainPassword, String[] roles)
			throws UsernameAlreadyUsedException, IOException {
		if (users.containsKey(username)) {
			throw new UsernameAlreadyUsedException();
		}

		byte[] salt = generateSalt();
		byte[] hashedPassword = getEncryptedPassword(plainPassword, salt);

		users.put(username, new User(username, hashedPassword, salt, roles));

		String userLine = username + ":";
		userLine += Base64.getEncoder().encodeToString(hashedPassword) + ":";
		userLine += Base64.getEncoder().encodeToString(salt) + ":";
		for (int i = 0; i < roles.length; i++) {
			if (i != 0) {
				userLine += ",";
			}
			userLine += roles[i];
		}
		userLine += "\n";

		Files.write(Paths.get(USERS_FILE_PATH),
				userLine.getBytes(StandardCharsets.UTF_8),
				StandardOpenOption.APPEND);
	}

	/**
	 * 
	 * @return
	 */
	public synchronized static Set<User> getUsers() {
		return new HashSet<User>(users.values());
	}

	/**
	 * 
	 * @param username
	 * @param attemptedPassword
	 * @return
	 */
	public synchronized static boolean authenticate(String username,
			String attemptedPassword) {
		User u = users.get(username);
		if (u == null) {
			return false;
		}

		byte[] hashedPassword = getEncryptedPassword(attemptedPassword,
				u.getSalt());
		if (Arrays.equals(hashedPassword, u.getHashedPassword())) {
			return true;
		}

		return false;
	}

	/**
	 * 
	 * @return
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
	 * @param salt
	 * @return
	 */
	private static byte[] getEncryptedPassword(String password, byte[] salt) {
		try {
			KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 20000,
					20 * 8);
			SecretKeyFactory f = SecretKeyFactory
					.getInstance("PBKDF2WithHmacSHA1");

			return f.generateSecret(spec).getEncoded();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
