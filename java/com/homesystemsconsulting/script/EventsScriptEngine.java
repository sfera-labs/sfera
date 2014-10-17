package com.homesystemsconsulting.script;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import javax.script.Compilable;
import javax.script.ScriptEngineManager;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import com.homesystemsconsulting.core.FilesWatcher;
import com.homesystemsconsulting.core.Plugin;
import com.homesystemsconsulting.core.Sfera;
import com.homesystemsconsulting.core.Task;
import com.homesystemsconsulting.drivers.Driver;
import com.homesystemsconsulting.drivers.webserver.WebServer;
import com.homesystemsconsulting.events.Bus;
import com.homesystemsconsulting.events.Event;
import com.homesystemsconsulting.events.SystemEvent;
import com.homesystemsconsulting.script.parser.EventsGrammarLexer;
import com.homesystemsconsulting.script.parser.EventsGrammarParser;
import com.homesystemsconsulting.script.parser.EventsGrammarParser.ParseContext;
import com.homesystemsconsulting.util.logging.SystemLogger;


public abstract class EventsScriptEngine {

	private static final ScriptEngineManager SCRIPT_ENGINE_MANAGER = new ScriptEngineManager();
	static final SystemLogger LOG = SystemLogger.getLogger("scripts");
	
    private static HashMap<String, HashSet<Rule>> triggerActionsMap;
    private static HashMap<Path, ArrayList<String>> errors;
    
    /**
     * 
     * @param event
     */
    public synchronized static void executeActionsTriggeredBy(Event event) {
    	try {
    		HashSet<Rule> actionsToRun = new HashSet<Rule>();
			String id = event.getId();
			
			int dotIdx = id.length();
			do {
				   String idPart = id.substring(0, dotIdx);
				   HashSet<Rule> triggeredActions = triggerActionsMap.get(idPart);
				   if (triggeredActions != null) {
				 	   for (Rule action : triggeredActions) {
				 		   if (action.eval(event)) {
				 			   actionsToRun.add(action);
				 		   }
				 	   }
				   }
				   dotIdx = id.lastIndexOf('.', dotIdx - 1);
			} while (dotIdx > 0);
			
			for (Rule action : actionsToRun) {
				   action.execute(event);
			}
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    /**
     * 
     * @param driver
     */
    public synchronized static void addDriver(Driver driver) {
    	SCRIPT_ENGINE_MANAGER.put(driver.getId(), driver);
	}
    
    /**
     * 
     * @throws IOException
     */
    public synchronized static void loadScriptFiles() throws IOException {
    	Path localScriptsDir = Paths.get("scripts");
    	try {
	    	triggerActionsMap = new HashMap<String, HashSet<Rule>>();
	    	errors = new HashMap<Path, ArrayList<String>>();
	    	
	    	loadScriptFiles(localScriptsDir);
	    	for (Plugin plugin : Sfera.getPlugins()) {
				try (FileSystem pluginFs = FileSystems.newFileSystem(plugin.getPath(), null)) {
					loadScriptFiles(pluginFs.getPath("scripts"));
				}
		    }
	    	
	    	if (!errors.isEmpty()) {
	    		SystemLogger.SYSTEM.error("scripts", "Errors in script files");
		    	
		    	for (Entry<Path, ArrayList<String>> error : errors.entrySet()) {
	    			Path file = error.getKey();
	    			for (String message : error.getValue()) {
	    				LOG.error("File: " + file + " - " + message);
	    			}
	    		}
	    	}
	    	
	    	Bus.post(new SystemEvent("reload", null));
	    	
    	} finally {
	    	try {
	    		Task reloadScriptFiles = new Task("Script files watcher") {
					
					@Override
					public void execute() {
						try {
							loadScriptFiles();
						} catch (IOException e) {
							SystemLogger.SYSTEM.error("error loading script files: " + e);
						}					
					}
				};
	    		
				FilesWatcher.register(localScriptsDir, reloadScriptFiles);
				FilesWatcher.register(Paths.get("plugins"), reloadScriptFiles);
				
			} catch (Exception e) {
				WebServer.getLogger().error("error registering script files watcher: " + e);
			}
    	}
	}
    
    /**
     * 
     * @param scriptsDirectory
     * @throws IOException
     */
    private static void loadScriptFiles(Path scriptsDirectory) throws IOException {
    	try {
    		Files.walkFileTree(scriptsDirectory, new SimpleFileVisitor<Path>() {
    			
    			@Override
    			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
    				try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
    					// A script engine for each folder
    					Compilable engine = (Compilable) SCRIPT_ENGINE_MANAGER.getEngineByName("nashorn");
    					for (Path file : stream) {
    						if (Files.isRegularFile(file) && file.getFileName().toString().endsWith(".ev")) {
    							LOG.debug("loading script file " + file);
    							try {
    								parseScriptFile(file, engine);
    							} catch (IOException e) {
    								addError(file, "IOException: " + e.getLocalizedMessage());
    							}
    						}
    					}
    				}
    				return FileVisitResult.CONTINUE;
    			}
    		});
    	} catch (NoSuchFileException nsfe) {}
	}

	/**
     * 
     * @param file
     * @param message
     */
    private static void addError(Path file, String message) {
		ArrayList<String> messages = errors.get(file);
		if (messages == null) {
			messages = new ArrayList<String>();
			errors.put(file, messages);
		}
		messages.add(message);
	}

	/**
	 * 
	 * @param appName
	 * @param scriptFile
	 * @param engine
	 * @throws IOException
	 */
    private static void parseScriptFile(Path scriptFile, Compilable engine) throws IOException {
    	try (BufferedReader r = Files.newBufferedReader(scriptFile, Sfera.CHARSET)) {
    		EventsGrammarLexer lexer = new EventsGrammarLexer(new ANTLRInputStream(r));
        	CommonTokenStream tokens = new CommonTokenStream(lexer);
            EventsGrammarParser parser = new EventsGrammarParser(tokens);
            
            EventsGrammarErrorListener grammarErrorListener = new EventsGrammarErrorListener();
            lexer.removeErrorListeners();
        	lexer.addErrorListener(grammarErrorListener);
            parser.removeErrorListeners();
            parser.addErrorListener(grammarErrorListener);
            
            ParseContext tree = parser.parse();
            
            if (grammarErrorListener.errors.size() != 0) {
            	for (String e : grammarErrorListener.errors) {
            		addError(scriptFile, e);
            	}
            	
            	return;
        	} 
            
        	TriggerActionMapListener triggerActionMapListener = new TriggerActionMapListener(scriptFile, engine);
        	ParseTreeWalker.DEFAULT.walk(triggerActionMapListener, tree);
        	
        	if (triggerActionMapListener.errors.size() != 0) {
    	    	for (String e : triggerActionMapListener.errors) {
    	    		addError(scriptFile, e);
    	    	}
    	    	
    	    	return;
        	}
        	
        	for (Entry<String, HashSet<Rule>> entry : triggerActionMapListener.triggerActionsMap.entrySet()) {
        		String trigger = entry.getKey();
        		HashSet<Rule> actions = triggerActionsMap.get(trigger);
        		if (actions == null) {
        			actions = new HashSet<Rule>();
        			triggerActionsMap.put(trigger, actions);
        		}
        		actions.addAll(entry.getValue());
        	}
    	}
	}
}
