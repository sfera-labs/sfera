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
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

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
public class MoveFilesServlet extends AuthorizedAdminApiServlet {

	public static final String PATH = ApiServlet.PATH + "files/mv";

	@Override
	protected void processAuthorizedRequest(HttpServletRequest req, HttpResponse resp)
			throws ServletException, IOException {
		try {
			String[] sources = getRequiredParameterValues("source", req, resp);
			String target = getRequiredParameter("target", req, resp);
			boolean force = "true".equalsIgnoreCase(req.getParameter("force"));

			Path targetPath = Paths.get(".", target);
			if (!FilesUtil.isInRoot(targetPath)) {
				resp.sendErrors(HttpServletResponse.SC_FORBIDDEN,
						new ErrorMessage(0, "Target outside root dir"));
				return;
			}
			boolean targetIsDir = Files.exists(targetPath) && Files.isDirectory(targetPath);

			if (sources.length > 1 && !targetIsDir) {
				resp.sendErrors(HttpServletResponse.SC_NOT_FOUND,
						new ErrorMessage(0, "Directory '" + target + "' not found"));
				return;
			}

			List<ErrorMessage> errs = new ArrayList<>();
			for (String source : sources) {
				Path sourcePath = Paths.get(".", source);
				if (!FilesUtil.isInRoot(sourcePath) || !Files.exists(sourcePath)) {
					errs.add(new ErrorMessage(0, "File '" + source + "' not found"));
				}
				if (targetIsDir) {
					targetPath = targetPath.resolve(sourcePath.getFileName());
				}
				if (!force && Files.exists(targetPath)) {
					errs.add(new ErrorMessage(0, "File '" + targetPath + "' already exists"));
				}
				FilesUtil.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
			}

			if (errs.isEmpty()) {
				resp.sendResult("ok");
			} else {
				resp.sendErrors(HttpServletResponse.SC_BAD_REQUEST, errs);
				return;
			}

		} catch (MissingRequiredParamException e) {
		}
	}

}
