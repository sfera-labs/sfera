package com.homesystemsconsulting.drivers.webserver.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.client.utils.DateUtils;

public abstract class DateUtil {
	
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(DateUtils.PATTERN_RFC1123);

	/**
	 * 
	 * @return
	 */
	public static String now() {
		return format(new Date());
	}

	/**
	 * 
	 * @param date
	 * @return
	 */
	public static String format(Object date) {
		return DATE_FORMAT.format(date);
	}
}
