package org.kutty.fetch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.UnknownHostException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import db.MongoBase;
import dbo.User;


/** 
 * Retrieves basic information pertaining to a given user from Instagram and inserts it in the database
 * Additionally collects recent user feed from Instagram and inserts them in MongoDB 
 * 
 * @author Rupak Chakraborty
 * @for Kutty
 * @since 17 July, 2015
 */ 

public class UserFetch {

	String access_token;
	String user_id; 
	String base_url; 
	String suffix_url; 
	String user_endpoint; 
	
	/** 
	 * public constructor to intialize the base url, suffix url and access token with default values 
	 */ 
	
	public UserFetch() { 

		base_url = "https://api.instagram.com/v1/users/";
		suffix_url = "/?access_token=";
		access_token = "1058271351.5b9e1e6.c1ba660f72704f8d98c48340e3029e9e";
	}
	
	/** 
	 * public constructor to initialize the access_token for making api calls
	 * @param access_token String containing the access_token
	 */ 
	
	public UserFetch(String access_token) { 

		this.access_token = access_token;
		base_url = "https://api.instagram.com/v1/users/";
		suffix_url = "/?access_token=";
	}
	
	/** 
	 * public constructor to initialize the  
	 * @param user_id
	 * @param access_token
	 */ 
	
	public UserFetch(String user_id,String access_token) {

		this.access_token = access_token;
		this.user_id = user_id;
		base_url = "https://api.instagram.com/v1/users/";
		suffix_url = "/?access_token=";
		user_endpoint = base_url + this.user_id + suffix_url + this.access_token;
	}

	/** 
	 * For a given user_id retrieves the basic information from Instagram and stores in MongoDB
	 * @param user_id String containing the user_id whose information is to be retrieved
	 * @throws IOException 
	 * @throws ParseException
	 */ 

	public void fetchAndStore(String user_id) throws IOException, ParseException { 

		this.user_id = user_id;
		this.user_endpoint = this.base_url + this.user_id + this.suffix_url + this.access_token;


		String json_response = "";
		JSONObject data_object;  
		JSONObject user_object;
		JSONParser parser; 
		User user; 

		json_response = getJSONResponse(this.user_endpoint); 
		System.out.println(json_response);
		parser = new JSONParser();
		user_object = (JSONObject) parser.parse(json_response);
		data_object = (JSONObject) user_object.get("data");
		user = getInstagramUserObject(data_object);
		insertInDB(user);
	}

	/** 
	 * Converts a JSON response object into a Instagram user object 
	 * @param data_object JSONObject which is to be converted into a Instagram user object
	 * @return Instagram User object for easy insertion in the database
	 */ 

	public User getInstagramUserObject(JSONObject data_object) { 

		String username;
		String bio;
		String website;
		String id;
		String profile_picture;
		String full_name;
		long media_count;
		long followed_by;
		long follows;
		JSONObject count_object;
		User user = new User(); 

		username = (String) data_object.get("username");
		id = (String) data_object.get("id");
		bio = (String) data_object.get("bio");
		website = (String) data_object.get("website");
		full_name = (String) data_object.get("full_name");
		profile_picture = (String) data_object.get("profile_picture");

		count_object = (JSONObject) data_object.get("counts");
		media_count = (long) count_object.get("media");
		followed_by = (long) count_object.get("followed_by");
		follows = (long) count_object.get("follows");

		user.setId(id);
		user.setUsername(username);
		user.setBio(bio);
		user.setWebsite(website);
		user.setProfilePicture(profile_picture);
		user.setFullName(full_name);
		user.setMediaCount(media_count);
		user.setFollowedByCount(followed_by);
		user.setFollowsCount(follows);

		return user;

	} 

	public void insertInDB(User user) throws UnknownHostException { 

		MongoBase mongo = new MongoBase();
		mongo.setCollection("Fashion"); 
		
		try { 
			
			mongo.putInDB(user); 
			
		} catch (Exception e) { 
			
			mongo.closeConnection();
		} 

		mongo.closeConnection();
	} 
	
	/** 
	 * Given a link to the API endpoint returns the JSON response
	 * @param link String containing the link of the endpoint
	 * @return String containing the response
	 * @throws IOException
	 */ 

	public String getJSONResponse(String link)throws IOException { 

		URL url = new URL(link);
		String temp = "";
		String response = "";
		BufferedReader br;

		br = new BufferedReader(new InputStreamReader(url.openStream()));

		while((temp = br.readLine()) != null) { 

			response = response + temp;
		}

		return response;
	} 

	public static void main(String args[]) throws IOException, ParseException { 

		UserFetch userfetch = new UserFetch();

		userfetch.fetchAndStore("145483772");
	}
}
