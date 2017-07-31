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

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import jssc.SerialPortTimeoutException;

/**
 * Class representing a serial port available on the host machine.
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class LocalCommPort extends CommPort {

	private final SerialPort serialPort;

	/**
	 * @param portName
	 *            local port name
	 * @throws CommPortException
	 *             if an error occurs when creating or opening the port
	 */
	LocalCommPort(String portName) throws CommPortException {
		super(portName);
		try {
			this.serialPort = new SerialPort(portName);
			if (!serialPort.openPort()) {
				throw new CommPortException("open failed");
			}
		} catch (Exception e) {
			throw new CommPortException(e.getLocalizedMessage(), e);
		}
	}

	@Override
	public void clear() throws CommPortException {
		try {
			if (!serialPort.purgePort(SerialPort.PURGE_RXCLEAR)) {
				throw new CommPortException("clear failed");
			}
		} catch (SerialPortException e) {
			throw new CommPortException(e.getLocalizedMessage(), e);
		}
	}

	@Override
	public void setParams(int baudRate, int dataBits, int stopBits, int parity, int flowControl)
			throws CommPortException {
		try {
			if (!serialPort.setParams(baudRate, dataBits, stopBits, parity)) {
				throw new CommPortException("error setting params");
			}
			if (!serialPort.setFlowControlMode(flowControl)) {
				throw new CommPortException("error setting flow control");
			}
		} catch (SerialPortException e) {
			throw new CommPortException(e.getLocalizedMessage(), e);
		}
	}

	@Override
	public synchronized void setListener(CommPortListener listener) throws CommPortException {
		SerialPortEventListener spel = new SerialPortEventListener() {

			@Override
			public void serialEvent(SerialPortEvent event) {
				try {
					int len = event.getEventValue();
					byte[] bytes = new byte[len];
					readBytes(bytes, 0, len);
					listener.onRead(bytes);
				} catch (Throwable t) {
					listener.onError(t);
				}
			}
		};
		try {
			serialPort.addEventListener(spel);
		} catch (SerialPortException e) {
			throw new CommPortException(e.getLocalizedMessage(), e);
		}
	}

	@Override
	public synchronized void removeListener() throws CommPortException {
		try {
			serialPort.removeEventListener();
		} catch (SerialPortException e) {
			throw new CommPortException(e.getLocalizedMessage(), e);
		}
	}

	@Override
	public int getAvailableBytesCount() throws CommPortException {
		try {
			int n = serialPort.getInputBufferBytesCount();
			if (n < 0) {
				throw new CommPortException("operation failed");
			}
			return n;
		} catch (SerialPortException e) {
			throw new CommPortException(e.getLocalizedMessage(), e);
		}
	}

	@Override
	public void writeByte(int b) throws CommPortException {
		try {
			if (!serialPort.writeByte((byte) b)) {
				throw new CommPortException("write failed");
			}
		} catch (SerialPortException e) {
			throw new CommPortException(e.getLocalizedMessage(), e);
		}
	}

	@Override
	public void writeBytes(byte[] bytes) throws CommPortException {
		try {
			if (!serialPort.writeBytes(bytes)) {
				throw new CommPortException("write failed");
			}
		} catch (SerialPortException e) {
			throw new CommPortException(e.getLocalizedMessage(), e);
		}
	}

	@Override
	public void writeBytes(int[] bytes) throws CommPortException {
		try {
			if (!serialPort.writeIntArray(bytes)) {
				throw new CommPortException("write failed");
			}
		} catch (SerialPortException e) {
			throw new CommPortException(e.getLocalizedMessage(), e);
		}
	}

	@Override
	public void writeString(String string, Charset charset) throws CommPortException {
		try {
			if (!serialPort.writeString(string, charset.name())) {
				throw new CommPortException("write failed");
			}
		} catch (SerialPortException | UnsupportedEncodingException e) {
			throw new CommPortException(e.getLocalizedMessage(), e);
		}
	}

	@Override
	public void close() throws CommPortException {
		try {
			removeListener();
		} catch (Exception e) {
		}
		try {
			if (!serialPort.closePort()) {
				throw new CommPortException("close failed");
			}
		} catch (SerialPortException e) {
			throw new CommPortException(e.getLocalizedMessage(), e);
		}
	}

	@Override
	public synchronized int readBytes(byte[] b, int offset, int len) throws CommPortException {
		try {
			if (len < 0) {
				throw new IndexOutOfBoundsException("parameter 'len' is negative");
			}
			byte[] read = serialPort.readBytes(len);
			for (int i = 0; i < read.length; i++) {
				b[offset + i] = read[i];
			}
			return read.length;

		} catch (SerialPortException e) {
			throw new CommPortException(e.getLocalizedMessage(), e);
		}
	}

	@Override
	public synchronized int readBytes(byte[] b, int offset, int len, int timeoutMillis)
			throws CommPortException, CommPortTimeoutException {
		try {
			byte[] read = serialPort.readBytes(len, timeoutMillis);
			for (int i = 0; i < read.length; i++) {
				b[offset + i] = read[i];
			}
			return read.length;

		} catch (SerialPortException e) {
			throw new CommPortException(e.getLocalizedMessage(), e);
		} catch (SerialPortTimeoutException e) {
			throw new CommPortTimeoutException(e.getLocalizedMessage(), e);
		}
	}
}
