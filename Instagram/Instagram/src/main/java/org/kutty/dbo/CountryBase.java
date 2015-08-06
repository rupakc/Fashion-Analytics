package org.kutty.dbo;

import java.util.Date;

public class CountryBase {
	
	public String country;
	public String code;
	public String username;
	public String channel;
	public Date timestamp;
	public String product; 
	
	/**
	 * @return the product
	 */ 
	
	public String getProduct() { 
		
		return product;
	}

	/**
	 * @param product the product to set
	 */ 
	
	public void setProduct(String product) { 
		
		this.product = product;
	}

	/**
	 * @return the country
	 */ 
	
	public String getCountry() { 
		
		return country;
	} 
	
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the channel
	 */
	public String getChannel() {
		return channel;
	}

	/**
	 * @param channel the channel to set
	 */
	public void setChannel(String channel) {
		this.channel = channel;
	}

	/**
	 * @return the timestamp
	 */
	public Date getTimestamp() {
		return timestamp;
	}

	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * @param country the country to set
	 */ 
	
	public void setCountry(String country) { 
		
		this.country = country;
	} 
	
	/**
	 * @return the code
	 */ 
	
	public String getCode() { 
		
		return code;
	} 
	 
	/**
	 * @param code the code to set
	 */ 
	
	public void setCode(String code) { 
		
		this.code = code;
	} 
		
}
