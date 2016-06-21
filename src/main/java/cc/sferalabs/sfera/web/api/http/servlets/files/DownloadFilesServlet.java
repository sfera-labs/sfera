/*-
 * +======================================================================+
 * Sfera
 * ---
 * Copyright (C) 2015 - 2016 Sfera Labs S.r.l.
 * ---
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * -======================================================================-
 */

package cc.sferalabs.sfera.web.api.http.servlets.files;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class DownloadFilesServlet extends AuthorizedAdminApiServlet {

	public static final String PATH = ApiServlet.PATH + "files/download";

	private final static Logger logger = LoggerFactory.getLogger(DownloadFilesServlet.class);

	@Override
	protected void processAuthorizedRequest(HttpServletRequest req, HttpResponse resp)
			throws ServletException, IOException {
		Path tempZipFile = null;
		try {
			String[] paths = getRequiredParameterValues("path", req, resp);
			List<Path> sources = new ArrayList<>();
			for (String path : paths) {
				Path source = Paths.get(".", path);
				if (!FilesUtil.isInRoot(source) || !Files.exists(source)) {
					resp.sendErrors(HttpServletResponse.SC_NOT_FOUND,
							new ErrorMessage(0, "File '" + path + "' not found"));
					return;
				}
				sources.add(source);
			}
			Path source = sources.get(0);
			String fileName;
			boolean zip = false;
			if (sources.size() > 1) {
				fileName = "files";
				zip = true;
			} else {
				fileName = source.toAbsolutePath().normalize().getFileName().toString();
				if (Files.isDirectory(source)) {
					zip = true;
				}
			}
			if (zip) {
				tempZipFile = Files.createTempFile(getClass().getName(), ".zip");
				logger.debug("Compressing '{}'...", fileName);
				FilesUtil.zip(sources, tempZipFile);
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
