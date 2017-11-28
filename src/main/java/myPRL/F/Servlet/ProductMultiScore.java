package myPRL.F.Servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ansj.vec.Word2VEC;

import myPRL.F.MultiDControl;

public class ProductMultiScore extends HttpServlet 
{
	private static final long serialVersionUID = 5482577806888366210L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		doPost(request,response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{	
		request.setCharacterEncoding("UTF-8"); 
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/json; charset=utf-8");
		String tid1=request.getParameter("topicIDs");
		String sw1=request.getParameter("sw");
		String[] tids=tid1.split(",");
		int[] tid=new int[tids.length];
		for(int i=0;i<tids.length;i++)
			tid[i]=Integer.parseInt(tids[i]);
		String[] sw=sw1.split(" ");
		String output=MultiDControl.getProduct(sw, tid);
		PrintWriter out=response.getWriter();
		out.print(output);
		out.close();
	}

}
