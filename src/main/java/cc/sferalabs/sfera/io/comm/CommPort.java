package cc.sferalabs.sfera.io.comm;

import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jssc.SerialPort;

public abstract class CommPort {

	private static final Logger logger = LoggerFactory.getLogger(CommPort.class);

	public static final int PARITY_NONE = SerialPort.PARITY_NONE;
	public static final int PARITY_ODD = SerialPort.PARITY_ODD;
	public static final int PARITY_EVEN = SerialPort.PARITY_EVEN;
	public static final int PARITY_MARK = SerialPort.PARITY_MARK;
	public static final int PARITY_SPACE = SerialPort.PARITY_SPACE;

	public static final int FLOWCONTROL_RTSCTS = jssc.SerialPort.FLOWCONTROL_RTSCTS_IN
			| jssc.SerialPort.FLOWCONTROL_RTSCTS_OUT;

	public static final int FLOWCONTROL_XONXOFF = jssc.SerialPort.FLOWCONTROL_XONXOFF_IN
			| jssc.SerialPort.FLOWCONTROL_XONXOFF_OUT;

	/**
	 * 
	 * @param portName
	 * @return
	 * @throws CommPortException
	 */
	public static CommPort open(String portName) throws CommPortException {
		logger.debug("Trying getting local port '{}'", portName);
		CommPort commPort = null;
		try {
			commPort = new LocalCommPort(portName);
		} catch (CommPortException e) {
			logger.debug("Error getting local port '{}': {}", portName,
					e.getLocalizedMessage());
			try {
				logger.debug("Trying getting IP port '{}'", portName);
				commPort = new IPCommPort(portName);
			} catch (CommPortException e1) {
				logger.debug("Error getting IP port '{}': {}", portName,
						e.getLocalizedMessage());
			}
		}
		if (commPort == null) {
			throw new CommPortException("could not open port " + portName);
		}
		logger.debug("Comm port '{}' open", portName);
		return commPort;
	}

	/**
	 * 
	 * @param baudRate
	 * @param dataBits
	 * @param stopBits
	 * @param parity
	 * @param flowControl
	 * @throws CommPortException
	 */
	public abstract void setParams(int baudRate, int dataBits, int stopBits, int parity,
			int flowControl) throws CommPortException;

	/**
	 * 
	 * @throws CommPortException
	 */
	public abstract void clear() throws CommPortException;

	/**
	 * 
	 * @param reader
	 * @throws CommPortException
	 */
	public abstract void setReader(CommPortReader reader) throws CommPortException;

	/**
	 * 
	 * @throws CommPortException
	 */
	public abstract void removeReader() throws CommPortException;

	/**
	 * 
	 * @return
	 * @throws CommPortException
	 */
	public abstract int getAvailableBytesCount() throws CommPortException;

	/**
	 * 
	 * @param bytes
	 */
	public abstract void writeBytes(byte[] bytes) throws CommPortException;

	/**
	 * 
	 * @param string
	 * @param charset
	 * @throws CommPortException
	 */
	public abstract void writeString(String string, Charset charset) throws CommPortException;

	/**
	 * 
	 * @throws CommPortException
	 */
	public abstract void close() throws CommPortException;

	/**
	 * 
	 * @param b
	 * @param offset
	 * @param len
	 * @return
	 * @throws CommPortException
	 */
	public abstract int readBytes(byte[] b, int offset, int len) throws CommPortException;

	/**
	 * 
	 * @param b
	 * @param offset
	 * @param len
	 * @param timeoutMillis
	 * @return
	 * @throws CommPortException
	 * @throws CommPortTimeoutException
	 */
	public abstract int readBytes(byte[] b, int offset, int len, int timeoutMillis)
			throws CommPortException, CommPortTimeoutException;

}
