package org.kutty.trends;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import org.kutty.fetch.Reddit;

/** 
 * Fetches latest trends in fashion from Reddit prompts 
 * 
 * @author Rupak Chakraborty
 * @for Kutty
 * @since 22 July,2015
 */ 

public class RedditTrends extends TimerTask {
	
	public void getRedditFashionTrends() {
		
		try { 
			
			Reddit reddit = new Reddit();
			reddit.fetchTopTrendsReddit("Fashion"); 
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run() { 
		
		getRedditFashionTrends();
	}
	
	public static void executeTimers() { 
		
		long delay = 5*1000;
		long period = 60 * 60 *1000;
		
		Timer t = new Timer();
		t.scheduleAtFixedRate(new RedditTrends(), delay, period);
	}
	
	public static void main(String args[]) { 
		
		RedditTrends.executeTimers();
	}
}	
