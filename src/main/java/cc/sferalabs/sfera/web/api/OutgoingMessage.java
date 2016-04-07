package cc.sferalabs.sfera.web.api;

import java.io.IOException;

/**
 * Abstract class for API messages to be sent to the remote API client.
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public abstract class OutgoingMessage extends JsonMessage {

	private boolean sent = false;

	/**
	 * Sends this message.
	 * 
	 * @throws IOException
	 *             if an I/O error occurs
	 * @throws IllegalStateException
	 *             if this message has already been sent
	 */
	public synchronized void send() throws IOException, IllegalStateException {
		if (sent) {
			throw new IllegalStateException("Already sent");
		}
		doSend(toJsonString());
		sent = true;
	}

	/**
	 * Sets the 'result' attribute of this message to the specified value and
	 * sends it.
	 * 
	 * @param result
	 *            the value to set the 'result' attribute to
	 * @throws IOException
	 *             if an I/O error occurs
	 * @throws IllegalStateException
	 *             if this message has already been sent
	 */
	public void sendResult(Object result) throws IOException, IllegalStateException {
		send("result", result);
	}

	/**
	 * Sets the 'error' attribute of this message to the specified message and
	 * sends it.
	 * 
	 * @param message
	 *            the error message
	 * @throws IOException
	 *             if an I/O error occurs
	 * @throws IllegalStateException
	 *             if this message has already been sent
	 */
	public void sendError(String message) throws IOException, IllegalStateException {
		send("error", message);
	}

	/**
	 * Sets the attribute of this message specified by {@code key} to the
	 * specified {@code value} and sends it.
	 * 
	 * @param key
	 *            attribute to set
	 * @param value
	 *            value for the attribute to set
	 * @throws IOException
	 *             if an I/O error occurs
	 * @throws IllegalStateException
	 *             if this message has already been sent
	 */
	public void send(String key, Object value) throws IOException, IllegalStateException {
		put(key, value);
		send();
	}

	/**
	 * Sends the text data to the remote end.
	 * 
	 * @param text
	 *            data to be sent
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	protected abstract void doSend(String text) throws IOException;

}
