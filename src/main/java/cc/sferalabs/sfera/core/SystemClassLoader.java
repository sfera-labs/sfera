package cc.sferalabs.sfera.core;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.EventListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cc.sferalabs.sfera.core.events.PluginsEvent;
import cc.sferalabs.sfera.events.Bus;

import com.google.common.eventbus.Subscribe;

public class SystemClassLoader {

	private static final Logger logger = LogManager.getLogger();
	private static ClassLoader CLASS_LOADER;

	/**
	 * 
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
			cl = new URLClassLoader(urls, SystemClassLoader.class.getClassLoader());
			logger.debug("Class loader created");
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
	public synchronized static Class<?> getClass(String className) throws ClassNotFoundException {
		if (CLASS_LOADER == null) {
			load();
			Bus.register(new PluginsListener());
		}
		return Class.forName(className, true, CLASS_LOADER);
	}

	/**
	 *
	 */
	private static class PluginsListener implements EventListener {

		@Subscribe
		private void reload(PluginsEvent event) {
			if (event == PluginsEvent.RELOAD) {
				load();
			}
		}
	}

}
