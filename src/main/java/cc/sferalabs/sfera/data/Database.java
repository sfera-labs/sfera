package cc.sferalabs.sfera.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cc.sferalabs.sfera.core.services.AutoStartService;

public class Database implements AutoStartService {

	private static final String DB_FILE = "data/sfera";
	private static final String DB_PROPERTIES = ";hsqldb.write_delay_millis=100";
	private static final String DB_USER = "sfera";
	private static final String DB_PASSWORD = "cduxg678df2";

	private static Connection dbConnection = null;

	private static final Logger logger = LogManager.getLogger();

	@Override
	public void init() throws Exception {
		if (dbConnection == null) {
			System.setProperty("hsqldb.reconfig_logging", "false");

			Class.forName("org.hsqldb.jdbc.JDBCDriver");
			dbConnection = DriverManager.getConnection("jdbc:hsqldb:file:"
					+ DB_FILE + DB_PROPERTIES, DB_USER, DB_PASSWORD);
			dbConnection.setAutoCommit(true);
		}
	}

	@Override
	public void quit() {
		if (dbConnection != null) {
			try {
				dbConnection.close();
			} catch (SQLException e) {
			}
		}
	}
}
