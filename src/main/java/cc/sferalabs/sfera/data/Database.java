package cc.sferalabs.sfera.data;

import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.EventListener;
import java.util.GregorianCalendar;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.Subscribe;

import cc.sferalabs.sfera.core.Configuration;
import cc.sferalabs.sfera.core.SystemNode;
import cc.sferalabs.sfera.core.events.SystemStateEvent;
import cc.sferalabs.sfera.core.services.AutoStartService;
import cc.sferalabs.sfera.core.services.Task;
import cc.sferalabs.sfera.core.services.TasksManager;
import cc.sferalabs.sfera.events.Bus;
import cc.sferalabs.sfera.events.Node;
import cc.sferalabs.sfera.util.files.FilesUtil;

/**
 *
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class Database extends Node implements AutoStartService, EventListener {

	private static final Logger logger = LoggerFactory.getLogger(Database.class);

	private static final String DB_DIR = "data/db/";
	private static final String DB_FILE = DB_DIR + "db";
	private static final String DB_PROPERTIES = ";hsqldb.write_delay_millis=100";
	private static final String TABLE_NAME = "key_value";

	private static Database instance;
	private static Connection dbConnection = null;
	private static Object dbLock = new Object();

	private static PreparedStatement insert_stmt;
	private static PreparedStatement update_stmt;
	private static PreparedStatement delete_stmt;
	private static PreparedStatement select_stmt;

	/**
	 * 
	 */
	public Database() {
		super("db");
		instance = this;
	}

	/**
	 * @return the Database instance
	 */
	public static Database getInstance() {
		return instance;
	}

	@Override
	public void init() throws Exception {
		synchronized (dbLock) {
			if (dbConnection == null) {
				Configuration config = SystemNode.getConfiguration();
				String user = config.get("db_user", "sfera");
				String password = config.get("db_password", "sfera");
				System.setProperty("hsqldb.reconfig_logging", "false");
				Class.forName("org.hsqldb.jdbc.JDBCDriver");
				logger.debug("Connecting to database...");

				dbConnection = DriverManager.getConnection(
						"jdbc:hsqldb:file:" + DB_FILE + DB_PROPERTIES, user, password);

				logger.debug("Initializing database...");

				dbConnection.setAutoCommit(true);

				Statement create_table_stmt = dbConnection.createStatement();
				create_table_stmt.execute("CREATE CACHED TABLE IF NOT EXISTS " + TABLE_NAME
						+ " (key VARCHAR(512) PRIMARY KEY, val VARCHAR(1024) NOT NULL)");

				insert_stmt = dbConnection.prepareStatement(
						"INSERT INTO " + TABLE_NAME + " (key, val) values (?, ?)");
				update_stmt = dbConnection
						.prepareStatement("UPDATE " + TABLE_NAME + " SET val = ? WHERE key = ?");
				delete_stmt = dbConnection
						.prepareStatement("DELETE FROM " + TABLE_NAME + " WHERE key = ?");
				select_stmt = dbConnection
						.prepareStatement("SELECT val FROM " + TABLE_NAME + " WHERE key = ?");

				logger.info("Database initialized");

				Bus.register(this);
			}
		}
	}

	@Subscribe
	public void startCheckPointTask(SystemStateEvent event) {
		if (event.getValue().equals("ready")) {
			Bus.unregister(this);
			TasksManager.execute(DatabaseCheckPointTask.INSTANCE);
		}
	}

	/**
	 * Set the specified key to the specified value. if value is {@code null}
	 * the entry is deleted.
	 * 
	 * @param key
	 *            the key
	 * @param value
	 *            the value
	 * @throws SQLException
	 *             if a database access error occurs
	 */
	public void set(String key, String value) throws SQLException {
		Objects.requireNonNull(key, "key must not be null");
		if (value == null) {
			delete(key);
		} else {
			update(key, value);
		}
	}

	/**
	 * Returns the value set to the specified key or {@code null} if not found.
	 * 
	 * @param key
	 *            the key
	 * @return the value set to the specified key or {@code null} if not found
	 * @throws SQLException
	 *             if a database access error occurs
	 */
	public String get(String key) throws SQLException {
		Objects.requireNonNull(key, "key must not be null");
		ResultSet rs = null;
		try {
			synchronized (select_stmt) {
				select_stmt.setString(1, key);
				rs = select_stmt.executeQuery();
			}

			if (!rs.next()) {
				return null;
			}
			return rs.getString(1);
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
	}

	/**
	 * 
	 * @param key
	 * @param value
	 * @return
	 * @throws SQLException
	 */
	private void update(String key, String value) throws SQLException {
		synchronized (update_stmt) {
			update_stmt.setString(1, value);
			update_stmt.setString(2, key);
			if (update_stmt.executeUpdate() == 0) { // The key did not exist
				insert_stmt.setString(1, key);
				insert_stmt.setString(2, value);
				insert_stmt.executeUpdate();
			}
		}
	}

	/**
	 * 
	 * @param key
	 * @return
	 * @throws SQLException
	 */
	private void delete(String key) throws SQLException {
		synchronized (delete_stmt) {
			delete_stmt.setString(1, key);
			delete_stmt.executeUpdate();
		}
	}

	@Override
	public void quit() {
		synchronized (dbLock) {
			if (dbConnection != null) {
				try {
					dbConnection.createStatement().execute("SHUTDOWN");
					dbConnection.close();
				} catch (SQLException e) {
				}
				dbConnection = null;
			}
		}
	}

	/**
	 *
	 */
	private static class DatabaseCheckPointTask extends Task {

		private static final DatabaseCheckPointTask INSTANCE = new DatabaseCheckPointTask();
		private long lastHouseKeeping;

		/**
		 * 
		 */
		private DatabaseCheckPointTask() {
			super("DatabaseCheckPointTask");
		}

		@Override
		protected void execute() {
			try {
				Thread.sleep(60000);
				synchronized (dbLock) {
					if (dbConnection != null) {
						try {
							logger.debug("Running database checkpoint...");
							dbConnection.createStatement().execute("CHECKPOINT");
							logger.debug("Database checkpoint completed");
						} catch (Exception e) {
							logger.error("Database checkpoint failed", e);
						}

						Task next = null;
						if (System.currentTimeMillis() > lastHouseKeeping + 12 * 60000) {
							// TODO add a configuration param to set preferred
							// time
							GregorianCalendar cal = new GregorianCalendar();
							if (cal.get(GregorianCalendar.HOUR_OF_DAY) == 0) {
								next = DatabaseHousekeepingTask.INSTANCE;
								lastHouseKeeping = System.currentTimeMillis();
							}
						}
						if (next == null) {
							next = INSTANCE;
						}
						TasksManager.execute(next);
					}
				}

			} catch (InterruptedException e) {
			}
		}

	}

	/**
	 *
	 */
	private static class DatabaseHousekeepingTask extends Task {

		private static final DatabaseHousekeepingTask INSTANCE = new DatabaseHousekeepingTask();

		/**
		 * 
		 */
		public DatabaseHousekeepingTask() {
			super("DatabaseHousekeepingTask");
		}

		@Override
		protected void execute() {
			if (dbConnection != null) {
				try {
					logger.debug("Running database defrag...");
					dbConnection.createStatement().execute("CHECKPOINT DEFRAG");
				} catch (Exception e) {
					logger.error("Database defrag failed", e);
				}

				try {
					logger.debug("Creating database backup...");
					Path tmp = Paths.get(DB_DIR, "._backup").toAbsolutePath();
					try {
						FilesUtil.delete(tmp);
					} catch (NoSuchFileException e) {
					}
					dbConnection.createStatement().execute(
							"backup database to '" + tmp.toString() + "/' not blocking as files");
					Path backup = Paths.get(DB_DIR, "backup");
					try {
						FilesUtil.delete(backup);
					} catch (NoSuchFileException e) {
					}
					FilesUtil.move(tmp, backup);
				} catch (Exception e) {
					logger.error("Database backup failed", e);
				}
			}
			logger.debug("Database housekeeping terminated");
			TasksManager.execute(DatabaseCheckPointTask.INSTANCE);
		}

	}

}
