package cc.sferalabs.sfera.drivers.webserver;

import org.apache.http.client.utils.DateUtils;


public class HttpRequestHeader {
	
	private static final String[] HTTP_DATE_FORMATS = {DateUtils.PATTERN_ASCTIME, DateUtils.PATTERN_RFC1036, DateUtils.PATTERN_RFC1123, "EEE MMM d HH:mm:ss zzz yyyy"};
	
	public enum Method {GET, POST, HEAD};
	
	public final Method method;
	public final String uri;
	
	private boolean connectionClose;
	private String cookies;
	private long ifModifiedSinceTime;
	private boolean gzip;
	private String userAgent;
	private String host;
	private String contentType;
	private int contentLength;

	public HttpRequestHeader(String requestLine) throws NotImplementedRequestMethodException, MalformedRequestException {
		if (requestLine.startsWith("GET")) {
			method = Method.GET;
		} else if (requestLine.startsWith("HEAD")) {
			method = Method.HEAD;
		} else if (requestLine.startsWith("POST")) {
			method = Method.POST;
		} else {
			throw new NotImplementedRequestMethodException("not implemented request method: " + requestLine);
		}
		
		try {
			String [] sa = requestLine.split("\\s+");
			uri = sa[1];
			if (method == Method.POST) {
				connectionClose = false;
			} else {
				if (sa.length == 3 && sa[2].equalsIgnoreCase("HTTP/1.0")) {
					connectionClose = true;
				} else {
					connectionClose = false;
				}
			}
		} catch (Exception e) {
			throw new MalformedRequestException("malformed HTTP request line: " + requestLine, e);
		}
	}

	public void addField(String reqLine) {
		if (reqLine.startsWith("Cookie:")) {
			cookies = reqLine.substring(7).trim();
			
		} else if (reqLine.startsWith("If-Modified-Since:")) {
			try {
				ifModifiedSinceTime = DateUtils.parseDate(reqLine.substring(18).trim(), HTTP_DATE_FORMATS).getTime();
			} catch (Exception e) {}
			
		} else if (reqLine.startsWith("Accept-Encoding:")) {
			if (reqLine.contains("gzip")) {
				gzip = true;
			}
			
		} else if (reqLine.startsWith("User-Agent:")) {
			if (userAgent == null) {
				userAgent = reqLine.substring(11).trim();
			}
			
		} else if (reqLine.startsWith("Host:")) {
			host = reqLine.substring(5).trim();
			
		} else if (reqLine.startsWith("Content-Type:")) {
			contentType = reqLine.substring(13).trim();
			
		} else if (reqLine.startsWith("Connection:")) {
			if (reqLine.contains("close")) {
				connectionClose = true;
			}
			
		} else if (reqLine.startsWith("Content-Length:")) {
			try {
				contentLength = Integer.parseInt(reqLine.substring(15).trim());
			} catch (Exception e) {
				contentLength = 0;
			}
		}
	}
	
	public boolean getConnectionClose() {
		return connectionClose;
	}

	public String getURI() {
		return uri;
	}

	public String getCookies() {
		return cookies;
	}

	public String getHost() {
		return host;
	}
	
	public long getIfModifiedSinceTime() {
		return ifModifiedSinceTime;
	}
	
	public String getUserAgent() {
		return userAgent;
	}
}
