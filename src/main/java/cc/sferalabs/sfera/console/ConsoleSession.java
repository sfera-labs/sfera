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
	protected ConsoleSession(String name) {
		super(name);
	}

	/**
	 * Starts this console session as a new task.
	 */
	public final void start() {
		run = true;
		Console.addSession(this);
		TasksManager.execute(this);
	}

	/**
	 * Interrupts this console session.
	 */
	public final void quit() {
		logger.debug("{} quitting...", getName());
		run = false;
		interrupt();
	}

	@Override
	protected final void execute() {
		logger.debug("{} started", getName());
		if (init()) {
			while (run) {
				String cmd = acceptCommand();
				if (cmd == null) {
					break;
				}
				if (!cmd.isEmpty()) {
					String out = Console.processCommad(cmd.trim(), this);
					if (out != null) {
						output(out + "\n");
					}
				}
			}
		}
		Console.removeSession(this);
		cleanUp();
		logger.debug("{} quitted", getName());
	}

	/**
	 * Outputs the specified text to this console session.
	 * 
	 * @param text
	 *            the text to output
	 */
	public final void output(String text) {
		if (isActive()) {
			doOutput(text);
		}
	}

	/**
	 * @return {@code true} if this session is active, {@code false} if it has
	 *         been interrupted or requested to quit
	 */
	public boolean isActive() {
		if (!run) {
			return false;
		}
		Thread t = getThread();
		if (t == null) {
			return false;
		}
		return !t.isInterrupted();
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
	 * terminating the session.
	 */
	protected abstract void cleanUp();

	/**
	 * Waits for a command to be received. If {@code null} is returned the
	 * session in terminated.
	 * <p>
	 * The waiting must be interruptible and {@code null} shall be returned when
	 * interrupted.
	 * 
	 * @return the received command or {@code null} to terminate the session
	 */
	public abstract String acceptCommand();

	/**
	 * Outputs the specified text to the console.
	 * 
	 * @param text
	 *            the text to output
	 */
	protected abstract void doOutput(String text);

}
