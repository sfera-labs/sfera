package cc.sferalabs.sfera.core;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SystemClassLoader {

	private static final Logger logger = LogManager.getLogger();
	
	// TODO reload class loader when plugins dir modified

	private static final ClassLoader CLASS_LOADER;
	static {
		ClassLoader cl;
		try {
			Collection<Plugin> plugins = Plugins.getAll().values();
			URL[] urls = new URL[plugins.size()];
			int i = 0;
			for (Plugin plugin : plugins) {
				urls[i++] = plugin.getPath().toUri().toURL();
			}
			cl = new URLClassLoader(urls,
					SystemClassLoader.class.getClassLoader());
		} catch (Exception e) {
			logger.error("Error creating plugins class loader", e);
			cl = SystemClassLoader.class.getClassLoader();
		}
		CLASS_LOADER = cl;
	}

	/**
	 * 
	 * @param className
	 * @return
	 * @throws ClassNotFoundException
	 */
	public static Class<?> getClass(String className)
			throws ClassNotFoundException {
		return Class.forName(className, true, CLASS_LOADER);
	}

}
