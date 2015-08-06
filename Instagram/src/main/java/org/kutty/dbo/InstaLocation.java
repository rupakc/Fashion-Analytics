package org.kutty.dbo;

/** 
 * Defines a Instagram Location Object for Easy Insertion Into Database
 * @author Rupak Chakraborty
 * @for Kutty
 * @since 2 August, 2015
 */ 

public class InstaLocation {
	
	private String location_id;
	private double location_latitude;
	private double location_longitude;
	private String location_name;
	private Tag location_media;
	private double timestamp;
	
	/** 
	 * public constructor to initialize the InstaLocation object with default values 
	 */
	
	public InstaLocation() {
	
	}

	/** 
	 * 
	 * @return the location_id
	 */ 
	
	public String getLocationId() { 
		
		return location_id;
	}

	/** 
	 * 
	 * @param location_id the location_id to set
	 */ 
	
	public void setLocationId(String location_id) { 
		
		this.location_id = location_id;
	}

	/** 
	 * 
	 * @return the location_latitude
	 */ 
	
	public double getLocationLatitude() { 
		
		return location_latitude;
	}

	/** 
	 * 
	 * @param location_latitude the location_latitude to set
	 */ 
	
	public void setLocationLatitude(double location_latitude) { 
		
		this.location_latitude = location_latitude;
	}

	/** 
	 * 
	 * @return the location_longitude
	 */ 
	
	public double getLocationLongitude() { 
		
		return location_longitude;
	}

	/** 
	 * 
	 * @param location_longitude the location_longitude to set
	 */ 
	
	public void setLocationLongitude(double location_longitude) { 
		
		this.location_longitude = location_longitude;
	}

	/** 
	 * 
	 * @return the location_name
	 */ 
	
	public String getLocationName() { 
		
		return location_name;
	}

	/** 
	 * 
	 * @param location_name the location_name to set
	 */ 
	
	public void setLocationName(String location_name) { 
		
		this.location_name = location_name;
	}

	/**
	 * @return the location_media
	 */ 
	
	public Tag getLocationMedia() { 
		
		return location_media;
	}

	/**
	 * @param location_media the location_media to set
	 */ 
	
	public void setLocationMedia(Tag location_media) { 
		
		this.location_media = location_media;
	}
	
	/** 
	 * 
	 * @return
	 */ 
	
	public double getTimestamp() { 
		
		return timestamp;
	}
	
	/** 
	 * 
	 * @param timestamp
	 */ 
	
	public void setTimestamp(double timestamp) { 
		
		this.timestamp = timestamp;
	}
}
