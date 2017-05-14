package com.gapidu.tracking;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import com.gapidu.context.VisitorContext;

@WebServlet("/trk/gapidu")
public class GpduServlet extends HttpServlet {

	final static Logger logger = Logger.getLogger(GpduServlet.class);

	private static final long serialVersionUID = 1L;
	public static boolean testMode;

	public GpduServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) {

		this.doPost(request, response);

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) {

		BasicConfigurator.configure();

		response.addHeader("Access-Control-Allow-Origin", "*");
		response.addHeader("Access-Control-Allow-Methods", "POST");
		response.addHeader("Access-Control-Allow-Headers", "Origin, Content-Type, X-Auth-Token");

		try {

			logger.info("parsing visitor's identifiers from session");
			VisitorContext visitor = new VisitorContext(request, response);

			// new GpduHandler(visitor);

			response.getWriter().append("").append(request.getContextPath());

		} catch (Exception e) {
			logger.error(e);
			// response.getWriter().append(e.getMessage()).append(request.getContextPath());
		}

	}

}
