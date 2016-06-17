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
public class InfoServlet extends AuthorizedUserServlet {

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
