package cc.sferalabs.sfera.files;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

import java.io.IOException;
import java.nio.file.ClosedWatchServiceException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cc.sferalabs.sfera.core.AutoStartService;
import cc.sferalabs.sfera.core.Task;
import cc.sferalabs.sfera.core.TasksManager;

public class FilesWatcher extends Task implements AutoStartService {

	private static final Logger logger = LogManager.getLogger();

	private static final Map<Path, Set<WatcherTask>> PATHS_TASKS_MAP = new HashMap<>();
	private static final Set<Path> NON_EXISTING_PATHS = new HashSet<Path>();
	private static final Object LOCK = new Object();
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
		WatchKey wkey = WATCHER.poll(20, TimeUnit.SECONDS);
		if (wkey != null) {
			Set<WatchKey> keys = new HashSet<WatchKey>();
			do { // in case there are other events combined
				keys.add(wkey);
			} while ((wkey = WATCHER.poll(1, TimeUnit.SECONDS)) != null
					&& keys.size() < 50);

			Set<WatcherTask> toExecute = new HashSet<>();
			for (WatchKey key : keys) {
				Path path = (Path) key.watchable();
				logger.debug("Event on path: {}", path);
				synchronized (LOCK) {
					Set<WatcherTask> ts = PATHS_TASKS_MAP.remove(path);
					if (ts != null) {
						if (toExecute.addAll(ts)) {
							logger.info("Directory '{}' modified", path);
						}
					}
					for (WatchEvent<?> event : key.pollEvents()) {
						Path changed = (Path) event.context();
						changed = path.resolve(changed);
						if (!Files.isDirectory(changed)) {
							ts = PATHS_TASKS_MAP.remove(changed);
							if (ts != null) {
								if (toExecute.addAll(ts)) {
									logger.info("File '{}' modified", changed);
								}
							}
						}
					}
				}

				key.cancel();
			}

			executeTasks(toExecute);
		}

		synchronized (LOCK) {
			Iterator<Path> it = NON_EXISTING_PATHS.iterator();
			while (it.hasNext()) {
				Path path = it.next();
				if (Files.exists(path)) {
					it.remove();
					Set<WatcherTask> ts = PATHS_TASKS_MAP.remove(path);
					if (ts != null) {
						logger.info("File '{}' created", path);
						executeTasks(ts);
					}
				}
			}
		}
	}

	/**
	 * 
	 * @param tasks
	 */
	private static void executeTasks(Set<WatcherTask> tasks) {
		for (WatcherTask t : tasks) {
			logger.debug("Executing task: {}", t.task.getName());
			if (!t.remove) {
				try {
					register(t);
				} catch (IOException e) {
					logger.error("Error registering path " + t.path, e);
				}
			}
			TasksManager.getDefault().execute(t.task);
		}
	}

	/**
	 * 
	 * @param path
	 * @param task
	 * @throws IOException
	 */
	public static String register(Path path, Runnable task) throws IOException {
		return register(path, task, true);
	}

	/**
	 * 
	 * @param path
	 * @param task
	 * @param removeWhenDone
	 * @throws IOException
	 */
	public static String register(Path path, Runnable task,
			boolean removeWhenDone) throws IOException {
		return register(new WatcherTask(path, task, removeWhenDone));
	}

	/**
	 * 
	 * @param watcherTask
	 * @throws IOException
	 */
	private static String register(WatcherTask watcherTask) throws IOException {
		if (WATCHER == null) {
			return null;
		}

		Path path = watcherTask.path;

		Set<Path> registered = new HashSet<>();
		if (!Files.exists(path)) {
			synchronized (LOCK) {
				NON_EXISTING_PATHS.add(path);
			}
			registered.add(path);

		} else if (!Files.isDirectory(path)) {
			Path parent = path.getParent();
			parent.register(WATCHER, ENTRY_MODIFY, ENTRY_DELETE);
			registered.add(path);

		} else {
			Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult preVisitDirectory(Path dir,
						BasicFileAttributes attrs) throws IOException {
					dir.register(WATCHER, ENTRY_CREATE, ENTRY_DELETE,
							ENTRY_MODIFY);
					registered.add(dir);
					return FileVisitResult.CONTINUE;
				}
			});
		}

		synchronized (LOCK) {
			for (Path r : registered) {
				Set<WatcherTask> ts = PATHS_TASKS_MAP.get(r);
				if (ts == null) {
					ts = new HashSet<>();
				}
				ts.add(watcherTask);
				PATHS_TASKS_MAP.put(r, ts);
			}
		}

		logger.debug("Watching '{}'", path);
		return watcherTask.getId();
	}

	/**
	 * 
	 * @param path
	 * @param id
	 */
	public static void unregister(Path path, String id) {
		synchronized (LOCK) {
			Set<WatcherTask> ts = PATHS_TASKS_MAP.get(path);
			if (ts != null) {
				Iterator<WatcherTask> it = ts.iterator();
				while (it.hasNext()) {
					if (it.next().getId().equals(id)) {
						it.remove();
					}
				}
				if (ts.isEmpty()) {
					PATHS_TASKS_MAP.remove(path);
					NON_EXISTING_PATHS.remove(path);
				}
				logger.debug("Unregistered '{}'", path);
			}
		}
	}

	/**
	 *
	 */
	private static class WatcherTask {

		private final String id;
		private final Path path;
		private final Task task;
		private final boolean remove;

		/**
		 * 
		 * @param path
		 * @param task
		 * @param remove
		 */
		WatcherTask(Path path, Runnable task, boolean remove) {
			this.id = UUID.randomUUID().toString();
			this.path = path;
			this.task = Task.create("File '" + path + "' watcher", task);
			this.remove = remove;
		}

		/**
		 * 
		 * @return
		 */
		public String getId() {
			return id;
		}

	}

}
