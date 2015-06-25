package cc.sferalabs.sfera.http.api;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SuppressWarnings("serial")
public abstract class ApiServlet extends HttpServlet {

	/**
	 * 
	 */
	public static final String PATH = "/x/";

	private static final Logger logger = LogManager.getLogger();

	/**
	 * 
	 * @param req
	 * @param resp
	 * @throws Exception
	 */
	abstract protected void processRequest(HttpServletRequest req,
			HttpServletResponse resp) throws Exception;

	/**
	 * 
	 * @param req
	 * @param resp
	 * @throws IOException
	 */
	private void doRequest(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		try {
			resp.setHeader("Cache-Control", "max-age=0, no-cache, no-store");
			processRequest(req, resp);
		} catch (Throwable t) {
			logger.error("Exception processing HTTP API request", t);
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					t.getMessage());
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
