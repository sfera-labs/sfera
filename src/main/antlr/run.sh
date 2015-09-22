#!/bin/sh
export CLASSPATH=".:/usr/local/lib/antlr-4.5.1-complete.jar:$CLASSPATH"
java -jar /usr/local/lib/antlr-4.5.1-complete.jar -package cc.sferalabs.sfera.script.parser SferaScriptGrammar.g4
javac -d . SferaScriptGrammar*.java
java org.antlr.v4.gui.TestRig cc.sferalabs.sfera.script.parser.SferaScriptGrammar parse -gui test.ev
