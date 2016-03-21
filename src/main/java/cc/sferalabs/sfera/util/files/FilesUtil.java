/**
 * 
 */
package cc.sferalabs.sfera.util.files;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 *
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public abstract class FilesUtil {

	/**
	 * Returns whether or not the specified path in inside Sfera's root
	 * directory.
	 * 
	 * @param path
	 *            the path
	 * @return whether or not the specified path in inside Sfera's root
	 *         directory
	 */
	public static boolean isInRoot(Path path) {
		Path root = Paths.get(".").toAbsolutePath().normalize();
		path = path.toAbsolutePath().normalize();
		return path.startsWith(root);
	}

	/**
	 * Creates a zip file containing the specified source file or directory.
	 * 
	 * @param source
	 *            path of the file or directory to compress
	 * @param zipTarget
	 *            path of the zip file to create
	 * @param options
	 *            options specifying how the zip file is opened
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public static void zip(Path source, Path zipTarget, OpenOption... options) throws IOException {
		try (OutputStream fos = Files.newOutputStream(zipTarget, options);
				ZipOutputStream zos = new ZipOutputStream(fos)) {

			Files.walkFileTree(source, new SimpleFileVisitor<Path>() {

				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
						throws IOException {
					String name;
					if (file.isAbsolute()) {
						name = source.getParent().relativize(file).toString();
					} else {
						name = file.toString();
					}
					zos.putNextEntry(new ZipEntry(name));
					Files.copy(file, zos);
					zos.closeEntry();
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
						throws IOException {
					String name;
					if (dir.isAbsolute()) {
						name = source.getParent().relativize(dir).toString();
					} else {
						name = dir.toString();
					}
					if (!name.endsWith("/")) {
						name += "/";
					}
					zos.putNextEntry(new ZipEntry(name));
					zos.closeEntry();
					return FileVisitResult.CONTINUE;
				}
			});
		}
	}

}
