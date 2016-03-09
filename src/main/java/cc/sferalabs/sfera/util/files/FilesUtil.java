/**
 * 
 */
package cc.sferalabs.sfera.util.files;

import java.nio.file.Path;
import java.nio.file.Paths;

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

}
