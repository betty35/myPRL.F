package myPRL.F.Servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import myPRL.F.MathControl;

public class MathServlet extends HttpServlet 
{
	private static final long serialVersionUID = -4529873348725242019L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		doPost(request,response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{	
		System.out.println("received, mathModule");
		request.setCharacterEncoding("UTF-8"); 
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/json; charset=utf-8");
		String w1=request.getParameter("d");
		System.out.println("w1："+w1);
		String output=MathControl.stepWise(w1);
		PrintWriter out=response.getWriter();
		out.print(output);
		out.close();
	}

}
