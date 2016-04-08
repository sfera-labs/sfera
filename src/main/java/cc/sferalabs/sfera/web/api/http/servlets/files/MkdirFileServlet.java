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

import cc.sferalabs.sfera.util.files.FilesUtil;
import cc.sferalabs.sfera.web.api.ErrorMessage;
import cc.sferalabs.sfera.web.api.http.HttpResponse;
import cc.sferalabs.sfera.web.api.http.MissingRequiredParamException;
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

	@Override
	protected void processAuthorizedRequest(HttpServletRequest req, HttpResponse resp)
			throws ServletException, IOException {
		try {
			String path = getRequiredParameter("path", req, resp);
			Path target = Paths.get(".", path);
			if (!FilesUtil.isInRoot(target)) {
				resp.sendErrors(HttpServletResponse.SC_FORBIDDEN,
						new ErrorMessage(0, "File outside root dir"));
				return;
			}
			if (Files.isHidden(target)) {
				resp.sendErrors(HttpServletResponse.SC_FORBIDDEN,
						new ErrorMessage(0, "Cannot write hidden files"));
				return;
			}
			try {
				Files.createDirectory(target);
			} catch (NoSuchFileException | FileAlreadyExistsException e) {
				resp.sendErrors(HttpServletResponse.SC_FORBIDDEN,
						new ErrorMessage(0, e.toString()));
				return;
			}
			resp.sendResult("ok");

		} catch (MissingRequiredParamException e) {
		}
	}

}
