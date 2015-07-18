package dbo;

import java.util.HashSet;
import java.util.Set;

/** 
 * Defines the database object for a Instagram tag
 * @author Rupak Chakraborty
 * @for Kutty
 * @since 13 July, 2015
 *
 */ 

public class Tag {
	
	private String username;
	private String profile_picture;
	private String full_name;
	private String user_id;
	private double timestamp;
	private String tag_id;
	private String filter;
	private String type;
	private String caption_text;
	private Set<String> tag_set;
	private int like_count;
	private String link;
	private double latitude;
	private double longitude;
	private int comment_count;
	private String image_url;
	private int image_height;
	private int image_width;
	
	/** 
	 * 
	 */ 
	
	public Tag() { 
		
		latitude = 0.0;
		longitude = 0.0;
		caption_text = "";
		username = "";
		profile_picture = "";
		full_name = "";
		user_id = "";
		timestamp = 0.0;
		tag_id = "";
		filter = "";
		type = "";
		tag_set = new HashSet<String>();
		like_count = 0;
		link = "";
		image_url = "";
		image_height = 0;
		image_width = 0;
		comment_count = 0;
	} 
	
	/** 
	 * 
	 * @return
	 */ 
	
	public String getImageURL() { 
		
		return image_url;
	}
	
	/** 
	 * 
	 * @param image_url
	 */ 
	
	public void setImageURL(String image_url) { 
		
		this.image_url = image_url;
	}
	
	/** 
	 * 
	 * @return
	 */ 
	
	public int getImageHeight() { 
		
		return image_height;
	}
	
	/** 
	 * 
	 * @param image_height
	 */ 
	
	public void setImageHeight(int image_height) { 
		
		this.image_height = image_height;
	}
	
	/** 
	 * 
	 * @return
	 */ 
	
	public int getImageWidth() { 
		
		return image_width;
	}
	
	/** 
	 * 
	 * @param image_width
	 */ 
	
	public void setImageWidth(int image_width) { 
		
		this.image_width = image_width;
	}
	
	/** 
	 * 
	 * @return
	 */ 
	
	public String getUsername() { 
		
		return username;
	} 
	
	/** 
	 * 
	 * @param username
	 */ 
	
	public void setUsername(String username) { 
		
		this.username = username;
	} 
	
	/** 
	 * 
	 * @return
	 */ 
	
	public String getProfilePicture() { 
		
		return profile_picture;
	} 
	
	/** 
	 * 
	 * @param profile_picture
	 */ 
	
	public void setProfilePicture(String profile_picture) { 
		
		this.profile_picture = profile_picture;
	} 
	
	/** 
	 * 
	 * @return
	 */ 
	
	public String getAuthor() { 
		
		return full_name;
	} 
	
	/** 
	 * 
	 * @param full_name
	 */ 
	
	public void setAuthor(String full_name) { 
		
		this.full_name = full_name;
	} 
	
	/** 
	 * 
	 * @return
	 */ 
	
	public String getUserId() { 
		
		return user_id;
	} 
	
	/** 
	 * 
	 * @param user_id
	 */ 
	
	public void setUserId(String user_id) { 
		
		this.user_id = user_id;
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
	
	public String getFilter() { 
		
		return filter;
	} 
	
	/** 
	 * 
	 * @param filter
	 */ 
	
	public void setFilter(String filter) { 
		
		this.filter = filter;
	} 
	
	/** 
	 * 
	 * @return
	 */ 
	
	public String getType() { 
		
		return type;
	} 
	
	/** 
	 * 
	 * @param type
	 */ 
	
	public void setType(String type) { 
		
		this.type = type;
	} 
	
	/** 
	 * 
	 * @return
	 */ 
	
	public String getCaptionText() { 
		
		return caption_text;
	} 
	
	/** 
	 * 
	 * @param caption_text
	 */ 
	
	public void setCaptionText(String caption_text) { 
		
		this.caption_text = caption_text;
	} 
	
	/** 
	 * 
	 * @return
	 */ 
	
	public Set<String> getTagSet() { 
		
		return tag_set;
	} 
	
	/** 
	 * 
	 * @param tag_set
	 */ 
	
	public void setTagSet(Set<String> tag_set) { 
		
		this.tag_set = tag_set;
	} 
	
	/** 
	 * 
	 * @return
	 */ 
	
	public int getLikeCount() { 
		
		return like_count;
	} 
	
	/** 
	 * 
	 * @param like_count
	 */ 
	
	public void setLikeCount(int like_count) { 
		
		this.like_count = like_count;
	} 
	
	/** 
	 * 
	 * @return
	 */ 
	
	public String getLink() { 
		
		return link;
	} 
	
	/** 
	 * 
	 * @param link
	 */ 
	
	public void setLink(String link) { 
		
		this.link = link;
	} 
	
	/** 
	 * 
	 * @return
	 */ 
	
	public double getLatitude() { 
		
		return latitude;
	} 
	
	/** 
	 * 
	 * @param latitude
	 */ 
	
	public void setLatitude(double latitude) { 
		
		this.latitude = latitude;
	} 
	
	/** 
	 * 
	 * @return
	 */ 
	
	public double getLongitude() { 
		
		return longitude;
	} 
	
	/** 
	 * 
	 * @param longitude
	 */ 
	
	public void setLongitude(double longitude) { 
		
		this.longitude = longitude;
	} 
	
	/** 
	 * 
	 * @return
	 */ 
	
	public int getCommentCount() { 
		
		return comment_count;
	} 
	
	
	/** 
	 * 
	 * @param comment_count
	 */ 
	
	public void setCommentCount(int comment_count) { 
		
		this.comment_count = comment_count;
	}
}
