package myPRL.F.Servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import myPRL.F.ProductFeatureFinder;
import myPRL.F.TopicDictBean;
import net.minidev.json.JSONValue;

public class GetTopics extends HttpServlet 
{
	private static final long serialVersionUID = -6521333469113541188L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		doPost(request,response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{	

		request.setCharacterEncoding("UTF-8"); 
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/json; charset=utf-8");

		String topicIDs=request.getParameter("topicIDs");
		String[] id=topicIDs.split(",");
		int[] ids=new int[id.length];
		for(int i=0;i<ids.length;i++)
		{
			ids[i]=Integer.parseInt(id[i]);
		}
		TopicDictBean tdb=new TopicDictBean(ids);
		String json=JSONValue.toJSONString(tdb);
		PrintWriter out=response.getWriter();
		out.print("{\"status\":\"success\",\"data\":"+json+"}");
		out.close();
	}

}
