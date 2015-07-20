package analytics;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;

import org.joda.time.DateTime;

import utils.DateConverter;
import utils.LanguageDetector;

import com.cybozu.labs.langdetect.LangDetectException;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import db.MongoBase;

/** 
 * @author Rupak Chakraborty
 * @for Adobe Systems,India
 * @since 25 Feb, 2015
 * 
 * Base class for performing basic analytics on the facebook posts stored in MongoDB, the basic analytics include things like
 * Sorting content by date
 * Getting the top posts
 * Printing the content to Screen 
 * 
 */ 

public class FbAnalytics {

	HashMap<String,String> product_names = new HashMap<String,String>(); 
	String product_name;   

	/** 
	 * Public constructor which initializes the product name
	 * @param product_name String specifying the product for which the posts have to be retrieved
	 * @throws LangDetectException
	 * @throws IOException
	 */

	public FbAnalytics(String product_name) throws LangDetectException, IOException {
		String profilesFolderPath = "profiles";
		LanguageDetector.init(profilesFolderPath);
		//init("product_list.txt");

		this.product_name = product_name.toLowerCase();
		this.product_name = product_names.get(this.product_name);
	}

	public FbAnalytics() { 
		
	}

	/** 
	 * Initializes a HashMap containing the specific product name and collection name
	 * @param filename String specifying the file from which the product and its collection names are retrieved
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

	public static void main(String args[]) throws LangDetectException, IOException {

		FbAnalytics fb = new FbAnalytics("Acrobat");
		DateTime from = new DateTime("2015-01-01");
		DateTime to = new DateTime("2015-05-29");
		System.out.println(fb.getTopPosts(from.toDate(),to.toDate() , 1));
		//fb.printPosts();
		//fb.getMessages();
		//System.out.println(fb.getTopAuthorPosts(10));
		//fb.getPostsByDate();
	}

	/** 
	 * Prints the posts to screen
	 * @throws UnknownHostException
	 */ 

	public void printPosts() throws UnknownHostException { 

		MongoBase mongo = new MongoBase();
		mongo.setCollection(product_name);
		DBCollection collection = mongo.getCollection();

		BasicDBObject query = new BasicDBObject("Channel","Facebook");
		DBCursor cursor = collection.find(query);
		BasicDBObject facebook_post; 

		while(cursor.hasNext()) { 

			facebook_post = (BasicDBObject) cursor.next();

			System.out.println("----------------------------------------------------"); 

			System.out.println("UserName : " + facebook_post.get("UserName"));
			System.out.println("Message : " + facebook_post.get("Message"));
			System.out.println("TimeStamp : " + facebook_post.get("TimeStamp"));
			System.out.println("Likes : " + facebook_post.get("Likes"));
			System.out.println("Comments : " + facebook_post.get("Comments"));
			System.out.println("Shares : " + facebook_post.get("Shares"));

			System.out.println("-----------------------------------------------------");
		}

		System.out.println("Total Posts : " + cursor.size());
		mongo.closeConnection();
	} 

	/** 
	 * Returns the messages which are in English only
	 * @return ArrayList<String> of messages
	 * @throws UnknownHostException
	 * @throws LangDetectException
	 */ 

	public ArrayList<String> getMessages() throws UnknownHostException, LangDetectException { 

		ArrayList<String> messages = new ArrayList<String>();
		MongoBase mongo = new MongoBase();
		mongo.setCollection(product_name);
		DBCollection collection = mongo.getCollection();

		BasicDBObject facebook_post;
		BasicDBObject fields = new BasicDBObject("Message",1);
		BasicDBObject query = new BasicDBObject("Channel","Facebook");

		DBCursor cursor = collection.find(query, fields);
		Object generic_message;
		String message = ""; 

		while(cursor.hasNext()) { 

			facebook_post = (BasicDBObject) cursor.next();

			if ((generic_message = facebook_post.get("Message")) != null) {

				message = (String)generic_message;

				if(LanguageDetector.detect(message).equalsIgnoreCase("en")) {

					messages.add(message);
				}
			}

		}

		mongo.closeConnection(); 

		return messages;
	} 

	/** 
	 * Returns messages contained in a particular language only
	 * @param language String specifying the language whose posts have to be retrieved
	 * @return ArrayList<String> of the messages
	 * @throws UnknownHostException
	 * @throws LangDetectException
	 */ 

	public ArrayList<String> getMessages(String language) throws UnknownHostException, LangDetectException { 

		ArrayList<String> messages = new ArrayList<String>();
		MongoBase mongo = new MongoBase();
		mongo.setCollection(product_name);
		DBCollection collection = mongo.getCollection();

		BasicDBObject facebook_post;
		BasicDBObject fields = new BasicDBObject("Message",1);
		BasicDBObject query = new BasicDBObject("Channel","Facebook");

		DBCursor cursor = collection.find(query, fields);
		Object generic_message;
		String message = ""; 

		while(cursor.hasNext()) { 

			facebook_post = (BasicDBObject) cursor.next();

			if ((generic_message = facebook_post.get("Message")) != null) {

				message = (String)generic_message;

				if(LanguageDetector.detect(message).equalsIgnoreCase(language)) {

					messages.add(message);
				}
			}

		}

		mongo.closeConnection();
		return messages;
	} 

	/** 
	 * Prints the top posts on the screen the top posts are defined by the criteria that they have more shares,likes than others
	 * @throws LangDetectException
	 * @throws UnknownHostException
	 */ 

	public void getTopPosts() throws LangDetectException, UnknownHostException {

		Object generic_likes;
		Object generic_shares;
		Object generic_comments; 
		Object generic_message; 

		ArrayList<String> top_posts = new ArrayList<String>(); 
		String message = "";

		MongoBase mongo = new MongoBase();
		mongo.setCollection(product_name);
		DBCollection collection = mongo.getCollection();

		DBCursor cursor = collection.find(new BasicDBObject("Channel","Facebook"));
		BasicDBObject facebook_post; 

		System.out.println("--------------Posts With Likes/Shares/Comments-------------------"); 

		while(cursor.hasNext()) {

			facebook_post = (BasicDBObject) cursor.next();

			generic_likes = facebook_post.get("Likes");
			generic_shares = facebook_post.get("Shares");
			generic_comments = facebook_post.get("Comments");
			generic_message = facebook_post.get("Message"); 

			if (generic_likes != null || generic_shares != null || generic_comments != null) { 

				if (generic_message != null) { 

					message = (String)(generic_message);

					if (LanguageDetector.detect(message).equalsIgnoreCase("en")) { 

						top_posts.add(message);
						System.out.println(message);
					}
				}
			}
		}

		System.out.println("-----------------------------------------------------------------");
	}

	/** 
	 * Returns the top post on facebook for a given time period
	 * @param from String in standard format representing the starting date
	 * @param to String in standard format representing the ending date
	 * @return The post with the maximum number of shares
	 * @throws UnknownHostException
	 */ 

	public String getTopPosts(String from, String to)throws UnknownHostException { 

		Date from_date = DateConverter.getJavaDate(from);
		Date to_date = DateConverter.getJavaDate(to);
		MongoBase mongo = new MongoBase();

		mongo.setCollection(product_name);
		DBCollection collection;
		DBCursor cursor;
		DBObject query;
		DBObject sort;

		String top_message = "";
		collection = mongo.getCollection();
		query = new BasicDBObject("Channel","Facebook").append("Shares", new BasicDBObject("$gt",0)).append("TimeStamp", new BasicDBObject("$gte",from_date).append("$lte",to_date));
		sort = new BasicDBObject("Shares",-1);

		cursor = collection.find(query);
		cursor = cursor.sort(sort);

		if (cursor.hasNext()) { 

			top_message = (String) cursor.next().get("Message");
		}

		mongo.closeConnection(); 

		return top_message;
	} 

	/** 
	 * Extracts the ton N posts from Facebook for a given time period
	 * @param from String representing the starting date
	 * @param to String representing the ending date
	 * @param n Integer limiting the number of posts
	 * @return Set<String> containing the top N posts
	 * @throws UnknownHostException
	 */ 

	public Set<String> getTopPosts(String from, String to,int n)throws UnknownHostException { 

		Date from_date = DateConverter.getJavaDate(from);
		Date to_date = DateConverter.getJavaDate(to);
		MongoBase mongo = new MongoBase();

		mongo.setCollection(product_name);
		DBCollection collection;
		DBCursor cursor;
		DBObject query;
		DBObject sort;

		String top_message = "";
		Set<String> message_set = new HashSet<String>(); 

		collection = mongo.getCollection();
		query = new BasicDBObject("Channel","Facebook").append("Shares", new BasicDBObject("$gt",0)).append("TimeStamp", new BasicDBObject("$gte",from_date).append("$lte",to_date));
		sort = new BasicDBObject("Shares",-1);

		cursor = collection.find(query);
		cursor = cursor.sort(sort);

		while (cursor.hasNext() && message_set.size() <= n) { 

			top_message = (String) cursor.next().get("Message");
			message_set.add(top_message);
		}

		mongo.closeConnection(); 

		return message_set;
	} 
	
	/** 
	 * Returns a Set<String> containing the posts in a given time interval 
	 * @param from java.util.Date object representing the starting point in time
	 * @param to java.util.Date object representing the ending point in time
	 * @param n Integer representing the number of posts to be fetched
	 * @return Set<String> containing the posts
	 * @throws UnknownHostException
	 */ 
	
	public Set<String> getTopPosts(Date from, Date to,int n)throws UnknownHostException { 

		MongoBase mongo = new MongoBase();

		mongo.setCollection(product_name);
		DBCollection collection;
		DBCursor cursor;
		DBObject query;
		DBObject sort;

		String top_message = "";
		Set<String> message_set = new HashSet<String>(); 

		collection = mongo.getCollection();
		query = new BasicDBObject("Channel","Facebook").append("Shares", new BasicDBObject("$gt",0)).append("TimeStamp", new BasicDBObject("$gte",from).append("$lte",to));
		sort = new BasicDBObject("Shares",-1);

		cursor = collection.find(query);
		cursor = cursor.sort(sort);

		while (cursor.hasNext() && message_set.size() <= n) { 

			top_message = (String) cursor.next().get("Message");
			message_set.add(top_message);
		}

		mongo.closeConnection(); 

		return message_set;
	} 

	/** 
	 * Returns the top N posts on Facebook
	 * @param n Integer specifying the top N number of posts to be retrieved
	 * @return Set<String> containing the top messages
	 * @throws UnknownHostException
	 */ 

	public Set<String> getTopPosts(int n)throws UnknownHostException { 

		MongoBase mongo = new MongoBase();

		mongo.setCollection(product_name);
		DBCollection collection;
		DBCursor cursor;
		DBObject query;
		DBObject sort;
		DBObject doc;
		int count; 

		Set<String> top_messages = new HashSet<String>();
		collection = mongo.getCollection();
		query = new BasicDBObject("Channel","Facebook").append("Shares", new BasicDBObject("$gt",0));
		sort = new BasicDBObject("Shares",-1);
		count = 0;
		cursor = collection.find(query);
		cursor = cursor.sort(sort);

		while(cursor.hasNext()) { 

			doc = cursor.next();

			if (count < n) { 

				top_messages.add((String) doc.get("Message")); 

			} else { 

				break;
			}

			count++;
		}

		mongo.closeConnection(); 

		return top_messages;
	}

	/** 
	 * Returns an association between the Date and its corressponding message
	 * @return TreeMap<Date,String> containing the Date and its associated post
	 * @throws UnknownHostException
	 * @throws LangDetectException
	 */ 

	public TreeMap<Date,String> getPostsByDate() throws UnknownHostException, LangDetectException { 

		TreeMap<Date,String> tm = new TreeMap<Date,String>();
		MongoBase mongo = new MongoBase();
		mongo.setCollection(product_name);
		DBCollection collection = mongo.getCollection();

		BasicDBObject fields = new BasicDBObject("Message",1).append("TimeStamp", 1);
		BasicDBObject query = new BasicDBObject("Channel","Facebook");
		BasicDBObject facebook_post;

		DBCursor cursor = collection.find(query, fields);

		while(cursor.hasNext()) { 

			facebook_post = (BasicDBObject) cursor.next();

			if( (facebook_post.get("Message")) != null && (facebook_post.get("TimeStamp")) != null) {

				if (LanguageDetector.detect(facebook_post.getString("Message")).equalsIgnoreCase("en")) { 

					tm.put(facebook_post.getDate("TimeStamp"), facebook_post.getString("Message"));
				}
			}
		}

		mongo.closeConnection(); 

		return tm;
	}

	/** 
	 * Returns a HashMap containing the author and message for top posts
	 * @param n Integer specifying the number of posts to be retrieved
	 * @return HashMap<String,String> containing the author and the associated post
	 * @throws UnknownHostException
	 */ 

	public HashMap<String,String> getTopAuthorPosts(int n) throws UnknownHostException { 

		MongoBase mongo = new MongoBase();
		mongo.setCollection(product_name);
		HashMap<String,String> author_map;
		int count; 

		DBCollection collection;
		DBCursor cursor;
		DBObject query;
		DBObject sort;
		DBObject doc; 

		author_map = new HashMap<String,String>();
		count = 0;
		collection = mongo.getCollection();
		query = new BasicDBObject("Channel","Facebook").append("Shares", new BasicDBObject("$gt",0));
		sort = new BasicDBObject("Shares",-1);

		cursor = collection.find(query);
		cursor = cursor.sort(sort);

		while(cursor.hasNext()) { 

			doc = cursor.next();

			if (count < n) { 

				author_map.put((String)doc.get("Author"), (String) doc.get("Message")); 

			} else { 

				break;
			}

			count++;
		}

		mongo.closeConnection(); 

		return author_map;
	}
}
