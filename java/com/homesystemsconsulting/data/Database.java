package com.homesystemsconsulting.data;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class Database {

	private static final String DB_FILE = "data/sfera";
	private static final String DB_PROPERTIES = ";hsqldb.write_delay_millis=100";
	
	private static Connection dbConnection = null;
	
	public static void init() throws ClassNotFoundException, SQLException {
		if (dbConnection == null) {
			System.setProperty("hsqldb.reconfig_logging", "false");
			
			Class.forName("org.hsqldb.jdbc.JDBCDriver");
			dbConnection = DriverManager.getConnection("jdbc:hsqldb:file:" + DB_FILE + DB_PROPERTIES, "sfera", "cduxg678df2");
			dbConnection.setAutoCommit(true);
			
			dbConnection.createStatement().execute("create cached table if not exists users (" +
					"id varchar(254)," +
					"password binary(20) not null," +
					"salt binary(8) not null," +
					"primary key (id)" +
					")");
		}
	}
	
	public static void addUser(String username, String password) throws SQLException, NoSuchAlgorithmException, InvalidKeySpecException {
		byte[] salt = generateSalt();
		byte[] hashedPassword = getEncryptedPassword(password, salt);		

		PreparedStatement insertUserStmt = dbConnection.prepareStatement("insert into users (id, password, salt) values (?, ?, ?)");
		insertUserStmt.setString(1, username);
		insertUserStmt.setBytes(2, hashedPassword);
		insertUserStmt.setBytes(3, salt);
		insertUserStmt.executeUpdate();
	}
	
	private static byte[] generateSalt() throws NoSuchAlgorithmException {
		SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
		byte[] salt = new byte[8];
		random.nextBytes(salt);

		return salt;
	}
	
	private static byte[] getEncryptedPassword(String password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
		KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 20000, 20 * 8);
		SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

		return f.generateSecret(spec).getEncoded();
	}
	
	public static byte[] getUserPassword(String username) throws SQLException {
		PreparedStatement selectUserPasswordStmt = dbConnection.prepareStatement("select password from users where id = ?");
		
		selectUserPasswordStmt.setString(1, username);
		ResultSet rs = selectUserPasswordStmt.executeQuery();
		
		if (rs.next()) {
			return rs.getBytes(1);
		}
		
		return null;
	}
}
