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
