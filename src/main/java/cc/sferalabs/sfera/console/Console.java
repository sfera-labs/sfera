package cc.sferalabs.sfera.console;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.sferalabs.sfera.core.SystemNode;
import cc.sferalabs.sfera.core.services.AutoStartService;

public class Console implements AutoStartService {

	private static final Logger logger = LoggerFactory.getLogger(Console.class);

	private static final Map<String, ConsoleCommandHandler> HANDLERS = new HashMap<>();
	private static final List<ConsoleSession> SESSIONS = new ArrayList<>();

	private TelnetConsoleServer tcs;

	/**
	 * Adds the specified handler.
	 * 
	 * @param handler
	 *            the handler
	 * @throws IllegalArgumentException
	 *             if an handler with the same key (
	 *             {@link ConsoleCommandHandler#getKey()}) has already been
	 *             added
	 */
	public static void addHandler(ConsoleCommandHandler handler) {
		String key = handler.getKey();
		synchronized (HANDLERS) {
			if (HANDLERS.containsKey(key)) {
				throw new IllegalArgumentException("Handler with same key already existing");
			}
			HANDLERS.put(key, handler);
		}
		logger.debug("Added console handler: {}", key);
	}

	@Override
	public void init() {
		StandardConsoleSession scs = new StandardConsoleSession();
		scs.start();

		Integer telnetPort = SystemNode.getConfiguration().get("console_telnet_port", null);
		if (telnetPort != null) {
			try {
				tcs = new TelnetConsoleServer(telnetPort);
			} catch (IOException e) {
				logger.error("Error creating telnet server", e);
			}
		} else {
			logger.info("Telnet console disabled");
		}

		addHandler(new ConsoleHelper(HANDLERS));
	}

	@Override
	public void quit() throws Exception {
		synchronized (SESSIONS) {
			for (ConsoleSession s : SESSIONS) {
				s.quit();
			}
		}
		if (tcs != null) {
			tcs.quit();
		}
	}

	/**
	 * Adds the specified session from the list of active sessions.
	 * 
	 * @param session
	 *            the session to add
	 */
	static void addSession(ConsoleSession session) {
		synchronized (SESSIONS) {
			SESSIONS.add(session);
		}
	}

	/**
	 * Removes the specified session from the list of active sessions.
	 * 
	 * @param session
	 *            the session to remove
	 */
	static void removeSession(ConsoleSession session) {
		synchronized (SESSIONS) {
			SESSIONS.remove(session);
		}
	}

	/**
	 * @param cmd
	 */
	static String processCommad(String cmd) {
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
			ConsoleCommandHandler handler;
			synchronized (HANDLERS) {
				handler = HANDLERS.get(key);
			}
			if (handler != null) {
				return handler.accept(rest);
			} else {
				return "No command handler for '" + key + "'";
			}
		} catch (Throwable t) {
			logger.debug("Error executing command '" + cmd + "'", t);
			return "Error executing command '" + cmd + "': " + t;
		}
	}

}
