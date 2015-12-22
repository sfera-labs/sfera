package cc.sferalabs.sfera.util.files;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.sferalabs.sfera.core.services.LazyService;
import cc.sferalabs.sfera.core.services.Task;
import cc.sferalabs.sfera.core.services.TasksManager;

/**
 * Utility service for watching files and triggering actions upon files
 * modification
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public final class FilesWatcher extends LazyService {

	private static final Logger logger = LoggerFactory.getLogger(FilesWatcher.class);
	private static final Map<Path, Set<WatcherTask>> PATHS_TASKS_MAP = new HashMap<>();
	private static final Set<Path> NON_EXISTING_PATHS = new HashSet<Path>();
	private static final Object LOCK = new Object();
	private static boolean run = true;
	private static WatchService WATCHER;

	private static FilesWatcher INSTANCE = new FilesWatcher();
	private static Thread THREAD;

	static {
		try {
			WATCHER = FileSystems.getDefault().newWatchService();
			THREAD = TasksManager.executeSystem("Files Watcher", INSTANCE::watch);
		} catch (IOException e) {
			logger.error("Error instantiating FilesWatcher", e);
		}
	}

	@Override
	public void quit() throws Exception {
		run = false;
		if (THREAD != null) {
			THREAD.interrupt();
		}
	}

	/**
	 * 
	 */
	private void watch() {
		try {
			while (run) {
				try {
					pollWatcher();
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
		} finally {
			if (WATCHER != null) {
				try {
					WATCHER.close();
				} catch (IOException e) {
				}
			}
		}
	}

	/**
	 * 
	 * @throws InterruptedException
	 */
	private void pollWatcher() throws InterruptedException {
		WatchKey wkey = WATCHER.poll(20, TimeUnit.SECONDS);
		if (wkey != null) {
			Set<WatchKey> keys = new HashSet<WatchKey>();
			do { // in case there are other events combined
				keys.add(wkey);
			} while ((wkey = WATCHER.poll(200, TimeUnit.MILLISECONDS)) != null && keys.size() < 10);

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
				} catch (Exception e) {
					logger.error("Error registering path " + t.path, e);
				}
			}
			TasksManager.execute(t.task);
		}
	}

	/**
	 * Registers the specified {@code task} to be executed when the file located
	 * at the specified {@code path} is modified.
	 * <p>
	 * Equivalent to {@link #register(Path, Runnable, boolean)
	 * FilesWatcher.register}{@code (path, task, true);}
	 * </p>
	 * 
	 * @param path
	 *            the path of the file to be watched
	 * @param task
	 *            the task to be executed on file modification
	 * @return a {@code UUID} that can be used to {@link #unregister(Path, UUID)
	 *         unregister} the task in the future
	 * @throws IOException
	 *             If an I/O error occurs
	 */
	public static UUID register(Path path, Runnable task) throws IOException {
		return register(path, task, true);
	}

	/**
	 * Registers the specified {@code task} to be executed when the file located
	 * at the specified {@code path} is modified.
	 * 
	 * @param path
	 *            the path of the file to be watched
	 * @param task
	 *            the task to be executed on file modification
	 * @param removeWhenDone
	 *            if {@code true} the task will be executed once after the first
	 *            modification and then removed. Otherwise the task will be
	 *            executed at every modification until
	 *            {@link #unregister(Path, UUID) unregistered}
	 * @return a {@code UUID} that can be used to {@link #unregister(Path, UUID)
	 *         unregister} the task in the future
	 * @throws IOException
	 *             If an I/O error occurs
	 */
	public static UUID register(Path path, Runnable task, boolean removeWhenDone)
			throws IOException {
		return register(new WatcherTask(path, task, removeWhenDone));
	}

	/**
	 * Performs the actual registration task.
	 * 
	 * @param watcherTask
	 * @return
	 * @throws IOException
	 *             If an I/O error occurs
	 */
	private static UUID register(WatcherTask watcherTask) throws IOException {
		if (WATCHER == null) {
			throw new IllegalStateException("Not initialized");
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
				public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
						throws IOException {
					dir.register(WATCHER, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
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
	 * Unregisters the previously {@link #register(Path, Runnable, boolean)
	 * registered} task on the specified {@code path} and with the specified
	 * {@code UUID} returned by the {@link #register(Path, Runnable, boolean)}
	 * method.
	 * 
	 * @param path
	 *            the path the task was registered on
	 * @param id
	 *            the registration {@code UUID}
	 */
	public static void unregister(Path path, UUID id) {
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

		private final UUID id;
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
			this.id = UUID.randomUUID();
			this.path = path;
			this.task = Task.create("File '" + path + "' watcher", task);
			this.remove = remove;
		}

		/**
		 * 
		 * @return
		 */
		public UUID getId() {
			return id;
		}

	}

}
