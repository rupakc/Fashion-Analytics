package org.kutty.fetch; 

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.kutty.db.MongoBase;
import org.kutty.dbo.Comment;
import org.kutty.utils.DateConverter;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.services.samples.youtube.cmdline.Auth;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Thumbnail;

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
	public Map<String,String> collection_map = new HashMap<String,String>();
	public Map<String,String> youtube_query_map = new HashMap<String,String>();
	public String product_list_filename = "product_list.txt"; 
	public String query_list_filename = "youtube_queries.txt"; 

	/** 
	 * default public constructor 
	 */ 

	public Tube() { 

	}

	/** 
	 * public constructor to initialize the collection name and the youtube query
	 * @param product String containing the product name
	 * @throws IOException
	 */ 

	public Tube(String product) throws IOException { 

		init(this.product_list_filename,this.collection_map);
		init(this.query_list_filename,this.youtube_query_map);
		this.product = product.toLowerCase().trim();
		this.query = youtube_query_map.get(this.product).trim();
		this.product = collection_map.get(this.product).trim();
	}

	/** 
	 * public constructor to initialize the product name and the search query
	 * @param product String containing the product name
	 * @param query String containing the search query
	 * @throws IOException 
	 */ 

	public Tube(String product,String query) throws IOException { 

		init(this.product_list_filename,this.collection_map);
		this.product = product.toLowerCase().trim();
		this.product = collection_map.get(this.product);
		this.query = query;
	} 

	/** 
	 * public constructor to initialize the product name, search query and api key
	 * @param product String containing the product name
	 * @param query String containing the search query
	 * @param api_key String containing the api key
	 * @throws IOException 
	 */ 

	public Tube(String product,String query,String api_key) throws IOException { 

		init(this.product_list_filename,this.collection_map);
		this.product = product.toLowerCase().trim();
		this.product = collection_map.get(this.product);
		this.query = query;
		this.API_KEY = api_key;
	}

	/** 
	 * public constructor to initialize the product name, query and the api key
	 * @param product String containing the product name
	 * @param query String containing the query term
	 * @param api_key String containing the api key
	 * @param number_of_videos long containing the number of videos returned
	 * @throws IOException 
	 */ 

	public Tube(String product,String query,String api_key,long number_of_videos) throws IOException { 

		init(this.product_list_filename,this.collection_map);
		this.product = product.toLowerCase().trim();
		this.product = collection_map.get(this.product);
		this.query = query;
		this.API_KEY = api_key;
		this.NUMBER_OF_VIDEOS_RETURNED = number_of_videos;
	}

	/** 
	 * Utility function to initialize the list of fashion brands and their associated collection names
	 * @param filename String containing the filename from which the data is to be read
	 * @throws IOException
	 */ 

	public void init(String filename,Map<String,String> name_map) throws IOException
	{ 
		BufferedReader br;
		FileReader fr;
		String alias;
		String collection_name;
		String s = ""; 
		int index; 

		fr = new FileReader(filename);
		br = new BufferedReader(fr);

		while((s = br.readLine()) != null) { 

			index = s.indexOf('=');

			if(index != -1) { 

				alias = s.substring(0,index);
				collection_name = s.substring(index+1,s.length());
				alias = alias.trim();
				collection_name = collection_name.trim();
				name_map.put(alias, collection_name); 
			}
		}

		br.close();
		fr.close();
	}  

	/** 
	 * Retrieves a set of page names from a given file and returns a set
	 * @param filename String containing the filename
	 * @return Set<String> containing the set of fashion queries
	 * @throws IOException
	 */ 

	public Set<String> getFashionQueries(String filename) throws IOException { 

		BufferedReader br;
		FileReader fr;
		String prompt_alias;
		String temp = "";
		Set<String> prompt_names = new HashSet<String>(); 

		fr = new FileReader(filename);
		br = new BufferedReader(fr); 

		while((temp = br.readLine()) != null) { 

			prompt_alias = temp.trim();
			prompt_names.add(prompt_alias);
		}

		br.close();
		fr.close();

		return prompt_names;
	}  

	/**
	 * Returns the query term for a search
	 * @return String containing the query
	 */ 

	public String getQuery() { 

		return query;
	}

	/**
	 * Sets the search query term
	 * @param query the query to set
	 */ 

	public void setQuery(String query) { 

		this.query = query;
	}

	/**
	 * Returns the API_KEY used to make calls
	 * @return the API_KEY
	 */ 

	public String getAPI_KEY() { 

		return API_KEY;
	}

	/**
	 * Sets the API_KEY used to make calls
	 * @param aPI_KEY the aPI_KEY to set
	 */ 

	public void setAPI_KEY(String API_KEY) { 

		this.API_KEY = API_KEY;
	}

	/** 
	 * Returns the product name to be searched for
	 * @return String containing the product name
	 */ 

	public String getProduct() { 

		return product;
	}

	/**
	 * Sets the product name for a given query
	 * @param product the product to set
	 */ 

	public void setProduct(String product) { 

		this.product = product;
	}


	/** 
	 * Overloaded run function to support multi-threading
	 */ 

	public void run() { 

		executeQuery();
	} 

	/** 
	 * Executes a given search query in youtube
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
	 * Prints the search result to console in a pretty fashion
	 * @param iteratorSearchResults Iterator<SearchResult> containing the search results
	 * @param query String containing the query which is used for the search
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
	 * Defines the pipeline for fetching and storing youtube comments in MongoDB
	 * @param searchResults Iterator<SearchResult> containing the iterator of search results
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
	 * Returns the video Id of a given search result
	 * @param rId ResourceId object which contains the video if
	 * @return String containing the videoId
	 */ 

	public String getVideoId(ResourceId rId) { 

		String videoId = ""; 

		if (rId.getKind().equalsIgnoreCase("youtube#video")) { 

			videoId = rId.getVideoId();
		}

		return videoId;
	} 

	/** 
	 * Given a videoId returns the set of comments associated with in a a json response
	 * @param videoId String containing the videoId
	 * @return String containing the JSON response of the comments
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
	 * Parses the JSON response and returns a JSONObject
	 * @param response String containing the JSON response
	 * @return JSONObject containing the parsed JSON response
	 * @throws ParseException
	 */ 

	public JSONObject getParsedResponse(String response) throws ParseException { 

		JSONParser parser = new JSONParser();
		JSONObject parsed_object = (JSONObject) parser.parse(response);

		return parsed_object;

	} 

	/** 
	 * Takes a parsed JSON response and instantiates a comment object to store in MongoDB
	 * @param parsed_response JSONObject containing the parsed JSON response
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
	 * Prints the parsed JSON comment response to console in a pretty fashion
	 * @param parsed_response JSON object containing the parsed response
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

	/** 
	 * For a set of pre-defined fashion queries on youtube defines the pipeline 
	 * for fetching and storing the youtube comments in file 
	 * @param filename String containing the filename
	 * @param collection_name String containing the collection name
	 */ 

	public void fetchFashionTrendPipeline(String filename,String collection_name) { 

		try { 

			Set<String> fashion_queries = getFashionQueries(filename);

			for (String fashion: fashion_queries) { 

				Tube tube = new Tube();
				tube.product = collection_name;
				tube.query = fashion;
				tube.start();
			} 
		} catch(Exception e) { 

			e.printStackTrace();
		}
	} 

	/** 
	 * Main function to test the functionality of the class
	 * @param args
	 */ 

	public static void main(String[] args) {

		Tube tube;
		try { 

			tube = new Tube("H&M");
			tube.start(); 

		} catch (IOException e) { 

			e.printStackTrace();
		}
	}
}