/**
 * 
 */
package cc.sferalabs.sfera.console;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.sferalabs.sfera.core.services.Task;
import cc.sferalabs.sfera.core.services.TasksManager;

/**
 *
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public abstract class ConsoleSession extends Task {

	private static final Logger logger = LoggerFactory.getLogger(ConsoleSession.class);

	private boolean run;

	/**
	 * @param name
	 *            the console name
	 */
	ConsoleSession(String name) {
		super(name);
	}

	/**
	 * Starts this console session as a new system task
	 */
	void start() {
		run = true;
		TasksManager.executeSystem(this);
	}

	/**
	 * Interrups this console session
	 */
	protected void quit() {
		logger.debug("{} quitting...", getName());
		run = false;
		interrupt();
	}

	@Override
	protected void execute() {
		logger.debug("{} started", getName());
		if (init()) {
			while (run) {
				String cmd = acceptCommand();
				if (cmd != null && !cmd.isEmpty()) {
					String out = Console.processCommad(cmd.trim());
					if (out != null) {
						output(out);
					}
				}
			}
		}
		cleanUp();
		logger.debug("{} quitted", getName());
	}

	/**
	 * Initializes this console session.
	 * 
	 * @return {@code true} if initialization is successful, {@code false}
	 *         otherwise
	 */
	protected abstract boolean init();

	/**
	 * Cleans up the resources used by this console session. Called before
	 * terminating the task.
	 */
	protected abstract void cleanUp();

	/**
	 * Waits for a command to be processed.
	 * 
	 * @return the received command
	 */
	protected abstract String acceptCommand();

	/**
	 * Outputs the specified text to the console.
	 * 
	 * @param text
	 *            the text to output
	 */
	protected abstract void output(String text);

}
