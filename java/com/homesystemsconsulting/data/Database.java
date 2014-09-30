package com.homesystemsconsulting.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

	private static final String DB_FILE = "data/sfera";
	private static final String DB_PROPERTIES = ";hsqldb.write_delay_millis=100";
	private static final String DB_USER = "sfera";
	private static final String DB_PASSWORD = "cduxg678df2";
	
	private static Connection dbConnection = null;
	
	public static void init() throws ClassNotFoundException, SQLException {
		if (dbConnection == null) {
			System.setProperty("hsqldb.reconfig_logging", "false");
			
			Class.forName("org.hsqldb.jdbc.JDBCDriver");
			dbConnection = DriverManager.getConnection("jdbc:hsqldb:file:" + DB_FILE + DB_PROPERTIES, DB_USER, DB_PASSWORD);
			dbConnection.setAutoCommit(true);
		}
	}
	
	public static void close() {
		if (dbConnection != null) {
			try {
				dbConnection.close();
			} catch (SQLException e) {}
		}
	}
}
