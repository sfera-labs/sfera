package cc.sferalabs.sfera.script;

import java.nio.file.FileSystem;

import javax.script.Bindings;
import javax.script.ScriptEngine;

public class Scope {

	private final Bindings bindings;

	/**
	 * 
	 * @param fileSystem
	 * @param scriptEngine
	 * @param bindings
	 */
	public Scope(FileSystem fileSystem, ScriptEngine scriptEngine,
			Bindings bindings) {
		this.bindings = bindings;
		ScriptLoader scriptLoader = new ScriptLoader(scriptEngine, fileSystem,
				bindings);
		bindings.put(ScriptLoader.VAR_NAME, scriptLoader);
	}

	/**
	 * 
	 * @return
	 */
	public Bindings getBindings() {
		return bindings;
	}

	/**
	 * 
	 * @param name
	 * @param value
	 */
	public void put(String name, Object value) {
		bindings.put(name, value);
	}

}
