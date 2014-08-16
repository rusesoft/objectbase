package org.objectbase.web;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.objectbase.parser.OBParser;

public class OBScriptServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6069528808503118059L;

	public void doGet(HttpServletRequest req, HttpServletResponse resp) {
		doPost(req, resp);
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) {
		String script = req.getParameter("script");
		script = script == null ? "" : script.trim();
		System.out.println("receive script: \n"+script);
		OBParser parser = new OBParser(script);

		try {
			Object result = parser.parseAndExecute();
			
			req.setAttribute("origin", script);
			req.setAttribute("result", result == null ? "" : result.toString());
			req.getRequestDispatcher("/index.jsp").forward(req, resp);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
