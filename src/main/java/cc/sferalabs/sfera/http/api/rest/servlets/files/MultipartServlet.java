package cc.sferalabs.sfera.http.api.rest.servlets.files;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.stream.Collectors;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import org.eclipse.jetty.server.Request;

import cc.sferalabs.sfera.http.api.rest.RestResponse;
import cc.sferalabs.sfera.http.api.rest.servlets.AuthorizedAdminApiServlet;

/**
 *
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
@SuppressWarnings("serial")
public abstract class MultipartServlet extends AuthorizedAdminApiServlet {

	private static final MultipartConfigElement MULTIPART_CONFIG;

	static {
		String tmp;
		try {
			tmp = Files.createTempDirectory(MultipartServlet.class.getName()).toString();
		} catch (Exception e) {
			tmp = System.getProperty("java.io.tmpdir");
			if (tmp == null) {
				tmp = "/tmp";
			}
		}
		MULTIPART_CONFIG = new MultipartConfigElement(tmp, -1L, -1L, 1024 * 1024);
	}

	@Override
	protected void processAuthorizedRequest(HttpServletRequest req, RestResponse resp)
			throws ServletException, IOException {
		try {
			req.setAttribute(Request.__MULTIPART_CONFIG_ELEMENT, MULTIPART_CONFIG);
			processMultipartRequest(req, resp);
		} finally {
			try {
				for (Part p : req.getParts()) {
					try {
						p.delete();
					} catch (Exception e) {
					}
				}
			} catch (Exception e) {
			}
		}
	}

	/**
	 * 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	protected abstract void processMultipartRequest(HttpServletRequest req, RestResponse resp)
			throws ServletException, IOException;

	/**
	 * 
	 * @param param
	 * @param req
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	protected String getMultipartParameter(String param, HttpServletRequest req)
			throws IOException, ServletException {
		Part part = req.getPart(param);
		if (part == null) {
			return null;
		}
		String value = new BufferedReader(
				new InputStreamReader(part.getInputStream(), StandardCharsets.UTF_8)).lines()
						.collect(Collectors.joining("\n"));
		return value;
	}

}
