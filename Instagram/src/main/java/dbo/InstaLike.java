package dbo;

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
	 * 
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
	 * 
	 * @return
	 */ 
	
	public String getAuthor() { 
		
		return author;
	} 
	
	/** 
	 * 
	 * @param author
	 */ 
	
	public void setAuthor(String author) { 
		
		this.author = author;
	} 
	
	/** 
	 * 
	 * @return
	 */ 
	
	public String getUsernameLike() { 
		
		return username_like;
	} 
	
	/** 
	 * 
	 * @param username_like
	 */ 
	
	public void setUsernameLike(String username_like) { 
		
		this.username_like = username_like; 
	} 
	
	/** 
	 * 
	 * @return
	 */ 
	
	public String getUserTag() { 
		
		return user_tag;
	} 
	
	/** 
	 * 
	 * @param user_tag
	 */ 
	
	public void setUserTag(String user_tag) { 
		
		this.user_tag = user_tag;
	} 
	
	/** 
	 * 
	 * @return
	 */ 
	
	public String getTagId() { 
		
		return tag_id;
	} 
	
	/** 
	 * 
	 * @param tag_id
	 */ 
	
	public void setTagId(String tag_id) { 
		
		this.tag_id = tag_id;
	} 
	
	/** 
	 * 
	 * @return
	 */ 
	
	public String getLikeId() { 
		
		return like_id;
	} 
	
	/** 
	 * 
	 * @param like_id
	 */ 
	
	public void setLikeId(String like_id) { 
		
		this.like_id = like_id;
	} 
	
	/** 
	 * 
	 * @return
	 */ 
	
	public double getTimestamp() { 
		
		return timestamp;
	} 
	
	/** 
	 * 
	 * @param timestamp
	 */ 
	
	public void setTimestamp(double timestamp) { 
		
		this.timestamp = timestamp;
	} 
}
