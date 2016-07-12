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

package jssc;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Ignore;
import org.junit.Test;

public class SerialPortTest {

	@Ignore
	@Test
	public void testSerialPortWithSierraGsmModem() {
		for (String portName : SerialPortList.getPortNames()) {
			if (portName.equals("/dev/tty.usbmodem411")) {
				SerialPort serialPort = new SerialPort(portName);
				try {
					assertTrue("openPort() failed", serialPort.openPort());
					assertTrue("setParams() failed",
							serialPort.setParams(SerialPort.BAUDRATE_115200, SerialPort.DATABITS_8,
									SerialPort.STOPBITS_1, SerialPort.PARITY_NONE));
					assertTrue("setFlowControlMode() failed", serialPort.setFlowControlMode(
							SerialPort.FLOWCONTROL_RTSCTS_IN | SerialPort.FLOWCONTROL_RTSCTS_OUT));
					assertTrue("writeBytes() failed", serialPort.writeString("AT\r"));

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
