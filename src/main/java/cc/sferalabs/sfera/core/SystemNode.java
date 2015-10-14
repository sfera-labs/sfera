package cc.sferalabs.sfera.core;

import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;
import java.util.ServiceLoader;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.Subscribe;

import cc.sferalabs.sfera.access.Access;
import cc.sferalabs.sfera.apps.Applications;
import cc.sferalabs.sfera.core.events.SystemStateEvent;
import cc.sferalabs.sfera.core.services.AutoStartService;
import cc.sferalabs.sfera.core.services.Service;
import cc.sferalabs.sfera.core.services.Task;
import cc.sferalabs.sfera.core.services.TasksManager;
import cc.sferalabs.sfera.drivers.Drivers;
import cc.sferalabs.sfera.events.Bus;
import cc.sferalabs.sfera.events.Node;

/**
 * The system node
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class SystemNode implements Node, EventListener {

	private static final Logger logger = LoggerFactory.getLogger(SystemNode.class);
	private static final SystemNode INSTANCE = new SystemNode();
	private static final List<Service> services = new ArrayList<>();
	private Configuration config;

	/**
	 * Constructor for singleton instance
	 */
	private SystemNode() {
	}

	@Override
	public String getId() {
		return "system";
	}

	/**
	 * 
	 * @return the system node instance
	 */
	public static SystemNode getInstance() {
		return INSTANCE;
	}

	/**
	 * 
	 * @return the system configuration
	 */
	public static Configuration getConfiguration() {
		return INSTANCE.config;
	}

	/**
	 * Starts the process
	 */
	void start() {
		logger.info("======== Started ========");

		Bus.post(SystemStateEvent.START);
		Bus.register(this);
		init();
		Bus.post(SystemStateEvent.READY);
	}

	@Subscribe
	public void handleSystemStateEvent(SystemStateEvent event) {
		if (event == SystemStateEvent.QUIT) {
			quit();
		}
	}

	/**
	 * Initializes data structures, launches services, drivers and applications
	 */
	private synchronized void init() {
		try {
			config = new Configuration("sfera.yml");
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

		Plugins.load();

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
			logger.error("Error loading services " + AutoStartService.class.getSimpleName(), e);
		}

		Drivers.load();
		Applications.load();
	}

	/**
	 * Gracefully stops the process quitting applications, drivers and services
	 */
	private synchronized void quit() {
		TasksManager.executeSystem(new Task("System quit") {

			@Override
			protected void execute() {
				logger.warn("System stopped");

				logger.debug("Disabling apps...");
				Applications.disable();

				logger.debug("Quitting drivers...");
				Drivers.quit();

				logger.debug("Terminating tasks...");
				TasksManager.shutdownTasksNow();
				try {
					if (TasksManager.awaitTasksTermination(15, TimeUnit.SECONDS)) {
						logger.debug("Tasks terminated");
					} else {
						logger.debug(
								"Some tasks are taking too long to terminate. We gave them a chance...");
					}
				} catch (InterruptedException e) {
				}

				for (Service service : services) {
					try {
						String name = service.getClass().getSimpleName();
						logger.debug("Quitting service {}...", name);
						service.quit();
						logger.debug("Service {} quitted", name);
					} catch (Exception e) {
						logger.error("Error quitting service '" + service.getClass() + "'", e);
					}
				}

				// just in case something interrupted this thread
				// Thread.interrupted() clears the interrupted status
				Thread.interrupted();
				logger.debug("Waiting 5 seconds for services to quit...");
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
				}

				logger.info("Quitted");
				System.exit(0);
				logger.error("If you are reding this in the logs run as fast as you can!");
			}
		});
	}

	/**
	 * Adds the specified service to the system life-cycle
	 * 
	 * @param service
	 *            the service to be added
	 */
	public static void addToLifeCycle(Service service) {
		services.add(service);
	}

}
