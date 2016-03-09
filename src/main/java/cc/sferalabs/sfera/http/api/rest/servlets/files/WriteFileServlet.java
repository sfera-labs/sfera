package cc.sferalabs.sfera.http.api.rest.servlets.files;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.sferalabs.sfera.http.api.rest.RestResponse;
import cc.sferalabs.sfera.http.api.rest.servlets.ApiServlet;
import cc.sferalabs.sfera.util.files.FilesUtil;

/**
 * API admin servlet handling file editing.
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
@SuppressWarnings("serial")
public class WriteFileServlet extends AuthorizedAdminServlet {

	public static final String PATH = ApiServlet.PATH + "files/write";

	private final static Logger logger = LoggerFactory.getLogger(WriteFileServlet.class);

	@Override
	protected void processAuthorizedRequest(HttpServletRequest req, RestResponse resp)
			throws ServletException, IOException {
		String path = req.getParameter("path");
		String md5 = req.getParameter("md5");
		String content = req.getParameter("content");

		if (path == null) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Param 'path' not specified");
			return;
		}
		if (content == null) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Param 'content' not specified");
			return;
		}

		Path target = Paths.get(".", path);
		if (!FilesUtil.isInRoot(target)) {
			resp.sendError(HttpServletResponse.SC_FORBIDDEN, "File outside root dir");
			return;
		}
		if (Files.isHidden(target)) {
			resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Cannot write hidden files");
			return;
		}

		try {
			writeToFile(content, target, md5);
			resp.sendResult("ok");
		} catch (Exception e) {
			logger.error("File write error", e);
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "File write error: " + e);
		}
	}

	/**
	 * 
	 * @param content
	 * @param target
	 * @param md5
	 * @throws Exception
	 */
	private void writeToFile(String content, Path target, String md5) throws Exception {
		Path temp = null;
		try {
			byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
			if (md5 != null) {
				String bytesMd5 = getMd5(bytes);
				if (!bytesMd5.equals(md5)) {
					throw new Exception("md5 mismatch");
				}
			}
			temp = Files.createTempFile(getClass().getName(), null);
			Files.write(temp, bytes);
			Path parent = target.getParent();
			if (parent != null) {
				Files.createDirectories(parent);
			}
			Files.move(temp, target, StandardCopyOption.REPLACE_EXISTING);
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
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 */
	private String getMd5(byte[] bytes) throws IOException, NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] digest = md.digest(bytes);
		return DatatypeConverter.printHexBinary(digest);
	}

}
