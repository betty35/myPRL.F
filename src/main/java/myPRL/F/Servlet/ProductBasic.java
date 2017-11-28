package myPRL.F.Servlet;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import myPRL.F.ProductFinder;
public class ProductBasic extends HttpServlet 
{

	/**
	 * UID
	 */
	private static final long serialVersionUID = 8302914162337622737L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		doPost(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{	
		request.setCharacterEncoding("UTF-8"); 
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/json; charset=utf-8");
		PrintWriter out=response.getWriter();
		String searchedWords=request.getParameter("searchedWords");
		String[] sw;
		if(searchedWords.contains(" "))sw=searchedWords.split(" ");
		else {sw=new String[1]; sw[0]=searchedWords;}
		String json=ProductFinder.getProductBasic(sw);
		out.print(json);
		out.close();
	}
}
