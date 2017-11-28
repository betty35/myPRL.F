package myPRL.F.Servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import myPRL.F.DemandsControl;
import myPRL.F.ProductFeatureFinder;

public class GetDemands extends HttpServlet 
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

		String output=null;
		String method=request.getParameter("method");
		if(method.equals("byTopics"))
		{
			String id=request.getParameter("id");
			String[] id1=id.split(",");
			int[] ids=new int[id1.length];
			for(int i=0;i<ids.length;i++)
			{
				ids[i]=Integer.parseInt(id1[i]);
			}
			output=DemandsControl.getTopicDemandsPair(ids);
		}
		else if(method.equals("update"))
		{
			String topicIDs=request.getParameter("topicIDs");
			//System.out.println(topicIDs);
			String demandNames=request.getParameter("demandNames");
			//System.out.println(demandNames);
			String maps=request.getParameter("map");
			
			String names[]=demandNames.split(",");
			Long[] demandIDs=DemandsControl.getDemandIDs(names);
			String[] id=topicIDs.split(",");
			int[] ids=new int[id.length];
			for(int i=0;i<ids.length;i++)
			{
				ids[i]=Integer.parseInt(id[i]);
			}
			String[] maps1=maps.split(";");
			double[][] map=new double[ids.length][demandIDs.length];
			for(int i=0;i<maps1.length;i++)
			{
				String[] maps2=maps1[i].split(",");
				for(int j=0;j<maps2.length;j++)
				{
					map[i][j]=Double.parseDouble(maps2[j]);
				}
			}
			output=DemandsControl.update(ids, demandIDs, map);
		}
		else if(method.equals("avgW"))
		{
			String topicIDs=request.getParameter("topicIDs");
			System.out.println(topicIDs);
			String demandNames=request.getParameter("demandNames");
			System.out.println(demandNames);
			String names[]=demandNames.split(",");
			Long[] demandIDs=DemandsControl.getDemandIDs(names);
			String[] id=topicIDs.split(",");
			int[] ids=new int[id.length];
			for(int i=0;i<ids.length;i++)
			{
				ids[i]=Integer.parseInt(id[i]);
			}
			output=DemandsControl.getAvgWeights(ids, demandIDs);
		}
		
		PrintWriter out=response.getWriter();
		out.print(output);
		out.close();
	}

}
