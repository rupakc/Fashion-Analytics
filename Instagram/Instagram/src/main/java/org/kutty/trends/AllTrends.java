package org.kutty.trends;

/** 
 * Collects Fashion trends from different social channels like
 * Facebook, Twitter, Reddit, and Instagram 
 * 
 * @author Rupak Chakraborty
 * @for Kutty
 * @since 25 July,2015
 *
 */

public class AllTrends {

	public static void startTrendTimers() { 
		
		try { 
			FacebookTrends.executeTimers();
		} catch(Exception e) { 
			e.printStackTrace();
		}
		
		try { 
			TwitterTrends.executeTimers();
		} catch(Exception e) { 
			e.printStackTrace();
		}
		
		try { 
			InstagramTrends.executeTimers();
		} catch(Exception e) { 
			e.printStackTrace();
		}
		
		try { 
			RedditTrends.executeTimers();
		} catch(Exception e) { 
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]) { 
		
		startTrendTimers();
	}
}
