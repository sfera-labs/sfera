package cc.sferalabs.sfera.util.os;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ProcessUtil {

	/**
	 * Constructs and runs a process.
	 * 
	 * @param timeout
	 *            the maximum time to wait for the process to terminate. If set
	 *            to 0 this method returns immediately after starting the
	 *            process and no correct termination is checked nor output is
	 *            collected
	 * @param unit
	 *            the time unit of the {@code timeout} argument
	 * @param getOutput
	 *            whether or not to collect and return the process output
	 * @param killOnTimeout
	 *            whether or not to kill the process if the timeout expires
	 * @param command
	 *            a string array containing the program and its arguments
	 * @return if {@code timeout} is greater than 0 it returns an Object array
	 *         containing the process exit value as first element and, if
	 *         {@code getOutput} is {@code true}, the String containing the
	 *         process output as second element. {@code null} otherwise
	 * @throws IOException
	 *             if an I/O error occurs
	 * @throws InterruptedException
	 *             if the current thread is interrupted while waiting
	 * @throws TimeoutException
	 *             if the timeout expires before termination
	 */
	public static Object[] execute(long timeout, TimeUnit unit, boolean getOutput, boolean killOnTimeout,
			String... command) throws IOException, InterruptedException, TimeoutException {
		ProcessBuilder b = new ProcessBuilder(command);
		if (getOutput) {
			b.redirectErrorStream(true);
		}
		Process p = b.start();
		if (timeout <= 0) {
			return null;
		}
		if (!p.waitFor(timeout, unit)) {
			if (killOnTimeout) {
				p.destroy();
			}
			throw new TimeoutException(command[0] + ": termination timeout");
		}
		Object[] ret = new Object[2];
		ret[0] = p.exitValue();
		if (getOutput) {
			try (InputStream is = p.getInputStream()) {
				if (is == null) {
					throw new IOException(command[0] + ": no output stream");
				}
				try (Scanner s = new Scanner(is)) {
					s.useDelimiter("\\A");
					if (!s.hasNext()) {
						throw new IOException(command[0] + ": no output");
					}
					ret[1] = s.next();
				}
			}
		}
		return ret;
	}

}
