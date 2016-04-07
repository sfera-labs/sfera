package cc.sferalabs.sfera.web.api.http.servlets.files;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

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
 * API admin servlet handling file editing.
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
@SuppressWarnings("serial")
public class MkdirFileServlet extends AuthorizedAdminApiServlet {

	public static final String PATH = ApiServlet.PATH + "files/mkdir";

	private final static Logger logger = LoggerFactory.getLogger(MkdirFileServlet.class);

	@Override
	protected void processAuthorizedRequest(HttpServletRequest req, RestResponse resp)
			throws ServletException, IOException {
		try {
			String path = getRequiredParam("path", req, resp);
			Path target = Paths.get(".", path);
			if (!FilesUtil.isInRoot(target)) {
				resp.sendError(HttpServletResponse.SC_FORBIDDEN, "File outside root dir");
				return;
			}
			if (Files.isHidden(target)) {
				resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Cannot write hidden files");
				return;
			}
			try {
				Files.createDirectory(target);
			} catch (NoSuchFileException | FileAlreadyExistsException e) {
				resp.sendError(HttpServletResponse.SC_FORBIDDEN, e.toString());
				return;
			}
			resp.sendResult("ok");

		} catch (MissingRequiredParamException e) {
		} catch (Exception e) {
			logger.error("File write error", e);
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "File write error: " + e);
		}
	}

}
