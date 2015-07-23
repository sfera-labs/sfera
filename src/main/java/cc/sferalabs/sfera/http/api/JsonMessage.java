package cc.sferalabs.sfera.http.api;

import java.io.IOException;

import org.json.simple.JSONObject;

public abstract class JsonMessage {

	private final JSONObject obj = new JSONObject();
	private boolean handled = false;

	@SuppressWarnings("unchecked")
	public void put(String key, Object value) {
		obj.put(key, value);
	}

	/**
	 * 
	 * @throws IOException
	 * @throws IllegalStateException
	 *             if this response has already been sent
	 */
	public synchronized void send() throws IOException, IllegalStateException {
		if (handled) {
			throw new IllegalStateException("Already handled");
		}
		doSend(obj.toJSONString());
		handled = true;
	}

	/**
	 * 
	 * @param result
	 * @throws IOException
	 * @throws IllegalStateException
	 *             if this response has already been sent
	 */
	public void sendResult(Object result) throws IOException, IllegalStateException {
		send("result", result);
	}

	/**
	 * 
	 * @param message
	 * @throws IOException
	 * @throws IllegalStateException
	 *             if this response has already been sent
	 */
	public void sendError(String message) throws IOException, IllegalStateException {
		send("error", message);
	}

	/**
	 * 
	 * @param key
	 * @param value
	 * @throws IOException
	 * @throws IllegalStateException
	 *             if this response has already been sent
	 */
	private void send(String key, Object value) throws IOException, IllegalStateException {
		put(key, value);
		send();
	}

	protected abstract void doSend(String text) throws IOException;

}
