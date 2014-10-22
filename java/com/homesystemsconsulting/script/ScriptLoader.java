package com.homesystemsconsulting.script;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

public class ScriptLoader {
	
	static final String VAR_NAME = ScriptLoader.class.getSimpleName();
	
	private final ScriptEngine engine;
	private final FileSystem fileSystem;
	private final Bindings bindings;

	/**
	 * 
	 * @param engine
	 * @param fileSystem
	 * @param bindings
	 */
	public ScriptLoader(ScriptEngine engine, FileSystem fileSystem, Bindings bindings) {
		this.engine = engine;
		this.fileSystem = fileSystem;
		this.bindings = bindings;
	}

	/**
	 * 
	 * @param file
	 * @throws ScriptException
	 * @throws IOException
	 */
	public void load(String file) throws ScriptException, IOException {
		engine.eval(Files.newBufferedReader(fileSystem.getPath(file)), bindings);
	}
}
