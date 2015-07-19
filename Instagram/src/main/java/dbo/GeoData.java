package dbo;

/** 
 * Defines the Geodata object which contains the necessary fields for Geographic data
 * @author Rupak Chakraborty
 * @for Kutty
 * @since 19 July, 2015
 */ 

public class GeoData {

	String country_name;
	String country_code;
	String place_id;
	String street_number;
	String route;
	String locality;
	String neighborhood;
	String postal_code;
	String formatted_address;
	double location_latitude;
	double location_longitude;
	String location_type;
	double viewport_northeast_latitude;
	double viewport_northeast_longitude;
	double viewport_southwest_latitude;
	double viewport_southwest_longitude; 
	
	/** 
	 * 
	 */ 
	
	public GeoData() { 
		
		country_name = "";
		country_code = "";
		place_id = "";
		street_number = "";
		route = "";
		locality = "";
		neighborhood = "";
		postal_code = "";
		formatted_address = "";
		location_latitude = 0.0;
		location_longitude = 0.0;
		location_type = "";
		viewport_northeast_latitude = 0.0;
		viewport_northeast_longitude = 0.0;
		viewport_southwest_latitude = 0.0;
		viewport_southwest_longitude = 0.0;
	} 
	
	/** 
	 * 
	 * @return
	 */ 
	
	public String getCountryName() { 
		
		return country_name;
	} 
	
	/** 
	 * 
	 * @param country_name
	 */ 
	
	public void setCountryName(String country_name) { 
		
		this.country_name = country_name;
	} 
	
	/** 
	 * 
	 * @return
	 */
	
	public String getCountryCode() { 
		
		return country_code;
	} 
	
	/** 
	 * 
	 * @param country_code
	 */ 
	
	public void setCountryCode(String country_code) { 
		
		this.country_code = country_code;
	} 
	
	/** 
	 * 
	 * @return
	 */ 
	
	public String getPlaceId() { 
		
		return place_id;
	} 
	
	/** 
	 * 
	 * @param place_id
	 */ 
	
	public void setPlaceId(String place_id) { 
		
		this.place_id = place_id;
	} 
	
	/** 
	 * 
	 * @return
	 */ 
	
	public String getStreetNumber() { 
		
		return street_number;
	} 
	
	/** 
	 * 
	 * @param street_number
	 */ 
	
	public void setStreetNumber(String street_number) { 
		
		this.street_number = street_number;
	} 
	
	/** 
	 * 
	 * @return
	 */ 
	
	public String getRoute() { 
		
		return route;
	} 
	
	/** 
	 * 
	 * @param route
	 */ 
	
	public void setRoute(String route) { 
		
		this.route = route;
	} 
	
	/** 
	 * 
	 * @return
	 */ 
	
	public String getLocality() { 
		
		return locality;
	} 
	
	/** 
	 * 
	 * @param locality
	 */ 
	
	public void setLocality(String locality) { 
		
		this.locality = locality;
	} 
	
	/** 
	 * 
	 * @return
	 */ 
	
	public String getNeighborhood() { 
		
		return neighborhood;
	} 
	
	/** 
	 * 
	 * @param neighborhood
	 */ 
	
	public void setNeighborhood(String neighborhood) { 
		
		this.neighborhood = neighborhood;
	} 
	
	/** 
	 * 
	 * @return
	 */ 
	
	public String getPostalCode() { 
		
		return postal_code;
	} 
	
	/** 
	 * 
	 * @param postal_code
	 */ 
	
	public void setPostalCode(String postal_code) { 
		
		this.postal_code = postal_code;
	} 
	
	/** 
	 * 
	 * @return
	 */ 
	
	public String getFormattedAddress() { 
		
		return formatted_address;
	} 
	
	/** 
	 * 
	 * @param formatted_address
	 */ 
	
	public void setFormattedAddress(String formatted_address) { 
		
		this.formatted_address = formatted_address;
	} 
	
	/** 
	 * 
	 * @return
	 */ 
	
	public double getLocationLatitude() { 
		
		return location_latitude;
	} 
	
	/** 
	 * 
	 * @param location_latitude
	 */ 
	
	public void setLocationLatitude(double location_latitude) { 
		
		this.location_latitude = location_latitude;
	} 
	
	/** 
	 * 
	 * @return
	 */ 
	
	public double getLocationLongitude() { 
		
		return location_longitude;
	} 
	
	/** 
	 * 
	 * @param location_longitude
	 */ 
	
	public void setLocationLongitude(double location_longitude) { 
		
		this.location_longitude = location_longitude;
	} 
	
	/** 
	 * 
	 * @return
	 */ 
	
	public String getLocationType() { 
		
		return location_type;
	} 
	
	/** 
	 * 
	 * @param location_type
	 */ 
	
	public void setLocationType(String location_type) { 
		
		this.location_type = location_type;
	} 
	
	/** 
	 * 
	 * @return
	 */ 
	
	public double getViewportNortheastLatitude() { 
		
		return viewport_northeast_latitude;
	} 
	
	/** 
	 * 
	 * @param viewport_northeast_latitude
	 */ 
	
	public void setViewportNortheastLatitude(double viewport_northeast_latitude) { 
		
		this.viewport_northeast_latitude = viewport_northeast_latitude;
	} 
	
	/** 
	 * 
	 * @return
	 */ 
	
	public double getViewportNortheastLongitude() { 
		
		return viewport_northeast_longitude;
	} 
	
	/** 
	 * 
	 * @param viewport_northeast_longitude
	 */ 
	
	public void setViewportNortheastLongitude(double viewport_northeast_longitude) { 
		
		this.viewport_northeast_longitude = viewport_northeast_longitude;
	} 
	
	/** 
	 * 
	 * @return
	 */ 
	
	public double getViewportSouthwestLatitude() { 
		
		return viewport_southwest_latitude;
	} 
	
	/** 
	 * 
	 * @param viewport_southwest_latitude
	 */ 
	
	public void setViewportSouthwestLatitude(double viewport_southwest_latitude) { 
		
		this.viewport_southwest_latitude = viewport_southwest_latitude;
	} 
	
	/** 
	 * 
	 * @return
	 */ 
	
	public double getViewportSouthwestLongitude() { 
		
 		return viewport_southwest_longitude;
	} 
	
	/** 
	 * 
	 * @param viewport_southwest_longitude
	 */ 
	
	public void setViewportSouthwestLongitude(double viewport_southwest_longitude) { 
		
		this.viewport_southwest_longitude = viewport_southwest_longitude;
	}	
}
