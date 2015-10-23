package cc.sferalabs.sfera.http.api.rest;

import java.io.IOException;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.sferalabs.sfera.http.api.JsonMessage;

/**
 * Class representing a response for an API REST request.
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class RestResponse extends JsonMessage {

	private static final long ASYNC_RESP_TIMEOUT = 30000;
	private static final Logger logger = LoggerFactory.getLogger(RestResponse.class);

	private final HttpServletResponse resp;
	private AsyncContext asyncContext;

	/**
	 * Construct a RestResponse.
	 * 
	 * @param resp
	 *            the {@code HttpServletResponse} to send the response to
	 */
	RestResponse(HttpServletResponse resp) {
		this.resp = resp;
		resp.setContentType("application/json");
		resp.setStatus(HttpServletResponse.SC_OK);
	}

	/**
	 * Returns the {@code HttpServletResponse} associated to this response
	 * 
	 * @return the {@code HttpServletResponse} associated to this response
	 */
	public HttpServletResponse getHttpServletResponse() {
		return resp;
	}

	/**
	 * Sets the {@link AsyncContext} to be used for asynchronous mode requests.
	 * 
	 * @param asyncContext
	 *            the {@code AsyncContext} to be used
	 */
	public void setAsyncContext(AsyncContext asyncContext) {
		this.asyncContext = asyncContext;
		asyncContext.setTimeout(ASYNC_RESP_TIMEOUT);
		asyncContext.addListener(new RestResponseAsyncListener());
	}

	@Override
	protected void doSend(String text) throws IOException {
		resp.getWriter().write(text);
		if (asyncContext != null) {
			asyncContext.complete();
		}
	}

	@Override
	public void sendError(String message) throws IOException, IllegalStateException {
		sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message);
	}

	/**
	 * Sets the status code for this HTTP response to the specified one, sets
	 * the 'error' attribute of this message to the specified message and sends.
	 * 
	 * @param sc
	 *            the status code
	 * @param message
	 *            the error message
	 * @throws IOException
	 *             if an I/O error occurs
	 * @throws IllegalStateException
	 *             if this message has already been sent
	 */
	public void sendError(int sc, String message) throws IOException, IllegalStateException {
		resp.setStatus(sc);
		super.sendError(message);
	}

	/**
	 *
	 * @author Giampiero Baggiani
	 *
	 * @version 1.0.0
	 *
	 */
	private class RestResponseAsyncListener implements AsyncListener {

		@Override
		public void onTimeout(AsyncEvent event) throws IOException {
			logger.warn("Async response timed out");
			sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Timeout");
			asyncContext.complete();
		}

		@Override
		public void onError(AsyncEvent event) throws IOException {
			Throwable t = event.getThrowable();
			logger.error("Async response error", t);
			sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, t.getMessage());
			asyncContext.complete();
		}

		@Override
		public void onComplete(AsyncEvent event) throws IOException {
		}

		@Override
		public void onStartAsync(AsyncEvent event) throws IOException {
		}

	}

}
