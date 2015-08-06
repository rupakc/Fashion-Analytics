package org.kutty.analytics;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.joda.time.DateTime;
import org.kutty.db.MongoBase;
import org.kutty.utils.DateConverter;
import org.kutty.utils.ListConverter;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

/** 
 * Performs basic analytics on the Instagram tags and associated content stored in the db
 * @author Rupak Chakraborty
 * @for Kutty 
 * @since 18 July, 2015 
 *  
 *  TODO - Add latitude,longitude and user analytics
 */ 

public class InstagramAnalytics {

	public static String product_name;
	public static String collection_name = "Fashion";
	public static Map<String,String> collection_names = new HashMap<String,String>();
	
	/** 
	 * Static block to initialize the product list and collection name
	 */ 
	
	static { 

		try {
			init("product_list.txt",collection_names);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/** 
	 * Utility function to initialize the list of fashion brands and their associated collection names
	 * @param filename String containing the filename from which the data is to be read
	 * @throws IOException
	 */ 

	public static void init(String filename,Map<String,String> name_map) throws IOException
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
				name_map.put(alias, collection_name); 
			}
		}

		br.close();
		fr.close();
	}  
	
	/** 
	 * Returns a set of most recent tag captions for a given product and a given time interval 
	 * @param product_name String containing the product name which is to be searched
	 * @param from Date containing the starting date
	 * @param to Date containing the ending date
	 * @param n Integer containing the number of results to be returned
	 * @return Set<String> containing the set of most recent caption associated with a tag
	 */ 

	public static Set<String> getMostRecentTagCaptions(String product_name,Date from,Date to,int n) { 

		double from_date = DateConverter.getJulianDate(from);
		double to_date = DateConverter.getJulianDate(to);
		Set<String> tag_set = new HashSet<String>();
		DBCollection collection;
		DBCursor cursor;
		DBObject query;
		BasicDBList query_list;
		DBObject temp;
		MongoBase mongo = null ; 
		collection_name = collection_names.get(product_name.toLowerCase().trim()); 

		query_list = new BasicDBList();
		query_list.add(new BasicDBObject("Type", "tag"));
		query_list.add(new BasicDBObject("Type", "image"));
		query_list.add(new BasicDBObject("Type", "video"));

		query = new BasicDBObject("Channel","Instagram").append("$or", query_list).
				append("Timestamp", new BasicDBObject("$gte",from_date).append("$lte", to_date));

		try { 

			mongo = new MongoBase();
			mongo.setCollection(collection_name);
			collection = mongo.getCollection(); 
			cursor = collection.find(query); 
			cursor = cursor.sort(new BasicDBObject("Timestamp",-1)).limit(n); 

			while (cursor.hasNext()) { 

				temp = cursor.next();
				tag_set.add((String) temp.get("CaptionText"));
			} 
		} catch (Exception e) { 
			e.printStackTrace();
		} finally { 
			mongo.closeConnection();
		}

		return tag_set;
	}

	/** 
	 * Returns the caption text of a set of most liked tags during a given time interval
	 * @param product_name String containing the product name
	 * @param from Date containing the starting date
	 * @param to Date containing the ending date
	 * @param n Integer containing the number of results to be fetched
	 * @return Set<String> containing the caption text of most liked posts
	 */ 

	public static Set<String> getMostLikedTags(String product_name,Date from,Date to,int n) { 

		double from_date = DateConverter.getJulianDate(from);
		double to_date = DateConverter.getJulianDate(to);
		Set<String> tag_set = new HashSet<String>();
		DBCollection collection;
		DBCursor cursor;
		DBObject query;
		DBObject fields;
		BasicDBList query_list;
		DBObject temp;
		MongoBase mongo = null; 
		collection_name = collection_names.get(product_name.toLowerCase().trim()); 

		query_list = new BasicDBList();
		query_list.add(new BasicDBObject("Type", "tag"));
		query_list.add(new BasicDBObject("Type", "image"));
		query_list.add(new BasicDBObject("Type", "video"));

		query = new BasicDBObject("Channel","Instagram").append("$or", query_list).
				append("Timestamp", new BasicDBObject("$gte",from_date).append("$lte", to_date)); 

		fields = new BasicDBObject("CaptionText",1).append("LikeCount", 1).append("Timestamp", 1);

		try { 

			mongo = new MongoBase();
			mongo.setCollection(collection_name);
			collection = mongo.getCollection(); 
			cursor = collection.find(query,fields); 
			cursor = cursor.sort(new BasicDBObject("LikeCount",-1)).limit(n); 

			while (cursor.hasNext()) { 

				temp = cursor.next();
				System.out.println(temp.get("LikeCount"));
				tag_set.add((String) temp.get("CaptionText"));
			} 

		} catch (Exception e) { 
			e.printStackTrace();
		} finally { 
			mongo.closeConnection();
		}

		return tag_set;
	}

	/** 
	 * Returns a set of caption text corresponding to most liked tags
	 * @param product_name String containing the product name
	 * @param from Date containing the starting date
	 * @param to Date containing the ending date
	 * @param n Integer containing the number of top comments
	 * @return Set<String> containing the caption text of the top commented posts
	 */ 

	public static Set<String> getMostCommentedTags(String product_name,Date from,Date to,int n) { 

		double from_date = DateConverter.getJulianDate(from);
		double to_date = DateConverter.getJulianDate(to);
		Set<String> tag_set = new HashSet<String>();
		DBCollection collection;
		DBCursor cursor;
		DBObject query;
		DBObject fields;
		BasicDBList query_list;
		DBObject temp;
		MongoBase mongo = null; 
		collection_name = collection_names.get(product_name.toLowerCase().trim()); 

		query_list = new BasicDBList();
		query_list.add(new BasicDBObject("Type", "tag"));
		query_list.add(new BasicDBObject("Type", "image"));
		query_list.add(new BasicDBObject("Type", "video"));

		query = new BasicDBObject("Channel","Instagram").append("$or", query_list).
				append("Timestamp", new BasicDBObject("$gte",from_date).append("$lte", to_date)); 

		fields = new BasicDBObject("CaptionText",1).append("CommentCount", 1).append("Timestamp", 1);

		try { 

			mongo = new MongoBase();
			mongo.setCollection(collection_name);
			collection = mongo.getCollection(); 
			cursor = collection.find(query,fields); 
			cursor = cursor.sort(new BasicDBObject("CommentCount",-1)).limit(n); 

			while (cursor.hasNext()) { 

				temp = cursor.next();
				tag_set.add((String) temp.get("CaptionText"));
			} 

		} catch (Exception e) { 
			e.printStackTrace();
		} finally { 
			mongo.closeConnection();
		}

		return tag_set;
	} 
	
	/** 
	 * Returns a set of caption text corresponding to most commented tags
	 * @param product_name String containing the product name
	 * @param from Date containing the starting date
	 * @param to Date containing the ending date
	 * @param n Integer containing the number of top comments
	 * @return Set<String> containing the caption text of the top commented posts
	 */ 
	
	public static Set<String> getMostPopularTagLinks(String product_name,Date from,Date to,int n) { 

		double from_date = DateConverter.getJulianDate(from);
		double to_date = DateConverter.getJulianDate(to);
		Set<String> tag_set = new HashSet<String>();
		DBCollection collection;
		DBCursor cursor;
		DBObject query;
		DBObject fields;
		BasicDBList query_list;
		DBObject temp;
		MongoBase mongo = null; 
		collection_name = collection_names.get(product_name.toLowerCase().trim()); 

		query_list = new BasicDBList();
		query_list.add(new BasicDBObject("Type", "tag"));
		query_list.add(new BasicDBObject("Type", "image"));
		query_list.add(new BasicDBObject("Type", "video"));

		query = new BasicDBObject("Channel","Instagram").append("$or", query_list).
				append("Timestamp", new BasicDBObject("$gte",from_date).append("$lte", to_date)); 

		fields = new BasicDBObject("CaptionText",1).append("CommentCount", 1).append("Timestamp", 1).
				append("Link", 1).append("LikeCount", 1);

		try { 

			mongo = new MongoBase();
			mongo.setCollection(collection_name);
			collection = mongo.getCollection(); 
			cursor = collection.find(query,fields); 
			cursor = cursor.sort(new BasicDBObject("LikeCount",-1)).limit(n); 

			while (cursor.hasNext()) { 

				temp = cursor.next();
				tag_set.add((String) temp.get("Link"));
			} 

		} catch (Exception e) { 
			e.printStackTrace();
		} finally { 
			mongo.closeConnection();
		}

		return tag_set;
	}  
	
	/** 
	 * Returns a set of most popular image links
	 * @param product_name String containing the product name
	 * @param from Date containing the starting date
	 * @param to Date containing the ending date
	 * @param n Integer containing number of top links to display
	 * @return Set<String> containing the most poplular image links
	 */ 
	
	public static Set<ArrayList<String>> getMostPopularTags(String product_name,Date from,Date to,int n) {

		double from_date = DateConverter.getJulianDate(from);
		double to_date = DateConverter.getJulianDate(to);
		Set<ArrayList<String>> tag_set = new HashSet<ArrayList<String>>();
		ArrayList<String> temp_tag; 
		DBCollection collection;
		DBCursor cursor;
		DBObject query;
		DBObject fields;
		BasicDBList query_list;
		BasicDBList tag_list; 
		MongoBase mongo = null; 
		collection_name = collection_names.get(product_name.toLowerCase().trim()); 

		query_list = new BasicDBList();
		query_list.add(new BasicDBObject("Type", "tag"));
		query_list.add(new BasicDBObject("Type", "image"));
		query_list.add(new BasicDBObject("Type", "video"));

		query = new BasicDBObject("Channel","Instagram").append("$or", query_list).
				append("Timestamp", new BasicDBObject("$gte",from_date).append("$lte", to_date)); 

		fields = new BasicDBObject("TagSet",1).append("LikeCount",1);
		
		try { 
			
			mongo = new MongoBase();
			mongo.setCollection(collection_name); 
			collection = mongo.getCollection(); 
			cursor = collection.find(query, fields); 
			
			while(cursor.hasNext()) { 
				
				tag_list = (BasicDBList) cursor.next().get("TagSet");
				temp_tag = ListConverter.getArrayListString(tag_list);
				tag_set.add(temp_tag);
			}
			
		} catch (Exception e) { 
			e.printStackTrace();
		} finally { 
			mongo.closeConnection();
		}
		
		return tag_set;
	} 
	
	/** 
	 * Returns a set of most popular image links
	 * @param product_name String containing the product name
	 * @param from Date containing the starting date
	 * @param to Date containing the ending date
	 * @param n Integer containing number of top links to display
	 * @return Set<String> containing the most poplular image links
	 */ 
	
	public static Set<String> getMostPopularImageLinks(String product_name,Date from,Date to,int n) { 

		double from_date = DateConverter.getJulianDate(from);
		double to_date = DateConverter.getJulianDate(to);
		Set<String> tag_set = new HashSet<String>();
		DBCollection collection;
		DBCursor cursor;
		DBObject query;
		DBObject fields;
		BasicDBList query_list;
		DBObject temp;
		MongoBase mongo = null; 
		collection_name = collection_names.get(product_name.toLowerCase().trim()); 

		query_list = new BasicDBList();
		query_list.add(new BasicDBObject("Type", "tag"));
		query_list.add(new BasicDBObject("Type", "image"));
		query_list.add(new BasicDBObject("Type", "video"));

		query = new BasicDBObject("Channel","Instagram").append("$or", query_list).
				append("Timestamp", new BasicDBObject("$gte",from_date).append("$lte", to_date)); 

		fields = new BasicDBObject("ImageLink",1).append("LikeCount", 1);

		try { 

			mongo = new MongoBase();
			mongo.setCollection(collection_name);
			collection = mongo.getCollection(); 
			cursor = collection.find(query,fields); 
			cursor = cursor.sort(new BasicDBObject("LikeCount",-1)).limit(n); 

			while (cursor.hasNext()) { 

				temp = cursor.next();
				tag_set.add((String) temp.get("ImageLink"));
			} 

		} catch (Exception e) { 
			e.printStackTrace();
		} finally { 
			mongo.closeConnection();
		}

		return tag_set;
	} 
	
	public static void main(String args[]) { 

		DateTime now = new DateTime();
		DateTime prev = now.minusYears(2);
		System.out.println(getMostPopularTags("Giveaway",prev.toDate(),now.toDate(), 17));
	}
}
