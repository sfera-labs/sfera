package cc.sferalabs.sfera.web.api.http.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.sferalabs.sfera.web.api.http.Connection;
import cc.sferalabs.sfera.web.api.http.HttpResponse;
import cc.sferalabs.sfera.web.api.http.MissingRequiredParamException;

/**
 * API servlet handlig subscription requests.
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
@SuppressWarnings("serial")
public class SubscribeServlet extends ConnectionRequiredApiServlet {

	public static final String PATH = ApiServlet.PATH + "subscribe";

	private static final Logger logger = LoggerFactory.getLogger(SubscribeServlet.class);

	@Override
	protected void processConnectionRequest(HttpServletRequest req, HttpResponse resp,
			Connection connection) throws ServletException, IOException {
		try {
			String nodes = getRequiredParameter("nodes", req, resp);
			connection.subscribe(nodes);
			logger.debug("Subscribed - session '{}' connection '{}' nodes: {}",
					req.getSession(false).getId(), connection.getId(), nodes);
			resp.sendResult("ok");
		} catch (MissingRequiredParamException e) {
		}
	}

}
