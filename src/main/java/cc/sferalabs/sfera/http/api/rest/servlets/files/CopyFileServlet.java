package cc.sferalabs.sfera.http.api.rest.servlets.files;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;

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
public class CopyFileServlet extends AuthorizedAdminApiServlet {

	public static final String PATH = ApiServlet.PATH + "files/cp";

	private final static Logger logger = LoggerFactory.getLogger(CopyFileServlet.class);

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
			if (!force && Files.exists(targetPath)) {
				resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Target file already exists");
				return;
			}

			if (Files.isDirectory(sourcePath)) {
				Files.walkFileTree(sourcePath, new FileCopier(sourcePath, targetPath));
			} else {
				if (force) {
					Files.copy(sourcePath, targetPath, StandardCopyOption.COPY_ATTRIBUTES,
							StandardCopyOption.REPLACE_EXISTING);
				} else {
					Files.copy(sourcePath, targetPath, StandardCopyOption.COPY_ATTRIBUTES,
							LinkOption.NOFOLLOW_LINKS);
				}
			}
			resp.sendResult("ok");

		} catch (MissingRequiredParamException e) {
		} catch (Exception e) {
			logger.error("File copy error", e);
			resp.sendError("File copy error: " + e);
		}
	}

	/**
	 *
	 */
	private static class FileCopier extends SimpleFileVisitor<Path> {
		private final Path source;
		private final Path target;

		private FileCopier(Path source, Path target) {
			this.source = source;
			this.target = target;
		}

		@Override
		public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
				throws IOException {
			Path newdir = target.resolve(source.relativize(dir));
			Files.copy(dir, newdir, StandardCopyOption.COPY_ATTRIBUTES,
					StandardCopyOption.REPLACE_EXISTING);
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
			Files.copy(file, target.resolve(source.relativize(file)),
					StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);
			return FileVisitResult.CONTINUE;
		}

	}

}
