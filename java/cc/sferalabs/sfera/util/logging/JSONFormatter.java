package cc.sferalabs.sfera.util.logging;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

import org.json.simple.JSONObject;

public class JSONFormatter extends Formatter {
	
	@Override
	public String format(LogRecord record) {
		JSONObject obj = new JSONObject();
		obj.put("ts", record.getMillis());
		obj.put("level", record.getLevel().getName());
		obj.put("id", record.getLoggerName());
		obj.put("message", record.getMessage());
		
	    return obj.toString() + "\r\n";
	}

}
