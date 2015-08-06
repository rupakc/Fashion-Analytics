package dbo;

import java.util.Date;

/** 
 * Defines a Youtube comment object which is to be used for inserting in the database
 * @author Rupak Chakraborty
 * @for Kutty
 * @since 18 July, 2015
 *
 */ 

public class Comment {
	
	private int replyCount;
	private String channelId;
	private String videoId;
	private String commentId;
	private String message;
	private String author; 
	private Date timeStamp;
	private Date updatedAt;
	private int likeCount;
	private String viewerRating;  
	private String channel; 
	
	/** 
	 * public constructor to initialize the Youtube comment object with default values
	 */ 
	
	public Comment() { 
		
		channel = "Youtube";
		replyCount = 0;
		channelId = "";
		videoId = "";
		commentId = "";
		message = "";
		author = "";
		timeStamp = new Date();
		updatedAt = new Date();
		likeCount = 0;
		viewerRating = "none";
	}

	/** 
	 * 
	 * @return the channel
	 */ 
	
	public String getChannel() {  
		
		return channel;
	} 
	
	/**
	 * 
	 * @param channel the channel to set
	 */ 
	
	public void setChannel(String channel) { 
		
		this.channel = channel;
	}

	/**
	 * Returns the replyCount for a given comment
	 * @return the replyCount Integer containing the replies on a particulat comment
	 */ 
	
	public int getReplyCount() { 
		
		return replyCount;
	} 
	
	/**
	 * Sets the number of replies received on a youtube comment
	 * @param replyCount the replyCount to set
	 */ 
	
	public void setReplyCount(int replyCount) { 
		
		this.replyCount = replyCount;
	} 
	
	/**
	 * Returns the channel Id of a particular youtube comment
	 * @return the channelId String containing the channelId
	 */ 
	
	public String getChannelId() { 
		
		return channelId;
	} 
	
	/**
	 * Sets the channel Id for a given youtube comment
	 * @param channelId the channelId to set 
	 */ 
	
	public void setChannelId(String channelId) { 
		
		this.channelId = channelId;
	} 
	
	/**
	 * Returns the videoId of a particular comment
	 * @return String containing the videoId
	 */ 
	
	public String getVideoId() { 
		
		return videoId;
	} 
	
	/**
	 * Sets the videoId of a given comment
	 * @param videoId the videoId to set
	 */ 
	
	public void setVideoId(String videoId) { 
		
		this.videoId = videoId;
	} 
	
	/**
	 * Returns the unique ID of a given comment
	 * @return String containing the commentId 
	 */ 
	
	public String getCommentId() { 
		
		return commentId;
	} 
	
	/**
	 * Sets the commentId for a given comment
	 * @param commentId the commentId to set
	 */ 
	
	public void setCommentId(String commentId) { 
		
		this.commentId = commentId;
	} 
	
	/**
	 * Returns the message i.e. comment content
	 * @return String containing the message
	 */ 
	
	public String getMessage() { 
		
		return message;
	} 
	
	/**
	 * Sets the comment content
	 * @param String containing message the message to set
	 */ 
	
	public void setMessage(String message) { 
		
		this.message = message;
	} 
	
	/** 
	 * Returns the name of the author of a given youtube comment
	 * @return String containing the author 
	 */ 
	
	public String getAuthor() { 
		
		return author;
	} 
	
	/** 
	 * 
	 * @param author the author to set
	 */ 
	
	public void setAuthor(String author) { 
		
		this.author = author; 
	} 
	
	/** 
	 * 
	 * @return the timeStamp
	 */ 
	
	public Date getTimeStamp() { 
		
		return timeStamp;
	} 
	
	/** 
	 * 
	 * @param timeStamp the timeStamp to set
	 */ 
	
	public void setTimeStamp(Date timeStamp) { 
		
		this.timeStamp = timeStamp;
	} 
	
	/** 
	 * 
	 * @return the updatedAt
	 */ 
	
	public Date getUpdatedAt() { 
		
		return updatedAt;
	} 
	
	/** 
	 * 
	 * @param updatedAt the updatedAt to set
	 */ 
	
	public void setUpdatedAt(Date updatedAt) { 
		
		this.updatedAt = updatedAt;
	} 
	
	/** 
	 * 
	 * @return the likeCount
	 */ 
	
	public int getLikeCount() { 
		
		return likeCount;
	} 
	
	/** 
	 * 
	 * @param likeCount the likeCount to set
	 */ 
	
	public void setLikeCount(int likeCount) { 
		
		this.likeCount = likeCount;
	} 
	
	/** 
	 * 
	 * @return the viewerRating
	 */ 
	
	public String getViewerRating() { 
		
		return viewerRating;
	} 
	
	/** 
	 * 
	 * @param viewerRating the viewerRating to set
	 */ 
	
	public void setViewerRating(String viewerRating) { 
		
		this.viewerRating = viewerRating;
	} 
	
}
