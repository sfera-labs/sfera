/*-
 * +======================================================================+
 * Sfera
 * ---
 * Copyright (C) 2015 - 2016 Sfera Labs S.r.l.
 * ---
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * -======================================================================-
 */

/**
 * 
 */
package cc.sferalabs.sfera.scripts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Consumer;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.sferalabs.sfera.core.Plugin;
import cc.sferalabs.sfera.core.Plugins;
import cc.sferalabs.sfera.scripts.parser.Parser;
import cc.sferalabs.sfera.scripts.parser.ScriptErrorListener;
import cc.sferalabs.sfera.scripts.parser.ScriptGrammarListener;
import cc.sferalabs.sfera.scripts.parser.antlr.SferaScriptGrammarParser;
import cc.sferalabs.sfera.scripts.parser.antlr.SferaScriptGrammarParser.ParseContext;

/**
 *
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class ScriptsLoader implements EventListener {

	public static final String SCRIPTS_DIR = "scripts";
	private static final String SCRIPT_FILES_EXTENSION = ".ev";
	private static final String LIBRARY_FILES_EXTENSION = ".js";

	private static final Logger logger = LoggerFactory.getLogger(ScriptsLoader.class);

	private Map<String, Set<Rule>> triggersRulesMap;
	private Map<Path, List<Object>> errors;
	private Map<String, Bindings> libraries = new HashMap<>();

	/**
	 * Constructs a ScriptsLoader.
	 * 
	 * @param triggersRulesMap
	 *            the triggers-rules map to fill
	 * @param errors
	 *            the errors list to fill
	 */
	ScriptsLoader(Map<String, Set<Rule>> triggersRulesMap, Map<Path, List<Object>> errors) {
		this.triggersRulesMap = triggersRulesMap;
		this.errors = errors;
	}

	/**
	 * Loads the script files
	 */
	synchronized void load() {
		logger.info("Loading scripts...");
		try {
			loadSferaLib();
			loadFiles(LIBRARY_FILES_EXTENSION, libFile -> addToLibraries(libFile));
			loadFiles(SCRIPT_FILES_EXTENSION, scriptFile -> parseScriptFile(scriptFile));
		} catch (Exception e) {
			logger.error("Error loading script files", e);
		}
		logger.info("Scripts loaded");
	}

	/**
	 * 
	 * @throws ScriptException
	 * @throws IOException
	 */
	private void loadSferaLib() throws ScriptException, IOException {
		try (InputStream in = getClass().getResourceAsStream("sfera.js");
				BufferedReader br = new BufferedReader(
						new InputStreamReader(in, StandardCharsets.UTF_8))) {
			logger.debug("Loading Sfera library");
			addToLibraries("sfera.js", br);
		}
	}

	/**
	 * 
	 * @param filesExtension
	 * @param action
	 * @throws IOException
	 */
	private void loadFiles(String filesExtension, Consumer<Path> action) throws IOException {
		loadFilesIn(FileSystems.getDefault(), filesExtension, action);
		for (Plugin plugin : Plugins.getAll().values()) {
			try (FileSystem pluginFs = FileSystems.newFileSystem(plugin.getPath(), null)) {
				loadFilesIn(pluginFs, filesExtension, action);
			}
		}
	}

	/**
	 * 
	 * @param libFile
	 */
	private void addToLibraries(Path libFile) {
		try (BufferedReader br = Files.newBufferedReader(libFile, StandardCharsets.UTF_8)) {
			String path = null;
			int count = libFile.getNameCount();
			for (int i = 0; i < count; i++) {
				if (libFile.getName(i).toString().equals(SCRIPTS_DIR)) {
					path = libFile.subpath(i + 1, count).toString();
					break;
				}
			}
			if (path != null) {
				addToLibraries(path, br);
			}
		} catch (Exception e) {
			addError(libFile, e);
		}
	}

	/**
	 * 
	 * @param path
	 * @param reader
	 * @throws ScriptException
	 */
	private void addToLibraries(String path, Reader reader) throws ScriptException {
		Bindings bs = ScriptsEngine.getBindings(reader);
		Bindings prev = libraries.put(path, bs);
		if (prev != null) {
			logger.warn("Library '{}' is overwriting another file", path);
		}
	}

	/**
	 * 
	 * @param fileSystem
	 * @param filesExtension
	 * @param action
	 * @throws IOException
	 */
	private void loadFilesIn(final FileSystem fileSystem, String filesExtension,
			Consumer<Path> action) throws IOException {
		try {
			Files.walkFileTree(fileSystem.getPath(SCRIPTS_DIR), new SimpleFileVisitor<Path>() {

				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
						throws IOException {
					super.visitFile(file, attrs);
					if (Files.isRegularFile(file)
							&& file.getFileName().toString().endsWith(filesExtension)) {
						if (logger.isDebugEnabled()) {
							if (fileSystem == FileSystems.getDefault()) {
								logger.debug("Loading file '{}'", file);
							} else {
								logger.debug("Loading file '{}' from '{}'", file, fileSystem);
							}
						}
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
	 * @param scriptFile
	 */
	private void parseScriptFile(Path scriptFile) {
		try (BufferedReader br = Files.newBufferedReader(scriptFile, StandardCharsets.UTF_8)) {
			ScriptErrorListener errorListener = new ScriptErrorListener();
			SferaScriptGrammarParser parser = Parser.getParser(br, errorListener);
			ParseContext tree = parser.parse();

			List<Object> ers = errorListener.getErrors();
			if (!ers.isEmpty()) {
				for (Object e : ers) {
					addError(scriptFile, e);
				}
				return;
			}

			ScriptEngine scriptEngine = ScriptsEngine.getNewNashornEngine();

			StringBuilder loggerName = new StringBuilder();
			Iterator<Path> it = scriptFile.iterator();
			while (it.hasNext()) {
				loggerName.append(it.next());
				loggerName.append('.');
			}
			scriptEngine.put("log", LoggerFactory.getLogger(loggerName.substring(0,
					loggerName.length() - SCRIPT_FILES_EXTENSION.length() - 1)));

			ScriptGrammarListener scriptListener = new ScriptGrammarListener(scriptFile, libraries,
					scriptEngine);
			ParseTreeWalker.DEFAULT.walk(scriptListener, tree);

			ers = scriptListener.getErrors();
			if (!ers.isEmpty()) {
				for (Object e : ers) {
					addError(scriptFile, e);
				}
				return;
			}

			for (Entry<String, Set<Rule>> entry : scriptListener.getTriggerRulesMap().entrySet()) {
				String trigger = entry.getKey();
				Set<Rule> rules = triggersRulesMap.get(trigger);
				if (rules == null) {
					rules = new HashSet<Rule>();
					triggersRulesMap.put(trigger, rules);
				}
				rules.addAll(entry.getValue());
			}
		} catch (Exception e) {
			addError(scriptFile, e);
		}
	}

	/**
	 * 
	 * @param file
	 * @param message
	 */
	private void addError(Path file, Object error) {
		logger.error("Error in script file '{}': {}", file, error);
		if (error instanceof Throwable) {
			logger.debug("Script error", (Throwable) error);
		}
		List<Object> fileErrors = errors.get(file);
		if (fileErrors == null) {
			fileErrors = new ArrayList<>();
			errors.put(file, fileErrors);
		}
		fileErrors.add(error);
	}

}
