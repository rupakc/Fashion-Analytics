package org.kutty.fetch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import dbo.GeoData;

public class GeoFetch {	
	//http://maps.googleapis.com/maps/api/geocode/json?latlng=1.327904258,103.84861408&sensor=true
	
	String base_url = "http://maps.googleapis.com/maps/api/geocode/json?latlng=";
	String latitude;
	String longitude; 
	String endpoint = ""; 
	
	public String getLatitude() { 		
		return latitude;
	} 
	
	public void setLatitude(String latitude) { 		
		this.latitude = latitude;
	} 
	
	public String getLongitude() { 		
		return longitude;
	} 
	
	public void setLongitude(String longitude) { 		
		this.longitude = longitude;
	}
	
	public GeoFetch() { 
		latitude = "";
		longitude = "";
		endpoint = base_url;
	}
	
	public GeoFetch(String latitude,String longitude) { 
		this.latitude = latitude.trim();
		this.longitude = longitude.trim();
		
		endpoint = this.base_url + this.latitude + "," + this.longitude;
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
	
	public JSONObject getParsedJSON(String json_response) throws ParseException { 		
		JSONParser parser = new JSONParser();
		JSONObject parsed_response; 
		
		parsed_response = (JSONObject) parser.parse(json_response);
		
		return parsed_response;
	}
	
	public boolean checkStatus(JSONObject parsed_response) { 		
		String metric = "OK";
		String status = "";
		
		status = (String) parsed_response.get("status");
		
		if (status.equalsIgnoreCase(metric)) { 			
			return true;
		}
		
		return false;
	}
	
	public GeoData GeoFetchPipeline(String latitude,String longitude) throws IOException, ParseException { 		
		this.latitude = latitude.trim();
		this.longitude = longitude.trim();
		
		String suffix = this.latitude + "," + this.longitude;
		endpoint = base_url + suffix;
		
		String json_response = getJSONResponse(this.endpoint);
		JSONObject parsed_response = getParsedJSON(json_response);
		GeoData geodata = new GeoData(); 
		
		if (checkStatus(parsed_response)) { 			
			JSONArray results = (JSONArray) parsed_response.get("results");
			JSONObject data_object = (JSONObject) results.get(0);
			geodata = getGeoDataFromJSON(data_object);
		}
		
		return geodata;
	} 
	
	public GeoData getGeoDataFromJSON(JSONObject data_object) { 		
		GeoData geodata = new GeoData(); 
		
		String country_name = "";
		String country_code = "";
		String place_id = "";
		String street_number = "";
		String route = "";
		String locality = "";
		String neighborhood = "";
		String postal_code = "";
		String formatted_address = "";
		double location_latitude = 0.0;
		double location_longitude = 0.0;
		String location_type = "";
		double viewport_northeast_latitude = 0.0;
		double viewport_northeast_longitude = 0.0;
		double viewport_southwest_latitude = 0.0;
		double viewport_southwest_longitude = 0.0;
		
		JSONObject geometry; 
		JSONObject location;
		JSONObject viewport;
		JSONObject viewport_northeast;
		JSONObject viewport_southwest;
		JSONArray address_components; 
		JSONArray temp_types; 
		JSONObject temp; 
		
		geometry = (JSONObject) data_object.get("geometry");
		location = (JSONObject) geometry.get("location"); 
		address_components = (JSONArray) data_object.get("address_components"); 
		viewport = (JSONObject) geometry.get("viewport");
		viewport_northeast = (JSONObject) viewport.get("northeast");
		viewport_southwest = (JSONObject) viewport.get("southwest");
		
		formatted_address = (String) data_object.get("formatted_address");
		place_id = (String) data_object.get("place_id");
		location_type = (String) geometry.get("location_type");
		location_latitude = (double) location.get("lat");
		location_longitude = (double) location.get("lng");
		viewport_northeast_latitude = (double) viewport_northeast.get("lat");
		viewport_northeast_longitude = (double) viewport_northeast.get("lng");
		viewport_southwest_latitude = (double) viewport_southwest.get("lat");
		viewport_southwest_longitude = (double) viewport_southwest.get("lng");
		
		for (int i = 0; i < address_components.size(); i++) { 			
			temp = (JSONObject) address_components.get(i);
			temp_types = (JSONArray) temp.get("types");
			
			for (int j = 0; j < temp_types.size(); j++) { 				
				String type = (String) temp_types.get(j);
				
				if (type.equalsIgnoreCase("street_number")) { 					
					street_number = ((String) temp.get("long_name")); 
				}
				
				else if (type.equalsIgnoreCase("country")) { 					
					country_name = (String) temp.get("long_name");
					country_code = (String) temp.get("short_name");
				}
				
				else if (type.equalsIgnoreCase("route")) { 					
					route = (String) temp.get("long_name");
				}
				
				else if (type.equalsIgnoreCase("locality")) { 					
					locality = (String) temp.get("long_name");
				}
				
				else if (type.equalsIgnoreCase("neighborhood")) { 					
					neighborhood = (String) temp.get("long_name");
				}
				
				else if (type.equalsIgnoreCase("postal_code")) { 					
					postal_code = (String) temp.get("long_name");
				}
			}
		}
		
		geodata.setCountryCode(country_code);
		geodata.setCountryName(country_name);
		geodata.setFormattedAddress(formatted_address);
		geodata.setLocality(locality);
		geodata.setLocationLatitude(location_latitude);
		geodata.setLocationLongitude(location_longitude);
		geodata.setLocationType(location_type);
		geodata.setNeighborhood(neighborhood);
		geodata.setPlaceId(place_id);
		geodata.setPostalCode(postal_code);
		geodata.setRoute(route);
		geodata.setStreetNumber(street_number);
		geodata.setViewportNortheastLatitude(viewport_northeast_latitude);
		geodata.setViewportNortheastLongitude(viewport_northeast_longitude);
		geodata.setViewportSouthwestLatitude(viewport_southwest_latitude);
		geodata.setViewportSouthwestLongitude(viewport_southwest_longitude);
		
		return geodata;		
	}

	public static void main(String args[]) throws IOException, ParseException { 		
		GeoFetch geo = new GeoFetch("1.327904258","103.84861408");
		System.out.println(geo.GeoFetchPipeline("1.327904258","103.84861408"));
	}	
}
