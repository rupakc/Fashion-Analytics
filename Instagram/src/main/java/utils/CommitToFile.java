package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import clean.Clean;

import com.cybozu.labs.langdetect.LangDetectException;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

import db.MongoBase;
/**
 * 
 * @author Rupak Chakraborty
 * @date 13 March, 2015
 * @for Adobe Systems, India 
 * 
 * Base class for writing tweets/posts/feeds to file for labeling and further processing 
 * The need of this class arose in order to fulfill the need to provide a unified interface to 
 * access the varied content from across the different social channels and write them to the files.
 * 
 */
public class CommitToFile {

	HashMap<String,String> collection_names = new HashMap<String,String>();
	HashMap<String,String> channel_names = new HashMap<String,String>(); 

	public CommitToFile() throws IOException { 

		populateDB("product_list.txt","channel_list.txt");
	}

	public void populateDB() { 

		collection_names.put("photoshop","Photoshop");
		collection_names.put("illustrator","Illustrator");
		collection_names.put("dreamweaver","Dreamweaver");
		collection_names.put("acrobat","Acrobat");
		collection_names.put("lightroom","Lightroom");
		collection_names.put("creativecloud","CreativeCloud");
		collection_names.put("marketingcloud","MarketingCloud");

		channel_names.put("twitter","Twitter");
		channel_names.put("facebook","Facebook");
		channel_names.put("rss","RSS");
		channel_names.put("reddit","Reddit");
	} 

	public void populateDB(String product_list_filename,String channel_list_filename) throws IOException { 

		BufferedReader br;
		FileReader fr; 

		fr = new FileReader(product_list_filename);
		br = new BufferedReader(fr); 

		String alias;
		String value;
		String s = "";
		int index = -1; 

		while ((s = br.readLine()) != null) { 

			index = s.indexOf('=');

			if (index != -1) { 

				alias = s.substring(0, index);
				value = s.substring(index+1,s.length());

				collection_names.put(alias, value);
			}
		}

		br.close();
		fr.close(); 

		fr = new FileReader(channel_list_filename);
		br = new BufferedReader(fr);

		while ((s = br.readLine()) != null) { 

			index = s.indexOf('=');

			if (index != -1) { 

				alias = s.substring(0, index);
				value = s.substring(index+1,s.length());

				channel_names.put(alias, value);
			}
		}

		br.close();
		fr.close(); 
	} 

	public void writeToFile(String collection_name,String channel_name) throws IOException, LangDetectException { 

		MongoBase mongo = new MongoBase(); 
		FileWriter fw;
		BufferedWriter bw; 

		collection_name = collection_name.toLowerCase();
		collection_name = collection_name.trim();
		channel_name = channel_name.toLowerCase();
		channel_name = channel_name.trim();

		collection_name = collection_names.get(collection_name);
		channel_name = channel_names.get(channel_name);

		mongo.setCollection(collection_name);
		DBCollection collection;
		DBObject query;

		collection = mongo.getCollection();
		query = new BasicDBObject("Channel",channel_name);

		@SuppressWarnings("unchecked")

		List<String> distinct_messages = collection.distinct("Message", query);
		String file_name = channel_name + ".txt";
		fw = new FileWriter(file_name,true);
		bw = new BufferedWriter(fw);
		Object temp_obj; 
		
		for(String s:distinct_messages) { 

			temp_obj = s; 
			
			if (temp_obj != null && s.length() > 2) {  
				
				if (channel_name.equalsIgnoreCase("RSS")) { 

					s = Clean.cleanHTML(Clean.extractRSSText(s));
				}

				else if (channel_name.equalsIgnoreCase("Twitter")) { 

					s = Clean.removeLinksFromTweets(s);
				}

				s = Clean.removeNewLines(s);
				s = Clean.removeURL(s);
				LanguageDetector.init("profiles"); 
				
				if (!checkInFile(s,file_name) && LanguageDetector.detect(s).equalsIgnoreCase("en")) { 

					s = "<eop>" + s + "</eop>";
					bw.write(s);
					System.out.println(s);
					bw.newLine();
				}
			}
		}

		bw.close();
		fw.close();
		mongo.closeConnection();
	} 

	public boolean checkInFile(String s,String filename)throws IOException { 

		boolean exists = false;
		BufferedReader br = null;
		FileReader fr = null; 
		String str = ""; 

		try {  

			fr = new FileReader(filename);
			br = new BufferedReader(fr); 

		} catch(Exception e) { 

			e.printStackTrace();
		}  

		while((str = br.readLine()) != null) { 

			if (str.equalsIgnoreCase(s)) { 

				exists = true;
				break;
			}	
		} 

		br.close(); 
		fr.close(); 

		return exists;
	} 

	public static void main(String args[]) throws IOException, LangDetectException { 

		CommitToFile f = new CommitToFile();
		f.writeToFile("MarketingCloud", "Reddit");
	}
}
