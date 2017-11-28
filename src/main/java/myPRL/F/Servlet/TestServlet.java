package myPRL.F.Servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ansj.vec.Word2VEC;

public class TestServlet extends HttpServlet 
{
	private static final long serialVersionUID = -6521333469113541188L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		doPost(request,response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{	
		System.out.println("received");
		if(!Word2VEC.loaded())Word2VEC.loadGoogleModel("E:/GD/words.bin");
		request.setCharacterEncoding("UTF-8"); 
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/json; charset=utf-8");
		String w1=request.getParameter("w1");
		String w2=request.getParameter("w2");
		PrintWriter out=response.getWriter();
		out.print("d:"+Word2VEC.distanceBetween(w1, w2));
		out.close();
	}

}
