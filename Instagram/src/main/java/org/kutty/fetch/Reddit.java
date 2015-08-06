package org.kutty.fetch;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.kutty.db.MongoBase;

import com.github.jreddit.entity.Submission;
import com.github.jreddit.entity.User;
import com.github.jreddit.retrieval.Submissions;
import com.github.jreddit.retrieval.params.QuerySyntax;
import com.github.jreddit.retrieval.params.SearchSort;
import com.github.jreddit.retrieval.params.SubmissionSort;
import com.github.jreddit.retrieval.params.TimeSpan;
import com.github.jreddit.utils.restclient.HttpRestClient;
import com.github.jreddit.utils.restclient.RestClient;

/** 
 * @author Rupak Chakraborty
 * @for Kutty
 * @since 17 July, 2015 
 *  
 * Class to fetch posts from Reddit prompts relating to fashion trends 
 */ 

public class Reddit extends Thread {

	RestClient restClient; 
	User user;  
	HashMap<String,SubmissionSort> submission_type = new HashMap<String,SubmissionSort>(); 
	HashMap<String,SearchSort> search_type = new HashMap<String,SearchSort>();
	Set<String> prompt_names = new HashSet<String>();

	String prompt;
	String result_type;
	String collection_name; 

	/** 
	 * Returns the prompt name of the current object
	 * @return the prompt String containing the prompt name
	 */ 

	public String getPrompt() { 

		return prompt;
	}

	/** 
	 * Sets the prompt name of the current object
	 * @param prompt String containing the prompt to set
	 */ 

	public void setPrompt(String prompt) { 

		this.prompt = prompt;
	}

	/** 
	 * Returns the type of result to search (i.e. New, Hot, Top) etc.
	 * @return String associated with the result_type
	 */ 

	public String getResultType() { 

		return result_type;
	}

	/**
	 * Sets the Result type of the given search
	 * @param String containing the result_type the result_type to set
	 */ 

	public void setResultType(String result_type) { 

		this.result_type = result_type;
	}

	/**
	 * Returns the database collection name for the given search
	 * @return String containing the collection_name
	 */ 

	public String getCollectionName() { 

		return collection_name;
	}

	/**
	 * Sets the collection name of the given search query
	 * @param collection_name the collection_name to set
	 */ 

	public void setCollectionName(String collection_name) { 

		this.collection_name = collection_name;
	}

	/**
	 * Function to initialize the Set with Reddit prompt names
	 * @param filename External file from which the set of prompt names is to be initialized
	 * @throws IOException
	 */ 

	public void init(String filename) throws IOException { 

		BufferedReader br;
		FileReader fr;
		String prompt_alias;
		String temp = "";

		fr = new FileReader(filename);
		br = new BufferedReader(fr); 

		while((temp = br.readLine()) != null) { 

			prompt_alias = temp.trim();
			prompt_names.add(prompt_alias);
		}

		br.close();
		fr.close();
	} 

	/** 
	 * Public constructor to initialize the prompt name and the result type
	 * @param prompt prompt Name for which the Reddit prompt is to be retrieved
	 * @param result_type String representing the many available result types in jReddit
	 * @throws IOException
	 */ 

	public Reddit(String prompt,String result_type) throws IOException { 

		String reddit_prompt = "reddit_prompt_list.txt";
		init(reddit_prompt);
		restClient = new HttpRestClient();
		restClient.setUserAgent("bot/1.0 by name");

		this.prompt = prompt;
		this.result_type = result_type; 

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
	 * @param prompt String holding the prompt name for which the submission has to be fetched
	 * @param result_type String containing the Submission sort of the result type like HOT, NEW etc
	 * @throws IOException
	 */ 

	public Reddit(String username,String password,String prompt,String result_type) throws IOException { 

		String reddit_prompt = "reddit_prompt_list.txt";
		init(reddit_prompt);
		restClient = new HttpRestClient();
		restClient.setUserAgent("bot/1.0 by name"); 

		this.prompt = prompt;
		this.result_type = result_type;

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
	 * Default public constructor 
	 * @throws IOException 
	 */ 

	public Reddit() throws IOException {

		String reddit_prompt = "reddit_prompt_list.txt";
		init(reddit_prompt);
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

		results = getSearch(prompt,result_type); 
		results.addAll(getSubmission(prompt,result_type));
		//printResults(results);
		MongoBase mongo = null; 
		setCollectionName(collection_name);
		try { 

			mongo = new MongoBase();
			mongo.putInDB(results,collection_name); 

		} catch (Exception e) { 

			e.printStackTrace(); 

		} finally { 

			if (mongo != null) {
				mongo.closeConnection();
			}
		}
	}

	/** 
	 * Retrieves submissions from the prompts tagged Relevance, Controversial and Rising
	 * @throws UnknownHostException
	 */ 

	public void getAndStoreDefault() throws UnknownHostException { 

		List<Submission> results; 
		results = getSearch(prompt,"RELEVANCE");
		results.addAll(getSubmission(prompt,"CONTROVERSIAL"));
		results.addAll(getSubmission(prompt,"RISING")); 
		//printResults(results);
		MongoBase mongo = null; 
		
		try { 
			
			mongo = new MongoBase();
			mongo.putInDB(results,collection_name); 
			
		} catch (Exception e) { 
			
			e.printStackTrace();
		} finally {
			if (mongo != null) {
				mongo.closeConnection();
			}
		}
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
	 * For a given set of reddit prompts defined in the file reddit_prompt_list.txt
	 * retrieves and stores the results prompt results
	 * @throws IOException
	 */ 

	public void fetchTopTrendsReddit(String collection_name) throws IOException { 

		for (String s : prompt_names) { 

			try { 

				redditPipeline(s,collection_name);  

			} catch (Exception e) { 

			}
		}
	} 

	/** 
	 * Defines the reddit storage and retrieval pipeline
	 * @param prompt_name String containing the prompt_name
	 * @throws IOException
	 */ 

	public void redditPipeline(String prompt_name,String collection_name) throws IOException { 

		Reddit red_hot = new Reddit(prompt_name,"hot");
		Reddit red_new = new Reddit(prompt_name,"New");
		Reddit red_top = new Reddit(prompt_name,"ToP");
		Reddit def = new Reddit(prompt_name,"");

		red_hot.collection_name = collection_name;
		red_new.collection_name = collection_name;
		red_top.collection_name = collection_name; 
		def.collection_name = collection_name; 

		red_hot.start();
		red_new.start();
		red_top.start();

		def.getAndStoreDefault();

	} 

	/** 
	 * Main function to test out the functionality of the class and its methods
	 * @param args
	 * @throws IOException
	 */ 

	public static void main(String[] args) throws IOException { 

		Reddit reddit = new Reddit();
		reddit.fetchTopTrendsReddit("Fashion");
	} 
}