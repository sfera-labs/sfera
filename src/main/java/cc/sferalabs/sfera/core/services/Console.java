package cc.sferalabs.sfera.core.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cc.sferalabs.sfera.core.events.SystemStateEvent;
import cc.sferalabs.sfera.drivers.Driver;
import cc.sferalabs.sfera.drivers.Drivers;
import cc.sferalabs.sfera.events.Bus;

public class Console extends Task implements AutoStartService {

	private static BufferedReader stdIn;
	private boolean run;
	private static final Logger logger = LogManager.getLogger();

	/**
	 * 
	 */
	public Console() {
		super("Console");
	}

	@Override
	public void init() throws Exception {
		run = true;
		stdIn = new BufferedReader(new InputStreamReader(System.in));
		TasksManager.getDefault().execute(this);
	}

	@Override
	public void quit() throws Exception {
		run = false;
		if (stdIn != null) {
			try {
				System.in.close();
			} catch (Exception e) {
			}
		}
	}

	@Override
	protected void execute() {
		try {
			while (run) {
				checkStandardInput();
			}
		} catch (Exception e) {
			logger.error("Error reading input. Console stopped", e);
		}
	}

	/**
	 * 
	 * @throws IOException
	 */
	private static void checkStandardInput() throws IOException {
		String cmd;
		if ((cmd = stdIn.readLine()) != null) {
			cmd = cmd.trim();
			if (cmd.isEmpty()) {
				return;
			}

			String[] args = cmd.split("\\s+");
			try {
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
						Bus.post(new SystemStateEvent("quit"));
					}
					break;

				case "start":
					if (args.length == 3) {
						if (args[1].equals("driver")) {
							Driver d = Drivers.getDriver(args[2]);
							if (d != null) {
								d.start();
							}
						}
					} else {
						System.err.println("Add target");
					}
					break;

				case "restart":
					if (args.length == 3) {
						if (args[1].equals("driver")) {
							Driver d = Drivers.getDriver(args[2]);
							if (d != null) {
								d.restart();
							}
						}
					} else {
						System.err.println("Add target");
					}
					break;

				case "kill":
					System.exit(0);
					break;

				default:
					System.err.println("Unknown command '" + cmd + "'");
					break;
				}
			} catch (Throwable t) {
				logger.error("Error executing command '" + cmd + "'", t);
			}
		}
	}

}
