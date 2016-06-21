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
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import cc.sferalabs.sfera.util.files.FilesUtil;
import cc.sferalabs.sfera.web.api.ErrorMessage;
import cc.sferalabs.sfera.web.api.http.HttpResponse;
import cc.sferalabs.sfera.web.api.http.servlets.ApiServlet;

/**
 *
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
@SuppressWarnings("serial")
public class UploadFilesServlet extends MultipartServlet {

	public static final String PATH = ApiServlet.PATH + "files/upload";

	@Override
	protected void processMultipartRequest(HttpServletRequest req, HttpResponse resp)
			throws ServletException, IOException {
		String path = getMultipartParameter("path", req);
		if (path == null) {
			resp.sendErrors(HttpServletResponse.SC_BAD_REQUEST,
					new ErrorMessage(0, "Param 'path' not specified"));
			return;
		}

		Path targetDir = Paths.get(".", path);
		if (!FilesUtil.isInRoot(targetDir) || !Files.exists(targetDir)
				|| !Files.isDirectory(targetDir)) {
			resp.sendErrors(HttpServletResponse.SC_NOT_FOUND,
					new ErrorMessage(0, "Directory '" + path + "' not found"));
			return;
		}

		boolean force = "true".equalsIgnoreCase(getMultipartParameter("force", req));

		List<ErrorMessage> errs = new ArrayList<>();
		for (Part p : req.getParts()) {
			if (p.getName().startsWith("file")) {
				String fileName = p.getSubmittedFileName();
				if (fileName != null && !fileName.isEmpty()) {
					Path target = targetDir.resolve(fileName);
					if (!force && Files.exists(target)) {
						errs.add(new ErrorMessage(0, "File '" + target + "' already exists"));
					}
					try (InputStream in = p.getInputStream()) {
						Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
					}
				}
			}
		}

		if (errs.isEmpty()) {
			resp.sendResult("ok");
		} else {
			resp.sendErrors(HttpServletResponse.SC_BAD_REQUEST, errs);
			return;
		}
	}

}
