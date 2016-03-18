package cc.sferalabs.sfera.http.api.rest.servlets.files;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.sferalabs.sfera.http.api.rest.MissingRequiredParamException;
import cc.sferalabs.sfera.http.api.rest.RestResponse;
import cc.sferalabs.sfera.http.api.rest.servlets.ApiServlet;
import cc.sferalabs.sfera.http.api.rest.servlets.AuthorizedAdminApiServlet;
import cc.sferalabs.sfera.util.files.FilesUtil;

/**
 *
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
@SuppressWarnings("serial")
public class ReadGetFileServlet extends AuthorizedAdminApiServlet {

	public static final String PATH_READ = ApiServlet.PATH + "files/read";
	public static final String PATH_GET = ApiServlet.PATH + "files/get";

	private final static Logger logger = LoggerFactory.getLogger(ReadGetFileServlet.class);

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
			if (req.getRequestURI().endsWith("get")) {
				httpResp.addHeader("Content-Type", "application/octet-stream");
				httpResp.addHeader("Content-Disposition",
						"attachment; filename=\"" + source.getFileName().toString() + "\"");
			}
			Files.copy(source, httpResp.getOutputStream());

		} catch (MissingRequiredParamException e) {
		} catch (Exception e) {
			logger.error("File get error", e);
			resp.sendError("File get error: " + e);
		}
	}

}
