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
	 */
	ConsoleSession(String name) {
		super(name);
	}

	/**
	 * 
	 */
	void start() {
		run = true;
		TasksManager.executeSystem(this);
	}

	/**
	 * 
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
		clear();
		logger.debug("{} quitted", getName());
	}

	/**
	 * @return
	 */
	protected abstract boolean init();

	/**
	 * 
	 */
	protected abstract void clear();

	/**
	 * @return
	 */
	protected abstract String acceptCommand();

	/**
	 * 
	 * @param text
	 */
	protected abstract void output(String text);

}
