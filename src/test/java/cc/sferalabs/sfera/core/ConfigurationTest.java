package cc.sferalabs.sfera.core;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class ConfigurationTest {

	@Test
	public void testGets() throws IOException {
		Configuration config = new Configuration(Paths.get("config-test.yml"));
		assertEquals("uno", config.get("uno", null));
		assertEquals("due", config.get("due", null));
		assertEquals(new Integer(3), config.get("tre", 0));
		assertEquals(true, config.get("qua", false));
		assertEquals(true, config.get("cin", false));
		List<Map<String, String>> sei = config.get("sei", null);
		List<Integer> set = config.get("set", null);
		Map<String, Object> ott = config.get("ott", null);
	}

}
