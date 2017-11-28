package myPRL.F;

import java.util.ArrayList;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import win.betty35.www.myPRL.MultiScore.Topic;


public class ProductFeatureFinder 
{
	public static String getTopicsOfSegment(String[] searchedWords)
	{
		TopicDictBean topicPot=new TopicDictBean(searchedWords);
		return Topic2Json(topicPot);
	}
	
	public static String Topic2Json(TopicDictBean t)
	{
		JSONObject o=new JSONObject();
		ArrayList<Topic> at=t.topics;
		o.put("topics", at);
		return o.toJSONString();
	}
	
	public static void main(String[] args)
	{
		String[] aa={"多功能","料理机"};
		System.out.println(getTopicsOfSegment(aa));
	}
	
	public static String getByIDs(int[] IDs)
	{
		TopicDictBean topicPot=new TopicDictBean(IDs);
		return Topic2Json(topicPot);
	}
}
