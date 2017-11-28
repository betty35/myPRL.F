package myPRL.F;

import java.util.ArrayList;

import net.minidev.json.JSONObject;
import win.betty35.www.myPRL.Pre.dbUtils.DB_MultiScore;
import win.betty35.www.myPRL.Pre.dbUtils.common.Configure;
import win.betty35.www.myPRL.bean.Product;

public class MultiDControl 
{
	public static String getProduct(String[] searchedWords,int[] topicID)
	{
		Configure c=new Configure();
		ArrayList<Product> products=ProductFinder.getProductsBySearchedWordsWithoutComment(searchedWords);
		ArrayList<Integer> tID=new ArrayList<Integer>();
		for(int i=0;i<topicID.length;i++)
			tID.add(topicID[i]);
		DB_MultiScore db=new DB_MultiScore(c);
		db.getMultiDScore(products, tID);
		db.close();
		JSONObject o=new JSONObject();
		o.put("p",products);
		return o.toJSONString();
	}
}
