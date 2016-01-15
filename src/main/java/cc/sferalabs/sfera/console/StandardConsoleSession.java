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
	private int errorCount = 0;

	/**
	 * 
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
	protected String acceptCommand() {
		try {
			if (reader.ready()) {
				String cmd = reader.readLine();
				errorCount = 0;
				return cmd;
			} else {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
				}
			}
		} catch (IOException e) {
			logger.error("I/O Exception", e);
			if (errorCount++ > 5) {
				quit();
			}
		}
		return null;
	}

	@Override
	protected void output(String text) {
		System.out.println(text);
	}

	@Override
	protected void clear() {
		if (reader != null) {
			try {
				reader.close();
			} catch (Exception e) {
			}
		}
	}

}
