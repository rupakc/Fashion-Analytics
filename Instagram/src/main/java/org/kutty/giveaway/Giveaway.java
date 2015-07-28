package org.kutty.giveaway;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import org.json.simple.parser.ParseException;
import org.kutty.fetch.TagFetch;

/** 
 * Fetches posts relating to Instagram Giveways
 * The post retrieval  code is fetched after every t minutes. 
 * 
 * @author Rupak Chakraborty
 * @for Kutty
 * @since 22 July,2015
 *
 */

public class Giveaway extends TimerTask {

	public void getGiveaway(int pagination_limit) { 

		TagFetch tagfetch = new TagFetch();
		tagfetch.pagination_limit = pagination_limit; 
		
		try {
			
			tagfetch.TagPipelineForTopTags("giveaway", "Giveaway");
			
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}

	public void getGiveaway() { 

		TagFetch tagfetch = new TagFetch();

		try {
			tagfetch.TagPipelineForTopTags("giveaway", "Giveaway");

		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}
	
	public void run() { 
		
		getGiveaway(1000);
	}
	
	public static void executeTimer() { 
		
		long delay = 5*1000;
		long period = 60 * 60 *1000;
		
		Timer t = new Timer();
		t.scheduleAtFixedRate(new Giveaway(), delay, period);
	}
	
	public static void main(String args[]) { 
		
		Giveaway.executeTimer();
	}
}
