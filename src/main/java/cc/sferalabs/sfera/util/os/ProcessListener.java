package cc.sferalabs.sfera.util.os;

/**
 *
 * @author Giampiero Baggiani
 *
 */
public interface ProcessListener {

	/**
	 * Called when the process is started.
	 */
	void onStarted();

	/**
	 * Called on each output line from the process.
	 * 
	 * @param line
	 *            the output line, or {@code null} if the end of the output
	 *            stream has been reached
	 */
	void onOutputLine(String line);

	/**
	 * Called on each error output line from the process.
	 * 
	 * @param line
	 *            the output line, or {@code null} if the end of the error
	 *            output stream has been reached
	 */
	void onErrorOutputLine(String line);

	/**
	 * Called if an error occurs while reading the process output.
	 * 
	 * @param e
	 *            the error
	 */
	void onReadError(Throwable e);

	/**
	 * Called when the process terminates.
	 */
	void onTerminated();

}
