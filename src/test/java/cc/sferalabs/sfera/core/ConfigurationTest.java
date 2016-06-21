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

	@Test
	public void testLoadAs() throws IOException, URISyntaxException {
		URL file = getClass().getResource("/config-test.yml");
		assertNotNull("Test file missing", file);
		Configuration config = new Configuration(Paths.get(file.toURI()));
		ConfigTestStructure cts = config.loadAs(ConfigTestStructure.class);
		assertEquals("uno", cts.uno);
		assertEquals("due", cts.due);
		assertEquals(3, cts.tre);
		assertEquals(true, cts.qua);
		assertEquals(true, cts.cin);
		assertEquals("[{a=a, a1=a1}, {b=b}]", cts.sei.toString());
		assertEquals(3, cts.set.size());
		assertEquals("{p=false, q=1.2}", cts.ott.toString());
		assertNull(cts.notThere);
	}

}
