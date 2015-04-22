package cc.sferalabs.sfera.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cc.sferalabs.sfera.core.events.SystemStateEvent;
import cc.sferalabs.sfera.events.Bus;

public class Console extends Task implements SferaService {

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
	public String getName() {
		return "Console";
	}

	@Override
	public void init() throws Exception {
		run = true;
		stdIn = new BufferedReader(new InputStreamReader(System.in));
		TasksManager.DEFAULT.execute(this);
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
	 * @throws IOException
	 * 
	 */
	private static void checkStandardInput() throws IOException {
		String cmd;
		if ((cmd = stdIn.readLine()) != null) {
			switch (cmd.trim().toLowerCase()) {
			case "quit":
				Bus.post(new SystemStateEvent("quit"));
				break;

			}
		}
	}

}
