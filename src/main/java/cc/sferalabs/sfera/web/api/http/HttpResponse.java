/*-
 * +======================================================================+
 * Sfera
 * ---
 * Copyright (C) 2015 - 2016 Sfera Labs S.r.l.
 * ---
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * -======================================================================-
 */

package cc.sferalabs.sfera.web.api.http;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.sferalabs.sfera.web.api.ErrorMessage;
import cc.sferalabs.sfera.web.api.OutgoingMessage;

/**
 * Class representing a response for an API REST request.
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class HttpResponse extends OutgoingMessage {

	private static final long ASYNC_RESP_TIMEOUT = 30000;
	private static final Logger logger = LoggerFactory.getLogger(HttpResponse.class);

	private final HttpServletResponse resp;
	private AsyncContext asyncContext;

	/**
	 * Construct a RestResponse.
	 * 
	 * @param resp
	 *            the {@code HttpServletResponse} to send the response to
	 */
	public HttpResponse(HttpServletResponse resp) {
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

	/**
	 * Sends an internal-server-error HTTP response with the 'errors' attribute
	 * containing an error object with the specified message.
	 * 
	 * @param message
	 *            the error message
	 * @throws IOException
	 *             if an I/O error occurs
	 * @throws IllegalStateException
	 *             if this message has already been sent
	 */
	public void sendServerError(String message) throws IOException, IllegalStateException {
		sendErrors(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, new ErrorMessage(0, message));
	}

	/**
	 * Sets the status code for this HTTP response to the specified one, sets
	 * the 'errors' attribute of this message to an array containing the
	 * specified error objects and sends it.
	 * 
	 * @param sc
	 *            the HTTP status code
	 * @param errors
	 *            the error objects to send
	 * @throws IOException
	 *             if an I/O error occurs
	 * @throws IllegalStateException
	 *             if this message has already been sent
	 */
	public void sendErrors(int sc, ErrorMessage... errors)
			throws IOException, IllegalStateException {
		resp.setStatus(sc);
		super.sendErrors(errors);
	}

	/**
	 * Sets the status code for this HTTP response to the specified one, sets
	 * the 'errors' attribute of this message to an array containing the
	 * specified error objects and sends it.
	 * 
	 * @param sc
	 *            the HTTP status code
	 * @param errors
	 *            the error objects to send
	 * @throws IOException
	 *             if an I/O error occurs
	 * @throws IllegalStateException
	 *             if this message has already been sent
	 */
	public void sendErrors(int sc, Collection<ErrorMessage> errors)
			throws IOException, IllegalStateException {
		resp.setStatus(sc);
		super.sendErrors(errors);
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
			sendServerError("Timeout");
			asyncContext.complete();
		}

		@Override
		public void onError(AsyncEvent event) throws IOException {
			Throwable t = event.getThrowable();
			logger.error("Async response error", t);
			sendServerError(t.getMessage());
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
