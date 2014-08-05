package com.homesystemsconsulting.util.files;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

public class ResourcesUtils {
	
	private static FileSystem jarFileSystem;
	
	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	public static FileSystem getJarFileSystem() throws IOException {
		if (jarFileSystem == null) {
			jarFileSystem = FileSystems.newFileSystem(Paths.get("sfera.jar"), null);
		}
		
		return jarFileSystem;
	}
	
	/**
	 * 
	 */
	public static void close() {
		if (jarFileSystem != null) {
			try {
				jarFileSystem.close();
			} catch (IOException e) {};
		}
	}
	
	/**
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static Path getResourceFromJar(Path path) throws IOException {
		return getJarFileSystem().getPath(path.toString());
	}
	
	/**
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static Path getResourceFromJarIfNotInFileSystem(Path path) throws IOException {
		if (Files.exists(path)) {
			return path;
		}
		
		return getResourceFromJar(path);
	}
	
	/**
	 * 
	 * @param dir
	 * @param includeFilesInJar
	 * @return
	 * @throws IOException
	 */
	public static Set<String> listDirectoriesNamesInDirectory(Path dir, boolean includeFilesInJar) throws IOException {
		Set<String> list = null;
		
		if (Files.exists(dir) && Files.isDirectory(dir)) {
			list = new HashSet<String>();
			try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
			    for (Path file : stream) {
			    	if (Files.isDirectory(file) && !Files.isHidden(file)) {
			    		String dirName = file.getFileName().toString();
			    		if (dirName.endsWith("/")) {
			    			dirName = dirName.substring(0, dirName.length() - 1);
			    		}
			    		
			    		list.add(dirName);
			    	}
			    }
			}
		}
		
		if (!includeFilesInJar) {
			return list;
		}
		
		FileSystem jfs = getJarFileSystem();
		Path jarDir = jfs.getPath(dir.toString());
		if (Files.exists(jarDir) && Files.isDirectory(jarDir)) {
			if (list == null) {
				list = new HashSet<String>();
			}
			try (DirectoryStream<Path> stream = Files.newDirectoryStream(jarDir)) {
			    for (Path file : stream) {
			    	if (Files.isDirectory(file) && !Files.isHidden(file)) {
			    		String dirName = file.getFileName().toString();
			    		if (dirName.endsWith("/")) {
			    			dirName = dirName.substring(0, dirName.length() - 1);
			    		}
			    		
			    		list.add(dirName);
			    	}
			    }
			}
		}
		
		return list;
	}
	
	/**
	 * 
	 * @param dir
	 * @param includeFilesInJar
	 * @return
	 * @throws IOException
	 */
	public static Set<String> listRegularFilesNamesInDirectory(Path dir, boolean includeFilesInJar) throws IOException {
		Set<String> list = null;
		
		if (Files.exists(dir) && Files.isDirectory(dir)) {
			list = new HashSet<String>();
			try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
			    for (Path file : stream) {
			    	if (Files.isRegularFile(file) && !Files.isHidden(file)) {
			    		list.add(file.getFileName().toString());
			    	}
			    }
			}
		}
		
		if (!includeFilesInJar) {
			return list;
		}
		
		Set<Path> jarFiles = listRegularFilesInJarDirectory(dir);
		if (jarFiles != null) {
			if (list == null) {
				list = new HashSet<String>();
			}
			for (Path file : jarFiles) {
				list.add(file.getFileName().toString());
			}
		}
		
		return list;
	}
	
	/**
	 * 
	 * @param dir
	 * @return
	 * @throws IOException
	 */
	public static Set<Path> listRegularFilesInJarDirectory(Path dir) throws IOException {
		Set<Path> list = null;
		
		FileSystem jfs = getJarFileSystem();
		Path jarDir = jfs.getPath(dir.toString());
		if (Files.exists(jarDir) && Files.isDirectory(jarDir)) {
			list = new HashSet<Path>();
			try (DirectoryStream<Path> stream = Files.newDirectoryStream(jarDir)) {
			    for (Path file : stream) {
			    	if (Files.isRegularFile(file) && !Files.isHidden(file)) {
			    		list.add(file);
			    	}
			    }
			}
		}
		
		return list;
	}
	
	/**
	 * 
	 * @param source
	 * @param target
	 * @param includeFilesInJar
	 * @throws IOException
	 */
	public static Set<Path> copyRecursive(Path source, Path target, boolean includeFilesInJar) throws IOException {
		Set<Path> list = copyRecursive(source, target);
		
		if (includeFilesInJar) {
			FileSystem jfs = getJarFileSystem();
			Path jarSource = jfs.getPath(source.toString());
			list.addAll(copyRecursive(jarSource, target));
		}
		
		return list;
	}
	
	/**
	 * 
	 * @param source
	 * @param target
	 * @throws IOException
	 */
	private static Set<Path> copyRecursive(Path source, Path target) throws IOException {
		Set<Path> list = new HashSet<Path>();
		if (Files.exists(source) && !Files.isHidden(source)) {
			try {
				Files.copy(source, target);
				list.add(target);
			} catch (FileAlreadyExistsException e) {}
			if (Files.isDirectory(source)) {
				try (DirectoryStream<Path> stream = Files.newDirectoryStream(source)) {
					for (Path file : stream) {
						list.addAll(copyRecursive(file, target.resolve(file.getFileName().toString())));
	        	    }
				}
			}
		}
		
		return list;
	}

	/**
	 * 
	 * @param dir
	 * @throws IOException 
	 */
	public static void deleteRecursive(Path dir) throws IOException {
        if (Files.exists(dir)) {
	        if (Files.isDirectory(dir)) {
	        	try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
	        	    for (Path file : stream) {
	        	    	deleteRecursive(file);
	        	    }
	        	}
	        }
	        Files.delete(dir);
        }
    }
}
