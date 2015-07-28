package org.kutty.fetch;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.kutty.db.MongoBase;
import org.kutty.utils.LanguageDetector;

import com.cybozu.labs.langdetect.LangDetectException;

import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.Post;
import facebook4j.Reading;
import facebook4j.ResponseList;
import facebook4j.auth.AccessToken;

/** 
 * @author Rupak Chakraborty
 * @for Kutty
 * @since 18 July, 2015 
 * 
 * Main class to Fetch the Posts from Pages of Fashion brands
 * and store the posts in the database (MongoDB in our case)
 * It also has basic functionalities like printing the posts to console and writing them to file
 * 
 */ 

public class FacebookFetch extends Thread {

	Facebook facebook = new FacebookFactory().getInstance(); 
	AccessToken token = null; 
	String product_name;
	String page_name;
	String APP_ID;
	String APP_SECRET;
	String filename;
	HashMap<String,String> product_names = new HashMap<String,String>();
	HashMap<String,String> page_names = new HashMap<String,String>();

	/** 
	 * Utility function to initialize the list of fashion brands and their associated collection names
	 * @param filename String containing the filename from which the data is to be read
	 * @throws IOException
	 */ 

	public void init(String filename,HashMap<String,String> name_map) throws IOException
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

	public FacebookFetch() throws FacebookException, LangDetectException { 

		setToken();	
	} 

	/** 
	 * public constructor to initialize the collection name and the page name 
	 * @param brand_name String containing the brand name
	 * @throws IOException
	 * @throws LangDetectException 
	 * @throws FacebookException 
	 */ 

	public FacebookFetch(String brand_name) throws IOException, FacebookException, LangDetectException { 

		String product_list = "product_list.txt";
		String page_list = "facebook_page_list.txt";
		init(product_list,product_names);
		init(page_list,page_names);

		this.product_name = brand_name.toLowerCase();
		this.page_name = page_names.get(this.product_name);
		this.product_name = product_names.get(this.product_name);

		setToken();	
	} 

	/** 
	 * public constructor to set the access token and initialize the page name and the corresponding product name
	 * @param product_name String containing the product name
	 * @param page_name String containing the facebook page name
	 * @throws FacebookException
	 * @throws LangDetectException
	 * @throws IOException
	 */ 

	public FacebookFetch(String product_name,String page_name) throws FacebookException, LangDetectException, IOException {

		String product_list = "product_list.txt";
		init(product_list,product_names);
		this.product_name = product_name.toLowerCase();
		this.product_name = product_names.get(this.product_name);
		this.page_name = page_name; 

		setToken();	
	} 

	/** 
	 * Overloaded public constructor to set the access token and initialize the page name
	 * and the corresponding product name
	 * Takes the APP_ID and APP_SECRET as input from the user 
	 *  	   
	 * @param product_name String containing the product name 
	 * @param page_name String containing the facebook page name
	 * @param APP_ID String containing the APP_ID
	 * @param APP_SECRET String containing the APP_SECRET
	 * @throws FacebookException
	 * @throws LangDetectException
	 * @throws IOException
	 */ 

	public FacebookFetch(String product_name,String page_name,String APP_ID,String APP_SECRET) throws FacebookException, LangDetectException, IOException {

		String product_list = "product_list.txt";
		init(product_list,product_names);
		this.product_name = product_name.toLowerCase();
		this.product_name = product_names.get(this.product_name);
		this.page_name = page_name;
		this.APP_ID = APP_ID;
		this.APP_SECRET = APP_SECRET;

		setToken(APP_ID,APP_SECRET);	
	} 

	/** 
	 * Overloaded public constructor to set the access token 
	 * and initialize the page name and the corresponding product name
	 * Takes the APP_ID and APP_SECRET as input from the the user specified file 
	 * the format of the file is as follows:
	 * APP_ID=YOUR_APP_ID
	 * APP_SECRET=YOUR_APP_SECRET 
	 * 
	 * @param product_name String containing product name
	 * @param page_name String containing the page name
	 * @param filename String containing the filename
	 * @throws FacebookException
	 * @throws LangDetectException
	 * @throws IOException
	 */

	public FacebookFetch(String product_name,String page_name,String filename) throws FacebookException, LangDetectException, IOException {

		String product_list = "product_list.txt";
		init(product_list,product_names);
		this.product_name = product_name.toLowerCase();
		this.product_name = product_names.get(this.product_name);
		this.page_name = page_name;
		this.filename = filename;

		setToken(filename);	
	} 

	/** 
	 * Overloading run function of the thread class to support multi-threading
	 */ 

	public void run() {  

		ResponseList<Post> response_list;

		try { 

			response_list = getPosts(page_name); 
			printPosts(response_list);
			System.out.println(response_list.size());
			MongoBase mongo = null; 

			try {
				mongo = new MongoBase();
				mongo.putInDB(response_list, product_name); 

			} catch(Exception e) { 

				e.printStackTrace(); 

			} finally { 

				if (mongo != null) {
					
					mongo.closeConnection();
				}
			}

		} catch (FacebookException e) { 

			e.printStackTrace(); 
		}
	} 

	/** 
	 * Set the token of the application with default values 
	 * @throws FacebookException
	 * @throws LangDetectException
	 */ 

	public void setToken() throws FacebookException, LangDetectException {

		facebook.setOAuthAppId("1555807898000214", "1c52a35351f12a10ab18f8aa03d46929"); 
		token = facebook.getOAuthAppAccessToken();
		facebook.setOAuthAccessToken(token);
	}

	/** 
	 * Set the token of the application with user given values in the function 
	 * @param APP_ID String containing APP_ID
	 * @param APP_SECRET String containing the APP_SECRET
	 * @throws FacebookException
	 * @throws LangDetectException
	 */ 

	public void setToken(String APP_ID, String APP_SECRET) throws FacebookException, LangDetectException { 

		facebook.setOAuthAppId(APP_ID, APP_SECRET);
		token = facebook.getOAuthAccessToken();
		facebook.setOAuthAccessToken(token);	 
	}

	/** Read the token value from the filename provided
	 *  the format of the file is as follows:
	 *  APP_ID=YOUR_APP_ID
	 *  APP_SECRET=YOUR_APP_SECRET 
	 *  
	 *  @param filename String containing the file from which the details have to be read
	 *  @throws IOException
	 *  @throws FacebookException
	 *  @throws LangDetectException
	 */ 

	public void setToken(String filename) throws IOException,FacebookException, LangDetectException { 

		FileReader fr;
		BufferedReader br;
		String s = ""; 
		String [] arr = new String[2]; 
		int index = -1; 

		fr = new FileReader(filename);
		br = new BufferedReader(fr);

		for (int i = 0; i < arr.length; i++) { 

			if ((s = br.readLine()) != null) { 

				index = s.indexOf("=");

				if (index != -1) { 

					arr[i] = s.substring(index+1);
				}
			}
		}

		br.close();
		fr.close(); 

		facebook.setOAuthAppId(arr[0], arr[1]);
		token = facebook.getOAuthAccessToken();
		facebook.setOAuthAccessToken(token);
	}

	/** 
	 * Reads the 250 recent posts from a page specified by page_name
	 * @param page_name String containing the page name
	 * @return ResponseLists<Post> containing the list of facebook responses
	 * @throws FacebookException
	 */ 

	public ResponseList<Post> getPosts(String page_name) throws FacebookException
	{

		return (facebook.getFeed(page_name,new Reading().limit(250)));
	}

	/** 
	 * The count of the posts is limited in this case to the user specified number
	 * @param page_name String containing the facebook page name
	 * @param count Integer containing the number of posts to be fetched
	 * @return ResponseList<Post> containing the posts retrieved
	 * @throws FacebookException
	 */ 

	public ResponseList<Post> getPosts(String page_name,int count) throws FacebookException {

		if (count > 0) { 

			return (facebook.getFeed(page_name,new Reading().limit(count))); 

		} else { 

			return null;
		}
	}

	/** 
	 * Same overloaded function which has more parameters like since and until dates for the posts 
	 * @param page_name String containing the facebook page name
	 * @param count Integer containing the number of posts to be retrived
	 * @param since String containing the since date in PHP format
	 * @param until String containing the until date in PHP format
	 * @return ResponseList<Post> containing the set of facebook repsonses
	 * @throws FacebookException
	 */ 

	public ResponseList<Post> getPosts(String page_name,int count,String since,String until) throws FacebookException {

		if (count > 0) { 

			return (facebook.getFeed(page_name,new Reading().limit(count).since(since).until(until))); 

		} else { 

			return null;
		}
	}

	/** 
	 * Prints the posts and its associated content in a pretty way 
	 * @param response_list ResponseList<Post> containing the set of facebook responses
	 */ 

	public void printPosts(ResponseList<Post> response_list) {

		for (Post post: response_list) {  

			System.out.println("-----------------------------------------------------");             
			System.out.println("Message : " + post.getMessage());
			System.out.println("Date Posted : " + post.getCreatedTime());
			System.out.println("Post Id : " + post.getId());
			System.out.println("Likes on the post : " + post.getLikes().getCount());
			System.out.println("Name of the author : " + post.getName());
			System.out.println("Comments on the post : " + post.getComments().getCount());
			System.out.println("From Name :" + post.getFrom().getName());
			System.out.println("Place Name : " + post.getPlace());
			System.out.println("Message Tags : " + post.getMessageTags());
			System.out.println("Number of shares : " + post.getSharesCount());
			System.out.println("Post Description : " + post.getDescription());
			System.out.println("Post MetaData : " + post.getMetadata());
			System.out.println("Post Object ID : " + post.getObjectId());
			System.out.println("Post Story : " + post.getStory()); 
			System.out.println("Post Properties : " + post.getProperties());
			System.out.println("Post Actions : " + post.getActions());
			System.out.println("Post Schedule Publish Time : " + post.getScheduledPublishTime());
			System.out.println("Post Caption : " + post.getCaption());
			System.out.println("Post Application : " + post.getApplication());
			System.out.println("Number of Posts fetched : " + response_list.size()); 
			System.out.println("----------------------------------------------------"); 

		}
	} 

	/** 
	 * Writes the posts to a file for further pre-processing (or any use whatsoever) 
	 * @param response_list ResponseList<Post> containing the list of facebook responses
	 * @param file_name String containing the filename to which it is to be written
	 * @throws LangDetectException
	 * @throws IOException
	 */ 

	public void writeMessagesToFile(ResponseList<Post> response_list, String file_name) throws LangDetectException,IOException
	{	
		FileWriter fw = new FileWriter(file_name,true);
		BufferedWriter bw = new BufferedWriter(fw);
		String m = ""; 

		for(Post post: response_list) {  

			m = post.getMessage();

			if (LanguageDetector.detect(m).equalsIgnoreCase("en") && !checkExists(m,file_name)) {  

				bw.write("<eop>");
				bw.write(m);
				bw.write("</eop>");
				bw.newLine();
			}
		}

		bw.close();
		fw.close();
	}

	/** 
	 * Checks if a given String already exists in the file or not
	 * @param s String containing the post details
	 * @param filename String containing the filename
	 * @return true if the post already exists false otherwise
	 * @throws IOException
	 */ 

	public  boolean checkExists(String s,String filename) throws IOException
	{
		ArrayList<String> message_list = new ArrayList<String>();
		BufferedReader br;
		FileReader fr;
		String temp;

		fr = new FileReader(filename);
		br = new BufferedReader(fr); 

		while((temp = br.readLine()) != null) { 

			message_list.add(temp);
		}

		br.close();
		fr.close();  

		for (int i = 0; i < message_list.size(); i++) {

			if(s.equalsIgnoreCase(message_list.get(i))) { 

				return true;
			}
		}

		return false;
	}

	/** 
	 * Retrieves a set of page names from a given file and returns a set
	 * @param filename String containing the filename
	 * @return Set<String> containing the set of page names
	 * @throws IOException
	 */ 

	public Set<String> getFashionPageNames(String filename) throws IOException { 

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
	 * Defines the facebook pipeline for fetching and storing the facebook fashion page tags
	 * @param filename
	 * @throws IOException
	 * @throws FacebookException
	 * @throws LangDetectException
	 */ 

	public void facebookPipelineForFashionTrends(String filename,String collection_name) throws IOException, FacebookException, LangDetectException { 

		Set<String> fashion_names = getFashionPageNames(filename); 

		for (String fashion : fashion_names) {  

			FacebookFetch fb = new FacebookFetch();
			fb.page_name = fashion;
			fb.product_name = collection_name;
			fb.start();
		}
	} 

	/** 
	 * Main function to check the functionality of the class
	 * @param args
	 * @throws LangDetectException
	 * @throws FacebookException
	 * @throws IOException
	 */ 

	public static void main(String args[])throws LangDetectException, FacebookException, IOException {

		FacebookFetch fb = new FacebookFetch("Forever21");
		fb.start();
		//fb.facebookPipelineForFashionTrends("facebook_fashion_list.txt","Fashion");
	}
}
