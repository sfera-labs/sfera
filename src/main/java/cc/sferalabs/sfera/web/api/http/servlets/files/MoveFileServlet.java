package cc.sferalabs.sfera.web.api.http.servlets.files;

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

import cc.sferalabs.sfera.util.files.FilesUtil;
import cc.sferalabs.sfera.web.api.http.MissingRequiredParamException;
import cc.sferalabs.sfera.web.api.http.RestResponse;
import cc.sferalabs.sfera.web.api.http.servlets.ApiServlet;
import cc.sferalabs.sfera.web.api.http.servlets.AuthorizedAdminApiServlet;

/**
 *
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
@SuppressWarnings("serial")
public class MoveFileServlet extends AuthorizedAdminApiServlet {

	public static final String PATH = ApiServlet.PATH + "files/mv";

	private final static Logger logger = LoggerFactory.getLogger(MoveFileServlet.class);

	@Override
	protected void processAuthorizedRequest(HttpServletRequest req, RestResponse resp)
			throws ServletException, IOException {
		try {
			String source = getRequiredParam("source", req, resp);
			String target = getRequiredParam("target", req, resp);
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
			if (Files.exists(targetPath) && Files.isDirectory(targetPath)) {
				targetPath = targetPath.resolve(sourcePath.getFileName());
			}
			if (!force && Files.exists(targetPath)) {
				resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Target file already exists");
				return;
			}
			if (force) {
				Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
			} else {
				Files.move(sourcePath, targetPath);
			}
			resp.sendResult("ok");

		} catch (MissingRequiredParamException e) {
		} catch (Exception e) {
			logger.error("File move error", e);
			resp.sendError("File move error: " + e);
		}
	}

}
