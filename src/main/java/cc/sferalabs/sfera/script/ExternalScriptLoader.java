package cc.sferalabs.sfera.script;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

public class ExternalScriptLoader {

	static final String VAR_NAME = ExternalScriptLoader.class.getSimpleName();

	private final ScriptEngine engine;
	private final FileSystem fileSystem;
	private final Bindings bindings;

	/**
	 * 
	 * @param engine
	 * @param fileSystem
	 * @param bindings
	 */
	public ExternalScriptLoader(ScriptEngine engine, FileSystem fileSystem, Bindings bindings) {
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
		try (BufferedReader br = Files.newBufferedReader(fileSystem.getPath(file))) {
			engine.eval(br, bindings);
		}
	}
}
