package cc.sferalabs.sfera.script;

import java.io.Reader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.Subscribe;

import cc.sferalabs.sfera.core.events.PluginsEvent;
import cc.sferalabs.sfera.core.services.AutoStartService;
import cc.sferalabs.sfera.core.services.FilesWatcher;
import cc.sferalabs.sfera.events.Bus;
import cc.sferalabs.sfera.events.Event;
import cc.sferalabs.sfera.events.Node;
import cc.sferalabs.sfera.events.Nodes;
import cc.sferalabs.sfera.script.parser.Parser;
import cc.sferalabs.sfera.script.parser.Rule;
import cc.sferalabs.sfera.script.parser.ScriptErrorListener;
import cc.sferalabs.sfera.script.parser.ScriptsLoader;
import cc.sferalabs.sfera.script.parser.antlr.SferaScriptGrammarParser;
import cc.sferalabs.sfera.script.parser.antlr.SferaScriptGrammarParser.TerminalNodeContext;

/**
 * Service for the processing of scripts.
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class ScriptsEngine implements AutoStartService, EventListener {

	private static final ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
	private static final ScriptEngine runtimeEngine = getNewNashornEngine();

	private static final Logger logger = LoggerFactory.getLogger(ScriptsEngine.class);

	private Map<String, Set<Rule>> triggersRulesMap;
	private Map<Path, List<Object>> errors;

	@Override
	public void init() throws Exception {
		loadScripts();
		try {
			FilesWatcher.register(Paths.get(ScriptsLoader.SCRIPTS_DIR), this::loadScripts, false);
		} catch (Exception e) {
			logger.error("Error registering script files watcher", e);
		}
		Bus.register(this);
	}

	/**
	 * 
	 */
	private void loadScripts() {
		triggersRulesMap = new HashMap<>();
		errors = new HashMap<>();
		ScriptNodes.clear();
		ScriptsLoader loader = new ScriptsLoader(triggersRulesMap, errors);
		loader.load();
	}

	/**
	 * Reload scripts when plugins reloaded
	 * 
	 * @param event
	 *            the plugins event
	 */
	@Subscribe
	public void handlePluginsReload(PluginsEvent event) {
		if (event == PluginsEvent.RELOAD) {
			loadScripts();
		}
	}

	@Override
	public void quit() throws Exception {
		Bus.unregister(this);
	}

	/**
	 * 
	 * @param reader
	 * @return
	 * @throws ScriptException
	 */
	public static Bindings getBindings(Reader reader) throws ScriptException {
		Bindings b = runtimeEngine.createBindings();
		runtimeEngine.eval(reader, b);
		return b;
	}

	/**
	 * 
	 * @return a new Nashorn {@code ScriptEngine}
	 * @see ScriptEngineManager
	 */
	public static ScriptEngine getNewNashornEngine() {
		return scriptEngineManager.getEngineByName("nashorn");
	}

	/**
	 * Executes the script actions triggered by the specified event. This method
	 * should only be called by the events Bus.
	 * 
	 * @param event
	 *            the trigger event
	 */
	@Subscribe
	public synchronized void executeActionsTriggeredBy(Event event) {
		try {
			Set<Rule> toExecute = new HashSet<Rule>();
			String id = event.getId();

			List<String> idParts = getParts(id);

			for (String idPart : idParts) {
				Set<Rule> triggeredRules = triggersRulesMap.get(idPart);
				if (triggeredRules != null) {
					for (Rule rule : triggeredRules) {
						if (rule.evalCondition(event)) {
							toExecute.add(rule);
						}
					}
				}
			}

			for (Rule rule : toExecute) {
				rule.executeAction(event);
			}
		} catch (Exception e) {
			logger.error("Error executing actions triggered by event: " + event.getId(), e);
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
	private static int lastIndexOf(String string, int fromIndex, char... chs) {
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
	 * Sets the specified key/value pair in the global scope of the script
	 * engine.
	 * 
	 * @param key
	 *            key to set
	 * @param value
	 *            value to set
	 */
	public synchronized static void putObjectInGlobalScope(String key, Object value) {
		scriptEngineManager.put(key, value);
	}

	/**
	 * Removes the object associated with the specified key from the global
	 * scope of the script engine.
	 * 
	 * @param key
	 *            key to of the object to remove
	 *
	 */
	public synchronized static void removeFromGlobalScope(String key) {
		scriptEngineManager.put(key, null);
	}

	/**
	 * TODO remove?
	 * 
	 * Adds the specified Java type to the global scope of the script engine.
	 * After this call the type will be accessible from the script using its
	 * class simple name.
	 * 
	 * @param clazz
	 *            the Java type to add
	 * @throws ScriptException
	 *             if the method fails
	 * @see Class#getSimpleName
	 */
	public synchronized static void putTypeInGlobalScope(Class<?> clazz) throws ScriptException {
		Object type = getJavaType(clazz);
		putObjectInGlobalScope(clazz.getSimpleName(), type);
	}

	/**
	 * @param clazz
	 * @return
	 * @throws ScriptException
	 */
	static Object getJavaType(Class<?> clazz) throws ScriptException {
		ScriptEngine engine = getNewNashornEngine();
		String script = "var _javaType = Java.type('" + clazz.getName() + "');";
		Bindings bindings = engine.createBindings();
		engine.eval(script, bindings);
		return bindings.get("_javaType");
	}

	/**
	 * Executes an action on a node instance.
	 * 
	 * @param nodeAction
	 *            the action
	 * @param bindings
	 *            the binding to add to the local scope
	 * @return
	 * @throws ScriptException
	 *             if an error occurs executing the action script
	 * @throws IllegalArgumentException
	 *             if {@code nodeAction} have syntax errors
	 */
	public static Object evalNodeAction(String nodeAction, Map<String, Object> bindings)
			throws ScriptException, IllegalArgumentException {
		ScriptErrorListener errorListener = new ScriptErrorListener();
		SferaScriptGrammarParser parser = Parser.getParser(nodeAction, errorListener);
		TerminalNodeContext commandContext = parser.terminalNode();
		List<Object> errors = errorListener.getErrors();
		if (!errors.isEmpty()) {
			throw new IllegalArgumentException("Invalid node action syntax: " + errors.get(0));
		}
		String nodeId = commandContext.NodeId().getText();
		Node node = Nodes.get(nodeId);
		if (node == null) {
			throw new IllegalArgumentException("Node '" + nodeId + "' not found");
		}
		if (!nodeAction.endsWith(")")) {
			nodeAction += "()";
		}
		return eval(nodeAction, bindings);
	}

	/**
	 * 
	 * @param script
	 * @param bindings
	 * @return
	 * @throws ScriptException
	 */
	public static Object eval(String script, Map<String, Object> bindings) throws ScriptException {
		Bindings b = runtimeEngine.createBindings();
		if (bindings != null) {
			b.putAll(bindings);
		}
		logger.debug("Executing script '{}'", script);
		return runtimeEngine.eval(script, b);
	}

}
