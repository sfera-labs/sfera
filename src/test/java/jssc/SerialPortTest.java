package jssc;

import static org.junit.Assert.*;
import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;

import org.junit.Test;

public class SerialPortTest {

	@Test
	public void testSerialPortWithSierraGsmModem() {
		for (String portName : SerialPortList.getPortNames()) {
			if (portName.equals("/dev/tty.usbmodem411")) {
				SerialPort serialPort = new SerialPort(portName);
				try {
					assertTrue("openPort() failed", serialPort.openPort());
					assertTrue("setParams() failed", serialPort.setParams(
							SerialPort.BAUDRATE_115200, SerialPort.DATABITS_8,
							SerialPort.STOPBITS_1, SerialPort.PARITY_NONE));
					assertTrue(
							"setFlowControlMode() failed",
							serialPort
									.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN
											| SerialPort.FLOWCONTROL_RTSCTS_OUT));
					assertTrue("writeBytes() failed",
							serialPort.writeString("AT\r"));

					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
					}

					String resp = serialPort.readString();
					assertTrue("resp error: " + resp, resp.contains("OK"));

					assertTrue("closePort() failed", serialPort.closePort());

				} catch (SerialPortException e) {
					fail("SerialPortException: " + e);
				}

				break;
			}
		}
	}
}
