/*-
 * +======================================================================+
 * Sfera
 * ---
 * Copyright (C) 2015 - 2016 Sfera Labs S.r.l.
 * ---
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * -======================================================================-
 */

package cc.sferalabs.sfera.io.comm;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.charset.Charset;

import cc.sferalabs.sfera.core.services.Task;
import cc.sferalabs.sfera.core.services.TasksManager;

/**
 * Class representing a remote IP/serial gateway.
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
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
		private CommPortListener reader;

		/**
		 * 
		 * @param reader
		 */
		public ReaderTask(CommPortListener reader) {
			super("CommPortReader:" + socket.getInetAddress().getHostName());
			this.reader = reader;
		}

		@Override
		protected void execute() {
			InputStream in;
			try {
				in = socket.getInputStream();
				socket.setSoTimeout(5000);
			} catch (Throwable t) {
				onError(t);
				try {
					close();
				} catch (CommPortException e) {
				}
				return;
			}
			while (run) {
				try {
					int b = in.read();
					if (b < 0) {
						onError(new IOException("End of stream is reached"));
						try {
							close();
						} catch (CommPortException e) {
						}
						return;
					}
					int len = in.available();
					byte[] bytes = new byte[len + 1];
					bytes[0] = (byte) b;
					if (len > 0) {
						in.read(bytes, 1, len);
					}
					onRead(bytes);
				} catch (SocketTimeoutException e) {
				} catch (Throwable t) {
					onError(t);
				}
			}
		}

		/**
		 * @param bytes
		 */
		private void onRead(byte[] bytes) {
			if (!closed && run) {
				reader.onRead(bytes);
			}
		}

		/**
		 * @param t
		 */
		private void onError(Throwable t) {
			if (!closed && run) {
				reader.onError(t);
			}
		}

	}

	/**
	 * @param portName
	 *            the port name
	 */
	protected IPCommPort(String portName) {
		super(portName);
	}

	@Override
	public boolean isOpen() {
		return !closed;
	}

	@Override
	protected void doOpen() throws CommPortException {
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
			throw new CommPortException("error instantiating socket: " + e.getLocalizedMessage(), e);
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
	public void setParams(int baudRate, int dataBits, int stopBits, int parity, int flowControl)
			throws CommPortException {
	}

	@Override
	public synchronized void setListener(CommPortListener listener) throws CommPortException {
		if (readerTask != null) {
			throw new CommPortException("Comm port listener already set");
		}
		readerTask = new ReaderTask(listener);
		TasksManager.execute(readerTask);
	}

	@Override
	public synchronized void removeListener() throws CommPortException {
		if (readerTask != null) {
			readerTask.run = false;
			readerTask = null;
		} else {
			throw new CommPortException("No listner added");
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
	public void writeByte(int b) throws CommPortException {
		try {
			OutputStream out = socket.getOutputStream();
			out.write(b);
			out.flush();
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
	public void writeBytes(int[] bytes) throws CommPortException {
		try {
			OutputStream out = socket.getOutputStream();
			for (int b : bytes) {
				out.write(b);
			}
			out.flush();
		} catch (IOException e) {
			throw new CommPortException(e.getLocalizedMessage(), e);
		}
	}

	@Override
	public void writeString(String string, Charset charset) throws CommPortException {
		writeBytes(string.getBytes(charset));
	}

	@Override
	protected void doClose() throws CommPortException {
		closed = true;
		try {
			removeListener();
		} catch (Exception e) {
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
	public synchronized int readBytes(byte[] b, int offset, int len) throws CommPortException {
		try {
			socket.setSoTimeout(0);
			InputStream in = socket.getInputStream();
			return in.read(b, offset, len);
		} catch (IOException e) {
			throw new CommPortException(e.getLocalizedMessage(), e);
		}
	}

	@Override
	public synchronized int readBytes(byte[] b, int offset, int len, int timeoutMillis)
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
