package cc.sferalabs.sfera.core;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;
import java.util.ServiceLoader;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cc.sferalabs.sfera.access.Access;
import cc.sferalabs.sfera.apps.Applications;
import cc.sferalabs.sfera.core.events.SystemStateEvent;
import cc.sferalabs.sfera.core.services.AutoStartService;
import cc.sferalabs.sfera.core.services.Service;
import cc.sferalabs.sfera.core.services.TasksManager;
import cc.sferalabs.sfera.drivers.Drivers;
import cc.sferalabs.sfera.events.Bus;
import cc.sferalabs.sfera.events.Node;

import com.google.common.eventbus.Subscribe;

public class SystemNode implements Node, EventListener {

	private static final Logger logger = LogManager.getLogger();
	private static final SystemNode INSTANCE = new SystemNode();
	private static final List<Service> services = new ArrayList<>();
	private Configuration config;

	/**
	 * 
	 */
	private SystemNode() {
	}

	/**
	 * 
	 * @return
	 */
	public static SystemNode getInstance() {
		return INSTANCE;
	}

	/**
	 * 
	 * @return
	 */
	public static Configuration getConfiguration() {
		return INSTANCE.config;
	}

	/**
	 * 
	 */
	void start() {
		logger.info("======== Started ========");

		Bus.post(new SystemStateEvent("start"));
		Bus.register(this);
		init();
		Bus.post(new SystemStateEvent("ready"));
	}

	@Override
	public String getId() {
		return "system";
	}

	@Subscribe
	public void handleSystemStateEvent(SystemStateEvent event) {
		String state = event.getValue();
		if ("quit".equals(state)) {
			quit();
			logger.info("Quitted");
			System.exit(0);
		}
	}

	/**
	 * 
	 */
	private void init() {
		try {
			config = new Configuration("sfera.ini");
		} catch (NoSuchFileException e) {
			// no config file, that's fine...
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		try {
			Access.init();
		} catch (Exception e) {
			logger.error("Error initializing access config", e);
		}

		try {
			Plugins.load();
		} catch (IOException e) {
			logger.error("Error loading plugins", e);
		}

		try {
			ServiceLoader<AutoStartService> autoStartServices = ServiceLoader
					.load(AutoStartService.class);
			for (AutoStartService service : autoStartServices) {
				String name = service.getClass().getSimpleName();
				try {
					logger.debug("Initializing service {}...", name);
					service.init();
					addToLifeCycle(service);
					logger.debug("Service {} initiated", name);
				} catch (Exception e) {
					logger.error("Error initiating service '" + name + "'", e);
				}
			}
		} catch (Throwable e) {
			logger.error(
					"Error loading services "
							+ AutoStartService.class.getSimpleName(), e);
		}

		Drivers.load();
		Applications.load();
	}

	/**
	 * 
	 */
	private void quit() {
		logger.warn("System stopped");

		logger.debug("Quitting drivers...");
		Drivers.quit();

		logger.debug("Disabling apps...");
		Applications.disable();

		logger.debug("Waiting 5 seconds for drivers to quit...");
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
		}

		logger.debug("terminating tasks...");
		try {
			TasksManager.getDefault().getExecutorService().shutdownNow();
			TasksManager.getDefault().getExecutorService()
					.awaitTermination(5, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
		}
		logger.debug("Tasks terminated");

		for (Service service : services) {
			try {
				String name = service.getClass().getSimpleName();
				logger.debug("Quitting service {}...", name);
				service.quit();
				logger.debug("Service {} quitted", name);
			} catch (Exception e) {
				logger.error("Error quitting service '" + service.getClass()
						+ "'", e);
			}
		}
	}

	/**
	 * 
	 * @param service
	 */
	public static void addToLifeCycle(Service service) {
		services.add(service);
	}

}
