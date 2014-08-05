package com.homesystemsconsulting.script;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import javax.script.Compilable;
import javax.script.ScriptEngineManager;

import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import com.homesystemsconsulting.core.TasksManager;
import com.homesystemsconsulting.drivers.Driver;
import com.homesystemsconsulting.events.Bus;
import com.homesystemsconsulting.events.Event;
import com.homesystemsconsulting.events.SystemEvent;
import com.homesystemsconsulting.script.parser.EventsGrammarLexer;
import com.homesystemsconsulting.script.parser.EventsGrammarParser;
import com.homesystemsconsulting.script.parser.EventsGrammarParser.ParseContext;
import com.homesystemsconsulting.util.logging.SystemLogger;


public abstract class EventsScriptEngine {
	
	static final String APPS_DIRECTORY = "src/com/sfera/apps/";

	private static ScriptEngineManager manager = new ScriptEngineManager();
    
    private static HashMap<String, HashSet<ConditionAction>> triggerActionsMap;
    
    // App - File - Errors
    private static HashMap<String, HashMap<String, ArrayList<String>>> errors;
    
    /**
     * 
     * @param event
     */
    public synchronized static void executeActionsTriggeredBy(Event event) {
    	
    	try {
    		HashSet<ConditionAction> actionsToRun = new HashSet<ConditionAction>();
			String id = event.getId();
			
			int dotIdx = id.length();
			do {
				   String idPart = id.substring(0, dotIdx);
				   HashSet<ConditionAction> triggeredActions = triggerActionsMap.get(idPart);
				   if (triggeredActions != null) {
			 	   for (ConditionAction action : triggeredActions) {
			 		   if (action.checkCondition(event)) {
			 			   actionsToRun.add(action);
			 		   }
			 	   }
				   }
				   dotIdx = id.lastIndexOf('.', dotIdx - 1);
			} while (dotIdx > 0);
			
			for (ConditionAction action : actionsToRun) {
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
    	manager.put(driver.getId(), driver);
	}
    
    /**
     * 
     * @throws IOException
     */
    public synchronized static void loadScriptFiles() {
    	triggerActionsMap = new HashMap<String, HashSet<ConditionAction>>();
    	errors = new HashMap<String, HashMap<String, ArrayList<String>>>();
    	
    	ArrayList<String> toWatch = new ArrayList<String>();
    	
    	File appsDir = new File(APPS_DIRECTORY);
    	if (appsDir.exists() && appsDir.isDirectory()) {
    		toWatch.add(APPS_DIRECTORY);
    		File[] appDirs = appsDir.listFiles();
    		if (appDirs != null) {
	        	for (File appDir : appDirs) {
	        		if (appDir.isDirectory()) {
		        		toWatch.add(appDir.getAbsolutePath());
		        		String appName = appDir.getName();
		        		File[] appFiles = appDir.listFiles();
		        		if (appFiles != null) {
		        			// A script engine for each app
		        			Compilable engine = (Compilable) manager.getEngineByName("JavaScript");
		        			
			        		for (File appFile : appFiles) {
			        			if (appFile.isFile() && appFile.getName().endsWith(".ev")) {
			        				String scriptFile = appFile.getPath();
			        				try {
										parseScriptFile(appName, scriptFile, engine);
									} catch (IOException e) {
										addError(appName, scriptFile, "IOException: " + e.getLocalizedMessage());
									}
			        			}
			        		}
		        		}
	        		}
	        	}
    		}
    	}
    	
    	if (!errors.isEmpty()) {
    		SystemLogger.SYSTEM.error("apps", "Errors in script files");
	    	for (Entry<String, HashMap<String, ArrayList<String>>> app : errors.entrySet()) {
	    		String appName = app.getKey();
	    		for (Entry<String, ArrayList<String>> file : app.getValue().entrySet()) {
	    			String fileName = file.getKey();
	    			for (String error : file.getValue()) {
	    				SystemLogger.getLogger(appName).error("File: " + fileName + " - " + error);
	    			}
	    		}
	    	}
    	}
    	
    	//TODO remove ===============================
    	for (Entry<String, HashSet<ConditionAction>> triggerAction : triggerActionsMap.entrySet()) {
    		String trigger = triggerAction.getKey();
    		System.out.println(trigger);
    		for (ConditionAction ca : triggerAction.getValue()) {
	    		String condition = ca.getCondition().getText();
	    		String action = ca.getAction();
	    		System.out.println("\t" + condition);
	    		System.out.println("\t\t" + action);
	    		System.out.println();
    		}
    		System.out.println();
    	}
    	// ==========================================
    	
    	Bus.post(new SystemEvent("reload", null));
    	
		try {
			TasksManager.DEFAULT.execute(new FilesWatcher(toWatch));
		} catch (IOException e) {
			SystemLogger.SYSTEM.error("Exception creating script files watcher: " + e.getLocalizedMessage());
		}
	}
    
    /**
     * 
     * @param app
     * @param file
     * @param message
     */
    private static void addError(String app, String file, String message) {
		HashMap<String, ArrayList<String>> files = errors.get(app);
		if (files == null) {
			files = new HashMap<String, ArrayList<String>>();
			errors.put(app, files);
		}
		ArrayList<String> errs = files.get(file);
		if (errs == null) {
			errs = new ArrayList<String>();
			files.put(file, errs);
		}
		errs.add(message);
	}

	/**
	 * 
	 * @param appName
	 * @param scriptFile
	 * @param engine
	 * @throws IOException
	 */
    private static void parseScriptFile(String appName, String scriptFile, Compilable engine) throws IOException {
    	EventsGrammarLexer lexer = new EventsGrammarLexer(new ANTLRFileStream(scriptFile, "UTF-8"));
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
        		addError(appName, scriptFile, e);
        	}
        	
        	return;
    	} 
        
    	TriggerActionMapListener triggerActionMapListener = new TriggerActionMapListener(appName, scriptFile, engine);
    	ParseTreeWalker.DEFAULT.walk(triggerActionMapListener, tree);
    	
    	if (triggerActionMapListener.errors.size() != 0) {
	    	for (String e : triggerActionMapListener.errors) {
	    		addError(appName, scriptFile, e);
	    	}
	    	
	    	return;
    	}
    	
    	for (Entry<String, HashSet<ConditionAction>> entry : triggerActionMapListener.triggerActionsMap.entrySet()) {
    		String trigger = entry.getKey();
    		HashSet<ConditionAction> actions = triggerActionsMap.get(trigger);
    		if (actions == null) {
    			actions = new HashSet<ConditionAction>();
    			triggerActionsMap.put(trigger, actions);
    		}
    		actions.addAll(entry.getValue());
    	}
	}
}
