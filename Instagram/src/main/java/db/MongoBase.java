package db;

import java.net.UnknownHostException;
import java.util.List;
import java.util.Set;

import twitter4j.GeoLocation;
import twitter4j.QueryResult;
import twitter4j.Status;
import dbo.Comment;

import com.github.jreddit.entity.Submission;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.WriteConcern;
import com.mongodb.WriteResult;

import dbo.GeoData;
import dbo.InstaComment;
import dbo.InstaLike;
import dbo.Tag;
import dbo.User;
import facebook4j.Post;
import facebook4j.ResponseList;

/** @author Rupak Chakraborty
 *  @for Kutty
 *  @since 13 July,2015 
 * 
 * The basic database Interaction Module which contains a host of adaptor functions to convert a variety of query objects into
 * MongoDB objects the prefix Adaptor implies it is a adaptor function for a specific social channel. Apart from basic function of CRUD
 * (Create, Read, Update and Delete). It also checks for duplicates in the database itself
 * 
 */ 

public class MongoBase {

	private MongoClient mongoclient;
	private DB db;
	private DBCollection collection;

	/** 
	 * Public constructor for connecting to the MongoDB server 
	 * default hostname is localhost and port number is 27017
	 * @throws UnknownHostException
	 */ 

	public MongoBase() throws  UnknownHostException { 

		mongoclient = new MongoClient("localhost",27017);
		db = mongoclient.getDB("Central");
		mongoclient.setWriteConcern(WriteConcern.ACKNOWLEDGED);
	}

	/** 
	 * Public constructor for connecting with the MongoDB server using user specified port no and host name
	 * @param host_name String representing the host name a MongoDBUri object can also be used in its place
	 * @param port_no Integer corresponding to the port number of the MongoDB
	 * @throws UnknownHostException
	 */ 

	public MongoBase(String host_name,int port_no) throws UnknownHostException { 

		mongoclient = new MongoClient(host_name,port_no);
		db = mongoclient.getDB("Central");
		mongoclient.setWriteConcern(WriteConcern.ACKNOWLEDGED);
	}

	/** 
	 * Returns the set of collections in the database
	 * @return Set<String> containing the set of collection names
	 */ 

	public Set<String> getCollections() { 

		return db.getCollectionNames();
	} 

	/** 
	 * Sets the database name for the given mongoclient
	 * @param db_name String containing the database name
	 */ 

	public void setDB(String db_name) { 

		db = mongoclient.getDB(db_name);
	} 

	/** 
	 * Returns the database instance of the present MongoDB object
	 * @return DB instance of the MongoDB object
	 */ 

	public DB getDB() {

		return db;
	}

	/** 
	 * Sets the collection name of the present MongoDB instance
	 * @param collection_name String containing the collection name which roughly mirrors the product name
	 */ 

	public void setCollection(String collection_name) {

		collection = db.getCollection(collection_name);
	}

	/** 
	 * Returns the collection associated with present instance of the MongoDB object
	 * @return the collection object representing the present collection
	 */ 

	public DBCollection getCollection() {

		return collection;
	}

	/** 
	 * Inserts a document in the DB
	 * @param doc Document to be inserted
	 */ 

	synchronized public void insertDocument(BasicDBObject doc) {

		collection.insert(doc);
	}

	/** 
	 * Removes all the documents in the database collection which matches a given query
	 * @param remove_query BasicDBObject containing the query 
	 */ 

	synchronized public void removeDocument(BasicDBObject remove_query) { 

		collection.remove(remove_query);
	} 

	/** 
	 * Removes the documents from the database matching a given query and returns the number of documents returned
	 * @param remove_query DBObject containing the document to be removed
	 * @return Integer containing the number of documents to be removed
	 */ 

	synchronized public int removeDocument(DBObject remove_query) { 

		WriteResult result = collection.remove(remove_query); 

		return result.getN();
	}
	
	/** 
	 * Updates a given document matching a specific query
	 * @param query DBObject corresponding to a particular query
	 * @param update DBObject containing the update parameters
	 */ 
	
	synchronized public void updateDocument(DBObject query,DBObject update) { 
		
		collection.update(query, update, true, true);
	} 
	
	/** 
	 * Updates a given document matching a specific query
	 * @param query DBObject corresponding to a particular query
	 * @param update DBObject containing the update parameters
	 * @return Integer containing the number of records updated
	 */ 
	
	synchronized public int updateDocument(BasicDBObject query,BasicDBObject update) { 
		
		WriteResult write = collection.update(query, update, true, true);
		
		return write.getN();
	}
	
	/** 
	 * Checks whether a given object is already present in the database or not
	 * @param doc Document representing the basic database object
	 * @param channel_name String which contains the product name corresponding to the collection name
	 * @return true if the object already exists in the database false otherwise
	 */ 

	synchronized public boolean checkExists(BasicDBObject doc,String channel_name) {

		DBCursor cursor;
		BasicDBObject query = new BasicDBObject();

		query.append("Channel",channel_name).append("_id", doc.get("_id"));

		cursor = collection.find(query);

		if(cursor.hasNext()) {

			return true; 

		} else { 

			return false;
		}
	}

	/** 
	 * Prints all the entries present in a given channel to the console
	 * @param channel_name String representing the channel name for which entries are printed on the screen
	 */ 

	synchronized public void printChannelEntries(String channel_name) {

		DBCursor cursor = collection.find(new BasicDBObject("Channel",channel_name));

		while(cursor.hasNext()) { 

			System.out.println(cursor.next());
		}

		System.out.println("Number of Entries : " + cursor.size()); 

		cursor.close();
	}

	/** 
	 * Executes a query and returns the cursor corresponding to the query
	 * @param query BasicDBObject representing the database query
	 * @return DBCursor which corresponds to the results of the query fired
	 */ 

	public DBCursor getQuery(BasicDBObject query) { 

		DBCursor cursor = collection.find(query); 

		return cursor; 
	}

	/** 
	 * Converts a Instagram object into BasicDBObject for insertion into MongoDB
	 * @param instagram Instagram tag object which is to be inserted
	 * @return BasicDBObject which is a representation of the Instagram tag object
	 */ 

	public BasicDBObject getInstagramAdaptor(Tag instagram,String tag_searched) { 

		BasicDBObject insta_doc = new BasicDBObject("Channel","Instagram").
				append("Username", instagram.getUsername()).
				append("ProfilePicture", instagram.getProfilePicture()).
				append("Author", instagram.getAuthor()).
				append("UserId", instagram.getUserId()).
				append("Timestamp",instagram.getTimestamp()).
				append("TagId", instagram.getTagId()).
				append("Filter", instagram.getFilter()).
				append("Type",instagram.getType()).
				append("CaptionText", instagram.getCaptionText()).
				append("TagSet", instagram.getTagSet()).
				append("LikeCount", instagram.getLikeCount()).
				append("Link", instagram.getLink()).
				append("Latitude", instagram.getLatitude()).
				append("Longitude", instagram.getLongitude()).
				append("CommentCount", instagram.getCommentCount()).
				append("Query", tag_searched).
				append("ImageLink", instagram.getImageURL()).
				append("ImageWidth", instagram.getImageWidth()).
				append("ImageHeight", instagram.getImageHeight()).
				append("Country",instagram.getCountry()).
				append("Type", "tag");

		return insta_doc;
	}

	/** 
	 * Adaptor function for converting a Reddit submission into a DBObject 
	 * @param sub Reddit submission which is to be converted into  a DBObject
	 * @return DBObject corresponding to a particular Reddit submission
	 */ 

	public DBObject getRedditAdaptor(Submission sub) {

		BasicDBObject reddit_doc = new BasicDBObject();

		reddit_doc.append("Channel", "Reddit").
		append("Message", sub.getSelftext()).
		append("Author", sub.getAuthor()).
		append("TimeStamp", sub.getCreatedUTC()).
		append("FullName", sub.getFullName()).
		append("Title", sub.getTitle()).
		append("DownVotes", sub.getDownVotes()).
		append("UpVotes", sub.getUpVotes()).
		append("Score",sub.getScore()).
		append("CommentCount", sub.getCommentCount()).
		append("Gilded", sub.getGilded()).
		append("Domain", sub.getDomain());

		return reddit_doc;
	}
	
	/** 
	 * Adaptor function to convert the Facebook post object into a BasicDBObject 
	 * @param post Object corresponding to the facebook post
	 * @return DBObject representing the facebook post which is serializable
	 */ 

	public DBObject getFbPostAdaptor(Post post) { 

		BasicDBObject post_doc = new BasicDBObject();

		try { 

			post_doc.append("Channel", "Facebook").
			append("_id",post.getId()).
			append("Message", post.getMessage()).
			append("TimeStamp",post.getCreatedTime()).
			append("UserName", post.getFrom().getName()).
			append("Likes", post.getLikes().getCount()).
			append("Comments",post.getComments().getCount()).
			append("Shares", post.getSharesCount());			

		} catch (Exception e) { 

			e.printStackTrace();
		}

		return post_doc;
	} 
	
	/**
	 * Adaptor function to convert a Twitter Response object into a MongoDB object
	 * @param tweet Object representing a tweet which is to be converted into a DBObject
	 * @return DBObject corresponding to a tweet object
	 */ 

	public DBObject getTweetAdaptor(Status tweet)
	{
		BasicDBObject tweet_doc = new BasicDBObject();
		String location = "";
		GeoLocation Geo; 

		try {  

			if( (Geo = tweet.getGeoLocation()) != null) { 

				location = Geo.toString();
			} 

			tweet_doc.append("Channel","Twitter").
			append("UserName",tweet.getUser().getName()).
			append("UserScreenName",tweet.getUser().getScreenName()).
			append("UserID",tweet.getUser().getId()).
			append("UserLocation", tweet.getUser().getLocation()).
			append("Message",tweet.getText()).
			append("TimeStamp", tweet.getCreatedAt()).
			append("TweetLocation", location).
			append("_id", tweet.getCreatedAt()).
			append("RetweetCount",tweet.getRetweetCount()).
			append("DeviceUsed", tweet.getSource()); 

		} catch (Exception e) { 

			e.printStackTrace();
		}

		return tweet_doc;
	}

	/**
	 * Inserts a set of tweets into the database 
	 * @param result QueryResult object which contains the set of tweets obtained from a given query
	 * @param product_name String representing the product name whose tweet list is to be put in the collection
	 * @throws UnknownHostException
	 */

	synchronized public void putInDB(QueryResult result,String product_name) throws UnknownHostException
	{
		List<Status> tweets = result.getTweets();
		BasicDBObject tweet_doc; 
		setCollection(product_name);

		for(Status tweet: tweets) { 

			tweet_doc = (BasicDBObject) getTweetAdaptor(tweet);

			if (!checkExists(tweet_doc, "Twitter")) { 
				insertDocument(tweet_doc);
			}
		}
	} 

	/** 
	 * Inserts a list of facebook responses into the database
	 * @param response_list List of facebook posts to be inserted
	 * @param product_name String representing the product name which roughly corresponds to the collection name
	 * @throws UnknownHostException
	 */ 

	synchronized public void putInDB(ResponseList<Post> response_list,String product_name) throws UnknownHostException { 

		setCollection(product_name);

		for (Post post: response_list) { 

			BasicDBObject post_doc = (BasicDBObject)getFbPostAdaptor(post);

			if(!checkExists(post_doc, "Facebook")) { 

				insertDocument(post_doc);
			}
		}
	} 
	
	/** 
	 * Inserts a List of reddit submissions into the database
	 * @param reddit List containing the reddit submissions
	 * @param product_name product_name corresponding to the collection name in which the document is to be inserted
	 * @throws UnknownHostException
	 */ 

	synchronized public void putInDB(List<Submission> reddit,String product_name) throws UnknownHostException { 

		setCollection(product_name); 

		BasicDBObject reddit_doc; 
		DBObject query;
		DBObject update; 
		DBCursor cursor; 
		
		for(Submission sub: reddit) { 

			reddit_doc = (BasicDBObject) getRedditAdaptor(sub);
		
			query = new BasicDBObject("Channel","Reddit").
					append("TimeStamp", reddit_doc.get("TimeStamp")).
					append("Author", reddit_doc.getString("Author"));
			
			cursor = collection.find(query); 
			
			if (!cursor.hasNext()) {  
				
				insertDocument(reddit_doc);  
				
			} else { 
				
				update = new BasicDBObject("$set", new BasicDBObject("UpVotes", reddit_doc.get("UpVotes"))).
							append("$set", new BasicDBObject("DownVotes", reddit_doc.get("DownVotes"))).
							append("$set", new BasicDBObject("Score", reddit_doc.get("Score"))).
							append("$set", new BasicDBObject("CommentCount", reddit_doc.get("CommentCount"))); 
				
				updateDocument(query,update);
			}
		}
	}

	/** 
	 * Checks if a given Instagram tag object already exists in the database or not
	 * @param insta_doc Instagram object in BSON format which is to be checked
	 * @return true if the object already exists false otherwise
	 */ 

	public boolean checkInstagram(BasicDBObject insta_doc) { 

		DBObject query;
		DBCursor cursor;

		query = new BasicDBObject("Channel","Instagram").append("Author", insta_doc.getString("Author")).
				append("Timestamp", insta_doc.get("Timestamp"));

		cursor = collection.find(query);
		
		if (cursor.hasNext()) { 

			return true;
		}

		return false;
	}

	/** 
	 * Inserts a Instagram Tag object in the database if it does not already exist
	 * @param instagram Instagram object which is to be inserted
	 */ 

	public void putInDB(Tag instagram,String tag_searched) { 

		BasicDBObject insta_doc = getInstagramAdaptor(instagram,tag_searched);

		if (!checkInstagram(insta_doc)) { 

			insertDocument(insta_doc);
		}
	}

	/** 
	 * Converts a given Instagram comment object into a BasicDBObject for easy insertion in the database
	 * @param comment Instagram comment which is to be inserted in the database
	 * @param tag_searched String containing the string query which is to be searched
	 * @return BasicDBObject which contains a representation of the Instagram comment object
	 */

	public BasicDBObject getInstaCommentAdaptor(InstaComment comment,String tag_searched) { 

		BasicDBObject comment_doc = new BasicDBObject("Channel","Instagram").
				append("Message", comment.getText()).
				append("Author",comment.getAuthor()).
				append("UsernameTag",comment.getUsernameTag()).
				append("TagId",comment.getTagId()).
				append("CommentId", comment.getCommentId()).
				append("Timestamp", comment.getTimestamp()).
				append("Query",tag_searched).
				append("Type", "comment");

		return comment_doc;
	}
	
	public BasicDBObject getInstagramUserAdaptor(User user) { 
		
		BasicDBObject user_doc = new BasicDBObject("Channel","Instagram").
				append("UserId", user.getId()).
				append("Username", user.getUsername()).
				append("Bio", user.getBio()).
				append("Website", user.getWebsite()).
				append("ProfilePicture", user.getProfilePicture()).
				append("Fullname", user.getFullName()).
				append("MediaCount", user.getMediaCount()).
				append("FollowedByCount", user.getFollowedByCount()).
				append("FollowsCount", user.getFollowsCount()).
				append("Type", "user");
		
		return user_doc;
	} 
	
	/** 
	 * Converts a given Instagram like object to a BasicDBObject for insertion in the database
	 * @param like Instagram like object which is to be inserted in the database
	 * @param tag_searched String containing the tag which is to be searched
	 * @return BasicDBObject containing the representation of the Instagram like object
	 */ 
	
	public BasicDBObject getInstaLikeAdaptor(InstaLike like,String tag_searched) { 

		BasicDBObject like_doc = new BasicDBObject("Channel","Instagram").
				append("Author", like.getAuthor()).
				append("UsernameLike", like.getUsernameLike()).
				append("UsernameTag",like.getUserTag()).
				append("TagId", like.getTagId()).
				append("LikeId", like.getLikeId()).
				append("Timestamp", like.getTimestamp()).
				append("Query", tag_searched).
				append("Type", "like");

		return like_doc;
	}
	
	/** 
	 * Inserts an Instagram comment object in the database if it already doesn't exist	
	 * @param comment Instagram comment object which is to be inserted
	 * @param tag_searched String containing the tag query which is to be searched
	 */ 
	
	public void putInDB(InstaComment comment,String tag_searched) { 
		
		BasicDBObject comment_doc = getInstaCommentAdaptor(comment,tag_searched);
		DBObject query; 
		DBCursor cursor; 
		
		query = new BasicDBObject("Channel","Instagram").
				append("Author", comment.getAuthor()).
				append("Timestamp", comment.getTimestamp()).
				append("CommentId", comment.getCommentId()).
				append("Type", "comment");
		
		cursor = collection.find(query);
		
		if (!cursor.hasNext()) { 
			
			insertDocument(comment_doc);
		}
	}
	
	/** 
	 * Inserts a Instagram Like object in the database if it doesn't already exists
	 * @param like Instagram Like object which is to be inserted in the database
	 * @param tag_searched String containing the tag which is searched in the API
	 */ 
	
	public void putInDB(InstaLike like,String tag_searched) { 
		
		BasicDBObject like_doc = getInstaLikeAdaptor(like,tag_searched);
		DBObject query;
		DBCursor  cursor;
		
		query = new BasicDBObject("Channel","Instagram").
				append("Author", like.getAuthor()).
				append("TagId", like.getTagId()).
				append("Type", "like"); 
		
		cursor = collection.find(query);
		
		if (!cursor.hasNext()) { 
			
			insertDocument(like_doc);
		}
	} 
	
	public void putInDB(User user) { 
		
		BasicDBObject user_doc = getInstagramUserAdaptor(user);
		BasicDBObject query;
		BasicDBObject update; 
		DBCursor cursor;  
		
		query = new BasicDBObject("Channel","Instagram").
				append("UserId", user.getId()).
				append("Type", "user");
		
		cursor = collection.find(query);
		
		if (!cursor.hasNext()) { 
			
			insertDocument(user_doc); 
			
		} else { 
			
			update = new BasicDBObject("$set", new BasicDBObject("Username",user.getUsername()).
						append("$set", new BasicDBObject("Bio", user.getBio())).
						append("$set", new BasicDBObject("Website", user.getWebsite())).
						append("$set", new BasicDBObject("ProfilePicture", user.getProfilePicture())).
						append("$set", new BasicDBObject("Fullname", user.getFullName())).
						append("$set", new BasicDBObject("MediaCount", user.getMediaCount())).
						append("$set", new BasicDBObject("FollowedByCount", user.getFollowedByCount())).
						append("$set", new BasicDBObject("FollowsCount", user.getFollowsCount())));
			
			collection.update(query, update);
		}
	} 
		
	/** 
	 * Adpator function to convert the youtube comment object into a DBObject for serialization and insertion in MongoDB
	 * @param comment Youtube comment object which is to be inserted in the DB
	 * @return DBObject corresponding to the youtube comment object
	 */ 

	public DBObject getCommentAdaptor(Comment comment) { 

		DBObject comment_doc = new BasicDBObject("ReplyCount",comment.getReplyCount()).
				append("ChannelId", comment.getChannelId()).
				append("VideoId", comment.getVideoId()).
				append("CommentId", comment.getCommentId()).
				append("Message", comment.getMessage()).
				append("Author", comment.getAuthor()).
				append("TimeStamp", comment.getTimeStamp()).
				append("UpdatedAt", comment.getUpdatedAt()).
				append("LikeCount", comment.getLikeCount()).
				append("ViewerRating",comment.getViewerRating()).
				append("Channel", "Youtube");

		return comment_doc;
	}

	/** 
	 * Inserts a youtube comment object into the db if it doesnot already exists
	 * @param comment Youtube comment object which is to be inserted
	 */ 

	public void putInDB(Comment comment) { 

		if (!checkExists(comment)) { 

			DBObject comment_doc = getCommentAdaptor(comment);
			insertDocument((BasicDBObject) comment_doc);
		}
	}

	/** 
	 * Checks if a youtube comment object already exists in the database or not
	 * @param comment Youtube comment object which is to be checked
	 * @return true if the comment object exists false otherwise
	 */ 

	public boolean checkExists(Comment comment) { 

		DBCursor cursor;
		DBObject query;

		query = new BasicDBObject("Channel","Youtube").
				append("CommentId", comment.getCommentId()).
				append("TimeStamp", comment.getTimeStamp());

		cursor = collection.find(query);

		if (cursor.hasNext()) { 

			return true;
		}

		return false;
	}
	
	/** 
	 * Adaptor function to convert a GeoData object into a BasicDBObject
	 * @param geodata GeoData object which is to be converted to a BasicDBObject
	 * @return BasicDBObject containing the representation of the GeoData object
	 */ 
	
	public BasicDBObject getGeoDataAdaptor(GeoData geodata) { 
	
		BasicDBObject geo_doc = new BasicDBObject("CountryName",geodata.getCountryName()).
								append("CountryCode", geodata.getCountryCode()).
								append("PlaceId", geodata.getPlaceId()).
								append("StreetNumber", geodata.getStreetNumber()).
								append("Route", geodata.getRoute()).
								append("Locality", geodata.getLocality()).
								append("Neighborhood", geodata.getNeighborhood()).
								append("PostalCode", geodata.getPostalCode()).
								append("FormattedAddress", geodata.getFormattedAddress()).
								append("LocationLatitude", geodata.getLocationLatitude()).
								append("LocationLongitude", geodata.getLocationLongitude()).
								append("LocationType", geodata.getLocationType()).
								append("ViewportNorthEastLatitude", geodata.getViewportNortheastLatitude()).
								append("ViewportNorthEastLongitude", geodata.getViewportNortheastLongitude()).
								append("ViewportSouthWestLatitude", geodata.getViewportSouthwestLatitude()).
								append("ViewportSouthWestLongitude", geodata.getViewportSouthwestLongitude());
		
		return geo_doc;
	}
	
	/** 
	 * Inserts a GeoData object in the database if it doesn't already exist
	 * @param geodata GeoData object which is to be inserted in the database
	 */ 
	
	public void putInDB(GeoData geodata) { 
		
		DBObject query;
		DBCursor cursor;
		
		query = new BasicDBObject("PlaceId",geodata.getPlaceId());
		cursor = collection.find(query);
		
		if(!cursor.hasNext()) { 
			
			BasicDBObject geo_doc = getGeoDataAdaptor(geodata);
			insertDocument(geo_doc);
		}
	}
	
	/** 
	 * Closes an open connection to the mongodb server
	 */ 
	
	public void closeConnection() { 
		
		mongoclient.close();
	}
}

