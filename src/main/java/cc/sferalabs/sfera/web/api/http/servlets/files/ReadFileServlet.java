package cc.sferalabs.sfera.web.api.http.servlets.files;

import java.io.IOException;
import java.nio.file.Files;
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
 *
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
@SuppressWarnings("serial")
public class ReadFileServlet extends AuthorizedAdminApiServlet {

	public static final String PATH = ApiServlet.PATH + "files/read";

	private final static Logger logger = LoggerFactory.getLogger(ReadFileServlet.class);

	@Override
	protected void processAuthorizedRequest(HttpServletRequest req, RestResponse resp)
			throws ServletException, IOException {
		try {
			String path = getRequiredParam("path", req, resp);
			Path source = Paths.get(".", path);
			if (!FilesUtil.isInRoot(source) || !Files.exists(source) || !Files.isRegularFile(source)
					|| Files.isHidden(source)) {
				resp.sendError(HttpServletResponse.SC_NOT_FOUND, "File '" + path + "' not found");
				return;
			}
			HttpServletResponse httpResp = resp.getHttpServletResponse();
			httpResp.addHeader("Content-Length", "" + Files.size(source));
			Files.copy(source, httpResp.getOutputStream());

		} catch (MissingRequiredParamException e) {
		} catch (Exception e) {
			logger.error("File get error", e);
			resp.sendError("File get error: " + e);
		}
	}

}
