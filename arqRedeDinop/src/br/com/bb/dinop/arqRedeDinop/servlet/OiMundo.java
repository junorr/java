package br.com.bb.dinop.arqRedeDinop.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class OiMundo extends HttpServlet {
	
	protected void service(HttpServletRequest req, HttpServletResponse rsp) throws ServletException, IOException {
		PrintWriter out = rsp.getWriter();
		File f = new File("./");
		out.println("<html>");
		out.println("  <head><title>Oi Mundo Servlet</title></head>");
		out.println("  <body>");
		out.println("    <h4>Oi Mundo Servlet!</h4>");
		out.println("    <h4>File(\"./\"): '"+ f.getAbsolutePath()+"'</h4>");
		out.println("  </body>");
		out.println("</html>");
		out.flush();
	}

}
