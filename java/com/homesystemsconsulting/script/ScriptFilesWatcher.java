package com.homesystemsconsulting.script;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;

import com.homesystemsconsulting.core.Task;
import com.homesystemsconsulting.util.logging.SystemLogger;


public class ScriptFilesWatcher extends Task {

	WatchService watcher;
	
	public ScriptFilesWatcher(List<String> dirs) throws IOException {
		super("Application files watcher");
		watcher = FileSystems.getDefault().newWatchService();
		for (String dir : dirs) {
			Path path = Paths.get(dir);
			try {
				path.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
			} catch (IOException e) {
				SystemLogger.SYSTEM.error("Exception setting watcher on directory: " + path);
			}
		}		
	}
	
	@Override
	public void execute() {
		WatchKey key;
		try {
			key = watcher.take();
		} catch (InterruptedException ie) {
			return;
		}
		
		SystemLogger.SYSTEM.info("Application files modified: reloading...");
	    EventsScriptEngine.loadScriptFiles();
	    
	    key.reset();
	    try {
			watcher.close();
		} catch (IOException e) {}
	}
}
