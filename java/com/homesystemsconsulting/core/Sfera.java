package com.homesystemsconsulting.core;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.homesystemsconsulting.apps.Application;
import com.homesystemsconsulting.apps.myapp.MyApp;
import com.homesystemsconsulting.apps.myapp2.MyApp2;
import com.homesystemsconsulting.data.Database;
import com.homesystemsconsulting.drivers.Driver;
import com.homesystemsconsulting.events.Bus;
import com.homesystemsconsulting.events.EventsMonitor;
import com.homesystemsconsulting.script.EventsScriptEngine;
import com.homesystemsconsulting.util.files.ResourcesUtils;
import com.homesystemsconsulting.util.logging.SystemLogger;


public class Sfera {
	
	public static final String VERSION = "0.0.1";

	private static boolean run = true;

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) {
		try {
			System.setProperty("java.awt.headless", "true");
			
			Thread.setDefaultUncaughtExceptionHandler(new SystemExceptionHandler());
			
//			ImageIO.setUseCache(false);
			
			Configuration.load();
			
			SystemLogger.setup();
			SystemLogger.SYSTEM.info("STARTED - version " + VERSION);
			
			SystemNode.init();
			Database.init();
			
			/*
			// connects to HTTPS servers without validating the certificates
			TrustManager[] trustAllCerts = new TrustManager[]{
				new X509TrustManager() {
					public java.security.cert.X509Certificate[] getAcceptedIssuers() {
						return null;
					}
	
					public void checkClientTrusted(
						java.security.cert.X509Certificate[] certs, String authType) {
					}
					
					public void checkServerTrusted(
						java.security.cert.X509Certificate[] certs, String authType) {
					}
				}
			};
			try { // this is for javax.net.ssl
			    SSLContext sc = SSLContext.getInstance("TLS");
			    sc.init(null, trustAllCerts, new java.security.SecureRandom());
			    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			    HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier(){
			    	public boolean verify(String string,SSLSession ssls) {
			    		return true;
			    	}
			    });
			} catch (Exception e) {
				
			}
			*/
			
			List<Driver> drivers = loadDrivers();
			for (Driver d : drivers) {
				EventsScriptEngine.addDriver(d);
			}
			
			//TODO initialize all applications...
			final Application myApp = new MyApp();
			final Application myApp2 = new MyApp2();
			
			Bus.register(EventsMonitor.INSTANCE);
			EventsScriptEngine.loadScriptFiles();
			
			//register all listeners...
			if (myApp instanceof EventListener) {
	//			Bus.register((EventListener) myApp);
			}
			if (myApp2 instanceof EventListener) {
	//			Bus.register((EventListener) myApp2);
			}
			
			while (run) {
				for (final Driver d : drivers) {
					d.startIfNotActive();
				}
				
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					if (!run) {
						break;
					}
				}
			}
	
			SystemLogger.SYSTEM.warning("System stopped");
			SystemLogger.SYSTEM.info("Quitting modules...");
			
			for (final Driver d : drivers) {
				d.quit();
			}
			
			SystemLogger.SYSTEM.debug("Waiting 5 seconds for modules to quit...");
			
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {}
			
			SystemLogger.SYSTEM.debug("Shutting down remaining threads...");
			
			try {
				TasksManager.DEFAULT.getExecutorService().shutdownNow();
				TasksManager.DEFAULT.getExecutorService().awaitTermination(5, TimeUnit.SECONDS);
			} catch (InterruptedException e) {}
			
			ResourcesUtils.close();
			Database.close();
			
			SystemLogger.SYSTEM.info("Bye!");
			System.exit(0);
			
		} catch (Throwable t) {
			SystemLogger.SYSTEM.error("exception in main thread: " + t);
		} finally {
			SystemLogger.close();
		}
	}

	/**
	 * 
	 */
	private static List<Driver> loadDrivers() {
		// TODO read config and manifest files
		String[][] declaredDrivers = {
				{"http", "com.homesystemsconsulting.drivers.webserver.HttpServer"}, 
				{"https", "com.homesystemsconsulting.drivers.webserver.HttpsServer"},
//				{"mydr", "com.homesystemsconsulting.drivers.mydriver.MyDriver"},
		};
		List<Driver> drivers = new ArrayList<Driver>();
		
		for (String[] driver : declaredDrivers) {
			String id = driver[0];
			String type = driver[1];
			
			try {
				Class<?> driverClass = Class.forName(type);
				Constructor<?> constructor = driverClass.getConstructor(new Class[]{String.class});
				Object driverInstance = constructor.newInstance(id);
				if (driverInstance instanceof Driver) {
					drivers.add((Driver) driverInstance);
					SystemLogger.SYSTEM.info("drivers", "driver '" + id + "' of type '" + type + "' instantiated");
				}
			} catch (Throwable e) {
				SystemLogger.SYSTEM.error("drivers", "error instantiating driver '" + id + "' of type '" + type + "': " + e);
			}
		}
		
		return drivers;
	}
}
