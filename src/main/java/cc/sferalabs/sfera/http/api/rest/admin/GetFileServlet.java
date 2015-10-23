package cc.sferalabs.sfera.http.api.rest.admin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.sferalabs.sfera.http.api.rest.RestResponse;

/**
 *
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
@SuppressWarnings("serial")
public class GetFileServlet extends AuthorizedAdminServlet {

	public static final String PATH = AuthorizedAdminServlet.PATH + "file/get";

	private final static Logger logger = LoggerFactory.getLogger(GetFileServlet.class);

	@Override
	protected void processAuthorizedRequest(HttpServletRequest req, RestResponse resp)
			throws ServletException, IOException {
		String path = req.getParameter("path");
		if (path == null) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Param 'path' not specified");
			return;
		}
		Path source = Paths.get(path);
		if (!Files.exists(source)) {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND, "File '" + path + "' not found");
			return;
		}

		try {
			Files.copy(source, resp.getHttpServletResponse().getOutputStream());
		} catch (Exception e) {
			logger.error("File get error", e);
			resp.sendError("File get error: " + e.getMessage());
		}
	}

}
