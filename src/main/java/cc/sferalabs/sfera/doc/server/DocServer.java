/**
 * 
 */
package cc.sferalabs.sfera.doc.server;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.sferalabs.sfera.core.services.AutoStartService;
import cc.sferalabs.sfera.http.HttpServer;
import cc.sferalabs.sfera.util.files.FilesWatcher;

/**
 * This service looks for documentation available in the 'docs' directory. If
 * found, it adds the path "/docs" to the HTTP server where it serves the
 * available documentation.
 * <p>
 * If JavaDoc jars are found inside the directory 'docs/plugins' they are
 * automatically extracted. The jar's name must be of the form
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

	static final Path ROOT = Paths.get("docs/");
	private boolean servletAdded = false;

	@Override
	public void init() {
		try {
			try {
				extractJavaDocJarsIn(ROOT, jarName -> {
					return jarName.startsWith("sfera-") && jarName.endsWith("-javadoc.jar");
				});
				if (!servletAdded) {
					HttpServer.addServlet(DocServletHolder.INSTANCE, "/docs/*");
					servletAdded = true;
				}
				extractJavaDocJarsIn(ROOT.resolve("plugins"), jarName -> {
					return jarName.endsWith("-javadoc.jar");
				});
			} catch (NoSuchFileException e) {
			}
		} catch (Exception e) {
			logger.error("Error extracting documentation", e);
		}

		try {
			FilesWatcher.register(ROOT, this::init);
		} catch (Exception e) {
			logger.error("Error registering FilesWatcher", e);
		}
	}

	/**
	 * 
	 * @param dir
	 * @param jarNamePredicate
	 * @throws IOException
	 */
	private static void extractJavaDocJarsIn(Path dir, Predicate<String> jarNamePredicate)
			throws IOException {
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
			for (Path file : stream) {
				if (Files.isRegularFile(file)
						&& jarNamePredicate.test(file.getFileName().toString())) {
					try {
						extractJar(file);
					} catch (Exception e) {
						logger.error("Error extracting docs in " + file, e);
					}
				}
			}
		}
	}

	/**
	 * @param jarFile
	 * @throws IOException
	 */
	private static void extractJar(Path jarFile) throws IOException {
		String jarFileName = jarFile.toString();
		Path destDir = Paths.get(jarFileName.substring(0, jarFileName.indexOf('-')) + "/");
		deleteRecursive(destDir);

		try (FileSystem jarFs = FileSystems.newFileSystem(jarFile, null)) {
			Files.walkFileTree(jarFs.getPath("/"), new SimpleFileVisitor<Path>() {

				private Path getLocalDest(Path dir) {
					return destDir.resolve("." + dir.toString());
				}

				@Override
				public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
						throws IOException {
					if (!dir.toString().startsWith("/META-INF")) {
						Files.createDirectories(getLocalDest(dir));
					}
					return super.preVisitDirectory(dir, attrs);
				}

				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
						throws IOException {
					super.visitFile(file, attrs);
					if (!file.toString().startsWith("/META-INF")) {
						Files.copy(file, getLocalDest(file));
					}
					return FileVisitResult.CONTINUE;
				}
			});
		}

		Files.delete(jarFile);
	}

	/**
	 * @param dir
	 * @throws IOException
	 */
	private static void deleteRecursive(Path dir) throws IOException {
		try {
			Files.walkFileTree(dir, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
						throws IOException {
					Files.delete(file);
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult postVisitDirectory(Path dir, IOException exc)
						throws IOException {
					Files.delete(dir);
					return FileVisitResult.CONTINUE;
				}
			});
		} catch (NoSuchFileException e) {
		}
	}

	@Override
	public void quit() throws Exception {
		if (servletAdded) {
			HttpServer.removeServlet(DocServletHolder.INSTANCE);
		}
	}

}
