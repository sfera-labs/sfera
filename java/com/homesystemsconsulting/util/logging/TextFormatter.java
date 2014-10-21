package com.homesystemsconsulting.util.logging;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class TextFormatter extends Formatter {
	
	private final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.ENGLISH);
	
	@Override
	public String format(LogRecord record) {
		StringBuffer buf = new StringBuffer();
		buf.append(DATE_FORMAT.format(record.getMillis()));
		buf.append(" | ");
		if (record.getLevel().intValue() == SystemLogger.ERROR.intValue()) {
			buf.append("ERROR   ");
		} else if (record.getLevel().intValue() == SystemLogger.WARNING.intValue()) {
			buf.append("WARNING ");
		} else if (record.getLevel().intValue() == SystemLogger.INFO.intValue()) {
			buf.append("INFO    ");
		} else if (record.getLevel().intValue() == SystemLogger.EVENT.intValue()) {
			buf.append("EVENT   ");
		} else if (record.getLevel().intValue() == SystemLogger.DEBUG.intValue()) {
			buf.append("DEBUG   ");
		} else if (record.getLevel().intValue() == SystemLogger.VERBOSE.intValue()) {
			buf.append("VERBOSE ");
		} else {
			buf.append("[");
			buf.append(record.getLevel().getName());
			buf.append("]");
		}
	    buf.append("| ");
	    buf.append(record.getLoggerName());
	    buf.append("\t| ");
	    buf.append(record.getMessage());
	    buf.append("\r\n");

	    return buf.toString();
	}

}
