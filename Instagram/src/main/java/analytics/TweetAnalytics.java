package analytics;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.joda.time.DateTime;

import utils.DateConverter;
import utils.SortUtil;
import utils.UserBase;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import db.MongoBase;
import dbo.CountryBase;

/** 
 * @author Rupak Chakraborty
 * @for Adobe Systems,India
 * @since 25 Feb, 2015
 * 
 * Base Class for performing Analytics on the accumulated tweets the basic analytics include: 
 * 
 * Getting the User Base from the tweets
 * Retrieving the retweet count of the tweets
 * Fetching a list of top devices used to post on twitter
 * A list of top tweets sorted account to the retweet count 
 * 
 */ 

public class TweetAnalytics {

	HashMap<String,String> product_names = new HashMap<String,String>(); 
	String product_name;   

	/** 
	 * public constructor to initialize the product name
	 * @param product_name String representing the product name which loosely corresponds to a collection name
	 * @throws IOException
	 */ 

	public TweetAnalytics(String product_name) throws IOException { 

		String product_list = "product_list.txt";
		init(product_list);
		this.product_name = product_name.toLowerCase();
		this.product_name = product_names.get(this.product_name);
	} 

	/** 
	 * Initializes the product_names HashMap with product name and collection name
	 * @param filename String containing the file from which the names are retrieved
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
	 * Main function to test out the functionality of the class
	 * @param args
	 * @throws IOException
	 */ 

	public static void main(String args[]) throws IOException { 

		TweetAnalytics tweet = new TweetAnalytics("Acrobat");

		Date from = new DateTime("2015-01-01").toLocalDate().toDate();
		Date to = new DateTime("2015-06-19").toLocalDate().toDate(); 

		tweet.insertGeoLocation(from, to);
		//System.out.println(tweet.getGeoLocation(from, to));
		//System.out.println(tweet.getTweetDevices(from, to, 5));
	}

	/** 
	 * Returns the tweet with the most re-tweets in the given time period
	 * @param from String representing the starting time period
	 * @param to String representing the ending time perios
	 * @return Tweet which has the maximum number of retweets
	 * @throws UnknownHostException
	 */ 

	public String getTopTweet(String from,String to) throws UnknownHostException { 

		Date from_date = DateConverter.getJavaDate(from);
		Date to_date = DateConverter.getJavaDate(to);
		DBObject query;
		DBObject fields;
		String message = "";
		DBCursor cursor; 
		DBCollection collection; 
		DBObject orderby; 

		MongoBase mongo = new MongoBase();
		mongo.setCollection(product_name);
		collection = mongo.getCollection();  

		query = new BasicDBObject("Channel","Twitter").append("TimeStamp", new BasicDBObject("$gte",from_date).append("$lte",to_date));
		fields = new BasicDBObject("Message",1).append("RetweetCount", 1);
		orderby = new BasicDBObject("RetweetCount",-1);

		cursor = collection.find(query, fields);
		cursor = cursor.sort(orderby);

		if (cursor.hasNext()) { 

			message = (String) cursor.next().get("Message");
		} 

		mongo.closeConnection(); 

		return message;
	} 

	/** 
	 * Returns the set of Top N tweets for a given time period
	 * @param from String representing the starting date
	 * @param to String representing the ending date
	 * @param count Integer representing the number of results to be  returned
	 * @return Set<String> containing the top tweets
	 * @throws UnknownHostException
	 */ 

	public Set<String> getTopTweet(String from,String to,int count) throws UnknownHostException { 

		Date from_date = DateConverter.getJavaDate(from);
		Date to_date = DateConverter.getJavaDate(to);
		DBObject query;
		DBObject fields;
		String message = "";
		DBCursor cursor; 
		DBCollection collection; 
		DBObject orderby; 
		Set<String> message_set = new HashSet<String>();

		MongoBase mongo = new MongoBase();
		mongo.setCollection(product_name);
		collection = mongo.getCollection();  

		query = new BasicDBObject("Channel","Twitter").append("TimeStamp", new BasicDBObject("$gte",from_date).append("$lte",to_date));
		fields = new BasicDBObject("Message",1).append("RetweetCount", 1);
		orderby = new BasicDBObject("RetweetCount",-1);

		cursor = collection.find(query, fields);
		cursor = cursor.sort(orderby);

		while (cursor.hasNext() && message_set.size() <= count) { 

			message = (String) cursor.next().get("Message");
			message_set.add(message);
		}

		mongo.closeConnection();
		return message_set;
	}  

	/** 
	 * Returns the set of tweets trending for a given time period
	 * @param from java Date representing the starting point
	 * @param to java Date representing the ending point in time
	 * @param count Integer containing the number of top tweets to be fetched
	 * @return Set<String> containing the tweets
	 * @throws UnknownHostException
	 */ 

	public Set<String> getTopTweet(Date from,Date to,int count) throws UnknownHostException { 

		DBObject query;
		DBObject fields;
		String message = "";
		DBCursor cursor; 
		DBCollection collection; 
		DBObject orderby; 
		Set<String> message_set = new HashSet<String>();

		MongoBase mongo = new MongoBase();
		mongo.setCollection(product_name);
		collection = mongo.getCollection();  

		query = new BasicDBObject("Channel","Twitter").append("TimeStamp", new BasicDBObject("$gte",from).append("$lte",to));
		fields = new BasicDBObject("Message",1).append("RetweetCount", 1);
		orderby = new BasicDBObject("RetweetCount",-1);

		cursor = collection.find(query, fields);
		cursor = cursor.sort(orderby);

		while (cursor.hasNext() && message_set.size() <= count) { 

			message = (String) cursor.next().get("Message");
			message_set.add(message);
		}

		mongo.closeConnection();
		return message_set;
	}

	/** 
	 * Returns a distribution of devices used to post on Twitter
	 * @param from Starting date from which the devices have to be fetched
	 * @param to Ending date till which the devices used have to be fetched
	 * @return TreeMap<String,Integer> containing the device and the device count
	 * @throws UnknownHostException
	 */ 

	public TreeMap<String,Integer> getTweetDevices(String from, String to) throws UnknownHostException
	{
		MongoBase mongo = new MongoBase();
		mongo.setCollection(product_name);
		Date from_date = DateConverter.getJavaDate(from);
		Date to_date = DateConverter.getJavaDate(to); 

		DBCollection collection = mongo.getCollection();
		DBObject query;
		DBObject fields; 

		query = new BasicDBObject("Channel","Twitter").append("TimeStamp", new BasicDBObject("$gte",from_date).append("$lte",to_date));
		fields = new BasicDBObject("DeviceUsed",1);  

		DBCursor cursor = collection.find(query,fields);
		TreeMap<String,Integer> tm = new TreeMap<String,Integer>(); 
		TreeMap<String,Integer> top_devices = new TreeMap<String,Integer>(); 

		String s = ""; 
		int index_1 = -1;
		int index_2 = -1;
		Object k; 

		try {

			while(cursor.hasNext()) { 

				BasicDBObject tweet_doc = (BasicDBObject) cursor.next(); 

				if ((k =  tweet_doc.get("DeviceUsed")) != null ) { 

					s = (String)(k);
					index_1 = s.indexOf('>');
					index_2 = s.lastIndexOf('<');

					if (index_1 != -1 && index_2 != -1) {

						s = s.substring(index_1+1, index_2);

						if (tm.get(s) != null) { 

							tm.put(s,tm.get(s)+1); 

						} else {

							tm.put(s, 1);
						}
					}
				}
			}

		} catch(Exception e) { 

			e.printStackTrace();
		}

		Set<String> key_list = tm.keySet();

		for(String str: key_list) {

			if (tm.get(str) >= 100) { 

				top_devices.put(str,tm.get(str));
			}
		}

		mongo.closeConnection(); 

		return top_devices;
	} 

	/** 
	 * Returns a Map of top tweet devices and the number of users of each device during a given time interval
	 * @param from Date representing the starting date
	 * @param to Date object representing the terminating date
	 * @return Map<String,Integer> containing the device name and its count
	 * @throws UnknownHostException
	 */ 

	public Map<String,Integer> getTweetDevices(Date from, Date to) throws UnknownHostException
	{
		MongoBase mongo = new MongoBase();
		mongo.setCollection(product_name);

		DBCollection collection = mongo.getCollection();
		DBObject query;
		DBObject fields; 

		query = new BasicDBObject("Channel","Twitter").append("TimeStamp", new BasicDBObject("$gte",from).append("$lte",to));
		fields = new BasicDBObject("DeviceUsed",1);  

		DBCursor cursor = collection.find(query,fields);
		TreeMap<String,Integer> tm = new TreeMap<String,Integer>(); 
		TreeMap<String,Integer> top_devices = new TreeMap<String,Integer>(); 

		String s = ""; 
		int index_1 = -1;
		int index_2 = -1;
		Object k; 

		try {

			while(cursor.hasNext()) { 

				BasicDBObject tweet_doc = (BasicDBObject) cursor.next(); 

				if ((k =  tweet_doc.get("DeviceUsed")) != null ) { 

					s = (String)(k);
					index_1 = s.indexOf('>');
					index_2 = s.lastIndexOf('<');

					if (index_1 != -1 && index_2 != -1) {

						s = s.substring(index_1+1, index_2);

						if (tm.get(s) != null) { 

							tm.put(s,tm.get(s)+1); 

						} else {

							tm.put(s, 1);
						}
					}
				}
			}

		} catch(Exception e) { 

			e.printStackTrace();
		} 

		Set<String> key_list = tm.keySet();

		for(String str: key_list) {

			if (tm.get(str) >= 100) { 

				top_devices.put(str,tm.get(str));
			}
		} 

		mongo.closeConnection(); 

		return top_devices;
	} 
	
	/** 
	 * 
	 * @param from
	 * @param to
	 * @param n
	 * @return
	 * @throws UnknownHostException
	 */ 
	
	public Map<String,Integer> getTweetDevices(Date from, Date to,int n) throws UnknownHostException
	{
		MongoBase mongo = new MongoBase();
		mongo.setCollection(product_name);

		DBCollection collection = mongo.getCollection();
		DBObject query;
		DBObject fields; 

		query = new BasicDBObject("Channel","Twitter").append("TimeStamp", new BasicDBObject("$gte",from).append("$lte",to));
		fields = new BasicDBObject("DeviceUsed",1);  

		DBCursor cursor = collection.find(query,fields);
		TreeMap<String,Integer> tm = new TreeMap<String,Integer>(); 
		TreeMap<String,Integer> top_devices = new TreeMap<String,Integer>(); 

		String s = ""; 
		int index_1 = -1;
		int index_2 = -1;
		Object k; 

		try {

			while(cursor.hasNext()) { 

				BasicDBObject tweet_doc = (BasicDBObject) cursor.next(); 

				if ((k =  tweet_doc.get("DeviceUsed")) != null ) { 

					s = (String)(k);
					index_1 = s.indexOf('>');
					index_2 = s.lastIndexOf('<');

					if (index_1 != -1 && index_2 != -1) {

						s = s.substring(index_1+1, index_2);

						if (tm.get(s) != null) { 

							tm.put(s,tm.get(s)+1); 

						} else {

							tm.put(s, 1);
						}
					}
				}
			}

		} catch(Exception e) { 

			e.printStackTrace();
		} 

		mongo.closeConnection();  

		Set<String> key_list = tm.keySet();

		for(String str: key_list) {

			if (tm.get(str) >= 100) { 

				top_devices.put(str,tm.get(str));
			}
		} 

		SortedSet<Map.Entry<String, Integer>> sorted_devices = SortUtil.entriesSortedByValues(top_devices);
		HashMap<String,Integer> final_map = new HashMap<String,Integer>();
		int count = 0;  
		
		for(Map.Entry<String, Integer> temp : sorted_devices) { 
			
			if (count < n) { 
				
				final_map.put(temp.getKey(),temp.getValue());
			}
			
			count++;
		}


		return final_map;
	} 

	/** 
	 * Returns a map of all devices used to post on twitter 
	 * @return TreeMap<String,String> containing the device and the number of posts on the device
	 * @throws UnknownHostException
	 */ 

	public TreeMap<String,Integer> getTweetDevices() throws UnknownHostException
	{
		MongoBase mongo = new MongoBase();
		mongo.setCollection(product_name);

		DBCollection collection = mongo.getCollection();
		DBCursor cursor = collection.find(new BasicDBObject("Channel","Twitter"), new BasicDBObject("DeviceUsed",1));
		TreeMap<String,Integer> tm = new TreeMap<String,Integer>(); 
		TreeMap<String,Integer> top_devices = new TreeMap<String,Integer>(); 

		String s = ""; 
		int index_1 = -1;
		int index_2 = -1;
		Object k; 

		try {

			while(cursor.hasNext()) { 

				BasicDBObject tweet_doc = (BasicDBObject) cursor.next(); 

				if ((k =  tweet_doc.get("DeviceUsed")) != null ) { 

					s = (String)(k);
					index_1 = s.indexOf('>');
					index_2 = s.lastIndexOf('<');

					if (index_1 != -1 && index_2 != -1) {

						s = s.substring(index_1+1, index_2);

						if (tm.get(s) != null) { 

							tm.put(s,tm.get(s)+1); 

						} else {

							tm.put(s, 1);
						}
					}
				}
			}

		} catch(Exception e) { 

			e.printStackTrace();
		}

		System.out.println("\n----------------- Top Devices Used to Post on Twitter --------------------\n");

		Set<String> key_list = tm.keySet();

		for(String str: key_list) {

			if (tm.get(str) >= 100) { 

				top_devices.put(str,tm.get(str));

				System.out.println(str + " : " + tm.get(str));
			}
		}

		mongo.closeConnection(); 

		return top_devices;
	}

	/** 
	 * Utility function to sort a map according to its values and not keys
	 * @param map Map which is to be sorted
	 * @return a SortedSet of the map which is input
	 */ 

	static <K,V extends Comparable<? super V>> SortedSet<Map.Entry<K,V>> entriesSortedByValues(Map<K,V> map) {
		SortedSet<Map.Entry<K,V>> sortedEntries = new TreeSet<Map.Entry<K,V>>(
				new Comparator<Map.Entry<K,V>>() {
					public int compare(Map.Entry<K,V> e1, Map.Entry<K,V> e2) {
						int res = e1.getValue().compareTo(e2.getValue());
						return res != 0 ? -res : 1; // Special fix to preserve items with equal values
					}
				}
				);
		sortedEntries.addAll(map.entrySet());
		return sortedEntries;
	} 

	/** 
	 * Returns the user location and usernames of people in the given interval of time
	 * @param from String date representing the starting date
	 * @param to String date representing the ending date
	 * @return TreeMap<String,String> containing the UserName and UserLocation
	 * @throws UnknownHostException
	 */ 

	public TreeMap<String,String> getUserBase(String from,String to) throws UnknownHostException
	{	
		Date from_date = DateConverter.getJavaDate(from);
		Date to_date = DateConverter.getJavaDate(to); 

		MongoBase mongo = new MongoBase();  
		mongo.setCollection(product_name); 
		DBCollection collection = mongo.getCollection();
		DBObject query;
		DBObject fields; 

		query = new BasicDBObject("Channel","Twitter").append("TimeStamp", new BasicDBObject("$gte",from_date).append("$lte",to_date));
		fields = new BasicDBObject("UserLocation",1).append("UserName",1); 

		Object location = null;
		DBCursor cursor = collection.find(query,fields);
		BasicDBObject tweet_doc;  

		String s = "";
		String username = ""; 
		TreeMap<String,String> tm = new TreeMap<String,String>(); 

		while (cursor.hasNext()) { 

			tweet_doc = (BasicDBObject) cursor.next();

			if ((location = tweet_doc.get("UserLocation")) != null)  { 

				s = location.toString();
				username = (String) tweet_doc.get("UserName");
				tm.put(username, s);
			}
		}  

		System.out.println("\n -------------------------------------- User Base and Geolocation ------------------------\n");

		for(Map.Entry<String, String> m: tm.entrySet()) { 

			System.out.println(m.getKey() + " : " + m.getValue());
		}

		mongo.closeConnection();
		return tm;
	} 

	/** 
	 * Returns a mapping between the username and user locations
	 * @param from Date representing the starting date
	 * @param to Date representing the ending date
	 * @return Map<String,String> containing the username and userlocation
	 * @throws UnknownHostException
	 */ 

	public Map<String,String> getUserBase(Date from,Date to) throws UnknownHostException
	{	
		MongoBase mongo = new MongoBase();  
		mongo.setCollection(product_name); 
		DBCollection collection = mongo.getCollection();
		DBObject query;
		DBObject fields; 

		query = new BasicDBObject("Channel","Twitter").append("TimeStamp", new BasicDBObject("$gte",from).append("$lte",to));
		fields = new BasicDBObject("UserLocation",1).append("UserName",1); 

		Object location = null;
		DBCursor cursor = collection.find(query,fields);
		BasicDBObject tweet_doc;  

		String s = "";
		String username = ""; 
		TreeMap<String,String> tm = new TreeMap<String,String>(); 

		while (cursor.hasNext()) { 

			tweet_doc = (BasicDBObject) cursor.next();

			if ((location = tweet_doc.get("UserLocation")) != null)  { 

				s = location.toString();
				username = (String) tweet_doc.get("UserName");
				tm.put(username, s);
			}
		}  

		mongo.closeConnection(); 

		return tm;
	}

	/** 
	 * Inserts the tweets in the GeoLocation database in the given time interval
	 * @param from Date representing the starting date
	 * @param to Date representing the ending date
	 * @throws IOException
	 */ 

	public void insertGeoLocation(Date from,Date to) throws IOException
	{	
		MongoBase mongo = new MongoBase();
		mongo.setDB("GeoLocation");
		mongo.setCollection("Central");
		Set<DBObject> tweet_set = getTweetSet(from,to); 
		CountryBase country;
		UserBase userbase = new UserBase(); 
		String location = "";
		Object loc_object = null; 
		String country_name = "";
		String country_code = ""; 

		for(DBObject tweet_doc : tweet_set) { 

			if ( (loc_object = tweet_doc.get("UserLocation")) != null) { 

				location = (String) loc_object;
				country_name = userbase.matchCountry(location);

				if (!country_name.isEmpty()) { 

					country_code = userbase.alpha_map.get(country_name);

					country = new CountryBase();

					country.setChannel((String) tweet_doc.get("Channel"));
					country.setUsername((String) tweet_doc.get("UserName"));
					country.setCountry(country_name);
					country.setCode(country_code);
					country.setTimestamp((Date) tweet_doc.get("TimeStamp"));
					country.setProduct(product_name);

					mongo.putInDB(country);
				}
			}
		}

		mongo.closeConnection();
	}
	
	/** 
	 * 
	 * @param from
	 * @param to
	 * @throws IOException
	 */ 
	
	public static void insertGeoLocationAllProducts(Date from,Date to) throws IOException { 
		
		String product_list [] = {"Photoshop","Acrobat","Illustrator","Dreamweaver","Lightroom","CreativeCloud","MarketingCloud"};
		
		for (String product : product_list) { 
			
			TweetAnalytics tweet = new TweetAnalytics(product);
			tweet.insertGeoLocation(from, to);
		}
	} 
	
	/** 
	 * Returns the mapping between the Country name and the number of users in the region
	 * @param from Date representing the starting date
	 * @param to Date representing the ending date
	 * @return Map<String,Integer> containing the mapping between the country code and the user count
	 * @throws UnknownHostException
	 */ 

	public Map<String,Integer> getGeoLocation(Date from,Date to) throws UnknownHostException { 

		MongoBase mongo = new MongoBase();
		mongo.setDB("GeoLocation");
		mongo.setCollection("Central");

		DBCollection collection;
		DBObject query;
		DBCursor cursor;
		Map<String,Integer> country_map = new HashMap<String,Integer>();
		String country = ""; 
		DBObject temp_doc = null; 

		query = new BasicDBObject("Channel","Twitter").append("Product", product_name).
				append("TimeStamp", new BasicDBObject("$gte",from).append("$lte",to)); 

		collection = mongo.getCollection();
		cursor = collection.find(query); 

		while(cursor.hasNext()) { 

			temp_doc = cursor.next(); 
			country = (String) temp_doc.get("Code");
			
			if (country.isEmpty()) {
				
				country = "US";
			}

			if (!country_map.containsKey(country)) { 

				country_map.put(country, 1); 

			} else { 

				int count = country_map.get(country);
				count++; 
				country_map.put(country, count);
			}
		}

		return country_map;
	} 

	/** 
	 * Returns a set of tweets from the database within a given time interval
	 * @param from Date object representing the starting date
	 * @param to Date object representing the ending date
	 * @return Set<DBObject> containing the set of tweets in the given time period
	 * @throws UnknownHostException
	 */ 

	public Set<DBObject> getTweetSet(Date from,Date to) throws UnknownHostException { 

		MongoBase mongo = new MongoBase();
		mongo.setCollection(product_name); 

		DBCollection collection;
		DBCursor cursor;
		DBObject query; 
		Set<DBObject> tweet_set;  

		collection = mongo.getCollection();
		query = new BasicDBObject("Channel","Twitter").append("TimeStamp", new BasicDBObject("$gte",from).append("$lte", to));
		cursor = collection.find(query);
		tweet_set = new HashSet<DBObject>();

		while(cursor.hasNext()) { 

			tweet_set.add(cursor.next());
		}

		mongo.closeConnection();

		return tweet_set;
	}

	/** 
	 * Returns the user base for a given product, i.e. The username and the geolocation
	 * @return TreeMap<String,String> containing the username and geolocation
	 * @throws UnknownHostException
	 */ 

	public TreeMap<String,String> getUserBase() throws UnknownHostException
	{
		MongoBase mongo = new MongoBase();  
		mongo.setCollection(product_name); 
		DBCollection collection = mongo.getCollection();
		Object location = null;
		DBCursor cursor = collection.find(new BasicDBObject("Channel","Twitter"));
		BasicDBObject tweet_doc;  

		String s = "";
		String username = ""; 
		TreeMap<String,String> tm = new TreeMap<String,String>(); 

		while (cursor.hasNext()) { 

			tweet_doc = (BasicDBObject) cursor.next();

			if ((location = tweet_doc.get("UserLocation")) != null)  { 

				s = location.toString();
				username = (String) tweet_doc.get("UserName");
				tm.put(username, s);
			}
		}  

		System.out.println("\n -------------------------------------- User Base and Geolocation ------------------------\n");

		for(Map.Entry<String, String> m: tm.entrySet()) { 

			System.out.println(m.getKey() + " : " + m.getValue());
		}

		mongo.closeConnection();
		return tm;
	}

	/** 
	 * Returns the association between the tweet and its retweet count
	 * @return HashMap<String,Integer> containing the mapping between the tweet and the retweet count
	 * @throws UnknownHostException
	 */ 

	@SuppressWarnings("unused")
	public HashMap<String,Integer> getRetweetCount() throws UnknownHostException { 

		MongoBase mongo = new MongoBase(); 
		mongo.setCollection(product_name); 
		DBCollection collection = mongo.getCollection(); 
		HashMap<String,Integer> tm = new HashMap<String,Integer>(); 
		Object o = null; 
		BasicDBObject doc;
		BasicDBObject fields = new BasicDBObject("Message",1).append("RetweetCount",1);
		BasicDBObject query = new BasicDBObject("Channel","Twitter"); 

		DBCursor cursor = collection.find(query,fields);
		cursor.sort(new BasicDBObject("RetweetCount",-1));

		while(cursor.hasNext()) { 

			doc = (BasicDBObject) cursor.next();

			if ( (o = doc.get("RetweetCount")) != null) { 

				tm.put(doc.getString("Message"),doc.getInt("RetweetCount"));
			}

			System.out.println(doc);
		}

		mongo.closeConnection();
		return tm;
	}

	/** 
	 * Sorts the tweets according to their Retweet count and returns them in a sorted order
	 * @return HashMap<String,Integer> containing the tweets and their corresponding Retweet counts
	 * @throws UnknownHostException
	 */ 

	public HashMap<String,Integer> getTopTweets() throws UnknownHostException { 

		MongoBase mongo = new MongoBase();
		mongo.setCollection(product_name);
		DBCollection collection = mongo.getCollection();
		HashMap<String,Integer> message_map = new HashMap<String,Integer>();
		BasicDBObject fields = new BasicDBObject("Message",1).append("RetweetCount",1);
		BasicDBObject query = new BasicDBObject("Channel","Twitter"); 

		DBCursor cursor = collection.find(query,fields);
		cursor.sort(new BasicDBObject("RetweetCount",-1));
		int c = 0;
		BasicDBObject tweet_doc; 

		while(cursor.hasNext() && c != 5) { 

			tweet_doc = (BasicDBObject)cursor.next(); 
			message_map.put(tweet_doc.getString("Message"), tweet_doc.getInt("RetweetCount"));
			System.out.println(tweet_doc.get("Message") + " : " + tweet_doc.get("RetweetCount"));

			c++;
		}

		mongo.closeConnection();
		return message_map;
	}

	/** 
	 * Returns the top N tweets of the given product
	 * @param top_count Integer specifying the number of top tweets to be included
	 * @return HashMap<String,Integer> of the top tweets
	 * @throws UnknownHostException
	 */ 

	public HashMap<String,Integer> getTopTweets(int top_count) throws UnknownHostException { 

		MongoBase mongo = new MongoBase();
		mongo.setCollection(product_name);
		DBCollection collection = mongo.getCollection();
		HashMap<String,Integer> hm  = new HashMap<String,Integer>();
		BasicDBObject fields = new BasicDBObject("Message",1).append("RetweetCount",1);
		BasicDBObject query = new BasicDBObject("Channel","Twitter"); 

		DBCursor cursor = collection.find(query,fields);
		cursor.sort(new BasicDBObject("RetweetCount",-1));
		int c = 0;
		BasicDBObject tweet_doc; 

		System.out.println("------------------------- Top Trending Tweets -------------------------"); 

		while(cursor.hasNext() && c <= top_count) { 

			tweet_doc = (BasicDBObject)cursor.next(); 
			System.out.println(tweet_doc.get("Message") + " : " + tweet_doc.get("RetweetCount")); 
			hm.put(tweet_doc.getString("Message"),tweet_doc.getInt("RetweetCount"));
			c++;
		} 

		System.out.println("---------------------------------------------------------------------------");

		mongo.closeConnection();
		return hm;
	}

	/** 
	 * Returns the unique tweets for a given product
	 * @return A set containing the unique tweets
	 * @throws UnknownHostException
	 */ 

	public Set<String> getUniqueTweets() throws UnknownHostException { 

		Set<String> unique_tweets = new HashSet<String>();
		BasicDBObject tweet_doc; 
		MongoBase mongo = new MongoBase();
		mongo.setCollection(product_name);
		DBCollection collection = mongo.getCollection();

		DBCursor cursor = collection.find(new BasicDBObject("Channel","Twitter"), new BasicDBObject("Message",1));

		while(cursor.hasNext()) { 

			tweet_doc = (BasicDBObject) cursor.next();
			unique_tweets.add((String) tweet_doc.get("Message"));
		}

		System.out.println("-------------------Unique Tweets---------------------------");

		for(String s: unique_tweets) { 

			System.out.println(s);
		} 

		System.out.println("Total Number of Unique Tweets :" + unique_tweets.size()); 

		System.out.println("------------------------------------------------------------");

		mongo.closeConnection();
		return unique_tweets;
	}

	/** 
	 * Prints the tweets to the standard output in a pretty fashion
	 * @throws UnknownHostException
	 */

	public void printTweets() throws UnknownHostException { 

		MongoBase mongo = new MongoBase();
		mongo.setCollection(product_name);
		DBCollection collection = mongo.getCollection();

		BasicDBObject fields = new BasicDBObject("UserName",1).append("Message",1).
				append("TimeStamp", 1).append("RetweetCount", 1).
				append("DeviceUsed", 1).append("UserLocation", 1); 

		BasicDBObject query = new BasicDBObject("Channel","Twitter");
		DBCursor cursor = collection.find(query,fields);
		BasicDBObject tweet_doc;

		System.out.println("--------------------------------------------------------"); 

		while(cursor.hasNext()) { 

			System.out.println("--------------------------------------------------------");  

			try { 

				tweet_doc = (BasicDBObject) cursor.next();

				System.out.println("UserName : " + tweet_doc.get("UserName"));
				System.out.println("Message : " + tweet_doc.get("Message"));
				System.out.println("TimeStamp : " + tweet_doc.get("TimeStamp"));
				System.out.println("DeviceUsed : " + tweet_doc.get("DeviceUsed"));
				System.out.println("RetweetCount : " + tweet_doc.get("RetweetCount"));
				System.out.println("UserLocation : " + tweet_doc.get("UserLocation"));

				System.out.println("--------------------------------------------------------"); 

			} catch(Exception e) { 

				e.printStackTrace();
			}
		}

		System.out.println("Total Tweets of " + product_name + " : " + cursor.size());
		System.out.println("------------------------------------------------------------");

		mongo.closeConnection();
	}
}
