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
	
	public InstaComment() { 
		
		text = "";
		author = "";
		username_tag = "";
		tag_id = "";
		comment_id = "";
		timestamp = 0.0;
	} 
	
	public String getText() { 
		
		return text;
	} 
	
	public void setText(String text) { 
		
		this.text = text;
	} 
	
	public String getAuthor() { 
		
		return author;
	} 
	
	public void setAuthor(String author) { 
		
		this.author = author;
	} 
	
	public String getUsernameTag() { 
		
		return username_tag;
	} 
	
	public void setUsernameTag(String username_tag) { 
		
		this.username_tag = username_tag;
	} 
	
	public String getTagId() { 
		
		return tag_id;
	} 
	
	public void setTagId(String tag_id) { 
		
		this.tag_id = tag_id;
	} 
	
	public String getCommentId() { 
		
		return comment_id;
	} 
	
	public void setCommentId(String comment_id) { 
		
		this.comment_id = comment_id;
	} 
	
	public double getTimestamp() { 
		
		return timestamp;
	} 
	
	public void setTimestamp(double timestamp) { 
		
		this.timestamp = timestamp;
	}
}
