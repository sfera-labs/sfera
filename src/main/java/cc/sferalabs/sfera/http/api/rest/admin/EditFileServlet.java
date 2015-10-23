package cc.sferalabs.sfera.http.api.rest.admin;

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

/**
 * API admin servlet handling file editing.
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
@SuppressWarnings("serial")
public class EditFileServlet extends AuthorizedAdminServlet {

	public static final String PATH = AuthorizedAdminServlet.PATH + "file/edit";

	private final static Logger logger = LoggerFactory.getLogger(EditFileServlet.class);

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

		try {
			writeToFile(content, path, md5);
			resp.sendResult("ok");
		} catch (Exception e) {
			logger.error("File edit error", e);
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					"File edit error: " + e.getMessage());
		}
	}

	/**
	 * 
	 * @param content
	 * @param path
	 * @param md5
	 * @throws Exception
	 */
	private void writeToFile(String content, String path, String md5) throws Exception {
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
			Path target = Paths.get(path);
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
