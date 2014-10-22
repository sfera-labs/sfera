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
import java.util.EventListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.script.Compilable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import com.google.common.eventbus.Subscribe;
import com.homesystemsconsulting.core.FilesWatcher;
import com.homesystemsconsulting.core.Plugin;
import com.homesystemsconsulting.core.Sfera;
import com.homesystemsconsulting.core.Task;
import com.homesystemsconsulting.drivers.webserver.WebServer;
import com.homesystemsconsulting.events.Event;
import com.homesystemsconsulting.script.parser.SferaScriptGrammarLexer;
import com.homesystemsconsulting.script.parser.SferaScriptGrammarParser;
import com.homesystemsconsulting.script.parser.SferaScriptGrammarParser.ParseContext;
import com.homesystemsconsulting.util.logging.SystemLogger;


public class ScriptsEngine implements EventListener {

	public static final ScriptsEngine INSTANCE = new ScriptsEngine();
	private static final ScriptEngineManager SCRIPT_ENGINE_MANAGER = new ScriptEngineManager();
	static final SystemLogger LOG = SystemLogger.getLogger("scripts");
	
    private static HashMap<String, HashSet<Rule>> triggersActionsMap;
    private static HashMap<Path, List<String>> errors;
    
    /**
     * 
     * @return
     */
    private static ScriptEngine getNewEngine() {
    	return SCRIPT_ENGINE_MANAGER.getEngineByName("nashorn");
	}
    
    /**
     * 
     * @param event
     */
    @Subscribe
    public synchronized static void executeActionsTriggeredBy(Event event) {
    	try {
    		Set<Rule> toExecute = new HashSet<Rule>();
			String id = event.getId();
			
			List<String> idParts = getParts(id);
			
			for (String idPart : idParts) {
				HashSet<Rule> triggeredActions = triggersActionsMap.get(idPart);
				if (triggeredActions != null) {
					for (Rule rule : triggeredActions) {
						if (rule.eval(event)) {
							toExecute.add(rule);
						}
					}
				}
			}
			
			for (Rule rule : toExecute) {
				   rule.execute(event);
			}
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    /**
     * 
     * @param id
     * @return
     */
    private static List<String> getParts(String id) {
    	List<String> parts = new ArrayList<String>();
    	int dotIdx = id.length();
		do {
			String idPart = id.substring(0, dotIdx);
			parts.add(idPart);
			dotIdx = lastIndexOf(id, dotIdx - 1, '.', '(');
		} while (dotIdx > 0);
		
		return parts;
	}
    
    /**
     * 
     * @param string
     * @param fromIndex
     * @param chs
     * @return
     */
    private static int lastIndexOf(String string, int fromIndex, char ... chs) {
        int i = Math.min(fromIndex, string.length() - 1);
        for (; i >= 0; i--) {
        	for (char ch : chs) {
        		if (string.charAt(i) == ch) {
                    return i;
                }
        	}
        }
        return -1;
        
	}

	/**
     * 
     * @param key
     * @param value
     */
    public synchronized static void putInGlobalScope(String key, Object value) {
    	SCRIPT_ENGINE_MANAGER.put(key, value);
	}
    
    /**
     * 
     * @throws IOException
     */
    public synchronized static void loadScriptFiles() throws IOException {
    	try {
	    	triggersActionsMap = new HashMap<String, HashSet<Rule>>();
	    	errors = new HashMap<Path, List<String>>();
	    	
	    	loadScriptFilesIn(FileSystems.getDefault());
	    	for (Plugin plugin : Sfera.getPlugins()) {
				try (FileSystem pluginFs = FileSystems.newFileSystem(plugin.getPath(), null)) {
					loadScriptFilesIn(pluginFs);
				}
		    }
	    	
	    	if (!errors.isEmpty()) {
	    		LOG.error("Errors in script files");
		    	
		    	for (Entry<Path, List<String>> error : errors.entrySet()) {
	    			Path file = error.getKey();
	    			for (String message : error.getValue()) {
	    				LOG.error("File: " + file + " - " + message);
	    			}
	    		}
	    	}
	    	
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
	    		
				FilesWatcher.register(Paths.get("scripts"), reloadScriptFiles);
				FilesWatcher.register(Paths.get("plugins"), reloadScriptFiles);
				
			} catch (Exception e) {
				WebServer.getLogger().error("error registering script files watcher: " + e);
			}
    	}
	}
    
    /**
     * 
     * @param fileSystem
     * @throws IOException
     */
    private static void loadScriptFilesIn(final FileSystem fileSystem) throws IOException {
    	try {
    		Files.walkFileTree(fileSystem.getPath("scripts"), new SimpleFileVisitor<Path>() {
    			
    			@Override
    			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
    				try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
    					Compilable engine = (Compilable) getNewEngine();
    					for (Path file : stream) {
    						if (Files.isRegularFile(file) && file.getFileName().toString().endsWith(".ev")) {
    							LOG.debug("loading script file " + file);
    							try {
    								parseScriptFile(file, fileSystem, engine);
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
		List<String> messages = errors.get(file);
		if (messages == null) {
			messages = new ArrayList<String>();
			errors.put(file, messages);
		}
		messages.add(message);
	}

	/**
	 * 
	 * @param scriptFile
	 * @param fileSystem 
	 * @param engine
	 * @param localScope 
	 * @throws IOException
	 */
    private static void parseScriptFile(Path scriptFile, FileSystem fileSystem, Compilable engine) throws IOException {
    	try (BufferedReader r = Files.newBufferedReader(scriptFile, Sfera.CHARSET)) {
    		SferaScriptGrammarLexer lexer = new SferaScriptGrammarLexer(new ANTLRInputStream(r));
        	CommonTokenStream tokens = new CommonTokenStream(lexer);
        	SferaScriptGrammarParser parser = new SferaScriptGrammarParser(tokens);
            
            ScriptErrorListener grammarErrorListener = new ScriptErrorListener();
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
            
        	ScriptGrammarListener scriptListener = new ScriptGrammarListener(scriptFile, fileSystem, engine);
        	ParseTreeWalker.DEFAULT.walk(scriptListener, tree);
        	
        	if (scriptListener.errors.size() != 0) {
    	    	for (String e : scriptListener.errors) {
    	    		addError(scriptFile, e);
    	    	}
    	    	
    	    	return;
        	}
        	
        	for (Entry<String, HashSet<Rule>> entry : scriptListener.triggerActionsMap.entrySet()) {
        		String trigger = entry.getKey();
        		HashSet<Rule> actions = triggersActionsMap.get(trigger);
        		if (actions == null) {
        			actions = new HashSet<Rule>();
        			triggersActionsMap.put(trigger, actions);
        		}
        		actions.addAll(entry.getValue());
        	}
    	}
	}
}
