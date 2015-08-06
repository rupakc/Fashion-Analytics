package org.kutty.dbo;

/** 
 * Defines a Instagram like object for insertion in the database
 * @author Rupak Chakraborty
 * @for Kutty
 * @since 13 July, 2015
 *
 */ 

public class InstaLike {
	
	private String author;
	private String username_like;
	private String user_tag;
	private String tag_id;
	private String like_id;
	private double timestamp; 
	
	/** 
	 * public constructor to initialize the like object with default values
	 */ 
	
	public InstaLike() { 
		
		author = "";
		username_like = "";
		user_tag = "";
		tag_id = "";
		like_id = "";
		timestamp = 0.0; 
	} 
	
	/** 
	 * Returns the author of a given like object
	 * @return String containing the author name
	 */ 
	
	public String getAuthor() { 
		
		return author;
	} 
	
	/** 
	 * Sets the author of the given like object
	 * @param author String containing the author name
	 */ 
	
	public void setAuthor(String author) { 
		
		this.author = author;
	} 
	
	/** 
	 * Returns the username of the like object
	 * @return String containing the username
	 */ 
	
	public String getUsernameLike() { 
		
		return username_like;
	} 
	
	/** 
	 * Sets the username of the like object
	 * @param username_like String containing the username of the like object
	 */ 
	
	public void setUsernameLike(String username_like) { 
		
		this.username_like = username_like; 
	} 
	
	/** 
	 * Returns the user name of the post which is associated with the like object
	 * @return String containing the user name
	 */ 
	
	public String getUserTag() { 
		
		return user_tag;
	} 
	
	/** 
	 * Sets the username of the post with which the like is associated
	 * @param user_tag String containing the user tag
	 */ 
	
	public void setUserTag(String user_tag) { 
		
		this.user_tag = user_tag;
	} 
	
	/** 
	 * Returns the tagId of the post associated with the like object
	 * @return String containing the tagId
	 */ 
	
	public String getTagId() { 
		
		return tag_id;
	} 
	
	/** 
	 * Sets the tagId for a given like object
	 * @param tag_id String containing the tagId
	 */ 
	
	public void setTagId(String tag_id) { 
		
		this.tag_id = tag_id;
	} 
	
	/** 
	 * Returns the likeId of a given like object
	 * @return String containing the likeId
	 */ 
	
	public String getLikeId() { 
		
		return like_id;
	} 
	
	/** 
	 * Sets the likeId of a given like object
	 * @param like_id String containing the likeId
	 */ 
	
	public void setLikeId(String like_id) { 
		
		this.like_id = like_id;
	} 
	
	/** 
	 * Returns the timestamp of the given like object
	 * @return Double containing the timestamp
	 */ 
	
	public double getTimestamp() { 
		
		return timestamp;
	} 
	
	/** 
	 * Sets the timestamp of the given like object
	 * @param timestamp Double containing the timestamp
	 */ 
	
	public void setTimestamp(double timestamp) { 
		
		this.timestamp = timestamp;
	} 
}
