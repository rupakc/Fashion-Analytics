package org.kutty.fetch;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.kutty.db.MongoBase;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

/**
 * Retrives tweets corresponding to a given search term and stores it in MongoDB
 * @author Rupak Chakraborty
 * @for Kutty
 * @since 18 July, 2015
 */ 

public class TweetFetch extends Thread {

	Twitter twitter = new TwitterFactory().getInstance(); 
	AccessToken accessToken = null;  
	String query; 
	int count;
	String since;
	String until; 
	String product_name;
	HashMap<String,String> product_names = new HashMap<String,String>(); 

	public TweetFetch() { 

	} 

	/** 
	 * public constructor to initialize the search query and product name
	 * @param query String containing the query which is to be executed
	 * @param product_name String containing the product name whose posts are to be searched
	 * @throws IOException
	 */ 

	public TweetFetch(String query,String product_name) throws IOException { 

		init("product_list.txt"); 
		setKeys(); 

		this.query = query; 
		this.product_name = product_name.toLowerCase();
		this.product_name = product_names.get(this.product_name); 
	}

	/** 
	 * public constructor to initialize search query, product name, since and until fields
	 * @param query String containing the search query which is to be executed
	 * @param count Integer containing the number of tweets to be fetched (max 100)
	 * @param since String containing the starting date in PHP format
	 * @param until String containing the ending date in PHP format
	 * @param product_name String containing the product name 
	 * @throws IOException
	 */ 

	public TweetFetch(String query,int count,String since,String until,String product_name) throws IOException { 

		init(("product_list.txt"));
		setKeys();

		this.query = query;
		this.since = since;
		this.until = until;
		this.count = count;
		this.product_name = product_name;
		this.product_name = product_name.toLowerCase();
		this.product_name = product_names.get(this.product_name); 
	} 

	/** 
	 * Initialize the product list and map them to the collection names 
	 * @param filename String containing the filename which contains the product list
	 * @throws IOException 
	 */ 

	public void init(String filename) throws IOException
	{ 
		BufferedReader br;
		FileReader fr;
		String alias;
		String collection_name;
		String s = ""; 
		int index; 

		fr = new FileReader(filename);
		br = new BufferedReader(fr);

		while((s = br.readLine()) != null) { 

			index = s.indexOf('=');

			if(index != -1) { 

				alias = s.substring(0,index);
				collection_name = s.substring(index+1,s.length());
				alias = alias.trim();
				collection_name = collection_name.trim();
				product_names.put(alias, collection_name); 

			}
		}

		br.close();
		fr.close();
	}  

	/** 
	 * Overloaded run function to support multi-threading
	 */ 

	public void run() {

		QueryResult result;

		try {
			result = getQuery(query);
			//printTweets(result);
			MongoBase mongo = null; 
			mongo = new MongoBase();
			
			try { 
				
				mongo.setCollection(product_name);
				mongo.putInDB(result, product_name); 

			} catch (UnknownHostException e) {

				e.printStackTrace();
			}

			finally {  
				
				if (mongo != null) { 
					
					mongo.closeConnection();
				}
			}

		} catch (TwitterException | UnknownHostException e) {

			e.printStackTrace();
		}
	} 

	/** 
	 * Sets the Access Token, Access Token Secret, Consumer-key
	 * and Consumer-key secret with user given values in the function 
	 * 
	 * @param access_token String containing the access_token which is to be set
	 * @param access_token_secret String containing the access_token secret which is to be set
	 * @param consumer_key String containing the consumer key which is to be set
	 * @param consumer_key_secret String containing the consumer key secret which is to be set
	 */ 

	public void setKeys(String access_token,String access_token_secret,String consumer_key,String consumer_key_secret) {

		accessToken = new AccessToken(access_token, access_token_secret);
		twitter.setOAuthConsumer(consumer_key,consumer_key_secret);
		twitter.setOAuthAccessToken(accessToken);	
	} 

	/** 
	 * Sets the Access Token, Access Token Secret, Consumer-key 
	 * and Consumer-key secret with default values 
	 */ 

	public void setKeys() { 

		accessToken = new AccessToken("450529986-MRceSC7o2s5Ql6UtRjrD2QjKvkAMXrV5I1bbampV", "zLkRczDjc2jVPUj2KJa7euT37Ge9WjJgOK6ZcaidyKYsT");
		twitter.setOAuthConsumer("h5ly20TCX2cC0Pj8Ul269NFdX", "SoXEB7wlPZruHfL2aPnLbjEQz0TKaySqV3eLlKlp2AEaEpvvZ8");
		twitter.setOAuthAccessToken(accessToken);
	}

	/**
	 * Sets the Access Token, Access Token Secret, Consumer-key and Consumer-key secret with values taken from a file
	 * The file format is as follows:
	 * Access-Token=Your_access_token
	 * Access-Token_Secret=Your_access_token_secret
	 * Consumer-key=Your_consumer_key
	 * Consumer-key-secret=Your_consumer_key-secret 
	 * 
	 * @param filename String containing the file from which this information has to be retrieved
	 * @throws IOException 
	 * 
	 */

	public void setKeys(String filename)throws IOException { 

		BufferedReader br;
		FileReader fr;
		String [] arr = new String[4];
		String s = ""; 
		int index;
		fr = new FileReader((filename));
		br = new BufferedReader(fr);

		for (int i = 0; i < arr.length; i++) { 

			if ((s = br.readLine()) != null) { 

				index = s.indexOf("=");
				arr[i] = s.substring(index+1).trim();

			}
		}

		br.close();
		fr.close();

		accessToken = new AccessToken(arr[0], arr[1]);
		twitter.setOAuthConsumer(arr[2],arr[3]);
		twitter.setOAuthAccessToken(accessToken);
	}

	/** Uses the Search API to query for a given query string by default the count is set to
	 * 100 tweets in this function as compared to the default value of 15. And 100 is also the 
	 * limit on the number of tweets per search query. The tweet language is also set to English
	 *  
	 *  @param query String containing the query which is to be searched 
	 *  @return QueryResult object containing the result of the given query
	 *  @throws TwttterException
	 */ 

	public QueryResult getQuery(String query) throws TwitterException {

		Query q = new Query(query).count(100).lang("en");
		QueryResult result = null;
		result = twitter.search(q);

		return result;

	} 

	/**
	 * This is the overloaded getQuery() function which accepts count of tweets 
	 * the dates representing the starting time and ending time of tweets. 
	 * The values of the Strings query and until have to be set in yyyy-mm-dd format 
	 * 
	 * @param query String containing the query term which is to be searched
	 * @param count Integer containing the number of results to be searched (limit 100) per query
	 * @param since String containing the starting point of time
	 * @param until String containing the ending point of time
	 * @return QueryResult object containing the result of the executed search query
	 * @throws TwitterException
	 */

	public QueryResult getQuery(String query,int count,String since,String until) throws TwitterException {

		Query q = new Query(query).count(count);
		q.setSince(since);
		q.setUntil(until);
		q.setLang("en");

		QueryResult result = null;
		result = twitter.search(q);

		return result;
	}

	/** 
	 * Provided a list of Status objects as input the following method prints the various attributes of
	 * the tweets like content, username, creation data, retweet count geolocation etc  
	 * 
	 * @param tweets List<Status> containing the set of tweets
	 */ 

	public void printTweets(List<Status> tweets) { 

		int c;
		c = 0; 

		for (Status tweet : tweets) { 

			System.out.println("------------------------------------------------------");
			System.out.println("Username : " + tweet.getUser().getName());
			System.out.println("Message : " + tweet.getText());
			System.out.println("Timestamp : " + tweet.getCreatedAt().toString());
			System.out.println("Geolocation of Tweet : " + tweet.getUser().getLocation());
			System.out.println("Retweet Count : " + tweet.getRetweetCount());
			System.out.println("Device Used for Tweet : " + tweet.getSource());
			System.out.println("------------------------------------------------------");

			c++;
		}

		System.out.println("The total number of tweets are : " + c);
	}

	/** 
	 * Overloaded Function printTweets which accepts a QueryResult object as input and prints the same information
	 * @param result QueryResult object containing the result of the executed query
	 */ 

	public void printTweets(QueryResult result) {

		List<Status> tweets = result.getTweets();

		int c;
		c = 0; 

		for (Status tweet : tweets) { 

			System.out.println("------------------------------------------------------");
			System.out.println("Username : " + tweet.getUser().getName());
			System.out.println("Message : " + tweet.getText());
			System.out.println("Timestamp : " + tweet.getCreatedAt().toString());
			System.out.println("Geolocation of Tweet : " + tweet.getUser().getLocation());
			System.out.println("Retweet Count : " + tweet.getRetweetCount());
			System.out.println("Device Used for Tweet : " + tweet.getSource());
			System.out.println("------------------------------------------------------");

			c++;
		}

		System.out.println("The total number of tweets are : " + c);
	}

	/** 
	 * Log information regarding the query is displayed on the standard output
	 * @param result QueryResult object containing the result obtained after executing the query
	 */ 

	public void printSearchLog(QueryResult result)
	{	
		System.out.println("-------------------------------------------------------------"); 

		System.out.println("Maximum Id of the Tweet is : " + (result.getMaxId()));
		System.out.println("Id for the start duration of the tweet : " + result.getSinceId());
		System.out.println("The Rate Limit Status of the API : " + result.getRateLimitStatus());
		System.out.println("The Query Used to get the Results : " + result.getQuery());
		System.out.println("The URL of the Query is : " + result.getRefreshURL());
		System.out.println("The time taken to execute the query : " + result.getCompletedIn());
		System.out.println("The Access Level of the Search : " + result.getAccessLevel());
		System.out.println("The Class to which the Query Object belongs to : " + result.getClass()); 

		System.out.println("-----------------------------------------------------------------");
	}

	/** 
	 * Utility function to write the tweets and related content to a flat file to further storage and processing
	 * @param filename String containing the filename where the tweets have to be printed
	 * @param result QueryResult object containing the result of the query execution
	 * @throws IOException
	 */ 

	public void writeTweetsToFile(String filename,QueryResult result) throws IOException
	{
		FileWriter fw;
		BufferedWriter bw;
		List<Status> tweets;
		String s = null;

		fw = new FileWriter(filename);
		bw = new BufferedWriter(fw);
		tweets = result.getTweets();

		for(Status tweet: tweets) { 

			s = tweet.getUser().getName() + "\t" + tweet.getText() + "\t" + tweet.getPlace() + "\t" + tweet.getCreatedAt().toString() + "\t" + tweet.getRetweetCount();
			bw.write(s);
			bw.newLine();
		}

		bw.close();
		fw.close();
	}

	/** 
	 * Loads a set of hashtags from a given file
	 * @param filename String containing the filename
	 * @return Set<String> containing the hashtag names
	 * @throws IOException
	 */ 

	public Set<String> getFashionHashNames(String filename) throws IOException { 

		BufferedReader br;
		FileReader fr;
		String prompt_alias;
		String temp = "";
		Set<String> tag_names = new HashSet<String>(); 

		fr = new FileReader(filename);
		br = new BufferedReader(fr); 

		while((temp = br.readLine()) != null) { 

			prompt_alias = temp.trim();
			tag_names.add(prompt_alias);
		}

		br.close();
		fr.close();

		return tag_names;
	} 

	/** 
	 * Defines the pipeline for populating the database with tweets associated a given hashtag
	 * @param filename String containing the filename where the hashtags are
	 * @param collection_name String containing the collection name
	 * @throws IOException
	 */ 

	public void tweetPipelineForFashionTrends(String filename,String collection_name) throws IOException { 

		Set<String> tag_names = getFashionHashNames(filename);

		for (String tag : tag_names) { 

			TweetFetch tweet = new TweetFetch(tag,collection_name);
			tweet.start();
		}
	} 

	/** 
	 * Main function to test the utility of the class
	 * @param args
	 * @throws IOException
	 * @throws TwitterException
	 */ 

	public static void main(String args[])throws IOException,TwitterException {
		
		TweetFetch t1 = new TweetFetch("@7fam", "SevenForAllMankind");
		TweetFetch t2 = new TweetFetch("#7fam", "SevenForAllMankind");
		TweetFetch t3 = new TweetFetch("7forallmankind :)", "SevenForAllMankind"); 
		TweetFetch t4 = new TweetFetch("7forallmankind :(", "SevenForAllMankind"); 
		
		t1.start();
		t2.start();
		t3.start();
		t4.start();  

		TweetFetch tweet = new TweetFetch();
		tweet.tweetPipelineForFashionTrends("twitter_fashion_list.txt", "Fashion");
	}
}

