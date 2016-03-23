package cc.sferalabs.sfera.http.api.rest.servlets.files;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

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
public class UploadFileServlet extends MultipartServlet {

	public static final String PATH = ApiServlet.PATH + "files/upload";

	private final static Logger logger = LoggerFactory.getLogger(UploadFileServlet.class);

	@Override
	protected void processMultipartRequest(HttpServletRequest req, RestResponse resp)
			throws ServletException, IOException {
		List<String> errors = new ArrayList<>();
		try {
			String path = getMultipartParameter("path", req);
			if (path == null) {
				resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Param 'path' not specified");
				return;
			}

			boolean force = "true".equalsIgnoreCase(getMultipartParameter("force", req));

			Path targetDir = Paths.get(".", path);
			if (!FilesUtil.isInRoot(targetDir) || !Files.isDirectory(targetDir)) {
				resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Directory not found");
				return;
			}

			Map<Part, Path> files = new HashMap<>();
			for (Part p : req.getParts()) {
				if (p.getName().startsWith("file")) {
					String fileName = p.getSubmittedFileName();
					if (fileName != null && !fileName.isEmpty()) {
						Path taget = targetDir.resolve(fileName);
						if (Files.isHidden(taget)) {
							resp.sendError(HttpServletResponse.SC_FORBIDDEN,
									"Cannot write hidden files");
							return;
						}
						if (!force && Files.exists(taget)) {
							resp.sendError(HttpServletResponse.SC_FORBIDDEN,
									"File " + taget + " already exists");
							return;
						}
						files.put(p, taget);
					}
				}
			}

			if (files.isEmpty()) {
				resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "No file specified");
				return;
			}

			for (Entry<Part, Path> entry : files.entrySet()) {
				try (InputStream in = entry.getKey().getInputStream()) {
					if (force) {
						Files.copy(in, entry.getValue(), StandardCopyOption.REPLACE_EXISTING);
					} else {
						Files.copy(in, entry.getValue());
					}
				} catch (Exception e) {
					errors.add(e.toString());
				}
			}
		} catch (Exception e) {
			logger.error("File upload error", e);
			resp.sendError("File upload error: " + e);
		}

		if (errors.isEmpty()) {
			resp.sendResult("ok");
		} else {
			resp.getHttpServletResponse().setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			resp.send("errors", errors);
		}
	}

}
