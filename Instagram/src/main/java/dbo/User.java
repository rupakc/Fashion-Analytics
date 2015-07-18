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
	
	public String getId() { 
		
		return id;
	} 
	
	/** 
	 * 
	 * @param id
	 */ 
	
	public void setId(String id) { 
		
		this.id = id;
	} 
	
	/** 
	 * 
	 * @return
	 */ 
	
	public String getBio() { 
		
		return bio;
	} 
	
	/** 
	 * 
	 * @param bio
	 */ 
	
	public void setBio(String bio) { 
		
		this.bio = bio;
	} 
	
	/** 
	 * 
	 * @return
	 */ 
	
	public String getWebsite() { 
		
		return website;
	}  
	
	/** 
	 * 
	 * @param website
	 */ 
	
	public void setWebsite(String website) { 
		
		this.website = website;
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
	
	public String getFullName() { 
		
		return full_name;
	} 
	
	/** 
	 * 
	 * @param full_name
	 */ 
	
	public void setFullName(String full_name) { 
		
		this.full_name = full_name;
	} 
	
	/** 
	 * 
	 * @return
	 */ 
	
	public long getMediaCount() { 
		
		return media_count;
	} 
	
	/** 
	 * 
	 * @param media_count
	 */ 
	
	public void setMediaCount(long media_count) { 
		
		this.media_count = media_count;
	} 
	
	/** 
	 * 
	 * @return
	 */ 
	
	public long getFollowedByCount() { 
		
		return followed_by_count;
	} 
	
	/** 
	 * 
	 * @param followed_by_count
	 */
	
	public void setFollowedByCount(long followed_by_count) { 
		
		this.followed_by_count = followed_by_count;
	} 
	
	/** 
	 * 
	 * @return
	 */ 
	
	public long getFollowsCount() { 
		
		return follows_count;
	} 
	
	/** 
	 * 
	 * @param follows_count
	 */ 
	
	public void setFollowsCount(long follows_count) { 
		
		this.follows_count = follows_count;
	} 
}
