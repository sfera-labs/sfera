package cc.sferalabs.sfera.console;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.sferalabs.sfera.core.SystemNode;
import cc.sferalabs.sfera.core.services.AutoStartService;

public class Console implements AutoStartService {

	private static final Logger logger = LoggerFactory.getLogger(Console.class);

	private static final Map<String, ConsoleCommandHandler> HANDLERS = new HashMap<>();

	private StandardConsoleSession scs;
	private TelnetConsoleServer tcs;

	/**
	 * Sets the specified handler as the one handling commands starting with the
	 * specified key
	 * 
	 * @param key
	 *            the command key
	 * @param handler
	 *            the handler
	 */
	public static void setHandler(String key, ConsoleCommandHandler handler) {
		HANDLERS.put(key, handler);
		logger.debug("Added console handler: {}", key);
	}

	@Override
	public void init() {
		scs = new StandardConsoleSession();
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

		setHandler("help", new ConsoleHelper(HANDLERS));
	}

	@Override
	public void quit() throws Exception {
		if (scs != null) {
			scs.quit();
		}
		if (tcs != null) {
			tcs.quit();
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
			ConsoleCommandHandler handler = HANDLERS.get(key);
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
