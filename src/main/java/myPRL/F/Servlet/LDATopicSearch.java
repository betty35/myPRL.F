package myPRL.F.Servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import myPRL.F.ProductFeatureFinder;

public class LDATopicSearch extends HttpServlet 
{
	private static final long serialVersionUID = -6521333469113541188L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		doPost(request,response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		System.out.println("received");
		request.setCharacterEncoding("UTF-8"); 
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/json; charset=utf-8");

		String searchedWords=request.getParameter("searchedWords");
		String[] sw;
		if(searchedWords.contains(" "))sw=searchedWords.split(" ");
		else {sw=new String[1]; sw[0]=searchedWords;}
		
		PrintWriter out=response.getWriter();
		out.print(ProductFeatureFinder.getTopicsOfSegment(sw));
		out.close();
		
	}

}
