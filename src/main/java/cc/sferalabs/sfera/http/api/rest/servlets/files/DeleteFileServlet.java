package cc.sferalabs.sfera.http.api.rest.servlets.files;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

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
public class DeleteFileServlet extends AuthorizedAdminServlet {

	public static final String PATH = ApiServlet.PATH + "files/rm";

	private final static Logger logger = LoggerFactory.getLogger(DeleteFileServlet.class);

	@Override
	protected void processAuthorizedRequest(HttpServletRequest req, RestResponse resp)
			throws ServletException, IOException {
		String path = req.getParameter("path");
		if (path == null) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Param 'path' not specified");
			return;
		}
		Path source = Paths.get(".", path);
		if (!FilesUtil.isInRoot(source) || !Files.exists(source) || Files.isHidden(source)) {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND, "File '" + path + "' not found");
			return;
		}

		try {
			Files.walkFileTree(source, new FileDeleter());
			resp.sendResult("ok");
		} catch (Exception e) {
			logger.error("File delete error", e);
			resp.sendError("File delete error: " + e);
		}
	}

	/**
	 *
	 */
	private static class FileDeleter extends SimpleFileVisitor<Path> {

		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
			Files.delete(file);
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
			Files.delete(file);
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
			Files.delete(dir);
			return FileVisitResult.CONTINUE;
		}
	}

}
