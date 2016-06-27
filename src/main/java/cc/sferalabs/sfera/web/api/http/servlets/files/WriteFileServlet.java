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
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

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
public class WriteFileServlet extends MultipartServlet {

	public static final String PATH = ApiServlet.PATH + "files/write";

	@Override
	protected void processMultipartRequest(HttpServletRequest req, HttpResponse resp)
			throws ServletException, IOException {
		String path = getMultipartParameter("path", req);
		String content = getMultipartParameter("content", req);
		String md5 = getMultipartParameter("md5", req);

		if (path == null) {
			resp.sendErrors(HttpServletResponse.SC_BAD_REQUEST,
					new ErrorMessage(0, "Param 'path' not specified"));
			return;
		}
		if (content == null) {
			resp.sendErrors(HttpServletResponse.SC_BAD_REQUEST,
					new ErrorMessage(0, "Param 'content' not specified"));
			return;
		}

		Path target = Paths.get(".", path);
		if (!FilesUtil.isInRoot(target)) {
			resp.sendErrors(HttpServletResponse.SC_FORBIDDEN,
					new ErrorMessage(0, "File outside root dir"));
			return;
		}
		try {
			if (Files.isHidden(target)) {
				resp.sendErrors(HttpServletResponse.SC_FORBIDDEN,
						new ErrorMessage(0, "Cannot write hidden files"));
				return;
			}
		} catch (Exception e) {
		}
		if (!writeToFile(content, target, md5)) {
			resp.sendErrors(HttpServletResponse.SC_BAD_REQUEST,
					new ErrorMessage(0, "md5 mismatch"));
			return;
		}
		resp.sendResult("ok");
	}

	/**
	 * 
	 * @param content
	 * @param target
	 * @param md5
	 * @return
	 * @throws IOException
	 */
	private boolean writeToFile(String content, Path target, String md5) throws IOException {
		Path temp = null;
		try {
			byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
			if (md5 != null) {
				String bytesMd5 = getMd5(bytes);
				if (!bytesMd5.equals(md5)) {
					return false;
				}
			}
			temp = Files.createTempFile(getClass().getName(), null);
			Files.write(temp, bytes);
			Path parent = target.getParent();
			if (parent != null) {
				Files.createDirectories(parent);
			}
			Files.move(temp, target, StandardCopyOption.REPLACE_EXISTING);
			return true;
		} finally {
			if (temp != null) {
				try {
					Files.delete(temp);
				} catch (Exception e) {
				}
			}
		}
	}

	/**
	 * 
	 * @param bytes
	 * @return
	 */
	private String getMd5(byte[] bytes) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		byte[] digest = md.digest(bytes);
		return DatatypeConverter.printHexBinary(digest);
	}

}
