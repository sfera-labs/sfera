package cc.sferalabs.sfera.core;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.EventListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.Subscribe;

import cc.sferalabs.sfera.core.events.PluginsEvent;
import cc.sferalabs.sfera.events.Bus;

/**
 * Utility class to load classes from installed plugins
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public abstract class PluginsClassLoader {

	private static final Logger logger = LoggerFactory.getLogger(PluginsClassLoader.class);
	private static ClassLoader CLASS_LOADER;

	/**
	 * Adds the installed plugins to the base class loader
	 */
	private synchronized static void load() {
		ClassLoader cl;
		try {
			Collection<Plugin> plugins = Plugins.getAll().values();
			URL[] urls = new URL[plugins.size()];
			int i = 0;
			for (Plugin plugin : plugins) {
				urls[i++] = plugin.getPath().toUri().toURL();
			}
			cl = new URLClassLoader(urls, PluginsClassLoader.class.getClassLoader());
			logger.debug("Class loader created");
		} catch (Exception e) {
			logger.error("Error creating plugins class loader", e);
			cl = PluginsClassLoader.class.getClassLoader();
		}
		CLASS_LOADER = cl;
	}

	/**
	 * Returns the {@code Class} object associated with the class or interface
	 * with the given string name
	 * 
	 * @param className
	 *            fully qualified name of the desired class
	 * @return class object representing the desired class
	 * @throws ClassNotFoundException
	 *             if the class cannot be located
	 * 
	 * @see java.lang.Class#forName(String, boolean, ClassLoader)
	 * @see java.lang.ClassLoader
	 */
	public synchronized static Class<?> getClass(String className) throws ClassNotFoundException {
		if (CLASS_LOADER == null) {
			init();
		}
		return Class.forName(className, true, CLASS_LOADER);
	}

	/**
	 * Initializes the class loader and registers a {@code PluginsListener} for
	 * handling plugin reload events
	 */
	private static void init() {
		load();
		Bus.register(new PluginsListener());
	}

	/**
	 * Listener for reloading plugins when modified
	 * 
	 * @author Giampiero Baggiani
	 *
	 * @version 1.0.0
	 *
	 */
	private static class PluginsListener implements EventListener {

		@Subscribe
		public void reload(PluginsEvent event) {
			if (event == PluginsEvent.RELOAD) {
				load();
			}
		}
	}

}
