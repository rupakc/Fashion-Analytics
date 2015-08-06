package org.kutty.brands;

import java.util.Timer;


/** 
 * Aggregation class for retrieving and storing posts of all brands from across the social channels
 * @author Rupak Chakraborty
 * @for Kutty
 * @since 25 July,2015
 */ 

public class AllBrands {
	
	/** 
	 * Executes the post retrieval module for each class in turn
	 */
	
	public static void executeTimers() { 

		long delay = 5*1000;
		long period = 30*60*1000;

		try { 

			Timer t = new Timer();
			t.scheduleAtFixedRate(new Forever21(),delay,period);

		} catch(Exception e) {  

			e.printStackTrace();
		}

		try { 

			Timer t = new Timer();
			t.scheduleAtFixedRate(new FreePeople(),delay,period);

		} catch(Exception e) {  

			e.printStackTrace();
		}
		
		try { 

			Timer t = new Timer();
			t.scheduleAtFixedRate(new Guess(),delay,period);

		} catch(Exception e) {  

			e.printStackTrace();
		}
		
		try { 

			Timer t = new Timer();
			t.scheduleAtFixedRate(new HandM(),delay,period);

		} catch(Exception e) {  

			e.printStackTrace();
		}
		
		try { 

			Timer t = new Timer();
			t.scheduleAtFixedRate(new Levis(),delay,period);

		} catch(Exception e) {  

			e.printStackTrace();
		}
		
		try { 

			Timer t = new Timer();
			t.scheduleAtFixedRate(new Mango(),delay,period);

		} catch(Exception e) {  

			e.printStackTrace();
		}
		
		try { 

			Timer t = new Timer();
			t.scheduleAtFixedRate(new RagandBone(),delay,period);

		} catch(Exception e) {  

			e.printStackTrace();
		}
		
		try { 

			Timer t = new Timer();
			t.scheduleAtFixedRate(new SevenForAllMankind(),delay,period);

		} catch(Exception e) {  

			e.printStackTrace();
		}
		
		try { 

			Timer t = new Timer();
			t.scheduleAtFixedRate(new TrueReligion(),delay,period);

		} catch(Exception e) {  

			e.printStackTrace();
		}
	}
	
	public static void main(String args[]) { 
		
		AllBrands.executeTimers();
	}
}
