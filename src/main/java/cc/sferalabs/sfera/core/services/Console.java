package cc.sferalabs.sfera.core.services;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.sferalabs.sfera.core.events.SystemStateEvent;
import cc.sferalabs.sfera.drivers.Driver;
import cc.sferalabs.sfera.drivers.Drivers;
import cc.sferalabs.sfera.events.Bus;

public class Console extends Task implements AutoStartService {

	private static BufferedReader stdIn;
	private boolean run;
	private static final Logger logger = LoggerFactory.getLogger(Console.class);

	/**
	 * 
	 */
	public Console() {
		super("Console");
	}

	@Override
	public void init() throws Exception {
		String enabled = System.getProperty("sfera.console");
		if (enabled == null || !enabled.equalsIgnoreCase("true")) {
			logger.debug("Console disabled");
			return;
		}
		logger.info("Console enabled");
		run = true;
		stdIn = new BufferedReader(new InputStreamReader(System.in));
		TasksManager.executeSystem(this);
	}

	@Override
	public void quit() throws Exception {
		run = false;
	}

	@Override
	protected void execute() {
		try {
			while (run) {
				if (stdIn.ready()) {
					String cmd = stdIn.readLine().trim();
					if (!cmd.isEmpty()) {
						try {
							processCommad(cmd);
						} catch (Throwable t) {
							System.err.println(t.getMessage());
							logger.warn("Error executing command '" + cmd + "'", t);
						}
					}
				}
				Thread.sleep(500);
			}
		} catch (Exception e) {
			logger.error("Error reading input. Console stopped", e);
		}
	}

	/**
	 * 
	 * @param cmd
	 * @throws Exception
	 */
	private static void processCommad(String cmd) throws Exception {
		logger.debug("Processing command: {}", cmd);
		String[] args = cmd.split("\\s+");
		switch (args[0]) {
		case "quit":
			if (args.length == 3) {
				if (args[1].equals("driver")) {
					Driver d = Drivers.getDriver(args[2]);
					if (d != null) {
						d.quit();
					}
				}
			} else {
				Bus.post(SystemStateEvent.QUIT);
			}
			break;

		case "start":
			if (args.length == 3) {
				if (args[1].equals("driver")) {
					Driver d = Drivers.getDriver(args[2]);
					if (d != null) {
						d.start();
					} else {
						throw new Exception("Driver '" + args[2] + "' not found");
					}
				}
			} else {
				throw new Exception("Add target");
			}
			break;

		case "restart":
			if (args.length == 3) {
				if (args[1].equals("driver")) {
					Driver d = Drivers.getDriver(args[2]);
					if (d != null) {
						d.restart();
					} else {
						throw new Exception("Driver '" + args[2] + "' not found");
					}
				}
			} else {
				throw new Exception("Add target");
			}
			break;

		case "kill":
			System.exit(0);
			break;

		default:
			throw new Exception("Unknown command '" + cmd + "'");
		}
	}

}
