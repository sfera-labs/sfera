package cc.sferalabs.sfera.web.api.http.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cc.sferalabs.sfera.web.api.ErrorMessage;
import cc.sferalabs.sfera.web.api.http.Connection;
import cc.sferalabs.sfera.web.api.http.ConnectionsSet;
import cc.sferalabs.sfera.web.api.http.HttpResponse;
import cc.sferalabs.sfera.web.api.http.MissingRequiredParamException;

/**
 * Abstract {@link AuthorizedUserServlet} class extension to be extended by
 * servlets handling requests that need a previously instantiated connection.
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
@SuppressWarnings("serial")
public abstract class ConnectionRequiredApiServlet extends AuthorizedUserServlet {

	@Override
	protected void processAuthorizedRequest(HttpServletRequest req, HttpResponse resp)
			throws ServletException, IOException {
		try {
			String cid = getRequiredParameter("cid", req, resp);

			HttpSession session = req.getSession(false);
			ConnectionsSet connections = (ConnectionsSet) session
					.getAttribute(ConnectServlet.SESSION_ATTR_CONNECTIONS);
			if (connections == null) {
				resp.sendErrors(HttpServletResponse.SC_BAD_REQUEST,
						new ErrorMessage(0, "No connections instantiated"));
			}
			Connection connection = connections.get(cid);
			if (connection == null) {
				resp.sendErrors(HttpServletResponse.SC_BAD_REQUEST,
						new ErrorMessage(0, "Connection '" + cid + "' not found"));
			}

			processConnectionRequest(req, resp, connection);
		} catch (MissingRequiredParamException e) {
		}
	}

	/**
	 * Processes the authorized API request.
	 *
	 * @param req
	 *            an {@link HttpServletRequest} object that contains the request
	 *            the client has made of the servlet
	 * @param resp
	 *            an {@link HttpServletResponse} object that contains the
	 *            response the servlet sends to the client
	 * @param connection
	 *            the connection
	 *
	 * @throws ServletException
	 *             if the request could not be handled
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	protected abstract void processConnectionRequest(HttpServletRequest req, HttpResponse resp,
			Connection connection) throws ServletException, IOException;

}
