package cc.sferalabs.sfera.core;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

import java.io.IOException;
import java.nio.file.ClosedWatchServiceException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.Watchable;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FilesWatcher extends Task implements AutoStartService {

	private static final Logger logger = LogManager.getLogger();

	private static final Map<Path, Set<Task>> PATHS_TASKS_MAP = new HashMap<Path, Set<Task>>();
	private static final Set<Path> INVALIDATED_PATHS = new HashSet<Path>();
	private static boolean run = true;

	private static final WatchService WATCHER;
	static {
		WatchService ws = null;
		try {
			ws = FileSystems.getDefault().newWatchService();
		} catch (IOException e) {
			logger.error("Error getting Watch Service", e);
		}
		WATCHER = ws;
	}

	/**
	 * 
	 */
	public FilesWatcher() {
		super("Files Watcher");
	}

	@Override
	public void init() throws Exception {
		TasksManager.getDefault().submit(this);
	}

	@Override
	public void quit() throws Exception {
		run = false;
		WATCHER.close();
	}

	@Override
	protected void execute() {
		if (WATCHER != null) {
			try {
				while (run) {
					try {
						watch();
					} catch (InterruptedException e) {
						if (!run) {
							Thread.currentThread().interrupt();
							break;
						}
					}
				}
			} catch (ClosedWatchServiceException e) {
				if (run) {
					logger.error("WatchService Error. File Watcher stopped", e);
				}
			}
		}
	}

	/**
	 * 
	 * @throws InterruptedException
	 */
	private static void watch() throws InterruptedException {
		WatchKey wkey = WATCHER.take();
		Set<WatchKey> keys = new HashSet<WatchKey>();
		do { // in case there are other events combined
			keys.add(wkey);
		} while ((wkey = WATCHER.poll(1, TimeUnit.SECONDS)) != null
				&& keys.size() < 10);

		Set<Task> toExecute = new HashSet<Task>();
		for (WatchKey key : keys) {
			key.pollEvents();
			Watchable path = key.watchable();
			if (path instanceof Path) {
				logger.info("File '{}' modified", path);
				synchronized (PATHS_TASKS_MAP) {
					Set<Task> ts = PATHS_TASKS_MAP.remove(path);
					if (ts != null) {
						toExecute.addAll(ts);
					} else {
						for (Iterator<Path> it = PATHS_TASKS_MAP.keySet()
								.iterator(); it.hasNext();) {
							Path parent = it.next();
							if (((Path) path).startsWith(parent)) {
								ts = PATHS_TASKS_MAP.get(parent);
								it.remove();
								toExecute.addAll(ts);
							}
						}
					}
				}

				key.reset();
			}
		}

		executeTasks(toExecute);

		synchronized (INVALIDATED_PATHS) {
			for (Iterator<Path> it = INVALIDATED_PATHS.iterator(); it.hasNext();) {
				try {
					Path path = it.next();
					registerRecursive(path);
					it.remove();
					executeTasks(PATHS_TASKS_MAP.remove(path));
				} catch (IOException e) {
				}
			}
		}
	}

	/**
	 * 
	 * @param path
	 */
	private static void executeTasks(Set<Task> tasks) {
		if (tasks != null) {
			for (Task t : tasks) {
				TasksManager.getDefault().execute(t);
			}
		}
	}

	/**
	 * 
	 * @param path
	 * @param task
	 * @throws IOException
	 */
	public static void register(Path path, Task task) throws IOException {
		if (WATCHER == null) {
			return;
		}

		synchronized (PATHS_TASKS_MAP) {
			Set<Task> ts = PATHS_TASKS_MAP.get(path);
			if (ts == null) {
				ts = new HashSet<Task>();
			}
			ts.add(task);
			PATHS_TASKS_MAP.put(path, ts);
		}
		try {
			registerRecursive(path);
		} catch (NoSuchFileException nsfe) {
			synchronized (INVALIDATED_PATHS) {
				INVALIDATED_PATHS.add(path);
			}
		}
		logger.debug("Watching '{}'", path);
	}

	/**
	 * 
	 * @param path
	 * @throws IOException
	 */
	private static void registerRecursive(Path path) throws IOException {
		Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult preVisitDirectory(Path dir,
					BasicFileAttributes attrs) throws IOException {
				dir.register(WATCHER, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
				return FileVisitResult.CONTINUE;
			}
		});
	}

}
