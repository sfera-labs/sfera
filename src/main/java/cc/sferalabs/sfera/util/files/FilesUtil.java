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
package cc.sferalabs.sfera.util.files;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.CopyOption;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.Collection;
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
	 * Returns Sfera's root directory.
	 * 
	 * @return Sfera's root directory
	 */
	public static Path getRoot() {
		return Paths.get(".").toAbsolutePath().normalize();
	}

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
		return path.toAbsolutePath().normalize().startsWith(getRoot());
	}

	/**
	 * Returns the specified path resolved against Sfera's root directory.
	 * 
	 * @param path
	 *            the path to resolve
	 * @return the resolved path
	 */
	public static Path resolveAgainstRoot(String path) {
		return getRoot().resolve("./" + path).normalize();
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
		zip(Arrays.asList(source), zipTarget, options);
	}

	/**
	 * Creates a zip file containing the specified source files or directories.
	 * 
	 * @param sources
	 *            paths of the files or directories to compress
	 * @param zipTarget
	 *            path of the zip file to create
	 * @param options
	 *            options specifying how the zip file is opened
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public static void zip(Collection<Path> sources, Path zipTarget, OpenOption... options) throws IOException {
		try (OutputStream fos = Files.newOutputStream(zipTarget, options);
				ZipOutputStream zos = new ZipOutputStream(fos)) {
			for (Path source : sources) {
				Path sourceParent = source.getParent();

				Files.walkFileTree(source, new SimpleFileVisitor<Path>() {

					@Override
					public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
						String name;
						if (sourceParent != null) {
							name = sourceParent.relativize(file).toString();
						} else {
							name = file.toString();
						}
						zos.putNextEntry(new ZipEntry(name));
						Files.copy(file, zos);
						zos.closeEntry();
						return FileVisitResult.CONTINUE;
					}

					@Override
					public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
						String name;
						if (sourceParent != null) {
							name = sourceParent.relativize(dir).toString();
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

	/**
	 * Unzips the specified source file into the specified directory.
	 * 
	 * @param source
	 *            path of the zip file to extract
	 * @param dir
	 *            path of the directory to extract to
	 * @param options
	 *            options specifying how the files contained in the zip file are
	 *            copied
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public static void unzip(Path source, Path dir, CopyOption... options) throws IOException {
		try (FileSystem zipFs = FileSystems.newFileSystem(source, (ClassLoader) null)) {
			Files.walkFileTree(zipFs.getPath("/"), new SimpleFileVisitor<Path>() {

				/**
				 * @param file
				 * @return
				 */
				private Path getDestination(Path file) {
					return dir.resolve("." + file.toString()).normalize();
				}

				@Override
				public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
					Files.createDirectories(getDestination(dir));
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					Files.copy(file, getDestination(file), options);
					return FileVisitResult.CONTINUE;
				}
			});
		}
	}

	/**
	 * Deletes the specified file or directory.
	 * 
	 * @param path
	 *            path of the file or directory to compress
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public static void delete(Path path) throws IOException {
		Files.walkFileTree(path, new SimpleFileVisitor<Path>() {

			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				Files.delete(file);
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
				Files.delete(file);
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
				Files.delete(dir);
				return FileVisitResult.CONTINUE;
			}
		});
	}

	/**
	 * Copies the specified file or directory to the specified target path.
	 * 
	 * @param source
	 *            path of the file or directory to copy
	 * @param target
	 *            path of the target file or directory
	 * @param options
	 *            options specifying how the copy should be done
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public static void copy(Path source, Path target, CopyOption... options) throws IOException {
		Files.walkFileTree(source, new SimpleFileVisitor<Path>() {

			@Override
			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
				Path newdir = target.resolve(source.relativize(dir));
				Files.copy(dir, newdir, options);
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				Files.copy(file, target.resolve(source.relativize(file)), options);
				return FileVisitResult.CONTINUE;
			}

		});
	}

	/**
	 * Moves the specified file or directory to the specified target path.
	 * 
	 * @param source
	 *            path of the file or directory to move
	 * @param target
	 *            path of the target file or directory
	 * @param options
	 *            options specifying how the move should be done
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public static void move(Path source, Path target, CopyOption... options) throws IOException {
		try {
			Files.move(source, target, options);
		} catch (IOException e) {
			copy(source, target, options);
			delete(source);
		}
	}

	/**
	 * Returns the temporary directory's path
	 * 
	 * @return the temporary directory's path
	 */
	public static Path getTempDirectory() {
		// TODO check system property and/or add config param
		return Paths.get("data/tmp/");
	}

}
