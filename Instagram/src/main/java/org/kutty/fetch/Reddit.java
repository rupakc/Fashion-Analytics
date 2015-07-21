package org.kutty.fetch;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;

import com.github.jreddit.entity.Submission;
import com.github.jreddit.entity.User;
import com.github.jreddit.retrieval.Submissions;
import com.github.jreddit.retrieval.params.QuerySyntax;
import com.github.jreddit.retrieval.params.SearchSort;
import com.github.jreddit.retrieval.params.SubmissionSort;
import com.github.jreddit.retrieval.params.TimeSpan;
import com.github.jreddit.utils.restclient.HttpRestClient;
import com.github.jreddit.utils.restclient.RestClient;

import db.MongoBase;

/** 
* @author Rupak Chakraborty
* @for Kutty
* @since 17 July, 2015 
*  
* Class to fetch posts from Reddit prompts relating to a particular Adobe product as well as search the entire 
* spectrum of Reddit prompts to find posts pertaining to a particular Adobe product.
*/ 

public class Reddit extends Thread {

	RestClient restClient; 
	User user;  
	HashMap<String,SubmissionSort> submission_type = new HashMap<String,SubmissionSort>(); 
	HashMap<String,SearchSort> search_type = new HashMap<String,SearchSort>();
	HashMap<String,String> collection_names = new HashMap<String,String>(); 
	
	String product;
	String result_type;
	String collection_name; 
	
	/**
	 * Function to initialize the HashMap collection_names with the corresponding product names and Reddit prompt name
	 * @param filename External file from which a HashMap containing the product and collection names is initialized
	 * @throws IOException
	 */ 
	
	public void init(String filename) throws IOException { 
		
		BufferedReader br;
		FileReader fr;
		String product_alias;
		String collection;
		String s = "";
		
		fr = new FileReader(filename);
		br = new BufferedReader(fr); 
		int index = -1; 
		
		while((s = br.readLine()) != null) { 
			
			index = s.indexOf('=');
			
			if (index != -1) { 
				
				product_alias = s.substring(0, index);
				collection = s.substring(index+1, s.length());
				
				collection_names.put(product_alias, collection);
			}
		}
		
		br.close();
		fr.close();
	} 
	
	/** 
	 * Public constructor to initialize the product name and the result type
	 * @param product Product Name for which the Reddit prompt is to be retrieved
	 * @param result_type String representing the many available result types in jReddit
	 * @throws IOException
	 */ 
	
	public Reddit(String product,String result_type) throws IOException { 
		
		String reddit_prompt = "reddit_prompt_list.txt";
		//init(reddit_prompt);
		restClient = new HttpRestClient();
		restClient.setUserAgent("bot/1.0 by name");

		this.product = product;
		this.result_type = result_type;
		this.collection_name = collection_names.get(this.product); 
		
		submission_type.put("CONTROVERSIAL", SubmissionSort.CONTROVERSIAL);
		submission_type.put("HOT", SubmissionSort.HOT);
		submission_type.put("NEW", SubmissionSort.NEW);
		submission_type.put("RISING", SubmissionSort.RISING);
		submission_type.put("TOP", SubmissionSort.TOP);	

		search_type.put("HOT", SearchSort.HOT);
		search_type.put("NEW", SearchSort.NEW);
		search_type.put("RELEVANCE", SearchSort.RELEVANCE);
		search_type.put("TOP", SearchSort.TOP);
	} 

	/** 
	 * Overloaded public constructor which makes authenticated REST calls
	 * @param username String representing the username for authenticated results
	 * @param password String containing the password to be used
	 * @param product String holding the product name for which the submission has to be fetched
	 * @param result_type String containing the Submission sort of the result type like HOT, NEW etc
	 * @throws IOException
	 */ 
	
	public Reddit(String username,String password,String product,String result_type) throws IOException { 
		
		String reddit_prompt = "reddit_prompt_list.txt";
		//init(reddit_prompt);
		restClient = new HttpRestClient();
		restClient.setUserAgent("bot/1.0 by name"); 

		this.product = product;
		this.result_type = result_type;
		this.collection_name = collection_names.get(this.product);

		try { 

			user = new User(restClient, username, password); 
			user.connect(); 

		} catch (Exception exception) { 

			exception.printStackTrace();
		}

		submission_type.put("CONTROVERSIAL", SubmissionSort.CONTROVERSIAL);
		submission_type.put("HOT", SubmissionSort.HOT);
		submission_type.put("NEW", SubmissionSort.NEW);
		submission_type.put("RISING", SubmissionSort.RISING);
		submission_type.put("TOP", SubmissionSort.TOP);	

		search_type.put("HOT", SearchSort.HOT);
		search_type.put("NEW", SearchSort.NEW);
		search_type.put("RELEVANCE", SearchSort.RELEVANCE);
		search_type.put("TOP", SearchSort.TOP);
	} 

	/**
	 * Returns a List of submissions for a given prompt name and submission sort type
	 * @param prompt_name the Reddit prompt name for which the submissions have to be fetched
	 * @param submisson_sort_type Each submission is attached to a type like NEW, TOP, RISING etc.
	 * @return A List containing the set of submissions for a prompt name and submissio sort type
	 */ 
	
	public List<Submission> getSubmission(String prompt_name,String submisson_sort_type) {

		Submissions subms = new Submissions(restClient);
		List<Submission> reddit;
		submisson_sort_type = submisson_sort_type.toUpperCase(); 

		reddit = subms.ofSubreddit(prompt_name, submission_type.get(submisson_sort_type), -1, 100, null, null, true);

		return reddit;
	} 

	/** 
	 * Searches over all the Reddit prompts to submissions related to a search query 
	 * @param query String representing the query to search for
	 * @param search_sort_type
	 * @return List<Submission> a list containing the submissions
	 */ 
	
	public List<Submission> getSearch(String query,String search_sort_type) {

		Submissions subms = new Submissions(restClient);
		List<Submission> results;
		search_sort_type = search_sort_type.toUpperCase(); 

		results = subms.search(query, QuerySyntax.LUCENE, search_type.get(search_sort_type), TimeSpan.ALL, -1, 100, null, null, true); 

		return results;
	}
	
	/** 
	 * Fetches the submissions from a particular prompt and stores it in MongoDB
	 * @throws UnknownHostException
	 */ 
	
	public void getAndStore() throws UnknownHostException {  

		List<Submission> results;  

		results = getSearch(product,result_type); 
		results.addAll(getSubmission(product,result_type));
		printResults(results);
		MongoBase mongo = new MongoBase();
		mongo.putInDB(results,"Fashion");
		mongo.closeConnection();
	}
	
	/** 
	 * Retrieves submissions from the prompts tagged Relevance, Controversial and Rising
	 * @throws UnknownHostException
	 */ 
	
	public void getAndStoreDefault() throws UnknownHostException { 
		
		List<Submission> results; 
		results = getSearch(product,"RELEVANCE");
		results.addAll(getSubmission(product,"CONTROVERSIAL"));
		results.addAll(getSubmission(product,"RISING")); 
		printResults(results);
		MongoBase mongo = new MongoBase();
		mongo.putInDB(results,"Fashion");
		mongo.closeConnection();
	}
	
	/** 
	 * Prints the submission results obtained in a pretty fashion
	 * @param results A list containing the submission results
	 */ 
	
	public void printResults(List<Submission> results) {

		for (Submission sub : results) { 

			System.out.println("--------------------------------"); 

			System.out.println("Self Text : " + sub.getSelftext()); 
			System.out.println("Author : " + sub.getAuthor());
			System.out.println("Domain : "+ sub.getDomain());
			System.out.println("Full Name : " + sub.getFullName());
			System.out.println("Username : " + sub.getUser());
			System.out.println("Title : " + sub.getTitle());
			System.out.println("Created At : " + sub.getCreatedUTC());
			System.out.println("DownVotes : " + sub.getDownVotes());
			System.out.println("Upvotes : " + sub.getUpVotes());
			System.out.println("Score : " + sub.getScore());
			System.out.println("CommentCount : " + sub.getCommentCount());
			System.out.println("Gilded : " + sub.getGilded());

			System.out.println("--------------------------------");
		}

		System.out.println("Total Size : " + results.size());
	}
	
	/** 
	 * Overloaded run() method of the thread class to implement multithreading
	 */ 
	
	public void run()
	{
		try {
			
			getAndStore(); 
			
		} catch (UnknownHostException e) {
			
			e.printStackTrace();
		}
	}
	
	/** 
	 * Main function to test out the functionality of the class and its methods
	 * @param args
	 * @throws IOException
	 */ 
	
	public static void main(String[] args) throws IOException { 

		Reddit red_hot = new Reddit("femalefashionadvice","hot");
		Reddit red_new = new Reddit("femalefashionadvice","New");
		Reddit red_top = new Reddit("femalefashionadvice","ToP");
		Reddit def = new Reddit("femalefashionadvice","");
		
		red_hot.start();
		red_new.start();
		red_top.start();
		
		def.getAndStoreDefault();
	} 
}