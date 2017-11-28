package myPRL.F;

import java.util.ArrayList;
import java.util.HashMap;

import org.ansj.domain.Result;
import org.ansj.splitWord.analysis.ToAnalysis;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import win.betty35.www.myPRL.Pre.dbUtils.DB_Demands;
import win.betty35.www.myPRL.Pre.dbUtils.DB_Raw;
import win.betty35.www.myPRL.Pre.dbUtils.common.Configure;
import win.betty35.www.myPRL.bean.Comment;
import win.betty35.www.myPRL.bean.Product;

/**
 * Hello world!
 *
 */
public class DemandsControl 
{
	public static String getDemands(String searchedWords)
	{
		Configure c=new Configure();
        DB_Raw d=new DB_Raw(c);
                
		return null;
	}
	
	public static String getTopicDemandsPair(int[] topicID)
	{
		Configure c=new Configure();
		DB_Demands d=new DB_Demands(c);
		JSONArray ja=new JSONArray();
		for(int i=0;i<topicID.length;i++)
		{
			JSONObject o=new JSONObject();
			HashMap<String,Double> data=d.getDemandsByTopic(topicID[i]);
			if(!data.isEmpty())
			{
				o.put("id", topicID[i]);
				o.put("terms", data);
				ja.add(o);
			}
		}
		d.close();
		return ja.toJSONString();
	}
	
	public static String getAvgWeights(int[] topicIDs,Long[] demandIDs)
	{
		Configure c=new Configure();
		DB_Demands d=new DB_Demands(c);
		double[][] avg=d.getAvgWeights(topicIDs, demandIDs);
		d.close();
		JSONObject o=new JSONObject();
		o.put("avgWeight", avg);
		return o.toJSONString();
	}
	
	public static Long[] getDemandIDs(String[] demandNames)
	{
		Configure c=new Configure();
		DB_Demands d=new DB_Demands(c);
		Long[] re=new Long[demandNames.length];
		for(int i=0;i<demandNames.length;i++)
			{re[i]=d.newDemand(demandNames[i]);}
		d.close();
		return re;
	}
	
	public static String update(int[] topicIDs,Long[] demandIDs,double[][] map)
	{
		Configure c=new Configure();
		DB_Demands d=new DB_Demands(c);
		int tl=topicIDs.length;
		int dl=demandIDs.length;
		for(int i=0;i<tl;i++)
		{
			for(int j=0;j<dl;j++)
			{
				if(map[i][j]>0)
				d.updateTopicDemand(demandIDs[j], topicIDs[i], map[i][j]);
			}
		}
		d.close();
		return "{\"status\":\"success\"}";
	}
}
