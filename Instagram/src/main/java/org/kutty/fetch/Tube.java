package org.kutty.fetch; 

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import utils.DateConverter;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.services.samples.youtube.cmdline.Auth;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Thumbnail;

import db.MongoBase;
import dbo.Comment;

/** 
 * Fetches comments from youtube videos using Youtube Date API version 3.0 and Stores them in MongoDB
 * @author Rupak Chakraborty
 * @for Kutty 
 * @since 18 July, 2015
 *
 */

public class Tube extends Thread {


	private long NUMBER_OF_VIDEOS_RETURNED = 50;
	private YouTube youtube;
	public String query; 
	private String API_KEY = "AIzaSyAHiRQFPc3JmhIR2qzZjdlN-1Vnfli7mWU";
	public String product; 
	
	/** 
	 * 
	 * @param product
	 * @param query
	 */ 
	
	public Tube(String product,String query) { 

		this.product = product;
		this.query = query;
	} 
	
	/** 
	 * 
	 * @param product
	 * @param query
	 * @param api_key
	 */ 
	
	public Tube(String product,String query,String api_key) { 

		this.product = product;
		this.query = query;
		this.API_KEY = api_key;
	}
	
	/** 
	 * 
	 * @param product
	 * @param query
	 * @param api_key
	 * @param number_of_videos
	 */ 
	
	public Tube(String product,String query,String api_key,long number_of_videos) { 

		this.product = product;
		this.query = query;
		this.API_KEY = api_key;
		this.NUMBER_OF_VIDEOS_RETURNED = number_of_videos;
	}


	/**
	 * @return the query
	 */ 

	public String getQuery() { 

		return query;
	}

	/**
	 * @param query the query to set
	 */ 

	public void setQuery(String query) { 

		this.query = query;
	}

	/**
	 * @return the aPI_KEY
	 */ 

	public String getAPI_KEY() { 

		return API_KEY;
	}

	/**
	 * @param aPI_KEY the aPI_KEY to set
	 */
	public void setAPI_KEY(String API_KEY) { 

		this.API_KEY = API_KEY;
	}

	/**
	 * @return the product
	 */ 

	public String getProduct() { 

		return product;
	}

	/**
	 * @param product the product to set
	 */ 

	public void setProduct(String product) { 

		this.product = product;
	}

	
	/** 
	 * 
	 */ 
	
	public void run() { 
		
		executeQuery();
	} 
	
	/** 
	 * 
	 */ 
	
	public void executeQuery() {  

		try { 

			youtube = new YouTube.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY, new HttpRequestInitializer() {
				public void initialize(HttpRequest request) throws IOException {
				}}).setApplicationName("youtube-cmdline-search-sample").build();

			String queryTerm = this.query;
			YouTube.Search.List search = youtube.search().list("id,snippet");
			String apiKey = this.API_KEY;
			search.setKey(apiKey);
			search.setQ(queryTerm);
			search.setType("video");
			search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)");
			search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);
			SearchListResponse searchResponse = search.execute();
			List<SearchResult> searchResultList = searchResponse.getItems(); 

			if (searchResultList != null) { 

				fetchPipeline(searchResultList.iterator());
			} 

		} catch (GoogleJsonResponseException e) { 

			System.err.println("There was a service error: " + e.getDetails().getCode() + " : " + e.getDetails().getMessage()); 

		} catch (IOException e) { 

			System.err.println("There was an IO error: " + e.getCause() + " : " + e.getMessage()); 

		} catch (Throwable t) { 

			t.printStackTrace();
		}

	}
	
	/** 
	 * 
	 * @param iteratorSearchResults
	 * @param query
	 * @throws IOException
	 */ 
	
	public void prettyPrintResult(Iterator<SearchResult> iteratorSearchResults, String query) throws IOException {

		System.out.println("\n=============================================================");
		System.out.println("First " + NUMBER_OF_VIDEOS_RETURNED + " videos for search on \"" + query + "\".");
		System.out.println("=============================================================\n");

		if (!iteratorSearchResults.hasNext()) {
			System.out.println(" There aren't any results for your query.");
		}

		while (iteratorSearchResults.hasNext()) {

			SearchResult singleVideo = iteratorSearchResults.next();
			ResourceId rId = singleVideo.getId();

			if (rId.getKind().equals("youtube#video")) { 

				Thumbnail thumbnail = singleVideo.getSnippet().getThumbnails().getDefault();
				System.out.println(" Video Id : " + rId.getVideoId());
				System.out.println(" Title: " + singleVideo.getSnippet().getTitle());
				System.out.println(" Thumbnail: " + thumbnail.getUrl());
				System.out.println("Channel Title : " + singleVideo.getSnippet().getChannelTitle());
				System.out.println("\n-------------------------------------------------------------\n");
			}
		}
	}
	
	/** 
	 * 
	 * @param searchResults
	 * @throws IOException
	 * @throws ParseException
	 */ 
	
	public void fetchPipeline(Iterator<SearchResult> searchResults) throws IOException, ParseException { 

		if (!searchResults.hasNext()) { 

			System.out.println("No Search Results Found");
		}

		while (searchResults.hasNext()) { 

			SearchResult video = searchResults.next();
			ResourceId rId = video.getId();

			if (rId.getKind().equals("youtube#video")) { 

				try { 

					String videoId = rId.getVideoId();
					String response = getJsonResponse(videoId);
					JSONObject json_object = getParsedResponse(response);
					getAndStoreParsedResponse(json_object);  

				} catch (Exception e) {  

					e.printStackTrace();
				}
			}
		}
	} 
	
	/** 
	 * 
	 * @param rId
	 * @return
	 */ 
	
	public String getVideoId(ResourceId rId) { 

		String videoId = ""; 

		if (rId.getKind().equalsIgnoreCase("youtube#video")) { 

			videoId = rId.getVideoId();
		}

		return videoId;
	} 
	
	/** 
	 * 
	 * @param videoId
	 * @return
	 * @throws IOException
	 */ 
	
	public String getJsonResponse(String videoId) throws IOException { 

		String base_url = "https://www.googleapis.com/youtube/v3/commentThreads?part=snippet&maxResults=100&videoId=";
		String key_part = "&key=" + this.API_KEY;
		String full_url = base_url + videoId + key_part;
		String response = ""; 
		String temp = ""; 

		URL url = new URL(full_url);
		BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));

		while((temp = br.readLine()) != null) { 

			response = response + temp;
		}

		return response;
	}  
	
	/**
	 * 
	 * @param response
	 * @return
	 * @throws ParseException
	 */ 
	
	public JSONObject getParsedResponse(String response) throws ParseException { 

		JSONParser parser = new JSONParser();
		JSONObject parsed_object = (JSONObject) parser.parse(response);

		return parsed_object;

	} 
	
	/** 
	 * 
	 * @param parsed_response
	 * @throws UnknownHostException
	 */ 
	
	public void getAndStoreParsedResponse(JSONObject parsed_response) throws UnknownHostException { 

		JSONArray jarray = (JSONArray) parsed_response.get("items");
		Comment comment; 
		MongoBase mongo = new MongoBase(); 
		mongo.setCollection(product); 

		for (int i = 0; i < jarray.size(); i++) { 

			comment = new Comment(); 
			JSONObject temp_obj = (JSONObject) jarray.get(i);
			temp_obj = (JSONObject) temp_obj.get("snippet");
			JSONObject top_level = (JSONObject) temp_obj.get("topLevelComment");
			JSONObject snippet_obj = (JSONObject) top_level.get("snippet");

			comment.setReplyCount((int)(long) temp_obj.get("totalReplyCount"));
			comment.setChannelId((String) temp_obj.get("channelId"));
			comment.setVideoId((String) temp_obj.get("videoId"));
			comment.setCommentId((String) top_level.get("id"));
			comment.setMessage((String) snippet_obj.get("textDisplay"));
			comment.setAuthor((String) snippet_obj.get("authorDisplayName"));
			comment.setTimeStamp(DateConverter.getJavaDate((String) snippet_obj.get("publishedAt")));
			comment.setUpdatedAt(DateConverter.getJavaDate((String) snippet_obj.get("updatedAt")));
			comment.setLikeCount((int)(long) snippet_obj.get("likeCount"));
			comment.setViewerRating((String) snippet_obj.get("viewerRating"));

			mongo.putInDB(comment);
		}

		mongo.closeConnection();
	}
	
	/** 
	 * 
	 * @param parsed_response
	 */ 
	
	public void prettyPrintResponse(JSONObject parsed_response) { 

		JSONArray jarray = (JSONArray) parsed_response.get("items"); 

		for (int i = 0; i < jarray.size(); i++) { 

			JSONObject temp_obj = (JSONObject) jarray.get(i);
			temp_obj = (JSONObject) temp_obj.get("snippet");
			System.out.println("=============================================================");
			System.out.println("Total Reply Count : " + temp_obj.get("totalReplyCount"));
			System.out.println("ChannelId : " + temp_obj.get("channelId"));
			System.out.println("VideoId : " + temp_obj.get("videoId"));
			JSONObject top_level = (JSONObject) temp_obj.get("topLevelComment");
			System.out.println("Comment id : " + top_level.get("id"));
			JSONObject snippet_obj = (JSONObject) top_level.get("snippet");
			System.out.println("Text Display : " + snippet_obj.get("textDisplay"));
			System.out.println("Author Name : " + snippet_obj.get("authorDisplayName"));
			System.out.println("Published At : " + snippet_obj.get("publishedAt"));
			System.out.println("Updated At : " + snippet_obj.get("updatedAt"));
			System.out.println("Like Count : " + snippet_obj.get("likeCount"));
			System.out.println("Viewer Rating : " + snippet_obj.get("viewerRating"));
			System.out.println("=============================================================");
		}
	}
	
	public static void main(String[] args) {

		Tube tube = new Tube("CreativeCloud","Adobe creative cloud");
		tube.start();
	}
}