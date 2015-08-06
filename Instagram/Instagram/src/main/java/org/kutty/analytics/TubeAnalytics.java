package org.kutty.analytics;

import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.joda.time.DateTime;
import org.kutty.clean.Clean;
import org.kutty.db.MongoBase;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

/** 
 * Carries out Analytics on Youtube for retrieving the top contents in a given time interval
 * @author Rupak Chakraborty
 * @for Kutty
 * @since 19 July, 2015
 */ 

public class TubeAnalytics {
	
	public String product;
	public Date from;
	public Date to; 
	
	/** 
	 * public constructor to intialize the Youtube Analytics object with product name
	 * @param product String containing the product name
	 */ 
	
	public TubeAnalytics(String product) { 
		
		this.product = product;
	}
	
	/** 
	 * default public constructor to initialize the YoutubeAnalytics object
	 */ 
	
	public TubeAnalytics() { 
		
	} 
	
	/** 
	 * public constructor to initialize the object with product name, starting and ending dates
	 * @param product String containing the product name
	 * @param from Date object containing the starting date
	 * @param to Date object containing the ending date
	 */ 
	
	public TubeAnalytics(String product,Date from,Date to) { 
		
		this.product = product;
		this.from = from;
		this.to = to; 
	} 
	
	/** 
	 * Returns a set of top comments for a given product
	 * @param product String containing the product name
	 * @param from Date object containing the starting date
	 * @param to Date object containing the ending date
	 * @param n Integer containing the number of comments to be returned
	 * @return Set<String> containing the top comments
	 * @throws UnknownHostException
	 */ 
	
	public Set<String> getTopComments(String product,Date from,Date to,int n) throws UnknownHostException { 
		
		Set<String> comment_set = new HashSet<String>();
		
		MongoBase mongo = new MongoBase();
		mongo.setCollection(product);
		
		DBObject query;
		DBObject sort;
		DBObject fields;
		DBObject temp_doc;
		DBCollection collection;
		DBCursor cursor;
		String message; 
		
		collection = mongo.getCollection();
		query = new BasicDBObject("Channel","Youtube").append("TimeStamp", new BasicDBObject("$gte",from).append("$lte",to));
		fields = new BasicDBObject("Message",1).append("LikeCount", 1).append("ReplyCount", 1).append("TimeStamp", 1);
		sort = new BasicDBObject("LikeCount",-1);
		
		cursor = collection.find(query, fields);
		cursor = cursor.sort(sort).limit(n);
		
		message = ""; 
		
		while(cursor.hasNext()) { 
			
			temp_doc = cursor.next();
			message = (String) temp_doc.get("Message");
			message = Clean.cleanHTML(message);
			message = Clean.removePunctuationAndJunk(message); 
			
			comment_set.add(message);
		}
		
		mongo.closeConnection(); 
		
		return comment_set;
	} 
	
	/** 
	 * Returns the most recent comments for a given product
	 * @param product String containing the product name
	 * @param n Integer containing the count of comments
	 * @return Set<String> containing the set of comments
	 * @throws UnknownHostException
	 */ 
	
	public Set<String> getMostRecentComment(String product,int n) throws UnknownHostException { 
		
		Set<String> comment_set = new HashSet<String>(); 
		
		MongoBase mongo = new MongoBase();
		mongo.setCollection(product);
		
		DBCollection collection;
		DBObject query;
		DBObject fields;
		DBObject sort;
		DBCursor cursor;
		DBObject temp_doc; 
		String message; 
		
		collection = mongo.getCollection();
		query = new BasicDBObject("Channel","Youtube");
		fields = new BasicDBObject("Message",1).append("TimeStamp", 1).append("Author",1);
		sort = new BasicDBObject("TimeStamp",-1);
		
		message = ""; 
		cursor = collection.find(query, fields);
		cursor = cursor.sort(sort).limit(n); 
		
		while(cursor.hasNext()) { 
			
			temp_doc = cursor.next();
			message = (String) temp_doc.get("Message");
			comment_set.add(message);
		}
		
		mongo.closeConnection();  
		
		return comment_set;
	}
	
	/** 
	 * Get a set of top videos for a given product
	 * @param product String containing the product name
	 * @param n Integer containing the number of videos to be returned
	 * @return Set<String> containing the top video list
	 * @throws UnknownHostException 
	 */ 
	
	public Set<String> getTopVideos(String product,int n) throws UnknownHostException { 
		
		Set<String> videoId_set = new HashSet<String>(); 
		MongoBase mongo = new MongoBase();
		mongo.setCollection(product);
		
		DBCollection collection;
		DBObject query;
		DBObject fields;
		DBObject sort;
		DBCursor cursor;
		DBObject temp_doc; 
		String videoId; 
		
		collection = mongo.getCollection();
		query = new BasicDBObject("Channel","Youtube");
		fields = new BasicDBObject("Message",1).append("TimeStamp", 1).append("VideoId",1).append("LikeCount", 1);
		sort = new BasicDBObject("LikeCount",-1);
		
		videoId = ""; 
		
		cursor = collection.find(query, fields);
		cursor = cursor.sort(sort).limit(n);
		
		while(cursor.hasNext()) { 
			
			temp_doc = cursor.next(); 
			videoId = (String) temp_doc.get("VideoId");
			videoId_set.add(videoId);
		}
		
		return videoId_set;
	}
}
