package com.homesystemsconsulting.core;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.Watchable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.homesystemsconsulting.util.logging.SystemLogger;

public class FilesWatcher {

	private static final FilesWatcher INSTANCE = new FilesWatcher();
	private static final Map<Path, Set<Task>> PATHS_TASKS_MAP = new HashMap<Path, Set<Task>>();
	private static final Set<Path> INVALIDATED = new HashSet<Path>();
	
	private final WatchService watcher;
	
	/**
	 * 
	 */
	private FilesWatcher() {
		WatchService watcher = null;
		try {
			watcher = FileSystems.getDefault().newWatchService();
		} catch (IOException e) {
			SystemLogger.SYSTEM.error("Error instantiating File Watcher: " + e);
		}
		
		this.watcher = watcher;
	}
	
	/**
	 * 
	 * @param timeout
	 * @param unit
	 * @throws InterruptedException
	 */
	static void watch(long timeout, TimeUnit unit) throws InterruptedException {
		WatchKey key = INSTANCE.watcher.poll(timeout, unit);
		
		if (key != null) {
			key.pollEvents();
			Watchable path = key.watchable();
			if (path instanceof Path) {
				synchronized (PATHS_TASKS_MAP) {
					Set<Task> ts = PATHS_TASKS_MAP.remove(path);
					if (ts != null) {
						for (Task t : ts) {
							TasksManager.DEFAULT.execute(t);
						}
					}
				}
				
				key.reset();
			}
		}
		
		synchronized (INVALIDATED) {
			for (Iterator<Path> it = INVALIDATED.iterator(); it.hasNext(); ) {
				try {
					it.next().register(INSTANCE.watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
					it.remove();
				} catch (IOException e) {}
			}
		}
	}
	
	/**
	 * 
	 * @param path
	 * @param task
	 * @param events
	 * @throws IOException 
	 */
	public static void register(Path path, Task task) throws IOException {
		synchronized (PATHS_TASKS_MAP) {
			Set<Task> ts = PATHS_TASKS_MAP.get(path);
			if (ts == null) {
				ts = new HashSet<Task>();
			}
			ts.add(task);
			PATHS_TASKS_MAP.put(path, ts);
		}
		try {
			path.register(INSTANCE.watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
		} catch (NoSuchFileException nsfe) {
			synchronized (INVALIDATED) {
				INVALIDATED.add(path);
			}
		}
	}
	
	/**
	 * 
	 */
	public static void quit() {
		try {
			INSTANCE.watcher.close();
		} catch (Exception e) {}
	}
}
