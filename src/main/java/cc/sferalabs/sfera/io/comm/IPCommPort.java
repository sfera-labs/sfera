package cc.sferalabs.sfera.io.comm;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.charset.Charset;

import cc.sferalabs.sfera.core.Task;
import cc.sferalabs.sfera.core.TasksManager;

public class IPCommPort extends CommPort {

	private static final int SOCKET_CONNECT_TIMEOUT = 3000;

	private Socket socket;
	private ReaderTask readerTask;
	private boolean closed;

	/**
	 *
	 */
	private class ReaderTask extends Task {

		private boolean run = true;
		private CommPortReader reader;

		/**
		 * 
		 * @param reader
		 */
		public ReaderTask(CommPortReader reader) {
			super("CommPortReader:" + socket.getInetAddress().getHostName());
			this.reader = reader;
		}

		@Override
		protected void execute() {
			while (run) {
				try {
					InputStream in = socket.getInputStream();
					byte b = (byte) in.read();
					int len = in.available();
					byte[] bytes = new byte[len + 1];
					bytes[0] = b;
					if (len > 0) {
						readBytes(bytes, 1, len);
					}
					if (!closed) {
						reader.onRead(bytes);
					}

				} catch (Throwable t) {
					if (!closed) {
						reader.onError(t);
					}
				}
			}
		}
	}

	/**
	 * 
	 * @param portName
	 */
	protected IPCommPort(String portName) throws CommPortException {
		int colon = portName.indexOf(':');
		if (colon < 0) {
			throw new CommPortException("port name format error");
		}
		String host = portName.substring(0, colon);
		int port;
		try {
			port = Integer.parseInt(portName.substring(colon + 1));
		} catch (Exception e) {
			throw new CommPortException("ip port not specified", e);
		}
		InetSocketAddress address;
		try {
			address = new InetSocketAddress(host, port);
		} catch (Exception e) {
			throw new CommPortException("error instantiating socket: "
					+ e.getLocalizedMessage(), e);
		}
		try {
			socket = new Socket();
			socket.connect(address, SOCKET_CONNECT_TIMEOUT);
		} catch (IOException e) {
			throw new CommPortException(e.getLocalizedMessage(), e);
		}
	}

	@Override
	public void clear() throws CommPortException {
		try {
			InputStream in = socket.getInputStream();
			in.skip(in.available());
		} catch (IOException e) {
			throw new CommPortException(e.getLocalizedMessage(), e);
		}
	}

	@Override
	public void setParams(int baudRate, int dataBits, int stopBits, int parity,
			int flowControl) throws CommPortException {
	}

	@Override
	public synchronized void setReader(CommPortReader reader)
			throws CommPortException {
		if (readerTask != null) {
			throw new CommPortException("Comm port reader already set");
		}
		readerTask = new ReaderTask(reader);
		TasksManager.DEFAULT.execute(readerTask);
	}

	@Override
	public synchronized void removeReader() throws CommPortException {
		if (readerTask != null) {
			readerTask.run = false;
			readerTask = null;
		}
	}

	@Override
	public int getAvailableBytesCount() throws CommPortException {
		try {
			return socket.getInputStream().available();
		} catch (IOException e) {
			throw new CommPortException(e.getLocalizedMessage(), e);
		}
	}

	@Override
	public void writeBytes(byte[] bytes) throws CommPortException {
		try {
			OutputStream out = socket.getOutputStream();
			out.write(bytes);
			out.flush();
		} catch (IOException e) {
			throw new CommPortException(e.getLocalizedMessage(), e);
		}
	}

	@Override
	public void writeString(String string, Charset charset)
			throws CommPortException {
		writeBytes(string.getBytes(charset));
	}

	@Override
	public void close() throws CommPortException {
		closed = true;
		if (readerTask != null) {
			try {
				removeReader();
			} catch (Exception e) {
			}
		}
		try {
			if (socket != null) {
				socket.close();
			}
		} catch (IOException e) {
			throw new CommPortException(e.getLocalizedMessage(), e);
		}
	}

	@Override
	public int readBytes(byte[] b, int offset, int len)
			throws CommPortException {
		try {
			InputStream in = socket.getInputStream();
			return in.read(b, offset, len);
		} catch (IOException e) {
			throw new CommPortException(e.getLocalizedMessage(), e);
		}
	}

	@Override
	public int readBytes(byte[] b, int offset, int len, int timeoutMillis)
			throws CommPortException, CommPortTimeoutException {
		try {
			socket.setSoTimeout(timeoutMillis);
			InputStream in = socket.getInputStream();
			return in.read(b, offset, len);
		} catch (SocketTimeoutException e) {
			throw new CommPortTimeoutException(e.getLocalizedMessage(), e);
		} catch (IOException e) {
			throw new CommPortException(e.getLocalizedMessage(), e);
		} finally {
			try {
				socket.setSoTimeout(0);
			} catch (SocketException e) {
				throw new CommPortException(e.getLocalizedMessage(), e);
			}
		}
	}
}
