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
	private String country = "";
	
	/** 
	 * public constructor to initialize the fields with default values
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
		country = "";
		tag_set = new HashSet<String>();
		like_count = 0;
		link = "";
		image_url = "";
		image_height = 0;
		image_width = 0;
		comment_count = 0;
	} 
	 
	
	/** 
	 * Returns the image url of a given post
	 * @return String containing the image url
	 */ 
	
	public String getImageURL() { 
		
		return image_url;
	}
	
	/** 
	 * Sets the image url of a given post
	 * @param image_url String containing the image url
	 */ 
	
	public void setImageURL(String image_url) { 
		
		this.image_url = image_url;
	}
	
	/** 
	 * Returns the height of the image (only standard resolution)
	 * @return Integer containing the height in pixels
	 */ 
	
	public int getImageHeight() { 
		
		return image_height;
	}
	
	/** 
	 * Sets the height of a given image
	 * @param image_height Integer containing the height of the image in pixels
	 */ 
	
	public void setImageHeight(int image_height) { 
		
		this.image_height = image_height;
	}
	
	/** 
	 * Returns the width of the image (in standard resolution)
	 * @return Integer containing the width in pixels
	 */ 
	
	public int getImageWidth() { 
		
		return image_width;
	}
	
	/** 
	 * Sets the width of the given image
	 * @param image_width Integer containing the image width
	 */ 
	
	public void setImageWidth(int image_width) { 
		
		this.image_width = image_width;
	}
	
	/** 
	 * Returns the username of a given Instagram Tag
	 * @return String containing the username
	 */ 
	
	public String getUsername() { 
		
		return username;
	} 
	
	/** 
	 * Sets the username of a given Instagram Tag
	 * @param username String containing the username of the tag/post
	 */ 
	
	public void setUsername(String username) { 
		
		this.username = username;
	} 
	
	/** 
	 * Returns the link to the profile picture of the post author
	 * @return String containing the link to the profile picture
	 */ 
	
	public String getProfilePicture() { 
		
		return profile_picture;
	} 
	
	/** 
	 * Sets the profile picture url of the given user
	 * @param profile_picture String containing the url to the profile picture
	 */ 
	
	public void setProfilePicture(String profile_picture) { 
		
		this.profile_picture = profile_picture;
	} 
	
	/** 
	 * Returns the author of a given Instagram tag post
	 * @return String containing the author name
	 */ 
	
	public String getAuthor() { 
		
		return full_name;
	} 
	
	/** 
	 * Sets the author of a given Instagram post
	 * @param full_name String containing the full name of the author
	 */ 
	
	public void setAuthor(String full_name) { 
		
		this.full_name = full_name;
	} 
	
	/** 
	 * Returns the userId of the given author
	 * @return String containing the userId
	 */ 
	
	public String getUserId() { 
		
		return user_id;
	} 
	
	/** 
	 * Sets the userId of the given author
	 * @param user_id String containing the userId
	 */ 
	
	public void setUserId(String user_id) { 
		
		this.user_id = user_id;
	} 
	
	/** 
	 * Returns the timestamp of the given post
	 * @return Double number containing the timestamp
	 */ 
	
	public double getTimestamp() { 
		
		return timestamp;
	} 
	
	/** 
	 * Sets the timestamp of a given post
	 * @param timestamp Double containing the timestamp
	 */ 
	
	public void setTimestamp(double timestamp) { 
		
		this.timestamp = timestamp;
	} 
	
	/** 
	 * Returns the tagId associated with a given post
	 * @return String containing the tagId
	 */ 
	
	public String getTagId() { 
		
		return tag_id;
	} 
	
	/** 
	 * Sets the tag_id for a given tag
	 * @param tag_id String containing the tag id
	 */ 
	
	public void setTagId(String tag_id) { 
		
		this.tag_id = tag_id;
	} 
	
	/** 
	 * Returns the filter used for the given post
	 * @return String containing the filter name
	 */ 
	
	public String getFilter() { 
		
		return filter;
	} 
	
	/** 
	 * Sets the type of filter used on a photo
	 * @param filter String containing the type of filter
	 */ 
	
	public void setFilter(String filter) { 
		
		this.filter = filter;
	} 
	
	/** 
	 * Returns the type of post (i.e. Photo or Video)
	 * @return String containing the type of post 
	 */ 
	
	public String getType() { 
		
		return type;
	} 
	
	/** 
	 * Sets the type of post (i.e. photo or video)
	 * @param type String containing the type of post
	 */ 
	
	public void setType(String type) { 
		
		this.type = type;
	} 
	
	/** 
	 * Returns the caption text associated with a given post
	 * @return String containing the caption text
	 */ 
	
	public String getCaptionText() { 
		
		return caption_text;
	} 
	
	/** 
	 * Sets the caption text of a given post
	 * @param caption_text String containing the caption text
	 */ 
	
	public void setCaptionText(String caption_text) { 
		
		this.caption_text = caption_text;
	} 
	
	/** 
	 * Returns the set of tags associated with a given Instagram post
	 * @return Set<String> containing the tagset
	 */ 
	
	public Set<String> getTagSet() { 
		
		return tag_set;
	} 
	
	/** 
	 * Sets the Tagset of a given Instagram post
	 * @param tag_set Set<String> containing the tagset
	 */ 
	
	public void setTagSet(Set<String> tag_set) { 
		
		this.tag_set = tag_set;
	} 
	
	/** 
	 * Returns the number of likes received by a given tag post
	 * @return Integer containing the number of likes
	 */ 
	
	public int getLikeCount() { 
		
		return like_count;
	} 
	
	/** 
	 * Sets the number of likes of a given tag post
	 * @param like_count Integer containing the number of likes
	 */ 
	
	public void setLikeCount(int like_count) { 
		
		this.like_count = like_count;
	} 
	
	/** 
	 * Returns the url of the given tag post
	 * @return String containing the link
	 */ 
	
	public String getLink() { 
		
		return link;
	} 
	
	/** 
	 * Sets the url of a given tag
	 * @param link String containing the link to the post
	 */ 
	
	public void setLink(String link) { 
		
		this.link = link;
	} 
	
	/** 
	 * Returns the latitude of a given post
	 * @return Double containing the latitude
	 */ 
	
	public double getLatitude() { 
		
		return latitude;
	} 
	
	/** 
	 * Sets the latitude of a given place
	 * @param latitude Double containing the latitude
	 */ 
	
	public void setLatitude(double latitude) { 
		
		this.latitude = latitude;
	} 
	
	/** 
	 * Returns the longitude of a given place
	 * @return Double representing the longitude of a place
	 */ 
	
	public double getLongitude() { 
		
		return longitude;
	} 
	
	/** 
	 * Sets the longitude of a given post
	 * @param longitude Floating point number containing longitude of a place
	 */ 
	
	public void setLongitude(double longitude) { 
		
		this.longitude = longitude;
	} 
	
	/** 
	 * Returns the count of comments on a given post
	 * @return Integer containing the number of comments
	 */ 
	
	public int getCommentCount() { 
		
		return comment_count;
	} 
	
	
	/** 
	 * Sets the number of comments received on a given post
	 * @param comment_count Integer containing the number of comments
	 */ 
	
	public void setCommentCount(int comment_count) { 
		
		this.comment_count = comment_count;
	}
	
	/** 
	 * Returns the country name associated with the given post
	 * @return String containing the country name
	 */ 
	
	public String getCountry() { 
		
		return country;
	}
	
	/** 
	 * Sets the country name of the given post
	 * @param country String containing the country name
	 */
	
	public void setCountry(String country) { 
		
		this.country = country;
	}
}
