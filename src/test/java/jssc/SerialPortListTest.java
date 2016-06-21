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

import static org.junit.Assert.*;

import java.nio.file.Files;
import java.nio.file.Paths;

import jssc.SerialPortList;

import org.junit.Test;

public class SerialPortListTest {

	@Test
	public void testGetPortNames() {
		String[] portNames = SerialPortList.getPortNames();
		for (String portName : portNames) {
			assertTrue("Found non-existing port name: " + portName,
					Files.exists(Paths.get(portName)));
		}
	}
}
