package org.kutty.trends;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import org.kutty.fetch.FacebookFetch;

import com.cybozu.labs.langdetect.LangDetectException;

import facebook4j.FacebookException;

/**
 * Retrieves latest fashion trends from Facebook 
 * 
 * @author Rupak Chakraborty
 * @for Kutty
 * @since 22 July, 2015
 *
 */

public class FacebookTrends extends TimerTask {
	
	public void getFacebookTrends() { 
		
		try {
			FacebookFetch fb = new FacebookFetch();
			fb.facebookPipelineForFashionTrends("facebook_fashion_list.txt", "Fashion");
		} catch (FacebookException | LangDetectException | IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run() { 
		
		getFacebookTrends();
	}
	
	public static void executeTimers() { 
		
		long delay = 5*1000;
		long period = 60 * 60 *1000; 
		
		Timer t = new Timer();
		t.scheduleAtFixedRate(new FacebookTrends(), delay, period);
	}
	
	public static void main(String args[]) { 
		
		FacebookTrends.executeTimers();
	}
}
