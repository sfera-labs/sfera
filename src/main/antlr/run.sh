#!/bin/sh
export CLASSPATH=".:/usr/local/lib/antlr-4.5-complete.jar"
java -jar /usr/local/lib/antlr-4.5-complete.jar -package cc.sferalabs.sfera.script.parser SferaScriptGrammar.g4
javac -d . SferaScriptGrammar*.java
java org.antlr.v4.runtime.misc.TestRig cc.sferalabs.sfera.script.parser.SferaScriptGrammar parse -gui test.ev
