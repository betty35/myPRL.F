package myPRL.F;

import java.util.ArrayList;

import win.betty35.www.myPRL.Pre.dbUtils.DB_Raw;
import win.betty35.www.myPRL.Pre.dbUtils.common.Configure;
import win.betty35.www.myPRL.bean.LDA_Term;
import win.betty35.www.myPRL.MultiScore.Topic;
import win.betty35.www.myPRL.Pre.dbUtils.DB_LDA;

public class TopicDictBean 
{
	public ArrayList<Topic> topics;
	public int len=0;
	
	public TopicDictBean(String[] searchedWords)
	{
		this.init(searchedWords);
	}
	
	public TopicDictBean(int[] topicIDs)
	{
		this.init(topicIDs);
	}
	
	public int init(String[] searchedWords)
	{//return number of topics
		if(topics==null) topics=new ArrayList<Topic>();
		topics.clear();
		
		Configure c=new Configure();
        DB_Raw d=new DB_Raw(c);
        String IDs=d.getSearchedWordsIDs(searchedWords);
        d.close();
        DB_LDA d2=new DB_LDA(c);
        ArrayList<Integer> topicIDs=d2.findTopicIDBySearch(IDs);
        len=topicIDs.size();
        for(int i=0;i<topicIDs.size();i++)
        {
        	int topicID=topicIDs.get(i);
        	ArrayList<LDA_Term> terms=d2.getTermsByTopicID(topicID);
        	Topic t=new Topic();
        	t.setID(topicID);
        	for(int j=0;j<terms.size();j++)
        	{
        		LDA_Term tm=terms.get(j);
        		if(tm.getTerm()!=null&&tm.getTerm()!="")
        		t.setTerm(tm.getTerm(), tm.getProb());
        	}
        	topics.add(t);
        }
        d2.close();
        return len;
	}
	
	public int init(int[] topicIDs)
	{//return number of topics
		if(topics==null) topics=new ArrayList<Topic>();
		topics.clear();
		
		Configure c=new Configure();
        DB_LDA d2=new DB_LDA(c);
        for(int i=0;i<topicIDs.length;i++)
        {
        	int topicID=topicIDs[i];
        	ArrayList<LDA_Term> terms=d2.getTermsByTopicID(topicID);
        	Topic t=new Topic();
        	t.setID(topicID);
        	for(int j=0;j<terms.size();j++)
        	{
        		LDA_Term tm=terms.get(j);
        		if(tm.getTerm()!=null&&tm.getTerm()!="")
        		t.setTerm(tm.getTerm(), tm.getProb());
        	}
        	topics.add(t);
        }
        d2.close();
        return len;
	}
}
