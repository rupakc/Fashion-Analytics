package dbo;

/** 
 * Defines the database object for the user profile of an Instagram user
 * @author Rupak Chakraborty
 * @for Kutty
 * @since 17 July, 2015 
 * 
 */ 

public class User {
	
	private String username;
	private String id;
	private String bio;
	private String website;
	private String profile_picture;
	private String full_name;
	private long media_count;
	private long followed_by_count;
	private long follows_count; 
	
	/** 
	 * public constructor to initialize the user object with default values 
	 */ 
	
	public User() { 
		
		username = "";
		id = "";
		bio = "";
		website = "";
		profile_picture = "";
		full_name = "";
		media_count = 0L;
		followed_by_count = 0L;
		follows_count = 0L;
	} 
	
	/** 
	 * Returns the username of a given user
	 * @return String containing the username
	 */ 
	
	public String getUsername() { 
		
		return username;
	} 
	
	/** 
	 * Sets the username of a given user
	 * @param username String containing the username of a given user
	 */ 
	
	public void setUsername(String username) { 
		
		this.username = username;
	} 
	
	/** 
	 * Returns the Id of a given user
	 * @return String containing the user id
	 */ 
	
	public String getId() { 
		
		return id;
	} 
	
	/** 
	 * Sets the Id of a given user
	 * @param id String containing the user id
	 */ 
	
	public void setId(String id) { 
		
		this.id = id;
	} 
	
	/** 
	 * Returns the bio description of a given user
	 * @return String containing the short bio of the user
	 */ 
	
	public String getBio() { 
		
		return bio;
	} 
	
	/** 
	 * Sets the bio of a given user
	 * @param bio String containing the bio of a given user
	 */ 
	
	public void setBio(String bio) { 
		
		this.bio = bio;
	} 
	
	/** 
	 * Returns the website of a given user
	 * @return String containing the website
	 */ 
	
	public String getWebsite() { 
		
		return website;
	}  
	
	/** 
	 * Sets the website of a given user
	 * @param website String containing the website name
	 */ 
	
	public void setWebsite(String website) { 
		
		this.website = website;
	} 
	
	/** 
	 * Returns the link to the profile picture of a given user
	 * @return String containing the link to the profile picture
	 */ 
	
	public String getProfilePicture() { 
		
		return profile_picture;
	} 
	
	/** 
	 * Sets the profile picture of a given user
	 * @param profile_picture String containing the profile picture
	 */ 
	
	public void setProfilePicture(String profile_picture) { 
		
		this.profile_picture = profile_picture;
	} 
	
	/** 
	 * Returns the full name of a given user
	 * @return String containing the full name
	 */ 
	
	public String getFullName() { 
		
		return full_name;
	} 
	
	/** 
	 * Sets the full name of a given user
	 * @param full_name String containing the full name of the user
	 */ 
	
	public void setFullName(String full_name) { 
		
		this.full_name = full_name;
	} 
	
	/** 
	 * Returns the count of the posts made by the user
	 * @return Long number containing the post count
	 */ 
	
	public long getMediaCount() { 
		
		return media_count;
	} 
	
	/** 
	 * Sets the number of posts made by the user
	 * @param media_count Long number containing the media count 
	 */ 
	
	public void setMediaCount(long media_count) { 
		
		this.media_count = media_count;
	} 
	
	/** 
	 * Returns the number of followers for a given user
	 * @return Long number containing the follower count
	 */ 
	
	public long getFollowedByCount() { 
		
		return followed_by_count;
	} 
	
	/** 
	 * Sets the followed by count for a given user
	 * @param followed_by_count Long number containing the followed by count
	 */
	
	public void setFollowedByCount(long followed_by_count) { 
		
		this.followed_by_count = followed_by_count;
	} 
	
	/** 
	 * Returns the count of people followed by the given user
	 * @return Long number containing the follows count
	 */ 
	
	public long getFollowsCount() { 
		
		return follows_count;
	} 
	
	/** 
	 * Sets the follows count for a given user
	 * @param follows_count Long number containing the follows count
	 */ 
	
	public void setFollowsCount(long follows_count) { 
		
		this.follows_count = follows_count;
	} 
}
