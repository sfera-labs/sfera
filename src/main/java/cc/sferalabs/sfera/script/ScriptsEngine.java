package cc.sferalabs.sfera.script;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Consumer;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleBindings;

import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.Subscribe;

import cc.sferalabs.sfera.core.Plugin;
import cc.sferalabs.sfera.core.Plugins;
import cc.sferalabs.sfera.core.events.PluginsEvent;
import cc.sferalabs.sfera.core.services.AutoStartService;
import cc.sferalabs.sfera.core.services.FilesWatcher;
import cc.sferalabs.sfera.events.Bus;
import cc.sferalabs.sfera.events.Event;
import cc.sferalabs.sfera.events.Node;
import cc.sferalabs.sfera.events.Nodes;
import cc.sferalabs.sfera.script.parser.SferaScriptGrammarLexer;
import cc.sferalabs.sfera.script.parser.SferaScriptGrammarParser;
import cc.sferalabs.sfera.script.parser.SferaScriptGrammarParser.ParseContext;
import cc.sferalabs.sfera.script.parser.SferaScriptGrammarParser.TerminalNodeContext;

/**
 * Service for the processing of scripts.
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class ScriptsEngine implements AutoStartService, EventListener {

	private static final String SCRIPTS_DIR = "scripts";
	private static final String SCRIPT_FILES_EXTENSION = ".ev";
	private static final String LIBRARY_FILES_EXTENSION = ".js";
	private static final ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
	private static final ScriptEngine nodeActionsEngine = getNewNashornEngine();
	private static final Logger logger = LoggerFactory.getLogger(ScriptsEngine.class);
	private static final String SFERA_PACKAGE_IMPORTER = "__cc_sferalabs_sfera_import__";

	private static HashMap<String, HashSet<Rule>> triggersRulesMap;
	private static HashMap<Path, List<String>> errors;
	private static Map<Path, Bindings> libraries;

	@Override
	public void init() throws Exception {
		initScope();
		loadScripts();
		try {
			FilesWatcher.register(Paths.get(SCRIPTS_DIR), ScriptsEngine::loadScripts, false);
		} catch (Exception e) {
			logger.error("Error registering script files watcher", e);
		}
		Bus.register(this);
	}

	/**
	 * @throws ScriptException
	 */
	private void initScope() throws ScriptException {
		// TODO
		
//		ScriptEngine engine = getNewNashornEngine();
//		String scr = "load('nashorn:mozilla_compat.js'); importPackage(Packages.cc.sferalabs.sfera.events); print('**** ' + Nodes);";
//		putObjectInGlobalScope("o", new Object());
//		engine.eval(scr, scriptEngineManager.getBindings());
//		engine.eval("importPackage(Packages.cc.sferalabs.sfera.events); print(o); print('**** ' + Nodes);");

//		String script = "var " + SFERA_PACKAGE_IMPORTER
//				+ " = new JavaImporter(Packages.cc.sferalabs.sfera.events); with (" + SFERA_PACKAGE_IMPORTER + ") { print('**** ' ); }";
//		Bindings bindings = engine.createBindings();
//		engine.eval(script, bindings);
//		Object importer = bindings.get(SFERA_PACKAGE_IMPORTER);
//		putObjectInGlobalScope(SFERA_PACKAGE_IMPORTER, importer);
//		ScriptEngine engine2 = getNewNashornEngine();
//		engine2.eval("with (" + SFERA_PACKAGE_IMPORTER + ") { print('**** ' + Nodes); }");

//		String script = "var " + SFERA_PACKAGE_IMPORTER
//				+ " = new JavaImporter(Packages.cc.sferalabs.sfera.events);";
//		engine.eval(script, engine.getBindings(ScriptContext.GLOBAL_SCOPE));
//		engine.eval("with (" + SFERA_PACKAGE_IMPORTER + ") { print('**** ' + Nodes); }");
	}

	/**
	 * Reload scripts when plugins reloaded
	 * 
	 * @param event
	 *            the plugins event
	 */
	@Subscribe
	public static void handlePluginsReload(PluginsEvent event) {
		if (event == PluginsEvent.RELOAD) {
			loadScripts();
		}
	}

	@Override
	public void quit() throws Exception {
	}

	/**
	 * 
	 * @return a new Nashorn {@code ScriptEngine}
	 * @see ScriptEngineManager
	 */
	static ScriptEngine getNewNashornEngine() {
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
	public synchronized static void executeActionsTriggeredBy(Event event) {
		try {
			Set<Rule> toExecute = new HashSet<Rule>();
			String id = event.getId();

			List<String> idParts = getParts(id);

			for (String idPart : idParts) {
				HashSet<Rule> triggeredRules = triggersRulesMap.get(idPart);
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
	 * <p>
	 * Executes an action on a node instance.
	 * </p>
	 * <p>
	 * If an action is of the form "node.action(value)" then this should be
	 * passed as the {@code action} parameter.
	 * </p>
	 * If an action is of the form "node.attribute = value" then
	 * "node.attribute" should be passed as the {@code action} parameter and
	 * "value" as the {@code param} parameter.
	 * 
	 * @param action
	 *            the action
	 * @param param
	 *            optional parameter
	 * @param user
	 *            the user who requested the action
	 * @return The value returned by the script
	 * @throws ScriptException
	 *             if an error occurs executing the action script
	 * @throws IllegalArgumentException
	 *             if {@code action} or {@code param} have syntax errors
	 */
	public static Object executeNodeAction(String action, String param, String user)
			throws ScriptException, IllegalArgumentException {
		ScriptErrorListener errorListener = new ScriptErrorListener();

		SferaScriptGrammarParser parser = getParser(new ANTLRInputStream(action), errorListener);
		TerminalNodeContext commandContext = parser.terminalNode();
		if (!errorListener.errors.isEmpty()) {
			throw new IllegalArgumentException(
					"Invalid node syntax: " + errorListener.errors.get(0));
		}

		String nodeId = commandContext.NodeId().getText();
		Node node = Nodes.get(nodeId);
		if (node == null) {
			throw new IllegalArgumentException("Node '" + nodeId + "' not found");
		}

		String actionScript = action;
		if (param != null) {
			// Parse the parameters to make sure it is not some executable code
			parser = getParser(new ANTLRInputStream(param), errorListener);
			parser.paramsList();
			if (!errorListener.errors.isEmpty()) {
				throw new IllegalArgumentException(
						"Invalid param syntax: " + errorListener.errors.get(0));
			}
			actionScript += "=" + param;

		} else if (!actionScript.endsWith(")")) {
			actionScript += "()";
		}

		logger.info("Executing action: {} User: {}", actionScript, user);
		Bindings bindings = new SimpleBindings();
		bindings.put(nodeId, node);
		return nodeActionsEngine.eval(actionScript, bindings);
	}

	/**
	 * 
	 * @param input
	 * @param errorListener
	 * @return
	 */
	private static SferaScriptGrammarParser getParser(CharStream input,
			ANTLRErrorListener errorListener) {
		SferaScriptGrammarLexer lexer = new SferaScriptGrammarLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		SferaScriptGrammarParser parser = new SferaScriptGrammarParser(tokens);

		lexer.removeErrorListeners();
		lexer.addErrorListener(errorListener);
		parser.removeErrorListeners();
		parser.addErrorListener(errorListener);

		return parser;
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
	 * TODO remove?
	 * 
	 * Adds the specified Java type to the global scope of the script engine.
	 * After this call the type will be accessible from the script using the
	 * specified key.
	 * 
	 * @param key
	 *            the key to associate the type to
	 * @param clazz
	 *            the Java type to add
	 * @throws ScriptException
	 *             if the method fails
	 */
	// private synchronized static void putTypeInGlobalScope(String key,
	// Class<?> clazz)
	// throws ScriptException {
	// ScriptEngine engine = getNewEngine();
	// String script = "var " + key + " = Java.type('" + clazz.getName() +
	// "');";
	// Bindings bindings = engine.createBindings();
	// engine.eval(script, bindings);
	// Object type = bindings.get(key);
	// putObjectInGlobalScope(key, type);
	// }

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
	// private synchronized static void putTypeInGlobalScope(Class<?> clazz)
	// throws ScriptException {
	// putTypeInGlobalScope(clazz.getSimpleName(), clazz);
	// }

	/**
	 * Loads the script files
	 */
	private synchronized static void loadScripts() {
		logger.info("Loading scripts...");
		try {
			triggersRulesMap = new HashMap<>();
			errors = new HashMap<>();
			libraries = new HashMap<>();

			loadFiles(LIBRARY_FILES_EXTENSION, (Path libFile) -> getBindings(libFile));
			loadFiles(SCRIPT_FILES_EXTENSION, (Path scriptFile) -> parseScriptFile(scriptFile));

		} catch (IOException e) {
			logger.error("Error loading script files", e);
		}
	}

	/**
	 * 
	 * @param filesExtension
	 * @param action
	 * @throws IOException
	 */
	private static void loadFiles(String filesExtension, Consumer<Path> action) throws IOException {
		loadFilesIn(FileSystems.getDefault(), filesExtension, action);
		for (Plugin plugin : Plugins.getAll().values()) {
			try (FileSystem pluginFs = FileSystems.newFileSystem(plugin.getPath(), null)) {
				loadFilesIn(pluginFs, filesExtension, action);
			}
		}
	}

	/**
	 * 
	 * @param fileSystem
	 * @param filesExtension
	 * @param action
	 * @throws IOException
	 */
	private static void loadFilesIn(final FileSystem fileSystem, String filesExtension,
			Consumer<Path> action) throws IOException {
		try {
			Files.walkFileTree(fileSystem.getPath(SCRIPTS_DIR), new SimpleFileVisitor<Path>() {

				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
						throws IOException {
					super.visitFile(file, attrs);
					if (Files.isRegularFile(file)
							&& file.getFileName().toString().endsWith(filesExtension)) {
						logger.info("Loading file '{}'", file);
						action.accept(file);
					}

					return FileVisitResult.CONTINUE;
				}
			});
		} catch (NoSuchFileException nsfe) {
		}
	}

	/**
	 * 
	 * @param libFile
	 */
	private static void getBindings(Path libFile) {
		try (BufferedReader br = Files.newBufferedReader(libFile, StandardCharsets.UTF_8)) {
			ScriptEngine engine = getNewNashornEngine();
			engine.eval(br);
			Bindings bs = engine.getBindings(ScriptContext.ENGINE_SCOPE);
			libraries.put(libFile, bs);
		} catch (Exception e) {
			addError(libFile, e.toString());
		}
	}

	/**
	 * 
	 * @param scriptFile
	 */
	private static void parseScriptFile(Path scriptFile) {
		try (BufferedReader br = Files.newBufferedReader(scriptFile, StandardCharsets.UTF_8)) {
			ScriptErrorListener grammarErrorListener = new ScriptErrorListener();
			SferaScriptGrammarParser parser = getParser(new ANTLRInputStream(br),
					grammarErrorListener);

			ParseContext tree = parser.parse();

			if (!grammarErrorListener.errors.isEmpty()) {
				for (String e : grammarErrorListener.errors) {
					addError(scriptFile, e);
				}
				return;
			}

			ScriptEngine scriptEngine = getNewNashornEngine();

			String loggerName = scriptFile.toString();
			loggerName = loggerName
					.substring(0, loggerName.length() - SCRIPT_FILES_EXTENSION.length())
					.replace('/', '.');
			scriptEngine.put("log", LoggerFactory.getLogger(loggerName));

			ScriptGrammarListener scriptListener = new ScriptGrammarListener(scriptFile, libraries,
					scriptEngine);
			ParseTreeWalker.DEFAULT.walk(scriptListener, tree);

			if (!scriptListener.errors.isEmpty()) {
				for (String e : scriptListener.errors) {
					addError(scriptFile, e);
				}
				return;
			}

			for (Entry<String, HashSet<Rule>> entry : scriptListener.triggerRulesMap.entrySet()) {
				String trigger = entry.getKey();
				HashSet<Rule> rules = triggersRulesMap.get(trigger);
				if (rules == null) {
					rules = new HashSet<Rule>();
					triggersRulesMap.put(trigger, rules);
				}
				rules.addAll(entry.getValue());
			}
		} catch (Exception e) {
			addError(scriptFile, e.toString());
		}
	}

	/**
	 * 
	 * @param file
	 * @param message
	 */
	private static void addError(Path file, String message) {
		logger.error("Error loading script file '{}': {}", file, message);
		List<String> messages = errors.get(file);
		if (messages == null) {
			messages = new ArrayList<String>();
			errors.put(file, messages);
		}
		messages.add(message);
	}

}
