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

package cc.sferalabs.sfera.web.api.http.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import cc.sferalabs.sfera.core.Plugin;
import cc.sferalabs.sfera.core.Plugins;
import cc.sferalabs.sfera.core.SystemNode;
import cc.sferalabs.sfera.web.api.http.HttpResponse;

/**
 * API servlet handlig connection requests.
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
@SuppressWarnings("serial")
public class InfoServlet extends AuthenticatedUserServlet {

	public static final String PATH = ApiServlet.PATH + "info";

	@Override
	protected void processAuthorizedRequest(HttpServletRequest req, HttpResponse resp)
			throws ServletException, IOException {
		resp.put("version", SystemNode.getVersion());
		Map<String, Map<String, String>> plugins = new HashMap<>();
		for (Plugin plugin : Plugins.getAll().values()) {
			Map<String, String> plugProp = new HashMap<>();
			plugProp.put("name", plugin.getName());
			plugProp.put("description", plugin.getDescription());
			plugProp.put("version", plugin.getVersion());
			plugins.put(plugin.getId(), plugProp);
		}
		resp.put("plugins", plugins);
		resp.send();
	}

}
