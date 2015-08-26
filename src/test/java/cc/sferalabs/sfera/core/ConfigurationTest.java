package cc.sferalabs.sfera.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class ConfigurationTest {

	@Test
	public void testGets() throws IOException, URISyntaxException {
		URL file = getClass().getResource("/config-test.yml");
		assertNotNull("Test file missing", file);
		Configuration config = new Configuration(Paths.get(file.toURI()));
		assertEquals("uno", config.get("uno", null));
		assertEquals("due", config.get("due", null));
		assertEquals(new Integer(3), config.get("tre", 0));
		assertEquals(true, config.get("qua", false));
		assertEquals(true, config.get("cin", false));
		List<Map<String, String>> sei = config.get("sei", null);
		assertEquals("[{a=a, a1=a1}, {b=b}]", sei.toString());
		List<Integer> set = config.get("set", null);
		assertEquals(3, set.size());
		Map<String, Object> ott = config.get("ott", null);
		assertEquals("{p=false, q=1.2}", ott.toString());
		assertNull(config.get("notThere", null));
		Object def = new Object();
		assertEquals(def, config.get("notThere", def));
	}

}
