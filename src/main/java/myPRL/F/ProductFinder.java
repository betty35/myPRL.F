package myPRL.F;

import java.util.ArrayList;
import org.ansj.domain.Result;
import org.ansj.splitWord.analysis.ToAnalysis;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import win.betty35.www.myPRL.Pre.dbUtils.DB_Raw;
import win.betty35.www.myPRL.Pre.dbUtils.common.Configure;
import win.betty35.www.myPRL.bean.Comment;
import win.betty35.www.myPRL.bean.Product;

/**
 * Hello world!
 *
 */
public class ProductFinder 
{
	public static String getProductsJson(String[] searchedWords,int maxProductLen,int maxCommentLen)
	{
		ArrayList<Product> pl=getProductsBySearchedWords(searchedWords,maxProductLen);
		return ProductList2Json(pl,maxCommentLen);
	}
		
	public static ArrayList<Product> getProductsBySearchedWords(String[] searchedWords,int maxLen)
	{
        Configure c=new Configure();
        DB_Raw d=new DB_Raw(c);
        String IDs=d.getSearchedWordsIDs(searchedWords);
        //System.out.println(IDs);
        ArrayList<Long> PIDs=d.getPIDsByKeywords(IDs);
        //get Comments
        ArrayList<Product> products=new ArrayList<Product>();
        int len=PIDs.size();
        if(len>maxLen)len=maxLen;
        for(int i=0;i<len;i++)
        {
        	products.add(d.getProductByPID(PIDs.get(i)));
        }
        d.close();
        return products;
	}
	
	public static ArrayList<Product> getProductsBySearchedWordsWithoutComment(String[] searchedWords)
	{
        Configure c=new Configure();
        DB_Raw d=new DB_Raw(c);
        String IDs=d.getSearchedWordsIDs(searchedWords);
        //System.out.println(IDs);
        ArrayList<Long> PIDs=d.getPIDsByKeywords(IDs);
        //get Comments
        ArrayList<Product> products=new ArrayList<Product>();
        int len=PIDs.size();
        for(int i=0;i<len;i++)
        {
        	products.add(d.getProductByPIDWithoutComment(PIDs.get(i)));
        }
        d.close();
        return products;
	}
	
	public static String ProductList2Json(ArrayList<Product> products,int maxComments)
	{
		JSONArray ps = new JSONArray();
        
		int len=products.size();
		for(int i=0;i<len;i++)
		{
			Product p=products.get(i);
			JSONObject product = new JSONObject();
			JSONArray cs=new JSONArray();//comments
			
			product.put("id",p.getId());
			product.put("title", p.getTitle());
			product.put("price", p.getPrice());
			product.put("sales", p.getSales());
			product.put("image",p.getFilename());
			product.put("source", p.getSource());
			product.put("page", p.getPage());
			
			ArrayList<Comment> cl=p.getCommentList();
			int cLen=cl.size();
			int startMax=cLen>maxComments?cLen-maxComments:0;
			int start=(int) Math.floor(Math.random()*startMax);
			if(cLen>maxComments) cLen=maxComments;
			for(int m=start;m<(start+cLen);m++)
			{
				JSONObject c1=new JSONObject();//comment
				Comment c=cl.get(m);
				String text=c.getText();
				if(text.length()<20) 
				{
					if((start+cLen+1)<=cl.size())//if there are more comments that can be used
					cLen++;
					continue;
				}
				Result r=ToAnalysis.parse(text);
				c1.put("t", r.toString());
		    	c1.put("id", c.getCommentID());
		    	c1.put("oid", c.getOriginalID());
				cs.add(c1);
			}
			product.put("comments", cs);
			ps.add(product);
		}
		return ps.toJSONString();
	}
	
    
    public static String toStringWithSpace(Result r)
    {
    	return r.toStringWithOutNature().replaceAll(","," ");
    }
    
    public static boolean reserve(String nature)
    {
    	/*String[] t={"d","p","b","c","null","r","e","y","o"};
    	if(itIsIn(nature,t))
    		return false;
    	else if(nature.charAt(0)=='w') return false;
    	else
    	return true;*/
    	if(nature.charAt(0)=='n'||nature.charAt(0)=='a') return true;
    	else
    	return false;
    }
    
    
    public static boolean itIsIn(String str,String[] t)
    {
    	for(int i=0;i<t.length;i++)
    	{
    		String k=t[i];
    		if(str.equals(k)) return true;
    	}
    	return false;
    }
    
    public static String getProductBasic(String[] searchedWords)
    {
    	ArrayList<Product> pl=getProductsBySearchedWordsWithoutComment(searchedWords);
    	JSONObject o=new JSONObject();
    	o.put("product", pl);
    	return o.toJSONString();
    }
}
