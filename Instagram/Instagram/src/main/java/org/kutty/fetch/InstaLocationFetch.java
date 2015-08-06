package org.kutty.fetch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.kutty.db.MongoBase;
import org.kutty.dbo.GeoData;
import org.kutty.dbo.InstaLocation;
import org.kutty.dbo.Tag;

/** 
 * Fetches Recent Media of a given location using the Instagram Location API Endpoint
 * 
 * @author Rupak Chakraborty
 * @for Kutty
 * @since 2 August,2015
 * 
 */ 

public class InstaLocationFetch extends Thread {

	public String BASE_URL = "https://api.instagram.com/v1/locations/search?";
	public String LAT_URL = "lat=";
	public double LAT = 47.049;
	public String LONG_URL = "&lng=";
	public double LONG = 8.3041;
	public String ACCESS_TOKEN_URL = "&access_token=";
	public String ACCESS_TOKEN = "1058271351.5b9e1e6.c1ba660f72704f8d98c48340e3029e9e";

	/** 
	 * public constructor to initialize the InstaLocationFetch object with default values
	 */ 

	public InstaLocationFetch() {

	} 

	/** 
	 * public constructor to initialize the InstaLocation fetch object with latitude and longitude
	 * @param latitude Double containing the latitude of the given place
	 * @param longitude Double containing the longitude of the given place
	 */ 

	public InstaLocationFetch(double latitude,double longitude) { 

		this.LAT = latitude;
		this.LONG = longitude;
	} 

	/** 
	 * Returns the latitude of a given place
	 * @return Double containing the latitude
	 */ 

	public double getLatitude() {

		return LAT;
	}

	/** 
	 * Sets the latitude of a given place 
	 * @param lAT latitude of a given place
	 */ 

	public void setLatitude(double latitude) { 

		LAT = latitude;
	}

	/** 
	 * Returns the longitude of a given place
	 * @return Double containing the longitude
	 */ 

	public double getLongitude() { 

		return LONG;
	}

	/** 
	 * Sets the longitude of a given place
	 * @param lONG Double containing the longitude
	 */ 

	public void setLongitude(double longitude) { 

		LONG = longitude;
	}

	/** 
	 * Returns the access token associated with a given API call
	 * @return String containing the access token
	 */ 

	public String getACCESS_TOKEN() { 

		return ACCESS_TOKEN;
	}

	/** 
	 * Sets the access token for a given api call
	 * @param aCCESS_TOKEN String containing the access token
	 */ 

	public void setACCESS_TOKEN(String aCCESS_TOKEN) { 

		ACCESS_TOKEN = aCCESS_TOKEN;
	} 

	/** 
	 * Builds a search link given the latitude and longitude of a place
	 * @return String containing the complete url
	 */ 

	public String getLocationSearchLink() { 

		StringBuilder url = new StringBuilder(this.BASE_URL);

		url.append(this.LAT_URL);
		url.append(this.LAT);
		url.append(this.LONG_URL);
		url.append(this.LONG);
		url.append(this.ACCESS_TOKEN_URL);
		url.append(this.ACCESS_TOKEN);

		return url.toString();
	}

	/** 
	 * Builds the full search url of a given location
	 * @param lat double containing the latitude of the place
	 * @param longi double containing the longitude of the place
	 * @return String containing the fully formed url
	 */ 

	public String getLocationSearchLink(double lat,double longi) { 

		StringBuilder url = new StringBuilder(this.BASE_URL);
		this.LAT = lat;
		this.LONG = longi;
		url.append(this.LAT_URL);
		url.append(this.LAT);
		url.append(this.LONG_URL);
		url.append(this.LONG);
		url.append(this.ACCESS_TOKEN_URL);
		url.append(this.ACCESS_TOKEN);

		return url.toString();
	}

	/** 
	 * Builds the link for fetching recent media of a given location
	 * @param location_id String containing the location id of a given place
	 * @return String containing the fully formed url
	 */ 

	public String getLocationMediaLink(String location_id) { 

		String base_url = "https://api.instagram.com/v1/locations/";
		String suffix_url = "/media/recent?access_token=1058271351.5b9e1e6.c1ba660f72704f8d98c48340e3029e9e";
		String url = base_url + location_id + suffix_url;

		return url;
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

	/** 
	 * Given a JSON string returns the JSONArray containing the data
	 * @param response String containing the JSON response
	 * @return JSONArray containing the data field of the JSON response
	 * @throws ParseException
	 */ 

	public JSONArray getParsedDataArray(String response,String array_field_name) throws ParseException { 

		JSONParser parser = new JSONParser();
		JSONObject parsed_object = (JSONObject) parser.parse(response);
		JSONArray data_json = (JSONArray) parsed_object.get(array_field_name);

		return data_json;
	}

	/**
	 * Parses a string containing the String JSON response and returns a JSONObject 
	 * @param response String containing the json object
	 * @return JSONObject containing the JSON response
	 * @throws ParseException
	 */ 

	public static JSONObject getParsedJSON(String response) throws ParseException { 

		JSONParser parser;
		JSONObject json;

		parser = new JSONParser();
		json = (JSONObject) parser.parse(response);

		return json;
	}

	/** 
	 * Returns the set of tags in a given tag 
	 * @param data_object JSONObject containing a Instagram tag
	 * @return Set<String> containing unique tags in a given post
	 */ 

	public Set<String> getTagSet(JSONObject data_object) {

		Set<String> tag_set = new HashSet<String>();
		JSONArray tag_array = (JSONArray) data_object.get("tags");

		for (int i = 0; i < tag_array.size(); i++) { 

			tag_set.add((String) tag_array.get(i));
		}

		return tag_set;
	}

	/** 
	 * Given a set of coordinates returns a set of places
	 * @param latitude Double containing the latitude
	 * @param longitude Double containing the longitude
	 * @return ArrayList<InstaLocation> object containing an array of InstaLocation objects
	 * @throws IOException
	 * @throws ParseException
	 */ 

	public ArrayList<InstaLocation> getLocationObjects(double latitude,double longitude) throws IOException, ParseException {


		String link = getLocationSearchLink(latitude, longitude);
		String json_response = getJSONResponse(link);
		InstaLocation location;
		ArrayList<InstaLocation> location_objects = new ArrayList<InstaLocation>(); 
		JSONArray data_array = getParsedDataArray(json_response, "data");
		JSONObject temp; 

		for (int i = 0; i < data_array.size(); i++) { 

			temp = (JSONObject) data_array.get(i);
			location = new InstaLocation();
			location.setLocationId((String) temp.get("id"));
			location.setLocationLatitude((double)temp.get("latitude"));
			location.setLocationLongitude((double)temp.get("longitude"));
			location.setLocationName((String) temp.get("name"));

			location_objects.add(location);
		}

		return location_objects;
	}

	/** 
	 * Given a location id fetches the location details
	 * @param locationId String containing the locationId whose data is to be fetched
	 * @return InstaLocation object containing the necessary details
	 * @throws IOException
	 * @throws ParseException
	 */ 

	public InstaLocation getLocationFromId(String locationId) throws IOException, ParseException { 

		String base_url = "https://api.instagram.com/v1/locations/";
		String suffix_url = "?access_token=1058271351.5b9e1e6.c1ba660f72704f8d98c48340e3029e9e";
		String link = base_url + locationId + suffix_url;

		String json_response = getJSONResponse(link);
		InstaLocation locale = new InstaLocation(); 
		JSONObject parsed_response = getParsedJSON(json_response);
		JSONObject data_object = (JSONObject) parsed_response.get("data");

		locale.setLocationId((String) data_object.get("id"));
		locale.setLocationLatitude((double) data_object.get("latitude"));
		locale.setLocationLongitude((double) data_object.get("longitude"));
		locale.setLocationName((String) data_object.get("name"));

		return locale;
	} 

	/** 
	 * Given a pair of latitude and longitude retrieves nearby locations and inserts them in the db
	 * @param latitude Double containing the latitude of the given place
	 * @param longitude Double containing the longitude of the given place
	 */ 

	public void fetchLocationPipeline(double latitude,double longitude) { 

		try { 

			ArrayList<InstaLocation> location_objects = getLocationObjects(latitude, longitude); 
			String locationId = ""; 

			for (int i = 0; i < location_objects.size(); i++) { 

				locationId = location_objects.get(i).getLocationId();
				fetchLocationPipeline(locationId);
			} 

		} catch (IOException | ParseException e) { 

			e.printStackTrace();
		}
	} 

	/** 
	 * Define the processing pipeline for fetching recent media of a given location
	 * @param locationId String containing the locationId whose data is to be fetched and stored
	 */

	public void fetchLocationPipeline(String locationId) { 

		String link = getLocationMediaLink(locationId); 

		try { 

			String json_response = getJSONResponse(link);
			JSONObject response_object = getParsedJSON(json_response);
			JSONArray data_array = (JSONArray) response_object.get("data"); 
			JSONObject temp;
			Tag tag_info; 
			InstaLocation location_object; 
			JSONObject location; 

			for (int i = 0; i < data_array.size(); i++) { 

				location_object = new InstaLocation();
				temp = (JSONObject) data_array.get(i);
				location = (JSONObject) temp.get("location");
				location_object.setLocationId(String.valueOf(location.get("id")));
				location_object.setLocationLatitude((double) location.get("latitude"));
				location_object.setLocationLongitude((double) location.get("longitude"));
				location_object.setLocationName((String) location.get("name"));
				tag_info = getInstagramTagObject(temp);
				location_object.setLocationMedia(tag_info);
				location_object.setTimestamp(tag_info.getTimestamp());
				insertLocationInDB(location_object);
			} 

		} catch(IOException e) {  
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	} 

	/** 
	 * Inserts a given Instagram Location object in the database
	 * @param location Instagram Location object which is to be inserted in the db
	 */ 

	public void insertLocationInDB(InstaLocation location) { 

		MongoBase mongo = null; 

		try { 

			mongo = new MongoBase();
			mongo.setCollection("Fashion");
			mongo.putInDB(location); 

		} catch (Exception e) { 

			e.printStackTrace(); 

		} finally {  

			if (mongo != null) { 

				mongo.closeConnection();
			}
		}
	} 

	/** 
	 * Converts a given JSON post object into a Instagram Tag object
	 * @param data_object JSONObject which is to be inserted into the database
	 * @return Tag object containing the necessary fields of a Instagram post
	 * @throws ParseException 
	 * @throws IOException 
	 */ 

	public Tag getInstagramTagObject(JSONObject data_object) throws IOException, ParseException { 

		Tag tag = new Tag(); 

		String username = "";
		String profile_picture = "";
		String country = "";
		String full_name = "";
		String user_id = "";
		String image_url;
		int image_height;
		int image_width;
		double created_time = 0.0;
		String tag_id = "";
		String filter = "";
		String type = "";
		String caption_text = "";
		Set<String> tag_set = new HashSet<String>();
		int like_count = 0;
		int comment_count = 0;
		String link = "";
		double latitude = 0.0;
		double longitude = 0.0; 

		Object temp_location = null; 
		Object temp_caption = null;
		Object temp_caption_text = null; 

		JSONObject location;
		JSONObject caption;
		JSONObject likes;
		JSONObject comments;
		JSONObject user;
		JSONObject images; 
		JSONObject standard_resolution; 

		created_time = Double.valueOf((String) data_object.get("created_time"));
		tag_id = (String) data_object.get("id");
		filter = (String) data_object.get("filter");
		type = (String) data_object.get("type");
		link = (String) data_object.get("link");
		tag_set = getTagSet(data_object);
		temp_location = data_object.get("location");

		if (temp_location != null) {

			location = (JSONObject) temp_location; 

			if (location.get("latitude") != null && location.get("longitude") != null) { 

				latitude = (Double) (location.get("latitude"));
				longitude = (Double) (location.get("longitude"));

				GeoFetch geofetch = new GeoFetch();
				GeoData geodata = geofetch.GeoFetchPipeline(String.valueOf(latitude), String.valueOf(longitude));
				country = geodata.getCountryName();
			}
		}

		temp_caption = data_object.get("caption");

		if (temp_caption != null) { 

			caption = (JSONObject) temp_caption;
			temp_caption_text = caption.get("text");

			if (temp_caption_text != null) { 

				caption_text = (String) temp_caption_text;
			}
		}

		likes = (JSONObject) data_object.get("likes");

		if (likes != null) { 

			like_count = (int)(long) likes.get("count");
		}

		comments = (JSONObject) data_object.get("comments");

		if (comments != null) { 

			comment_count = (int)(long) comments.get("count");
		}

		user = (JSONObject) data_object.get("user");
		username = (String) user.get("username");
		full_name = (String) user.get("full_name");
		profile_picture = (String) user.get("profile_picture");
		user_id = (String) user.get("id");

		images = (JSONObject) data_object.get("images");
		standard_resolution = (JSONObject) images.get("standard_resolution");
		image_url = (String) standard_resolution.get("url");
		image_width = (int) (long) standard_resolution.get("width");
		image_height = (int) (long) standard_resolution.get("height");

		tag.setUsername(username);
		tag.setProfilePicture(profile_picture);
		tag.setAuthor(full_name);
		tag.setUserId(user_id);
		tag.setTimestamp(created_time);
		tag.setTagId(tag_id);
		tag.setFilter(filter);
		tag.setType(type);
		tag.setCaptionText(caption_text);
		tag.setLikeCount(like_count);
		tag.setCommentCount(comment_count);
		tag.setLink(link);
		tag.setLatitude(latitude);
		tag.setLongitude(longitude);
		tag.setTagSet(tag_set); 
		tag.setImageURL(image_url);
		tag.setImageHeight(image_height);
		tag.setImageWidth(image_width); 
		tag.setCountry(country);

		return tag;
	} 

	public void run() { 

		fetchLocationPipeline(this.LAT, this.LONG);
	} 

	/** 
	 * Main function to test the function of the class
	 * @param args
	 */

	public static void main(String args[]) { 

		new InstaLocationFetch(47.049,8.3041).start();
	}
}
