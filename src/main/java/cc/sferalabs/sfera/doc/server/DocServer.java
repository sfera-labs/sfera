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

/**
 * 
 */
package cc.sferalabs.sfera.doc.server;

import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.sferalabs.sfera.core.services.AutoStartService;
import cc.sferalabs.sfera.util.files.FilesUtil;
import cc.sferalabs.sfera.util.files.FilesWatcher;
import cc.sferalabs.sfera.web.WebServer;

/**
 * This service looks for documentation available in the 'docs' directory. If
 * found, it adds the path '/docs' to the HTTP server where it serves the
 * available documentation.
 * <p>
 * If plugins JavaDoc jars are found inside this directory they are
 * automatically extracted in 'docs/plugins'. The jar's name must be of the form
 * '&lt;plugin_name&gt;-&lt;version&gt;-javadoc.jar'; it will be extracted to a
 * directory named '&lt;plugin_name&gt;' overwriting its content if already
 * existing.
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class DocServer implements AutoStartService {

	private static final Logger logger = LoggerFactory.getLogger(DocServer.class);

	private boolean servletAdded = false;

	@Override
	public void init() {
		Path docsPath = Paths.get("docs/");
		try {
			try (DirectoryStream<Path> stream = Files.newDirectoryStream(docsPath)) {
				for (Path javaDocFile : stream) {
					if (Files.isRegularFile(javaDocFile)) {
						String fileName = javaDocFile.getFileName().toString();
						if (fileName.endsWith("-javadoc.jar")) {
							try {
								logger.info("Extracting doc from {}", javaDocFile);
								Path destDir = docsPath.resolve("plugins")
										.resolve(fileName.substring(0, fileName.indexOf('-')));
								try {
									FilesUtil.delete(destDir);
								} catch (NoSuchFileException e) {
								}
								FilesUtil.unzip(javaDocFile, destDir);
								Files.delete(javaDocFile);
								try {
									FilesUtil.delete(destDir.resolve("META-INF"));
								} catch (NoSuchFileException e) {
								}
							} catch (Exception e) {
								logger.error("Error extracting docs in " + javaDocFile, e);
							}
						}
					}
				}
			}

			if (!servletAdded) {
				WebServer.addServlet(DocServletHolder.INSTANCE, "/docs/*");
				servletAdded = true;
			}
		} catch (NoSuchFileException e) {
		} catch (Exception e) {
			logger.error("Error extracting documentation", e);
		}
		try {
			FilesWatcher.register(docsPath, "JavaDoc extractor", this::init, true, false);
		} catch (Exception e) {
			logger.error("Error registering FilesWatcher", e);
		}
	}

	@Override
	public void quit() throws Exception {
		if (servletAdded) {
			WebServer.removeServlet(DocServletHolder.INSTANCE);
			servletAdded = false;
		}
	}

}
