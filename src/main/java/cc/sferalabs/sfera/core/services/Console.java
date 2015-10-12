package cc.sferalabs.sfera.core.services;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.sferalabs.sfera.access.Access;
import cc.sferalabs.sfera.core.events.SystemStateEvent;
import cc.sferalabs.sfera.drivers.Driver;
import cc.sferalabs.sfera.drivers.Drivers;
import cc.sferalabs.sfera.events.Bus;

public class Console extends Task implements AutoStartService {

	private static final Logger logger = LoggerFactory.getLogger(Console.class);

	private BufferedReader reader;
	private boolean run;
	private boolean out;

	/**
	 * 
	 */
	public Console() {
		super("Console");
	}

	@Override
	public void init() throws Exception {
		String consoleIn = System.getProperty("sfera.console.in");
		if (consoleIn == null) {
			logger.debug("Console disabled");
			return;
		}
		if (consoleIn.equalsIgnoreCase("std")) {
			reader = new BufferedReader(new InputStreamReader(System.in));
			out = true;
		} else {
			Path path = Paths.get(consoleIn);
			Files.deleteIfExists(path);
			Files.createFile(path);
			reader = Files.newBufferedReader(path);
		}
		logger.info("Console enabled: {}", consoleIn);
		run = true;
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
				if (reader.ready()) {
					String cmd = reader.readLine().trim();
					if (!cmd.isEmpty()) {
						try {
							processCommad(cmd);
						} catch (Throwable t) {
							if (out) {
								System.err.println(t.getMessage());
							}
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

		case "user":
			if (args.length > 2) {
				String username = args[2];

				if (args[1].equals("add")) {
					if (args.length > 4) {
						String password = args[3];
						String[] roles = Arrays.copyOfRange(args, 4, args.length);
						Access.addUser(username, password, roles);
					} else {
						throw new Exception(
								"Usage: user add <username> <password> <role1> [<role2> ... <roleN>]");
					}

				} else if (args[1].equals("remove")) {
					Access.removeUser(username);

				} else {
					throw new Exception("Unknown user action '" + args[1] + "'");
				}
			} else {
				throw new Exception("Add parameters");
			}

			break;

		case "kill":
			System.exit(1);
			break;

		default:
			throw new Exception("Unknown command '" + cmd + "'");
		}
	}

}
