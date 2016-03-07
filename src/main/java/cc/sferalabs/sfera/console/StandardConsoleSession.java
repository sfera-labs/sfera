/**
 * 
 */
package cc.sferalabs.sfera.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class StandardConsoleSession extends ConsoleSession {

	private static final Logger logger = LoggerFactory.getLogger(StandardConsoleSession.class);

	private final BufferedReader reader;

	/**
	 * Constructor
	 */
	StandardConsoleSession() {
		super("Standard Console");
		reader = new BufferedReader(new InputStreamReader(System.in));
	}

	@Override
	protected boolean init() {
		return true;
	}

	@Override
	public String acceptCommand() {
		try {
			while (isActive()) {
				if (reader.ready()) {
					String cmd = reader.readLine();
					return cmd;
				} else {
					Thread.sleep(500);
				}
			}
		} catch (InterruptedException e) {
		} catch (IOException e) {
			logger.error("I/O Exception", e);
		}
		return null;
	}

	@Override
	public void doOutput(String text) {
		System.out.print(text);
	}

	@Override
	protected void cleanUp() {
		if (reader != null) {
			try {
				reader.close();
			} catch (Exception e) {
			}
		}
	}

}
