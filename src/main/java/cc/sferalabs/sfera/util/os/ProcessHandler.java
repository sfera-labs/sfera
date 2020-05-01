package cc.sferalabs.sfera.util.os;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import cc.sferalabs.sfera.core.services.Task;
import cc.sferalabs.sfera.core.services.TasksManager;

/**
 *
 * @author Giampiero Baggiani
 *
 */
public class ProcessHandler {

	private final String name;
	private final ProcessBuilder processBuilder;
	private ProcessListener listener;
	private Process process;
	private BufferedWriter out;
	private InputStream in;
	private InputStream errorIn;

	private boolean running;

	private final AtomicInteger terminatedStreamReaders = new AtomicInteger();

	/**
	 * Constructs a process handler with the specified operating system program and
	 * arguments.
	 * 
	 * @param name
	 *            a descriptive name for this process handler
	 * @param command
	 *            a string array containing the program and its arguments
	 */
	public ProcessHandler(String name, String... command) {
		this.name = Objects.requireNonNull(name, "name must be not null");
		this.processBuilder = new ProcessBuilder(command);
	}

	/**
	 * Returns whether or not this process is running.
	 * 
	 * @return {@code true} if the process is running, {@code false} otherwise
	 */
	public boolean isRunning() {
		return running;
	}

	/**
	 * Returns the handled process.
	 * 
	 * @return the handled process
	 */
	public Process getProcess() {
		return process;
	}

	/**
	 * Set the listener for this process. To be called before
	 * {@link ProcessHandler#start() start()}.
	 * 
	 * @param listener
	 *            the listener to set
	 */
	public void setListener(ProcessListener listener) {
		this.listener = listener;
	}

	/**
	 * Starts the process.
	 * 
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public void start() throws IOException {
		process = processBuilder.start();
		running = true;
		OutputStream outStr = process.getOutputStream();
		if (outStr != null) {
			out = new BufferedWriter(new OutputStreamWriter(outStr));
		}
		if (listener != null) {
			in = process.getInputStream();
			errorIn = process.getErrorStream();
			TasksManager.execute(new ProcessStreamReader(name + "-out", in, c -> listener.onOutputLine(c)));
			TasksManager.execute(new ProcessStreamReader(name + "-err", errorIn, c -> listener.onErrorOutputLine(c)));
			listener.onStarted();
		}
	}

	/**
	 * Writes a string to the process input stream.
	 * 
	 * @param str
	 *            String to be written
	 * @throws IOException
	 *             If an I/O error occurs
	 */
	public void write(String str) throws IOException {
		if (out == null) {
			throw new IOException("no output stream");
		}
		out.write(str);
		out.flush();
	}

	/**
	 *
	 */
	private class ProcessStreamReader extends Task {

		private InputStream in;
		private Consumer<String> consumer;

		public ProcessStreamReader(String name, InputStream in, Consumer<String> consumer) {
			super("ProcessStreamReader-" + name);
			this.in = in;
			this.consumer = consumer;
		}

		@Override
		protected void execute() {
			try {
				if (in != null) {
					try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
						String line;
						while (running && (line = reader.readLine()) != null) {
							consumer.accept(line);
						}
					} catch (IOException e) {
						if (running) {
							listener.onReadError(e);
						}
					}
				}
				if (running) {
					consumer.accept(null);
				}
			} finally {
				int t = terminatedStreamReaders.incrementAndGet();
				if (t == 2) {
					listener.onTerminated();
					doQuit();
				}
			}
		}
	}

	/**
	 * 
	 */
	private void doQuit() {
		running = false;
		try {
			out.close();
		} catch (Exception e) {
		}
		try {
			in.close();
		} catch (Exception e) {
		}
		try {
			errorIn.close();
		} catch (Exception e) {
		}
		if (process != null) {
			process.destroyForcibly();
		}
	}

	/**
	 * Kills the process.
	 */
	public void quit() {
		doQuit();
	}

}
