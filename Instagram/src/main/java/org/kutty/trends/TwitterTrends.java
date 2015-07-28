/**
 * 
 */
package org.kutty.trends;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import org.kutty.fetch.TweetFetch;

/**
 * Fetches latest fashion trends from Twitter 
 * 
 * @author Rupak Chakraborty
 * @for Kutty
 * @since 22 July,2015
 */ 

public class TwitterTrends extends TimerTask {
	
	public void getTwitterTrends(String filename,String collection_name)  { 
		
		TweetFetch tweet = new TweetFetch();
		
		try {
			tweet.tweetPipelineForFashionTrends(filename, collection_name);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run() { 
		
		getTwitterTrends("twitter_fashion_list.txt","Fashion");
	}
	
	public static void executeTimers() { 
		
		long delay = 5*1000;
		long period = 60 * 60 *1000;
		
		Timer t = new Timer();
		t.scheduleAtFixedRate(new TwitterTrends(), delay, period);
	}
	
	public static void main(String args[]) { 
		
		TwitterTrends.executeTimers();
	}
}
