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
public class DownloadFileServlet extends AuthorizedAdminApiServlet {

	public static final String PATH = ApiServlet.PATH + "files/download";

	private final static Logger logger = LoggerFactory.getLogger(DownloadFileServlet.class);

	@Override
	protected void processAuthorizedRequest(HttpServletRequest req, RestResponse resp)
			throws ServletException, IOException {
		Path tempZipFile = null;
		try {
			String path = getRequiredParam("path", req, resp);
			Path source = Paths.get(".", path);
			if (!FilesUtil.isInRoot(source) || !Files.exists(source) || Files.isHidden(source)) {
				resp.sendError(HttpServletResponse.SC_NOT_FOUND, "File '" + path + "' not found");
				return;
			}
			String fileName = source.getFileName().toString();
			if (Files.isDirectory(source)) {
				tempZipFile = Files.createTempFile(getClass().getName(), ".zip");
				logger.debug("Compressing directory '{}'...", fileName);
				FilesUtil.zip(source, tempZipFile);
				logger.debug("Done compressing '{}'", fileName);
				source = tempZipFile;
				fileName += ".zip";
			}
			HttpServletResponse httpResp = resp.getHttpServletResponse();
			httpResp.addHeader("Content-Length", "" + Files.size(source));
			httpResp.addHeader("Content-Type", "application/octet-stream");
			httpResp.addHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

			Files.copy(source, httpResp.getOutputStream());

		} catch (MissingRequiredParamException e) {
		} catch (Exception e) {
			logger.error("File download error", e);
			resp.sendError("File download error: " + e);
		} finally {
			if (tempZipFile != null) {
				try {
					Files.delete(tempZipFile);
				} catch (Exception e) {
				}
			}
		}
	}

}
