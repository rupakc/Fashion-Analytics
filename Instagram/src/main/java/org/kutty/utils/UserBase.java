package org.kutty.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

/** 
 * Class to return HashMap containing country codes and their respective count
 * @author Rupak Chakraborty
 * @for Adobe Systems, India
 * @since 1 June, 2015
 * 
 * TODO - Resolve Known Issue of similar naming cities in different countries 
 */ 

public class UserBase {

	public HashMap<String,String> alpha_map;
	HashMap<String,Set<String>> country_city_map; 

	/** 
	 * public constructor to initialize the country codes alpha map and the country city map
	 * @throws IOException 
	 */ 

	public UserBase() throws IOException { 

		alpha_map = getAlphaCodes("country_alpha.txt");
		country_city_map = getCountryCityMap("country_city_map.txt");

	} 

	/** 
	 * Returns the country name matching the country code
	 * @param code String containing the country code to search
	 * @return String containing the country name
	 */ 

	public String isCountryCode(String code) { 

		code = code.toUpperCase().trim();
		String empty = ""; 

		if (code.equalsIgnoreCase("UK")) { 

			return "United Kingdom";
		} 

		for (String s : alpha_map.keySet()) { 

			if (code.equalsIgnoreCase(alpha_map.get(s))) { 

				return s;
			}
		}

		return empty;
	}

	/** 
	 * Checks if a given location is a country name or not and returns the matching country
	 * @param country String containing the country name which is to be checked
	 * @return String containing the country name
	 */ 

	public String isCountry(String country) { 

		String empty = ""; 

		for (String s : alpha_map.keySet()) { 

			if (s.equalsIgnoreCase(country)) { 

				return s;
			}
		}

		return empty;
	}

	/** 
	 * Returns the country for which the city name exists
	 * @param city String containing the city name
	 * @return Country name for which the city name is found
	 */ 

	public String isCity(String city) { 

		String empty = ""; 

		for (String s : country_city_map.keySet()) { 

			Set<String> temp = country_city_map.get(s); 

			for (String temp_city : temp) { 

				if (temp_city.equalsIgnoreCase(city)) { 

					return s;
				}
			}
		}

		return empty;
	} 

	/** 
	 * Returns the alpha code and country name map for a given filename
	 * @param filename String containing the country alpha codes
	 * @return HashMap<String,String> containing the country name and alpha codes
	 * @throws IOException
	 */ 

	public HashMap<String,String> getAlphaCodes(String filename) throws IOException { 

		BufferedReader br;
		FileReader fr;
		String absolute_path = filename; 

		HashMap<String,String> alpha_map = new HashMap<String,String>();
		int index = -1;
		String temp = ""; 
		String country = "";
		String code = ""; 

		fr = new FileReader(absolute_path);
		br = new BufferedReader(fr);

		while( (temp = br.readLine()) != null) { 

			index = temp.indexOf('\t');

			if (index != -1) { 

				country = temp.substring(0,index);
				code = temp.substring(index+1);
				alpha_map.put(country, code);
			}
		}

		br.close();
		fr.close();

		return alpha_map;
	}

	/** 
	 * Returns HashMap<String,Set<String>> containing the country name and its respective city list
	 * @param filename String containing the filename containing the city names and the country name
	 * @return HashMap<String,Set<String>> containing the country name and its city list
	 * @throws IOException
	 */ 

	public HashMap<String,Set<String>> getCountryCityMap(String filename) throws IOException { 

		HashMap<String,Set<String>> country_city_map = new HashMap<String,Set<String>>(); 
		Set<String> city = null;
		String country = "";
		int index = -1; 
		String absolute_path = filename;
		BufferedReader br; 
		FileReader fr; 
		String temp = "";
		StringTokenizer st;
		String cities = ""; 

		fr = new FileReader(absolute_path);
		br = new BufferedReader(fr);

		while((temp = br.readLine()) != null) { 

			index = temp.indexOf('=');

			if (index != -1) { 

				country = temp.substring(0,index);
				cities = temp.substring(index+1);

				if (cities != null && !cities.isEmpty()) { 

					city = new HashSet<String>();
					st = new StringTokenizer(cities,",");

					while(st.hasMoreTokens()) { 

						city.add(st.nextToken());
					}
				}

				country_city_map.put(country, city);
			}
		}

		br.close();
		fr.close(); 

		return country_city_map;
	}

	/** 
	 * Returns the matching country name of a given tweet location  
	 * @param s String containing the country name
	 * @return country name is there is a match else returns a empty string
	 */ 

	public String matchCountry(String s) { 

		String country = ""; 
		String temp = s;
		StringTokenizer words = new StringTokenizer(temp," "); 
		s = s.trim(); 

		if (s.length() <= 2) { 

			country = isCountryCode(s);
		}

		else if (words.countTokens() == 1) { 

			country = isCountry(s);

			if (country.isEmpty()) { 

				country = isCity(s);
			}

			if(country.isEmpty()) { 

				country = isCountryCode(s);
			}
		} 

		else if (country.isEmpty() && words.countTokens() >= 2 && !s.contains(",")) { 

			String str = "";  

			if (words.countTokens() == 2) { 

				country = isCountry(s);

				if (country.isEmpty()) { 

					country = isCity(s);
				}
			} 

			while(words.hasMoreTokens() && country.isEmpty()) { 

				str = words.nextToken();
				str = str.trim(); 
				str = str.replace(",", ""); 

				country = isCountry(str);

				if (country.isEmpty()) { 

					country = isCity(str);
				}

				if (country.isEmpty()) { 

					country = isCountryCode(str);
				}

				if (!country.isEmpty()) { 

					break;
				}
			}
		}

		else if(country.isEmpty() && s.contains(",")) { 

			StringTokenizer csv = new StringTokenizer(s,",");
			String temp_str = ""; 

			while(csv.hasMoreTokens()) { 

				temp_str = csv.nextToken(); 
				temp_str = temp_str.trim(); 
				temp_str = temp_str.replace(",", ""); 

				country = isCity(temp_str);

				if (country.isEmpty()) { 

					country = isCountry(temp_str);
				}

				if (country.isEmpty()) {  

					country = isCountryCode(temp_str);
				}

				if (!country.isEmpty()) { 

					break;
				}
			}
		}

		return country;
	}
}
