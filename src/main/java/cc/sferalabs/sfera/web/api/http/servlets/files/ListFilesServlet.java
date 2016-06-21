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
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class ListFilesServlet extends AuthorizedAdminApiServlet {

	public static final String PATH = ApiServlet.PATH + "files/ls";

	@Override
	protected void processAuthorizedRequest(HttpServletRequest req, HttpResponse resp)
			throws ServletException, IOException {
		try {
			int depth;
			try {
				depth = Integer.parseInt(req.getParameter("depth"));
			} catch (Exception e) {
				depth = -1;
			}
			boolean includeHidden = "true".equalsIgnoreCase(req.getParameter("hidden"));
			String path = getRequiredParameter("path", req, resp);
			Path dir = Paths.get(".", path).normalize().toAbsolutePath();
			if (!FilesUtil.isInRoot(dir) || !Files.exists(dir) || !Files.isDirectory(dir)) {
				resp.sendErrors(HttpServletResponse.SC_NOT_FOUND,
						new ErrorMessage(0, "Directory '" + path + "' not found"));
				return;
			}
			FileLister fl = new FileLister(depth, includeHidden);
			Files.walkFileTree(dir, fl);
			resp.sendResult(fl.getTop());

		} catch (MissingRequiredParamException e) {
		}
	}

	/**
	*
	*/
	private static class FileLister extends SimpleFileVisitor<Path> {

		private final int maxDepth;
		private final boolean includeHidden;

		private int depth = -1;

		Deque<Map<String, Object>> stack;

		/**
		 * 
		 * @param maxDepth
		 * @param includeHidden
		 */
		public FileLister(int maxDepth, boolean includeHidden) {
			this.maxDepth = maxDepth;
			this.includeHidden = includeHidden;
			this.stack = new ArrayDeque<>();
			Map<String, Object> top = new HashMap<>();
			top.put("sub", new ArrayList<>());
			this.stack.push(top);
		}

		/**
		 * @return
		 */
		@SuppressWarnings("unchecked")
		public Map<String, Object> getTop() {
			return (Map<String, Object>) ((List<?>) stack.pop().get("sub")).get(0);
		}

		@Override
		public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
				throws IOException {
			if (!includeHidden && Files.isHidden(dir)) {
				return FileVisitResult.SKIP_SUBTREE;
			}

			Map<String, Object> dirMap = add(dir);
			if (maxDepth >= 0 && depth >= maxDepth) {
				dirMap.put("sub", "?");
				return FileVisitResult.SKIP_SUBTREE;
			}
			depth++;
			stack.push(dirMap);
			dirMap.put("sub", new ArrayList<>());
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
			if (includeHidden || !Files.isHidden(file)) {
				add(file);
			}
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
			depth--;
			stack.pop();
			return FileVisitResult.CONTINUE;
		}

		/**
		 * @param file
		 * @return
		 */
		@SuppressWarnings({ "rawtypes", "unchecked" })
		private Map<String, Object> add(Path path) {
			String name = path.getFileName().toString();
			String lastModified;
			try {
				lastModified = Files.getLastModifiedTime(path).toString();
			} catch (Exception e) {
				lastModified = null;
			}
			Long size;
			try {
				size = Files.size(path);
			} catch (Exception e) {
				size = null;
			}
			Map<String, Object> file = new HashMap<>();
			file.put("name", name);
			file.put("lastModified", lastModified);
			file.put("size", size);

			List<Map> sub = (List<Map>) stack.peek().get("sub");
			sub.add(file);
			return file;
		}

	}

}
