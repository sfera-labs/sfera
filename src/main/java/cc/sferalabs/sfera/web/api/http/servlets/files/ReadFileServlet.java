package cc.sferalabs.sfera.web.api.http.servlets.files;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cc.sferalabs.sfera.util.files.FilesUtil;
import cc.sferalabs.sfera.web.api.ErrorMessage;
import cc.sferalabs.sfera.web.api.http.HttpResponse;
import cc.sferalabs.sfera.web.api.http.MissingRequiredParamException;
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

	@Override
	protected void processAuthorizedRequest(HttpServletRequest req, HttpResponse resp)
			throws ServletException, IOException {
		try {
			String path = getRequiredParameter("path", req, resp);
			Path source = Paths.get(".", path);
			if (!FilesUtil.isInRoot(source) || !Files.exists(source) || !Files.isRegularFile(source)
					|| Files.isHidden(source)) {
				resp.sendErrors(HttpServletResponse.SC_NOT_FOUND,
						new ErrorMessage(0, "File '" + path + "' not found"));
				return;
			}
			HttpServletResponse httpResp = resp.getHttpServletResponse();
			httpResp.addHeader("Content-Length", "" + Files.size(source));
			Files.copy(source, httpResp.getOutputStream());

		} catch (MissingRequiredParamException e) {
		}
	}

}
