package cc.sferalabs.sfera.http.api.rest.servlets.files;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.sferalabs.sfera.http.api.rest.RestResponse;
import cc.sferalabs.sfera.http.api.rest.servlets.ApiServlet;
import cc.sferalabs.sfera.util.files.FilesUtil;

/**
 *
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
@SuppressWarnings("serial")
public class MoveFileServlet extends AuthorizedAdminServlet {

	public static final String PATH = ApiServlet.PATH + "files/mv";

	private final static Logger logger = LoggerFactory.getLogger(MoveFileServlet.class);

	@Override
	protected void processAuthorizedRequest(HttpServletRequest req, RestResponse resp)
			throws ServletException, IOException {
		String source = req.getParameter("source");
		if (source == null) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Param 'source' not specified");
			return;
		}
		String target = req.getParameter("target");
		if (target == null) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Param 'target' not specified");
			return;
		}
		boolean force = "true".equalsIgnoreCase(req.getParameter("force"));

		Path sourcePath = Paths.get(".", source);
		if (!FilesUtil.isInRoot(sourcePath) || !Files.exists(sourcePath)
				|| Files.isHidden(sourcePath)) {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND, "File '" + source + "' not found");
			return;
		}

		Path targetPath = Paths.get(".", target);
		if (!FilesUtil.isInRoot(targetPath)) {
			resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Target file outside root dir");
			return;
		}
		if (Files.isHidden(targetPath)) {
			resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Cannot write hidden files");
			return;
		}
		if (!force && Files.exists(targetPath)) {
			resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Target file already exists");
			return;
		}

		try {
			if (force) {
				Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
			} else {
				Files.move(sourcePath, targetPath);
			}
			resp.sendResult("ok");
		} catch (Exception e) {
			logger.error("File move error", e);
			resp.sendError("File move error: " + e);
		}
	}

}
