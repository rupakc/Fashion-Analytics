package org.kutty.fetch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import db.MongoBase;
import dbo.GeoData;
import dbo.InstaComment;
import dbo.InstaLike;
import dbo.Tag;


/** 
 * Fetches the data from Instagram corresponding to a particular tag
 * Parses the json response and then further analyzes it 
 * 
 * @author Rupak
 * @for Kutty 
 * @since 11 July, 2015 
 *  
 */ 

public class TagFetch extends Thread{

	public String base_url = "https://api.instagram.com/v1/tags/";
	public String access_token = "1058271351.5b9e1e6.c1ba660f72704f8d98c48340e3029e9e";
	public String tag = "fashion";
	public String suffix_url = "/media/recent?access_token=";

	/** 
	 * public constructor to initialize the tag name
	 * @param access_token String containing the tag name
	 */ 

	public TagFetch(String tag_name) {  

		this.tag = tag_name;
	} 

	/** 
	 * public constructor to initialize the tag name and the access token
	 * @param tag String containing the tag name
	 * @param access_token String containing the access-token
	 */ 

	public TagFetch(String tag,String access_token) { 

		this.tag = tag;
		this.access_token = access_token;
	}

	/** 
	 * public default constructor to initialize the object
	 */ 

	public TagFetch() { 

	}

	/** 
	 * Returns the string containing the Json response to a particular query
	 * @param query String containing the query term
	 * @return String containing the Json response
	 * @throws IOException
	 */ 

	public String getSearchResponse(String query) throws IOException { 

		String base_url = "https://api.instagram.com/v1/tags/search?q=";
		String suffix_url = "&access_token=";
		String link = base_url + query + suffix_url + this.access_token;

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
	 * Given a tag returns the first page of the JSON response
	 * @param tag String containing the tag name which is to be searched
	 * @return String containing the JSON response
	 * @throws IOException
	 */ 

	public String getJSONTag(String tag) throws IOException { 

		String url; 
		BufferedReader br;
		String temp = "";
		String response = ""; 

		url = base_url + tag + suffix_url + access_token;
		URL instagram = new URL(url);

		br = new BufferedReader(new InputStreamReader(instagram.openStream()));

		while( (temp = br.readLine()) != null) { 

			response = response + temp;
		}

		return response;
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
	 * Given a JSON response of the tag endpoint API returns the url to the next page
	 * @param response String containing the Json response
	 * @return String containing the next URL
	 * @throws ParseException
	 */ 

	public String getNextURL(String response) throws ParseException { 

		String next_url = "";
		JSONParser parser = new JSONParser(); 
		JSONObject parsed_object = (JSONObject) parser.parse(response);
		JSONObject pagination = (JSONObject) parsed_object.get("pagination");

		next_url = (String) pagination.get("next_url");

		return next_url;
	}

	/** 
	 * Returns a Set of tags from a given JSON data array
	 * @param data_array JSONArray containing the set of data per response
	 * @return Set<String> containing the collection of tags
	 */ 

	public Set<String> getTagSet(JSONArray data_array) throws IOException { 

		Set<String> tag_set = new HashSet<String>();
		JSONObject temp;
		JSONArray tag_array;

		for (int i = 0; i < data_array.size(); i++) { 

			temp = (JSONObject) data_array.get(i);
			tag_array = (JSONArray) temp.get("tags");

			for (int j = 0; j < tag_array.size(); j++) {  

				tag_set.add((String) tag_array.get(j));
			}
		}

		return tag_set;
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
	 * Returns a Set of caption text from a given JSON data array
	 * @param data_array JSONArray containing the set of data per response
	 * @return Set<String> containing the collection of caption text
	 */ 

	@SuppressWarnings("unused")
	public Set<String> getCaptionTimeText(JSONArray data_array) { 

		JSONObject temp;
		JSONObject caption_object;
		String text = "";
		Set<String> text_set = new HashSet<String>();
		Object temp_object = null; 

		for (int i = 0; i < data_array.size(); i++) { 

			temp = (JSONObject) data_array.get(i);
			caption_object = (JSONObject) temp.get("caption"); 

			if ( caption_object != null && ((temp_object = caption_object.get("text")) != null) ) { 

				text = (String) caption_object.get("text");
				text_set.add(text);
			}
		}

		return text_set;
	}

	/** 
	 * Returns a HashMap containing the top tags along with their associated counts
	 * @param query String containing the query-term for which the tags have to be retrieved
	 * @return HashMap<String,Long> containing the mapping between the tag name and its count
	 * @throws IOException
	 * @throws ParseException
	 */ 

	public HashMap<String,Long> getTopTags(String query) throws IOException, ParseException { 

		HashMap<String,Long> tag_map = new HashMap<String,Long>();
		String response = getSearchResponse(query);
		JSONArray data_array = getParsedDataArray(response,"data");
		String name = "";
		Long media_count;
		JSONObject temp; 

		for (int i = 0; i < data_array.size(); i++) { 

			temp = (JSONObject) data_array.get(i);
			media_count = (Long) temp.get("media_count");
			name = (String) temp.get("name");

			tag_map.put(name, media_count);
		}

		return tag_map;
	} 

	/** 
	 * Defines the tag processing pipeline for a given tag name
	 * @param tag_name String containing the tag name
	 * @throws IOException 
	 * @throws ParseException
	 */ 

	public void TagPipeline(String tag_name) throws IOException, ParseException { 

		String response = getJSONTag(tag_name);
		String next_url = getNextURL(response);
		int page_count = 1; 
		UserFetch userfetch = new UserFetch();

		while(!next_url.isEmpty()) { 

			JSONArray data_array = getParsedDataArray(response,"data");
			System.out.println("==================== Tag set ===================\n");
			System.out.println(getTagSet(data_array));
			System.out.println("\n==============================================\n");
			System.out.println("====================== Caption Text ==================\n");
			System.out.println(getCaptionTimeText(data_array));
			System.out.println("\n======================================================\n");
			response = getJSONResponse(next_url);
			next_url = getNextURL(response);
			System.out.println("Page Count : " + page_count++);
			System.out.println("Next URL : " + next_url);
			System.out.println();  

			long comment_count = 0;
			long like_count = 0; 

			JSONObject data_object;
			JSONObject comment_object;
			JSONObject like_object;
			JSONArray comment_data_array;
			JSONArray like_data_array;

			Tag tag; 
			InstaComment comment; 
			InstaLike like; 

			for (int i = 0; i < data_array.size(); i++) { 

				data_object = (JSONObject) data_array.get(i);
				tag = getInstagramTagObject(data_object);

				comment_object = (JSONObject) data_object.get("comments");
				comment_count = (Long) comment_object.get("count"); 
				like_object = (JSONObject) data_object.get("likes"); 
				like_count = (Long) like_object.get("count"); 

				if (comment_count > 0) { 

					comment_data_array = (JSONArray) comment_object.get("data");

					for (int j = 0; j < comment_data_array.size(); j++) { 

						comment = getInstagramCommentObject((JSONObject) comment_data_array.get(j));
						comment.setTagId(tag.getTagId());
						comment.setUsernameTag(tag.getAuthor()); 

						insertInstagramCommentObjectInDB(comment,tag_name);
					}
				}

				if (like_count > 0) { 

					like_data_array = (JSONArray) like_object.get("data"); 

					for (int k = 0; k < like_data_array.size(); k++) { 

						like = getInstagramLikeObject((JSONObject) like_data_array.get(k));
						like.setTagId(tag.getTagId());
						like.setUserTag(tag.getAuthor());
						like.setTimestamp(tag.getTimestamp());

						insertInstagramLikeObjectInDB(like,tag_name);
					} 
				}

				userfetch.fetchAndStore(tag.getUserId());
				insertInstagramTagObjectInDB(tag,tag_name);
			} 

			if (page_count > 10) { 

				break;
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

	/** 
	 * Converts a JSONObject into a Instagram Comment object for easy insertion in the data set
	 * @param data_object JSONObject which is to be converted into the Instagram Comment object
	 * @return InstaComment object containing the necessary details of a Instagram Comment
	 */ 

	public InstaComment getInstagramCommentObject(JSONObject data_object) { 

		InstaComment comment_object = new InstaComment(); 
		String text;
		String author;
		double created_time;
		String comment_id;
		JSONObject from; 

		created_time = Double.valueOf((String) data_object.get("created_time"));
		text = (String) data_object.get("text");
		comment_id = (String) data_object.get("id");

		from = (JSONObject) data_object.get("from");
		author = (String) from.get("full_name");  

		comment_object.setAuthor(author);
		comment_object.setCommentId(comment_id);
		comment_object.setText(text);
		comment_object.setTimestamp(created_time);

		return comment_object;
	} 

	/** 
	 * Converts a given JSON response object into an Instagram Like object for easy insertion in the database
	 * @param like_object JSONObject which is to be converted
	 * @return InstaLike object containing the necessary fields
	 */ 

	public InstaLike getInstagramLikeObject(JSONObject like_object) { 

		InstaLike like = new InstaLike();

		String full_name = "";
		String username = "";
		String like_id;

		full_name = (String) like_object.get("full_name");
		username = (String) like_object.get("username");
		like_id = (String) like_object.get("id");

		like.setAuthor(full_name);
		like.setUsernameLike(username);
		like.setLikeId(like_id);

		return like;
	} 

	/** 
	 * Inserts a given Instagram Tag object in the database
	 * @param tag Instagram tag object which is to be inserted in the database
	 * @param query_tag  String containing the query_tag which was used to search the object
	 * @throws UnknownHostException
	 */ 

	public void insertInstagramTagObjectInDB(Tag tag,String query_tag) throws UnknownHostException { 

		MongoBase mongo = new MongoBase(); 
		mongo.setCollection("Fashion");
		mongo.putInDB(tag, query_tag);
		mongo.closeConnection();
	}

	/** 
	 * Inserts a given Instagram Comment Object in the database
	 * @param comment Instagram Comment Object which is to be inserted
	 * @param query_tag String containing the query tag which was used to search the object
	 * @throws UnknownHostException
	 */ 

	public void insertInstagramCommentObjectInDB(InstaComment comment,String query_tag) throws UnknownHostException { 

		MongoBase mongo = new MongoBase();
		mongo.setCollection("Fashion");
		mongo.putInDB(comment, query_tag);
		mongo.closeConnection();
	}

	/** 
	 * Inserts a given Instagram Like object in the database
	 * @param like Instagram Like object which is to be inserted
	 * @param query_tag String containing the query tag which was used to search for the given like object
	 * @throws UnknownHostException
	 */ 

	public void insertInstagramLikeObjectInDB(InstaLike like,String query_tag) throws UnknownHostException { 

		MongoBase mongo = new MongoBase();
		mongo.setCollection("Fashion");
		mongo.putInDB(like, query_tag);
		mongo.closeConnection();
	} 

	/** 
	 * Defines the tag retrieval pipeline for a given search query
	 * @param query String containing the search query
	 * @throws IOException
	 * @throws ParseException
	 */ 

	public void TagPipelineForTopTags(String query) throws IOException, ParseException { 

		HashMap<String,Long> tag_map;
		tag_map = getTopTags(query);

		for (String s : tag_map.keySet()) { 

			TagPipeline(s);
		}
	} 

	/** 
	 * Overloaded run function of the thread class to support multithreading
	 */ 

	public void run() { 

		try { 

			TagPipeline(this.tag); 

		} catch (IOException | ParseException e) {

			e.printStackTrace();
		}
	}

	/** 
	 * Main function to test the functionality of the class
	 * @param args 
	 * @throws IOException
	 * @throws ParseException
	 */ 

	public static void main(String args[]) throws IOException, ParseException { 

		TagFetch tags = new TagFetch("levis");
		tags.start();
	}
}
