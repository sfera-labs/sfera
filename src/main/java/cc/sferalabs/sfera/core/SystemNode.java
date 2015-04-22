package cc.sferalabs.sfera.core;

import java.io.IOException;
import java.util.EventListener;
import java.util.ServiceLoader;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cc.sferalabs.sfera.apps.Applications;
import cc.sferalabs.sfera.core.events.SystemStateEvent;
import cc.sferalabs.sfera.drivers.Drivers;
import cc.sferalabs.sfera.events.Bus;
import cc.sferalabs.sfera.events.Node;

import com.google.common.eventbus.Subscribe;

public class SystemNode implements Node, EventListener {

	private static final ServiceLoader<SferaService> SERVICE_LOADER = ServiceLoader
			.load(SferaService.class);

	private static final Logger logger = LogManager.getLogger();

	public static final SystemNode INSTANCE = new SystemNode();

	public static final String BASE_PACKAGE = "cc.sferalabs.sfera";

	/**
	 * 
	 */
	private SystemNode() {
	}

	/**
	 * 
	 */
	void start() {
		logger.info("======== Started ========");

		Bus.post(new SystemStateEvent("start"));
		init();
		Bus.register(this);
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
			Configuration.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		try {
			Plugins.load();
		} catch (IOException e) {
			logger.error("Error loading plugins", e);
		}
		Drivers.load();
		Applications.load();

		for (SferaService service : SERVICE_LOADER) {
			String name = service.getName();
			try {
				logger.debug("Initializing service {}...", name);
				service.init();
				logger.debug("Service {} initiated", name);
			} catch (Exception e) {
				logger.error("Error initiating service '" + name + "'", e);
			}
		}

		Drivers.startAll();
	}

	/**
	 * 
	 */
	private void quit() {
		logger.warn("System stopped");

		logger.debug("Quitting drivers...");
		Drivers.quit();

		logger.debug("Waiting 5 seconds for drivers to quit...");
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
		}

		logger.debug("terminating tasks...");
		try {
			TasksManager.DEFAULT.getExecutorService().shutdownNow();
			TasksManager.DEFAULT.getExecutorService().awaitTermination(5,
					TimeUnit.SECONDS);
		} catch (InterruptedException e) {
		}
		logger.debug("Tasks terminated");

		for (SferaService service : SERVICE_LOADER) {
			try {
				String name = service.getName();
				logger.debug("Quitting service {}...", name);
				service.quit();
				logger.debug("Service {} quitted", name);
			} catch (Exception e) {
				logger.error("Error quitting service '" + service.getClass()
						+ "'", e);
			}
		}
	}

}
