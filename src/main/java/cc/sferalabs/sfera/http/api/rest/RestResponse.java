package cc.sferalabs.sfera.http.api.rest;

import java.io.IOException;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cc.sferalabs.sfera.http.api.JsonMessage;

class RestResponse extends JsonMessage {

	private static final long ASYNC_RESP_TIMEOUT = 30000;
	private static final Logger logger = LogManager.getLogger();

	private final HttpServletResponse resp;
	private AsyncContext asyncContext;

	/**
	 * 
	 * @param resp
	 */
	RestResponse(HttpServletResponse resp) {
		this.resp = resp;
		resp.setContentType("application/json");
		resp.setStatus(HttpServletResponse.SC_OK);
	}

	/**
	 * 
	 * @param asyncContext
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
	public void sendResult(Object result) throws IOException, IllegalStateException {
		super.sendResult(result);
	}

	@Override
	public void sendError(String message) throws IOException, IllegalStateException {
		sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message);
	}

	/**
	 * 
	 * @param sc
	 * @param message
	 * @throws IOException
	 * @throws IllegalStateException
	 */
	public void sendError(int sc, String message) throws IOException, IllegalStateException {
		resp.setStatus(sc);
		super.sendError(message);
	}

	/**
	 *
	 */
	private class RestResponseAsyncListener implements AsyncListener {

		@Override
		public void onTimeout(AsyncEvent event) throws IOException {
			logger.warn("Async response timed out");
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Timeout");
			asyncContext.complete();
		}

		@Override
		public void onError(AsyncEvent event) throws IOException {
			logger.error("Async response error", event.getThrowable());
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
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
