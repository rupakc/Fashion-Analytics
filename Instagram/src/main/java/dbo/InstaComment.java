package dbo;

/** 
 * Defines a Instagram comment object for insertion in the db
 * @author Rupak Chakraborty
 * @for Kutty
 * @since 13 July, 2015
 */ 

public class InstaComment {
	
	private String text;
	private String author;
	private String username_tag;
	private String tag_id;
	private String comment_id;
	private double timestamp; 
	
	/** 
	 * public constructor to initialize the Instagram Comment Object with default values 
	 */ 
	
	public InstaComment() { 
		
		text = "";
		author = "";
		username_tag = "";
		tag_id = "";
		comment_id = "";
		timestamp = 0.0;
	} 
	
	/** 
	 * Returns the comment text
	 * @return String containing the content of the comment
	 */ 
	
	public String getText() { 
		
		return text;
	} 
	
	/** 
	 * Sets the comment text of a given comment object
	 * @param text String containing the comment text
	 */ 
	
	public void setText(String text) { 
		
		this.text = text;
	} 
	
	/** 
	 * Returns the author of a given comment object
	 * @return String containing the author name
	 */ 
	
	public String getAuthor() { 
		
		return author;
	} 
	
	/** 
	 * Sets the author of a given comment object
	 * @param author String containing the author which is to be set
	 */ 
	
	public void setAuthor(String author) { 
		
		this.author = author;
	} 
	
	/** 
	 * Returns the username of the tag which is associated with the comment
	 * @return String containing the username
	 */ 
	
	public String getUsernameTag() { 
		
		return username_tag;
	} 
	
	/** 
	 * Sets the username of the tag associated with the comment
	 * @param username_tag String containing the username of the tag
	 */ 
	
	public void setUsernameTag(String username_tag) { 
		
		this.username_tag = username_tag;
	} 
	
	/** 
	 * Returns the tagId associated with a comment object
	 * @return String containing the tagId
	 */ 
	
	public String getTagId() { 
		
		return tag_id;
	} 
	
	/** 
	 * Sets the tagId of a given Instagram comment object
	 * @param tag_id String containing the tag_id
	 */ 
	
	public void setTagId(String tag_id) { 
		
		this.tag_id = tag_id;
	} 
	
	/** 
	 * Returns the commentId of a given Instagram comment object
	 * @return String containing the commentId
	 */ 
	
	public String getCommentId() { 
		
		return comment_id;
	} 
	
	/** 
	 * Sets the commentId of a given Instagram comment object
	 * @param comment_id String containing the commentId to set
	 */ 
	
	public void setCommentId(String comment_id) { 
		
		this.comment_id = comment_id;
	} 
	
	/** 
	 * Returns the timestamp of a given comment object
	 * @return Double containing the timestamp
	 */ 
	
	public double getTimestamp() { 
		
		return timestamp;
	} 
	
	/** 
	 * Sets the timestamp of a given Instagram comment
	 * @param timestamp Double containing the timestamp
	 */ 
	
	public void setTimestamp(double timestamp) { 
		
		this.timestamp = timestamp;
	}
}
