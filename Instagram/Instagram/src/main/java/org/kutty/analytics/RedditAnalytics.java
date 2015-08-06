package org.kutty.analytics;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.joda.time.DateTime;
import org.kutty.clean.Clean;
import org.kutty.db.MongoBase;
import org.kutty.utils.DateConverter;
import org.kutty.utils.LanguageDetector;

import com.cybozu.labs.langdetect.LangDetectException;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

/**
 * @author Rupak Chakraborty
 * @for Kutty
 * @since 19 July, 2015
 * 
 * Class for performing a set of analytics on the posts fetched from Reddit.com, it accesses the data stored in
 * MongoDB and performs basic analytic functions like sorting content by date and getting top voted content and most
 * commented upon posts. Additionally it can also filter the language by content if need be. In-fact there is a lot of
 * functionality lying latent it will be invoked as and when the need be.
 * 
 * TODO - Add logging support to all the classes System.out.println is not nice.
 */ 

public class RedditAnalytics {

	HashMap<String,String> product_names = new HashMap<String,String>(); 
	String product;  

	/**
	 * public constructor to initialize the product name and the associated collection name
	 * @param product String representing the product name which is initialized
	 * @throws LangDetectException
	 * @throws IOException
	 */

	public RedditAnalytics(String product) throws LangDetectException, IOException {
		
		String profile_path = "profiles";
		String product_list = "product_list.txt"; 
		
		LanguageDetector.init(profile_path);
		init(product_list); 

		this.product = product.toLowerCase();
		this.product = product_names.get(this.product);
	}
	
	public RedditAnalytics() { 
		
	} 
	
	/** 
	 * Initializes the product_names HashMap with the product names and collection names
	 * @param filename String representing the file from which these values are to be read
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
	 * Returns the content sorted according to Date
	 * @return HashMap<Date,String> containing the date and its associated content
	 * @throws UnknownHostException
	 */ 

	public HashMap<Double,String> sortContentByDate() throws UnknownHostException {

		System.out.println("----------------- Content Sorted Out By Date -------------------------------"); 

		MongoBase mongo = new MongoBase();
		mongo.setCollection(product);
		DBCollection collection;
		BasicDBObject reddit_doc; 
		HashMap<Double,String> hm = new HashMap<Double,String>();
		collection = mongo.getCollection();
		DBObject query = new BasicDBObject("Channel","Reddit");
		DBObject fields = new BasicDBObject("TimeStamp",1).append("Message", 1);

		DBCursor cursor = collection.find(query, fields);
		cursor = cursor.sort(new BasicDBObject("TimeStamp",-1));

		while(cursor.hasNext()) { 

			reddit_doc = (BasicDBObject) cursor.next();
			hm.put(reddit_doc.getDouble("TimeStamp"), reddit_doc.getString("Message"));
			System.out.println("--------------------------------------------------------");
			System.out.println(reddit_doc.get("TimeStamp") + " : " + reddit_doc.get("Message"));
			System.out.println("--------------------------------------------------------");
		}

		cursor.close();
		mongo.closeConnection(); 

		return hm;
	}

	/** 
	 * Returns the HashMap containing the mapping between the content and the score
	 * @return HashMap<String,Integer> containing the messages sorted according to score
	 * @throws UnknownHostException
	 */ 

	public HashMap<String,Integer> sortContentByScore() throws UnknownHostException {

		System.out.println("-------------- Content Sorted By Score ---------------------------"); 

		MongoBase mongo = new MongoBase();
		mongo.setCollection(product);
		DBCollection collection;
		BasicDBObject reddit_doc; 
		HashMap<String,Integer> hm = new HashMap<String,Integer>();
		collection = mongo.getCollection();
		DBObject query = new BasicDBObject("Channel","Reddit");
		DBObject fields = new BasicDBObject("TimeStamp",1).append("Message", 1).append("Score", 1);

		DBCursor cursor = collection.find(query, fields);
		cursor = cursor.sort(new BasicDBObject("Score",-1));

		while(cursor.hasNext()) { 

			reddit_doc = (BasicDBObject) cursor.next(); 
			hm.put(reddit_doc.getString("Message"),reddit_doc.getInt("Score"));
			System.out.println("--------------------------------------------------------");
			System.out.println(reddit_doc.get("Score") + " : " + reddit_doc.get("Message"));
			System.out.println("--------------------------------------------------------");
		}

		cursor.close();
		mongo.closeConnection(); 

		return hm;
	} 

	/** 
	 * Returns a HashMap with the top 10 content based on the score count
	 * @return HashMap<String,Integer> containing the title of the post and the score
	 * @throws UnknownHostException
	 */ 

	public HashMap<String,Integer> getTopContent() throws UnknownHostException {

		System.out.println("----------------------- Top Ten Content ------------------------"); 

		MongoBase mongo = new MongoBase();
		mongo.setCollection(product);
		DBCollection collection;
		BasicDBObject reddit_doc; 
		HashMap<String,Integer> hm = new HashMap<String,Integer>();
		collection = mongo.getCollection();
		DBObject query = new BasicDBObject("Channel","Reddit");

		DBCursor cursor = collection.find(query);
		cursor = cursor.sort(new BasicDBObject("Score",-1)).limit(10); 

		while (cursor.hasNext()) { 

			reddit_doc = (BasicDBObject) cursor.next(); 
			hm.put(reddit_doc.getString("Title"), reddit_doc.getInt("Score"));
			System.out.println("--------------------------------------------------------");
			System.out.println(reddit_doc.get("Title") + " : " + reddit_doc.get("Score"));
			System.out.println("--------------------------------------------------------");
		}

		cursor.close();
		mongo.closeConnection();

		return hm;
	} 

	/** 
	 * Returns the top N content of the given channel
	 * @param limit Integer limiting the number of top content of the given channel
	 * @return HashMap<String,Integer> containing the title of the post and the associated score
	 * @throws UnknownHostException
	 */ 

	public HashMap<String,Integer> getTopContent(int limit) throws UnknownHostException {

		System.out.println("----------------------- Top Content ------------------------"); 

		MongoBase mongo = new MongoBase();
		mongo.setCollection(product);
		DBCollection collection;
		BasicDBObject reddit_doc; 
		HashMap<String,Integer> hm = new HashMap<String,Integer>();
		collection = mongo.getCollection();
		DBObject query = new BasicDBObject("Channel","Reddit");

		DBCursor cursor = collection.find(query);
		cursor = cursor.sort(new BasicDBObject("Score",-1)).limit(limit); 

		while (cursor.hasNext()) { 

			reddit_doc = (BasicDBObject) cursor.next(); 
			hm.put(reddit_doc.getString("Title"), reddit_doc.getInt("Score"));
			System.out.println("--------------------------------------------");
			System.out.println(reddit_doc.get("Title") + " : " + reddit_doc.get("Score"));
			System.out.println("--------------------------------------------");
		}

		cursor.close();
		mongo.closeConnection();

		return hm;
	}  
	
	/** 
	 * Returns the top post from the given channel 
	 * @param from String representing the starting date
	 * @param to String representing the ending date
	 * @return The post with the maximum score
	 * @throws UnknownHostException
	 */ 
	
	public String getTopPost(String from, String to) throws UnknownHostException { 

		String message = "";
		MongoBase mongo = new MongoBase();
		mongo.setCollection(product);
		DBCollection collection;
		DBCursor cursor;
		DBObject query; 
		DBObject orderby; 
		DBObject fields;  

		double from_date = DateConverter.getJulianDate(from);
		double to_date = DateConverter.getJulianDate(to); 

		collection = mongo.getCollection();
		query = new BasicDBObject("Channel","Reddit").append("TimeStamp", new BasicDBObject("$gte",from_date).append("$lte",to_date));
		orderby = new BasicDBObject("Score",-1);
		fields = new BasicDBObject("Message",1).append("Score", 1);

		cursor = collection.find(query, fields);
		cursor = cursor.sort(orderby);

		if (cursor.hasNext()) { 

			message = (String) cursor.next().get("Message");
		}
		
		cursor.close();
		mongo.closeConnection(); 
		
		return message;
	} 
	
	/** 
	 * Returns the top posts sorted according to the score for a given time frame
	 * @param from Denotes the starting date of the posts
	 * @param to Represents the ending date of the posts
	 * @param n The total number of posts to be fetched
	 * @return Set<String> containing the top tops to be retrieved
	 * @throws UnknownHostException
	 */ 
	
	public Set<String> getTopPost(String from, String to,int n) throws UnknownHostException { 

		String message = "";
		MongoBase mongo = new MongoBase();
		mongo.setCollection(product);
		DBCollection collection;
		DBCursor cursor;
		DBObject query; 
		DBObject orderby; 
		DBObject fields;  
		Set<String> message_set = new HashSet<String>(); 
		
		double from_date = DateConverter.getJulianDate(from);
		double to_date = DateConverter.getJulianDate(to); 

		collection = mongo.getCollection();
		query = new BasicDBObject("Channel","Reddit").append("TimeStamp", new BasicDBObject("$gte",from_date).append("$lte",to_date));
		orderby = new BasicDBObject("Score",-1);
		fields = new BasicDBObject("Message",1).append("Score", 1);

		cursor = collection.find(query, fields);
		cursor = cursor.sort(orderby);

		while (cursor.hasNext() && message_set.size() <= n) { 

			message = (String) cursor.next().get("Message");
			message = Clean.cleanHTML(message);
			message = Clean.removeURL(message);
			message = Clean.removePunctuationAndJunk(message);
			message_set.add(message);
		}
		
		cursor.close();
		mongo.closeConnection(); 
		
		return message_set;
	} 
	
	/** 
	 * Returns a Set<String> of the top posts received by a given channel during a given time period
	 * @param from Date representing the starting point of the time interval
	 * @param to Date representing the terminating point of the time interval
	 * @param n Integer N representing the top N posts
	 * @return Set<String> containing the top N posts
	 * @throws UnknownHostException
	 */ 
	
	public Set<String> getTopPost(Date from, Date to,int n) throws UnknownHostException { 

		String message = "";
		MongoBase mongo = new MongoBase();
		mongo.setCollection(product);
		DBCollection collection;
		DBCursor cursor;
		DBObject query; 
		DBObject orderby; 
		DBObject fields;  
		Set<String> message_set = new HashSet<String>(); 
		
		double from_date = DateConverter.getJulianDate(from);
		double to_date = DateConverter.getJulianDate(to); 

		collection = mongo.getCollection();
		query = new BasicDBObject("Channel","Reddit").append("TimeStamp", new BasicDBObject("$gte",from_date).append("$lte",to_date));
		orderby = new BasicDBObject("Score",-1);
		fields = new BasicDBObject("Message",1).append("Score", 1);

		cursor = collection.find(query, fields);
		cursor = cursor.sort(orderby);

		while (cursor.hasNext() && message_set.size() <= n) { 

			message = (String) cursor.next().get("Message");
			message = Clean.cleanHTML(message);
			message = Clean.removeURL(message);
			message = Clean.removePunctuationAndJunk(message);
			message_set.add(message);
		}
		
		cursor.close();
		mongo.closeConnection(); 
		
		return message_set;
	} 
	
	/** 
	 * Returns the posts sorted according to the comment count
	 * @return HashMap<String,Long> containing the posts sorted according to the comment count
	 * @throws UnknownHostException
	 */ 

	public HashMap<String,Long> getContentWithComments() throws UnknownHostException {

		System.out.println("----------------- Content With Comments -----------------------"); 

		MongoBase mongo = new MongoBase();
		mongo.setCollection(product);
		DBCollection collection;
		HashMap<String,Long> hm = new HashMap<String,Long>(); 

		collection = mongo.getCollection();
		DBObject match = new BasicDBObject("$match",new BasicDBObject("Channel","Reddit").append("CommentCount", new BasicDBObject("$gt",5)));
		DBObject project = new BasicDBObject("$project",new BasicDBObject("Title",1).append("Message", 1).append("CommentCount",1));
		DBObject sort = new BasicDBObject("$sort",new BasicDBObject("CommentCount",-1));

		List<DBObject> pipeline = Arrays.asList(match,project,sort);
		AggregationOutput output = collection.aggregate(pipeline);

		for(DBObject result: output.results()) { 

			hm.put((String)result.get("Title"), (Long)result.get("CommentCount"));
			System.out.println("----------------------------------------");
			System.out.println(result.get("Title") + " : " + result.get("CommentCount"));
			System.out.println("-----------------------------------------");
		}
		
		mongo.closeConnection(); 

		return hm;
	} 

	/** 
	 * Returns the content title sorted according to the comment count
	 * @param limit Integer limiting the number of posts to be fetched
	 * @return HashMap<String,Long> containing the title of the post and its associated comment count
	 * @throws UnknownHostException
	 */ 

	public HashMap<String,Long> getContentWithComments(int limit) throws UnknownHostException {

		System.out.println("----------------- Content With Comments -----------------------"); 

		MongoBase mongo = new MongoBase();
		mongo.setCollection(product);
		DBCollection collection;
		HashMap<String,Long> hm = new HashMap<String,Long>(); 
		int count = 0; 

		collection = mongo.getCollection();
		DBObject match = new BasicDBObject("$match",new BasicDBObject("Channel","Reddit").append("CommentCount", new BasicDBObject("$gt",5)));
		DBObject project = new BasicDBObject("$project",new BasicDBObject("Title",1).append("Message", 1).append("CommentCount",1));
		DBObject sort = new BasicDBObject("$sort",new BasicDBObject("CommentCount",-1));

		List<DBObject> pipeline = Arrays.asList(match,project,sort);
		AggregationOutput output = collection.aggregate(pipeline);

		for(DBObject result: output.results()) { 

			if (count < limit) { 

				hm.put((String)result.get("Title"), (Long)result.get("CommentCount"));
				System.out.println("----------------------------------------");
				System.out.println(result.get("Title") + " : " + result.get("CommentCount"));
				System.out.println("-----------------------------------------"); 

			} else {  

				break;
			}

			count++;
		}

		mongo.closeConnection(); 

		return hm;
	}
}
