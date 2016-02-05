package cc.sferalabs.sfera.core;

import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.sferalabs.sfera.access.Access;
import cc.sferalabs.sfera.apps.Applications;
import cc.sferalabs.sfera.console.Console;
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
public class SystemNode extends Node {

	private static final Logger logger = LoggerFactory.getLogger(SystemNode.class);
	private static final SystemNode INSTANCE = new SystemNode();
	private static final List<Service> services = new ArrayList<>();
	private Configuration config;

	/**
	 * Constructor for singleton instance
	 */
	private SystemNode() {
		super("system");
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
		init();
		Bus.post(SystemStateEvent.READY);
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

		ServiceLoader<AutoStartService> autoStartServices = ServiceLoader
				.load(AutoStartService.class);
		Iterator<AutoStartService> it = autoStartServices.iterator();
		while (it.hasNext()) {
			try {
				AutoStartService service = it.next();
				String name = service.getClass().getSimpleName();
				try {
					logger.debug("Initializing service {}...", name);
					service.init();
					addToLifeCycle(service);
					logger.debug("Service {} initiated", name);
				} catch (Exception e) {
					logger.error("Error initiating service '" + name + "'", e);
				}
			} catch (Throwable e) {
				logger.error("Error initializing service", e);
			}
		}

		Drivers.load();
		Applications.load();

		Console.setHandler("sys", SystemConsoleCommandHandler.INSTANCE);
	}

	/**
	 * Gracefully stops the process quitting applications, drivers and services
	 */
	synchronized void quit() {
		TasksManager.executeSystem(new Task("System quit") {

			@Override
			protected void execute() {
				logger.warn("System stopped");

				logger.info("Disabling apps...");
				Applications.disable();

				logger.info("Quitting drivers...");
				Drivers.quit();

				try {
					Drivers.waitTermination(10000);
				} catch (InterruptedException e) {
				}
				logger.info("Drivers quitted");

				logger.info("Terminating tasks...");
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

	/**
	 * Returns the InetAddress of one of the site local interfaces on this
	 * machine.
	 * 
	 * @return the InetAddress of one of the site local interfaces on this
	 *         machine, or {@code null} if not found.
	 * @throws SocketException
	 *             if an error occurs
	 */
	public static InetAddress getSiteLocalAddress() throws SocketException {
		Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
		while (nis.hasMoreElements()) {
			for (InterfaceAddress ni : nis.nextElement().getInterfaceAddresses())
				if (ni.getAddress().isSiteLocalAddress()) {
					return ni.getAddress();
				}
		}
		return null;
	}

}
