package clean;

import java.util.ArrayList;
import java.util.StringTokenizer;

/** 
 * @author Rupak Chakraborty
 * @for Adobe Systems,India
 * @since 25 Feb, 2015 
 * 
 * Basic Utility class for cleaning the posts fetched from different social channels.
 * The utility functions include removing HTML tags, removing http hyperlinks
 * among other things.
 */ 

public class Clean {

	/**
	 *  Extracts only the RSS content from a given string 
	 * The RSS content fetched from ROMETools API is passed in to this function to extract the content 
	 * 
	 */ 

	public static String extractRSSText(String s)
	{
		int last_closing_bracket = 0;;
		String filtered = ""; 

		for (int i = 0; i < s.length(); i++) { 

			if (s.charAt(i) == '>') { 

				last_closing_bracket = i+1;
			}

			if (s.charAt(i) == '<') { 

				filtered = filtered + "" + s.substring(last_closing_bracket, i);
			}
		}

		return filtered.trim();
	}

	/** 
	 * Utility function to remove some of the most commonly occurring tags in RSS feeds
	 * @param s String to remove the HTML tags from
	 * @return the String without the HTML tags 
	 * 
	 */ 

	public static String cleanHTML(String s) {

		CharSequence target [] = {"&nbsp","&lt","&gt","&amp",";","<strong>","<em>","[1]","</strong>","</em>","<div>","</div>","<b>","</b>","[2]","[3]","...","[img]","[/img]","<u>","</u>","<p>","</p>","\n","\\t","<span>",
				"</span>","[Moved]","<br/>","<a>","</a>","&quot","<br>","<br />","Â","<a rel=\"nofollow\" class=\"ot-hashtag\"","&#39","<a","â€™"}; 
		
		CharSequence replacement = "";

		for (int i = 0; i < target.length; i++){ 

			s = s.replace(target[i], replacement); 
		}

		return s;
	}

	/** 
	 * 
	 * @param s String from which the most commonly occurring punctuation has to be removed
	 * @return the String sans the punctuation and other associated junk 
	 */ 

	public static String removePunctuationAndJunk(String s) {

		CharSequence target[] = {"!!","w/","!","!!!","w/","RT","@","#","/",":)",":(",":D","^_^","^","...",".","&","\\",":","?","<",">","$","%","*","`","~","-","_","+","=","{","}","[","]","|","\"",",",";",")","(","r/","/u/","*","-"};
		CharSequence replacement = "";

		for (int i = 0; i < target.length; i++) { 

			s = s.replace(target[i], replacement);
		}

		return s;
	}

	/** 
	 * Overloaded function to remove hyperlinks from tweets
	 * @param arr ArrayList of tweets to remove URLs from 
	 */ 

	public static void removeLinksFromTweets(ArrayList<String> arr)
	{
		int index = 0; 
		int i; 
		String s = "";

		for(i = 0; i < arr.size(); i++) { 

			s = arr.get(i); 
			index = s.indexOf("http");

			if (index != -1) {  

				s = s.substring(0, index);
				arr.set(i, s);
			}
		}
	}  

	/** 
	 * Specifically designed to remove the URLs from tweets, this is because in twitter the URLs appear at the end
	 * @param s String to remove the URLs from
	 * @return Tweet with the URL removed
	 */ 

	public static String removeLinksFromTweets(String s) { 

		String without_http = s;
		int index = -1;

		index = s.indexOf("http");

		if (index != -1) { 

			without_http = s.substring(0, index);
		}

		return without_http;
	}  

	/** 
	 * Removes the newlines from a given String in order to convert it to a single line string
	 * @param s String to remove the new lines from
	 * @return A single line String with the new lines removed
	 */ 

	public static String removeNewLines(String s) { 

		s = s.replace("\n", " ");

		return s;
	} 

	/** 
	 * Removes the commonly occurring URLs from a given text i.e. those that start with http://,https:// ftp:// etc
	 * @param s String to remove the URL from
	 * @return String with the URL removed 
	 * TODO - More functionality and robust URL removal
	 */ 

	public static String removeURL(String s) {

		// Assumes that the text is space separated which is more often the case 

		StringTokenizer st = new StringTokenizer(s," ");
		String temp = "";
		String without_url = ""; 

		while(st.hasMoreTokens()) { 

			temp = st.nextToken();

			if(!temp.contains("http:") && !temp.contains("https:") && !temp.contains("ftp:") && !temp.contains("www.") && !temp.contains(".com") && !temp.contains(".ly") && !temp.contains(".st")) { 

				without_url = without_url + temp + " ";
			}
		}

		return without_url;
	}

	/** 
	 * Removes the tags from a given string tags mean opening and closing HTM tags
	 * @param s String containing the tags
	 * @return String without the tags
	 */ 

	public static String removeTags(String s) { 

		s = s.replace("<", "");
		s = s.replace(">", "");

		return s;
	}
}
