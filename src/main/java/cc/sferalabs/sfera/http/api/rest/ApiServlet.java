package cc.sferalabs.sfera.http.api.rest;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract class to be extended by all servlets handling REST API requests.
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
@SuppressWarnings("serial")
public abstract class ApiServlet extends HttpServlet {

	/**
	 * Base path for REST API URLs
	 */
	public static final String PATH = "/api/";

	private static final Logger logger = LoggerFactory.getLogger(ApiServlet.class);

	/**
	 * Processes the API request.
	 * 
	 * @param req
	 *            an {@link HttpServletRequest} object that contains the request
	 *            the client has made of the servlet
	 * @param resp
	 *            an {@link HttpServletResponse} object that contains the
	 *            response the servlet sends to the client
	 * 
	 * @throws ServletException
	 *             if the request could not be handled
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	abstract protected void processRequest(HttpServletRequest req, RestResponse resp)
			throws ServletException, IOException;

	/**
	 * 
	 * @param req
	 * @param resp
	 * @throws IOException
	 */
	private void doRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		RestResponse rr = new RestResponse(resp);
		try {
			resp.setHeader("Cache-Control",
					"private, max-age=0, no-cache, no-store, must-revalidate");
			resp.setCharacterEncoding("UTF-8");
			processRequest(req, rr);
		} catch (Throwable t) {
			logger.error("Exception processing HTTP API request: " + req.getRequestURI(), t);
			String error = t.getMessage();
			if (error == null) {
				error = t.toString();
			}
			rr.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, error);
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doRequest(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doRequest(req, resp);
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doRequest(req, resp);
	}

	@Override
	protected void doHead(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doRequest(req, resp);
	}

}
