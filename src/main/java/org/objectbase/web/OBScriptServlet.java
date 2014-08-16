package org.objectbase.web;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
		Object result = "success";
		try {
			req.setAttribute("orign", script);
			req.setAttribute("result", result == null ? "" : result.toString());
			req.getRequestDispatcher("/index.jsp").forward(req, resp);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
