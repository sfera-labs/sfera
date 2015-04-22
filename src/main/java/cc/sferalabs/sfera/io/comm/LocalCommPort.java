package cc.sferalabs.sfera.io.comm;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import jssc.SerialPortTimeoutException;

public class LocalCommPort extends CommPort {

	private final SerialPort serialPort;

	/**
	 * 
	 * @param portName
	 * @throws CommPortException
	 */
	public LocalCommPort(String portName) throws CommPortException {
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
	public void setParams(int baudRate, int dataBits, int stopBits, int parity,
			int flowControl) throws CommPortException {
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
	public void setReader(CommPortReader reader) throws CommPortException {
		SerialPortEventListener spel = new SerialPortEventListener() {

			@Override
			public void serialEvent(SerialPortEvent event) {
				try {
					int len = event.getEventValue();
					byte[] bytes = new byte[len];
					readBytes(bytes, 0, len);
					reader.onRead(bytes);

				} catch (Throwable t) {
					reader.onError(t);
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
	public void removeReader() throws CommPortException {
		try {
			serialPort.removeEventListener();
		} catch (SerialPortException e) {
			throw new CommPortException(e.getLocalizedMessage(), e);
		}
	}

	@Override
	public int getAvailableBytesCount() throws CommPortException {
		try {
			return serialPort.getInputBufferBytesCount();
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
	public void writeString(String string, Charset charset)
			throws CommPortException {
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
			removeReader();
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
	public int readBytes(byte[] b, int offset, int len)
			throws CommPortException {
		try {
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
	public int readBytes(byte[] b, int offset, int len, int timeoutMillis)
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