#!/bin/sh
export CLASSPATH=".:/usr/local/lib/antlr-4.3-complete.jar"
alias antlr4='java -jar /usr/local/lib/antlr-4.3-complete.jar'
alias grun='java org.antlr.v4.runtime.misc.TestRig'
antlr4 -package com.homesystemsconsulting.script.parser SferaScriptGrammar.g4
javac -d . SferaScriptGrammar*.java
grun com.homesystemsconsulting.script.parser.SferaScriptGrammar parse -gui ../../../../../scripts/test.ev
