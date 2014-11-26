package cc.sferalabs.sfera.drivers.webserver.util;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import cc.sferalabs.sfera.core.Plugin;
import cc.sferalabs.sfera.core.Sfera;

public class ResourcesUtil {
	
	private static final Set<Closeable> OPEN_RESOURCES = new HashSet<Closeable>();
	
	private static final Comparator<? super Path> PLUGINS_NAME_COMPARATOR = new Comparator<Path>() {

		@Override
		public int compare(Path o1, Path o2) {
			return o1.getFileName().toString().compareTo(o2.getFileName().toString());
		}
	};
	
	private static Set<Path> pluginsOverwritingWebapp = new TreeSet<Path>(PLUGINS_NAME_COMPARATOR);
	
	/**
	 * 
	 * @throws IOException
	 */
	public static void lookForPluginsOverwritingWebapp() throws IOException {
		pluginsOverwritingWebapp = new TreeSet<Path>(PLUGINS_NAME_COMPARATOR);
		for (Plugin plugin : Sfera.getPlugins()) {
			try (FileSystem pluginFs = FileSystems.newFileSystem(plugin.getPath(), null)) {
				Path webappDir = pluginFs.getPath("webapp");
				if (Files.exists(webappDir) && Files.isDirectory(webappDir)) {
					pluginsOverwritingWebapp.add(plugin.getPath());
				}
			} catch (Exception e) {}
	    }
	}
	
	/**
	 * 
	 * @param path
	 * @return
	 * @throws NoSuchFileException 
	 * @throws IOException
	 */
	public static Path getResourceFromPluginsIfNotInLocalDirectory(Path path) throws NoSuchFileException, IOException {
		if (Files.exists(path)) {
			return path;
		}
		
		for (Path plugin : pluginsOverwritingWebapp) {
			try {
				return getResourceFromPlugin(plugin, path);
			} catch (NoSuchFileException nsfe) {}
		}
		
		throw new NoSuchFileException(path.toString());
	}
	
	/**
	 * 
	 * @param plugin
	 * @param path
	 * @return
	 * @throws NoSuchFileException
	 * @throws IOException
	 */
	private static Path getResourceFromPlugin(Path plugin, Path path) throws NoSuchFileException, IOException {
		FileSystem fs = FileSystems.newFileSystem(plugin, null);
		Path pPath = fs.getPath(path.toString());
		if (pPath != null && Files.exists(pPath)) {
			return pPath;
		}
		synchronized (OPEN_RESOURCES) {
			OPEN_RESOURCES.add(fs);
		}
		
		throw new NoSuchFileException(path.toString());
	}

	/**
	 * 
	 * @param dir
	 * @param includePlugins
	 * @return
	 * @throws NoSuchFileException
	 * @throws IOException
	 */
	public static Set<String> listDirectoriesNamesInDirectory(Path dir, boolean includePlugins) throws NoSuchFileException, IOException {
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
		
		if (includePlugins) {
			for (Path plugin : pluginsOverwritingWebapp) {
				try {
					Path pDir = getResourceFromPlugin(plugin, dir);
					if (Files.isDirectory(pDir)) {
						if (list == null) {
							list = new HashSet<String>();
						}
						try (DirectoryStream<Path> stream = Files.newDirectoryStream(pDir)) {
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
				} catch (NoSuchFileException nsfe) {} 
			}
		}
		
		if (list == null) {
			throw new NoSuchFileException(dir.toString());
		}
		
		return list;
	}
	
	/**
	 * 
	 * @param dir
	 * @param includePlugins
	 * @return
	 * @throws NoSuchFileException
	 * @throws IOException
	 */
	public static Set<String> listRegularFilesNamesInDirectory(Path dir, boolean includePlugins) throws NoSuchFileException, IOException {
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
		
		if (includePlugins) {
			for (Path plugin : pluginsOverwritingWebapp) {
				try {
					Path pDir = getResourceFromPlugin(plugin, dir);
					if (Files.isDirectory(pDir)) {
						if (list == null) {
							list = new HashSet<String>();
						}
						try (DirectoryStream<Path> stream = Files.newDirectoryStream(pDir)) {
						    for (Path file : stream) {
						    	if (Files.isRegularFile(file) && !Files.isHidden(file)) {
						    		list.add(file.getFileName().toString());
						    	}
						    }
						}
					}
				} catch (NoSuchFileException nsfe) {} 
			}
		}
		
		if (list == null) {
			throw new NoSuchFileException(dir.toString());
		}
		
		return list;
	}
	
	/**
	 * 
	 * @param source
	 * @param target
	 * @param includePlugins
	 * @return
	 * @throws IOException
	 */
	public static Set<Path> copyRecursive(Path source, Path target, boolean includePlugins) throws IOException {
		Set<Path> list = copyRecursive(source, target);
		
		if (includePlugins) {
			for (Path plugin : pluginsOverwritingWebapp) {
				try {
					Path pSource = getResourceFromPlugin(plugin, source);
					list.addAll(copyRecursive(pSource, target));
				} catch (NoSuchFileException nsfe) {} 
			}
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

	/**
	 * 
	 */
	public static void release() {
		synchronized (OPEN_RESOURCES) {
			for (Iterator<Closeable> it = OPEN_RESOURCES.iterator(); it.hasNext(); ) {
		        try {
					it.next().close();
				} catch (Exception e) {}
		        it.remove();
			}
		}
	}
}
