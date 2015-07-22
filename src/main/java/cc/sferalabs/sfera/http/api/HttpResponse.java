package cc.sferalabs.sfera.http.api;

import java.io.IOException;

import org.json.simple.JSONObject;

public abstract class HttpResponse {

	private final JSONObject obj = new JSONObject();

	@SuppressWarnings("unchecked")
	public void put(String key, Object value) {
		obj.put(key, value);
	}

	public void send() throws IOException {
		doSend(obj.toJSONString());
	}

	/**
	 * 
	 * @param result
	 * @throws IOException 
	 */
	public void sendResult(Object result) throws IOException {
		send("result", result);
	}

	/**
	 * 
	 * @param message
	 * @throws IOException 
	 */
	public void sendError(String message) throws IOException {
		send("error", message);
	}

	/**
	 * 
	 * @param key
	 * @param value
	 * @throws IOException 
	 */
	private void send(String key, Object value) throws IOException {
		put(key, value);
		send();
	}

	protected abstract void doSend(String text) throws IOException;

}
