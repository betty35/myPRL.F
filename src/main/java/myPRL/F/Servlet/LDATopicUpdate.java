package myPRL.F.Servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import myPRL.F.ProductFeatureFinder;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import win.betty35.www.myPRL.Pre.dbUtils.DB_LDA;
import win.betty35.www.myPRL.Pre.dbUtils.common.Configure;

public class LDATopicUpdate extends HttpServlet 
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

		String data=request.getParameter("topic");
		System.out.println(data);
		JSONArray ups=(JSONArray)JSONValue.parse(data);
		for(int i=0;i<ups.size();i++)
		{
			JSONObject topic=(JSONObject)ups.get(i);
			int topicID=(int) topic.get("id");
			String one=topic.get("add").toString();
			String two=topic.get("del").toString();
			System.out.println(one);
			System.out.println(two);
			String[] add=null;
			if(one!=""&&one!=null)
			add=one.split(" ");
			String[] del=null;
			if(two!=""&&two!=null)
			del=two.split(" ");
			Configure c=new Configure();
			DB_LDA db=new DB_LDA(c);
			if(add!=null)
			for(int j=0;j<add.length;j++)
			{
				db.updateTopicTerm(topicID, add[i], true);
			}
			if(del!=null)
			for(int j=0;j<del.length;j++)
			{
				db.updateTopicTerm(topicID, del[i], false);
			}
			db.close();
		}
		PrintWriter out=response.getWriter();
		out.print("{\"status\":\"succeed\"}");
		out.close();
	}

}
