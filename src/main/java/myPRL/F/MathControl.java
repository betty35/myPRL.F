package myPRL.F;

import java.util.ArrayList;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import win.betty35.www.math.regression.Stepwise;
import win.betty35.www.myPRL.MultiScore.Score;
import win.betty35.www.myPRL.bean.Product;

public class MathControl 
{
	public static String stepWise(String data)
	{
		System.out.println(data);
		JSONArray psJ=(JSONArray)JSONValue.parse(data);
		ArrayList<Product> ps=new ArrayList<Product>();
		ArrayList<String> names=new ArrayList<String>();
		int len=0;
		for(int i=0;i<psJ.size();i++)
		{
			Product p=new Product();
			JSONObject pJ=(JSONObject)psJ.get(i);
			long temp1=0;
			p.setSales(temp1+(Integer)pJ.get("sales"));
			JSONArray mdsJ=(JSONArray)pJ.get("multiScore");
			len=mdsJ.size();
			p.multiScore=new Score[len];
			for(int j=0;j<len;j++)
			{
				JSONObject m=(JSONObject)mdsJ.get(j);
				names.add((String)m.get("topicID"));
				p.multiScore[j]=new Score();
				p.multiScore[j].topicID=j;
				p.multiScore[j].good=(Double)m.get("good");
				p.multiScore[j].bad=(Double)m.get("bad");
			}
			ps.add(p);
		}
		
		int xBesidesMultiScore=0;
		double []y=new double[ps.size()];
		double [][]x=new double[ps.size()][len*2+xBesidesMultiScore+1];//*2+1];
		for(int i=0;i<ps.size();i++)
		{
			Product p=ps.get(i);
			y[i]=p.getSales();
			for(int j=0;j<len;j++)
			{
				/*double total=(p.multiScore[j].good+p.multiScore[j].bad);
				if(Math.abs(total)>0.0000001)
				x[i][j]=p.multiScore[j].good/(p.multiScore[j].good+p.multiScore[j].bad);
				else x[i][j]=0;
				x[i][j*2]=p.multiScore[j].good;
				x[i][j*2+1]=p.multiScore[j].bad;
				System.out.println(x[i][j]);*/
				x[i][j*2]=p.multiScore[j].good;
				x[i][j*2+1]=p.multiScore[j].bad;
			}
			x[i][len*2]=p.getSales();
		}		
		Stepwise regression=new Stepwise();
		regression.setData(x);
		for(int i=0;i<50;i++)
			try {
				regression.newStep();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		double[] b=null;
		try {
			 b=regression.newStep();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayList<Integer> li=regression.choose;
		
		JSONArray ar=new JSONArray();
		for(int i=0;i<li.size();i++)
		{
			String side=null;
			int index=li.get(i);
			if(index%2==0) side="good";
			else side="bad";
			index=index/2;
			String topic=names.get(index);
			String t_s=topic+","+side;
			JSONObject o=new JSONObject();
			o.put("x", t_s);
			o.put("b", b[i]);
			ar.add(o);
		}
		JSONObject o=new JSONObject();
		o.put("x", "常数");
		o.put("b", b[b.length-1]);
		ar.add(o);
		return ar.toJSONString();
	}
}
