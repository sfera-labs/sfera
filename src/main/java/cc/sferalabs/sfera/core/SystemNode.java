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
	private static final String VERSION = SystemNode.class.getPackage().getImplementationVersion();
	private static final List<Service> services = new ArrayList<>();
	private Configuration config;

	/**
	 * Constructor for singleton instance
	 */
	private SystemNode() {
		super("system");
	}

	/**
	 * @return Sfera's version
	 */
	public static String getVersion() {
		return VERSION;
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
		logger.info("Starting...");
		logSystemInfo();
		Bus.post(SystemStateEvent.START);
		init();
		Bus.post(SystemStateEvent.READY);
	}

	/**
	 * 
	 */
	private void logSystemInfo() {
		String osName = System.getProperty("os.name");
		String osArch = System.getProperty("os.arch");
		String osVersion = System.getProperty("os.version");
		String javaVersion = System.getProperty("java.version");
		String javaVmName = System.getProperty("java.vm.name");
		long maxMem = Runtime.getRuntime().maxMemory();

		logger.info("Sfera version: {}", VERSION);
		logger.info("OS: {} {} {}", osName, osArch, osVersion);
		logger.info("Java: {} {}", javaVersion, javaVmName);
		if (maxMem != Long.MAX_VALUE) {
			maxMem = maxMem / 1024 / 1024;
			logger.info("JVM Max memory: {} MB", maxMem);
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

		ServiceLoader<AutoStartService> autoStartServices = ServiceLoader.load(AutoStartService.class);
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

		Applications.load();
		Drivers.load();

		Console.addHandler(SystemConsoleCommandHandler.INSTANCE);
	}

	/**
	 * Gracefully stops the process quitting applications, drivers and services
	 */
	synchronized void quit() {
		TasksManager.executeSystem(new Task("System quit") {

			@Override
			protected void execute() {
				try {
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
							logger.debug("Some tasks are taking too long to terminate. We gave them a chance...");
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

					logger.warn("Quitted");

				} finally {
					System.exit(0);
					logger.error("If you are reding this in the logs run as fast as you can!");
				}
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
	 * Returns a site local InetAddress of the specified network interface.
	 * 
	 * @param interfaceName
	 *            the name of the network interface to search on
	 * @return a site local InetAddress of the specified network interface, or
	 *         {@code null} if not found
	 * @throws SocketException
	 *             if an error occurs
	 */
	public static InetAddress getSiteLocalAddress(String interfaceName) throws SocketException {
		NetworkInterface ni = NetworkInterface.getByName(interfaceName);
		if (ni == null) {
			return null;
		}
		for (InterfaceAddress ia : ni.getInterfaceAddresses()) {
			InetAddress addr = ia.getAddress();
			if (addr.isSiteLocalAddress()) {
				return addr;
			}
		}
		return null;
	}

	/**
	 * Returns a site local InetAddress of a network interface on this machine.
	 * 
	 * @return a site local InetAddress of a network interface on this machine, or
	 *         {@code null} if not found
	 * @throws SocketException
	 *             if an error occurs
	 */
	public static InetAddress getSiteLocalAddress() throws SocketException {
		Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
		while (nis.hasMoreElements()) {
			for (InterfaceAddress ia : nis.nextElement().getInterfaceAddresses()) {
				InetAddress addr = ia.getAddress();
				if (addr.isSiteLocalAddress()) {
					return addr;
				}
			}
		}
		return null;
	}

}
