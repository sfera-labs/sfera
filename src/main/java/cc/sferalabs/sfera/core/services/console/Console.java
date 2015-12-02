package cc.sferalabs.sfera.core.services.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.sferalabs.sfera.core.services.LazyService;
import cc.sferalabs.sfera.core.services.TasksManager;

public class Console extends LazyService {

	private static final Logger logger = LoggerFactory.getLogger(Console.class);

	private static final Console INSTANCE = new Console();

	private BufferedReader reader;
	private boolean run;
	private Map<String, ConsoleCommandHandler> handlers;
	private Boolean enabled;

	/**
	 * Sets the specified handler as the one handling commands starting with the
	 * specified key
	 * 
	 * @param key
	 *            the command key
	 * @param handler
	 *            the handler
	 */
	public static synchronized void setHandler(String key, ConsoleCommandHandler handler) {
		if (INSTANCE.init()) {
			INSTANCE.handlers.put(key, handler);
			logger.debug("Added console handler: {}", key);
		}
	}

	/**
	 * @return
	 */
	private boolean init() {
		if (enabled != null) {
			return enabled;
		}

		String consoleIn = System.getProperty("sfera.console.in");
		if (consoleIn == null) {
			logger.debug("Console disabled");
			enabled = false;
		} else {
			try {
				handlers = new HashMap<>();
				if (consoleIn.equalsIgnoreCase("std")) {
					reader = new BufferedReader(new InputStreamReader(System.in));
				} else {
					Path path = Paths.get(consoleIn);
					Files.deleteIfExists(path);
					Files.createFile(path);
					reader = Files.newBufferedReader(path);
				}
				logger.info("Console enabled: {}", consoleIn);
				run = true;
				TasksManager.executeSystem("Console", this::readInput);
				handlers.put("help", new ConsoleHelper(handlers));
				enabled = true;
			} catch (IOException e) {
				logger.error("Initialization error", e);
				enabled = false;
			}
		}

		return enabled;
	}

	@Override
	public void quit() throws Exception {
		run = false;
	}

	/**
	 * 
	 */
	private void readInput() {
		try {
			while (run) {
				if (reader.ready()) {
					String cmd = reader.readLine().trim();
					if (!cmd.isEmpty()) {
						processCommad(cmd);
					}
				}
				Thread.sleep(500);
			}
		} catch (Exception e) {
			logger.error("Error reading input. Console stopped", e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (Exception e2) {
				}
			}
		}
	}

	/**
	 * @param cmd
	 */
	private synchronized void processCommad(String cmd) {
		try {
			logger.debug("Processing command: {}", cmd);
			String key;
			String rest;
			int sep = cmd.indexOf(' ');
			if (sep < 0) {
				key = cmd;
				rest = "";
			} else {
				key = cmd.substring(0, sep);
				rest = cmd.substring(sep).trim();
			}
			ConsoleCommandHandler handler = handlers.get(key);
			if (handler != null) {
				handler.accept(rest);
			} else {
				logger.warn("No command handler for '{}'", key);
			}
		} catch (Throwable t) {
			logger.warn("Error executing command '" + cmd + "'", t);
		}
	}

}
