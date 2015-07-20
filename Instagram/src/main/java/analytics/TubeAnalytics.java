package analytics;

import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.joda.time.DateTime;

import clean.Clean;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import db.MongoBase;

/** 
 * Carries out Analytics on Youtube for retrieving the top contents in a given time interval
 * @author Rupak Chakraborty
 * @for Adobe Systems, India
 * @since 18 June, 2015
 */
public class TubeAnalytics {
	
	public String product;
	public Date from;
	public Date to; 
	
	/** 
	 * 
	 * @param product
	 */ 
	
	public TubeAnalytics(String product) { 
		
		this.product = product;
	}
	
	/** 
	 * 
	 */ 
	
	public TubeAnalytics() { 
		
	} 
	
	/** 
	 * 
	 * @param product
	 * @param from
	 * @param to
	 */ 
	
	public TubeAnalytics(String product,Date from,Date to) { 
		
		this.product = product;
		this.from = from;
		this.to = to; 
	} 
	
	/** 
	 * 
	 * @param product
	 * @param from
	 * @param to
	 * @param n
	 * @return
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
	 * 
	 * @param product
	 * @param n
	 * @return
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
	 * 
	 * @param product
	 * @param n
	 * @return
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
	
	/** 
	 * 
	 * @param args
	 * @throws UnknownHostException
	 */ 
	
	public static void main(String args[]) throws UnknownHostException { 
		
		TubeAnalytics analytics = new TubeAnalytics(); 
		DateTime from = new DateTime("2015-01-01");
		DateTime to = new DateTime("2015-06-03");
		
		System.out.println(analytics.getMostRecentComment("Photoshop", 5));
	}
}
