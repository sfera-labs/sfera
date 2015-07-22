package cc.sferalabs.sfera.http.api.rest;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import cc.sferalabs.sfera.http.api.HttpResponse;

class RestResponse extends HttpResponse {

	private final HttpServletResponse resp;

	/**
	 * 
	 * @param resp
	 */
	RestResponse(HttpServletResponse resp) {
		this.resp = resp;
	}

	@Override
	protected void doSend(String text) throws IOException {
		resp.setContentType("application/json");
		resp.setStatus(HttpServletResponse.SC_OK);
		resp.getWriter().print(text);
	}

}
