package org.kutty.trends;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import org.json.simple.parser.ParseException;
import org.kutty.fetch.TagFetch;

/** 
 * Fetches latest  fashion trends from Instagram
 * @author Rupak Chakraborty
 * @for Kutty
 * @since 22 July, 2015
 * 
 */ 

public class InstagramTrends extends TimerTask {

	public void getInstagramFashionTrends() { 
		
		TagFetch fashion_tag = new TagFetch();
		try {
			fashion_tag.TagPipelineForTopTags("fashion", "Fashion");
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}
	
	public void getInstagramFashionTrends(int pagination_limit) { 
		
		TagFetch fashion_tag = new TagFetch();
		fashion_tag.pagination_limit = pagination_limit; 
		
		try {
			fashion_tag.TagPipelineForTopTags("fashion", "Fashion");
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {  
		
		getInstagramFashionTrends(1000);
	}
	
	public static void executeTimers() { 
		
		long delay = 5*1000;
		long period = 60 * 60 *1000; 
		
		Timer t = new Timer();
		t.scheduleAtFixedRate(new InstagramTrends(), delay, period);
	}
	
	public static void main(String args[]) { 
		
		InstagramTrends.executeTimers();
	}
}
